package org.villcore.media.netease.cloudmusic.extractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class CacheDirectoryMonitor implements Runnable{
	private static final Logger log = LoggerFactory.getLogger(CacheDirectoryMonitor.class);
	
	private Path cacheDirectory;
	private ExtractorProcessor extractorProcessor;
	private volatile boolean isRun;

	public Path getCacheDirectory() {
		return cacheDirectory;
	}


	public void setCacheDirectory(Path cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
	}


	public ExtractorProcessor getExtractorProcessor() {
		return extractorProcessor;
	}


	public void setExtractorProcessor(ExtractorProcessor extractorProcessor) {
		this.extractorProcessor = extractorProcessor;
	}


	public CacheDirectoryMonitor(Path cacheDirectory, ExtractorProcessor extractorProcessor) {
		super();
		this.cacheDirectory = cacheDirectory;
		this.extractorProcessor = extractorProcessor;
	}

	public void start() {
		this.isRun = true;
	}

	public void stop() {
		this.isRun = false;
	}

	public void run() {
		WatchService watcher;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			this.cacheDirectory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
			while(this.isRun) {
				WatchKey key = watcher.take();
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind kind = event.kind();
					if (kind == StandardWatchEventKinds.OVERFLOW) {//事件可能lost or discarded
						System.out.println("事件可能lost or discarded");
						continue;
					}
					@SuppressWarnings("unchecked")
					WatchEvent<Path> e = (WatchEvent<Path>) event;
					String kindName = kind.name();
					Path fileName = e.context();
					//System.out.printf("Event %s has happened,which fileName is %s%n", kindName, fileName);

					if(fileName.toFile().getName().contains(".uc")) {
						try {
							long lastLength = -1;
							while(true) {
								TimeUnit.SECONDS.sleep(1);
								File srcFile = Paths.get(this.cacheDirectory.toFile().getAbsolutePath() + File.separator + fileName).toFile();
								long currentLen = srcFile.length();
								if(currentLen == lastLength) {
									break;
								}
								else {
									lastLength = srcFile.length();
								}
							} 

							MP3Extractor.extract(Paths.get(this.cacheDirectory.toFile().getAbsolutePath() + File.separator + fileName), this.extractorProcessor);
						} catch (UnsupportedTagException e1) {
							log.error(e1.getMessage(), e1);
						} catch (InvalidDataException e1) {
							log.error(e1.getMessage(), e1);
						} catch (Exception e1) {
							log.error(e1.getMessage(), e1);
						}
					}
				}
				// 重置 key 如果失败结束监控
				if (!key.reset()) {
					break;
				}
			}
		} catch (IOException e) {
			this.isRun = false;
			log.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			this.isRun = false;
			log.error(e.getMessage(), e);
		}
	}
}
