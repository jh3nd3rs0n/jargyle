package com.github.jh3nd3rs0n.jargyle.server.internal.io;

import java.io.File;

public interface FileStatusListener {
	
	void onFileCreated(final File file);
	
	void onFileDeleted(final File file);
	
	void onFileModified(final File file);
	
}