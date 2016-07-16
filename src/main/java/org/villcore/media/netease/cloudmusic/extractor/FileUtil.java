package org.villcore.media.netease.cloudmusic.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
	
	public static void headCreate(File f1, File f2) {   //f1 为源文件  f2 为目标文件，本方法的功能是 复制f1文件 为f2
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(f1);
			fo = new FileOutputStream(f2);
			in = fi.getChannel();//得到f1 的文件通道
			out = fo.getChannel();//得到f12的文件通道
			in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	} 
}
