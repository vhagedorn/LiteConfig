package me.vadim.util.conf;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Provides various resources to this library.
 *
 * @author vadim
 */

public interface ResourceProvider {

	/**
	 * Utility function for implementing classes.
	 */
	default String stripPrependingPathSeparator(String path) {
		if (path.startsWith("/") || path.startsWith("\\") || path.startsWith(File.separator))
			path = path.substring(1);
		return path;
	}

	/**
	 * Usually the CWD.
	 *
	 * @return the directory in which to store configuration files, etc.
	 */
	File getDataFolder();

	/**
	 * @return an {@link InputStream} of the packaged resource denoted by {@code name}
	 */
	InputStream getResource(String name);

	/**
	 * @return a {@link Logger java.util.logging.Logger} through which to log configuration messages
	 */
	Logger getLogger();

}