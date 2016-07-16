package org.villcore.media.netease.cloudmusic.extractor;

import java.nio.file.Path;

public class ExtractorProcessorEvent {
	private Path srcPath; //src
	private String name; //MP3文件名称
	
	public ExtractorProcessorEvent(Path srcPath, String name) {
		super();
		this.srcPath = srcPath;
		this.name = name;
	}
	
	public Path getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(Path srcPath) {
		this.srcPath = srcPath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ExtractorProcessorEvent [srcPath=" + srcPath + ", name=" + name + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((srcPath == null) ? 0 : srcPath.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtractorProcessorEvent other = (ExtractorProcessorEvent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (srcPath == null) {
			if (other.srcPath != null)
				return false;
		} else if (!srcPath.equals(other.srcPath))
			return false;
		return true;
	}
}