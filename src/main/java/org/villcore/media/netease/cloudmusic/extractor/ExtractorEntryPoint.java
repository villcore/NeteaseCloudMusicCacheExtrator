package org.villcore.media.netease.cloudmusic.extractor;

import java.io.File;
import java.nio.file.Paths;

public class ExtractorEntryPoint {
	public static void main(String[] args) {
		String defaultMusicCacheDirectory = "c://Users//Administrator//AppData//Local//Netease//CloudMusic//Cache//Cache//";
		String defaultExtractSaveDirectory = "e://music";

		ExtractorProcessor extractorProcessor = new DefaultSaveExtractProcessor(Paths.get(defaultExtractSaveDirectory));

		File file = new File(defaultMusicCacheDirectory);
		File[] files = file.listFiles();
		for(File file3 : files) {
			if(file3.getName().indexOf(".uc") > 0) {
				try {
					//MP3Extractor.extract(file3.toPath(), extractorProcessor);
				}  
				catch (Exception e) {

				}
			}		
		}

		CacheDirectoryMonitor monitor = new CacheDirectoryMonitor(Paths.get(defaultMusicCacheDirectory), extractorProcessor);
		monitor.start();
		new Thread(monitor).start();
	}
}
