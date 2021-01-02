package jargyle.common.io;

import java.io.File;

public interface FileStatusListener {
	
	void fileCreated(final File file);
	
	void fileDeleted(final File file);
	
	void fileModfied(final File file);
	
}