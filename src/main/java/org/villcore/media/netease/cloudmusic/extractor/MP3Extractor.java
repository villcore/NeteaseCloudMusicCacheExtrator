package org.villcore.media.netease.cloudmusic.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MP3Extractor {
	public static void extract(Path src, ExtractorProcessor processor) throws IOException, UnsupportedTagException, InvalidDataException {
		File srcFile = src.toFile();
		if(!srcFile.exists()) {
			throw new FileNotFoundException(src.toString());
		}
		Mp3File mp3file = new Mp3File(srcFile);

		String title = null;

		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			title = id3v2Tag.getTitle();
		}

		if(title == null) {
			if (mp3file.hasId3v1Tag()) {
				ID3v1 id3v1Tag = mp3file.getId3v1Tag();
				title = id3v1Tag.getTitle();
			}
			else {
				title = srcFile.getName();
			}
		}
		
		if(title != null && title.trim().length() != 0) {
			ExtractorProcessorEvent event = new ExtractorProcessorEvent(src, title + ".mp3");
			processor.onExtract(event);
		}
	}
}
