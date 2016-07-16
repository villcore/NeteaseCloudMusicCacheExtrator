package org.villcore.media.netease.cloudmusic.extractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSaveExtractProcessor implements ExtractorProcessor {
	private static final Logger log = LoggerFactory.getLogger(DefaultSaveExtractProcessor.class);
	
	private Path saveDirector;

	public DefaultSaveExtractProcessor(Path saveDirector) {
		super();
		this.saveDirector = saveDirector;
	}

	public void onExtract(ExtractorProcessorEvent event) {
		Path srcPath = event.getSrcPath();
		String fileName = event.getName();
		
		Path dstPath = Paths.get(this.saveDirector.toString() + File.separator + fileName);
		File dstFile = dstPath.toFile();
		if(dstFile.exists()) {
			log.info("{} -> already exist...", fileName);
		}
		else {
			try {
				dstFile.getParentFile().mkdirs();
				dstFile.createNewFile();
				FileUtil.headCreate(srcPath.toFile(), dstFile);
				log.info("{} -> save success...", fileName);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			} 
		}
	}
}
