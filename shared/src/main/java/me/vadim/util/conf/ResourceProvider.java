package me.vadim.util.conf;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Provides various resources to this library.
 * @author vadim
 */

public interface ResourceProvider {

	File getDataFolder();

	InputStream getResource(String name);

	Logger getLogger();

}