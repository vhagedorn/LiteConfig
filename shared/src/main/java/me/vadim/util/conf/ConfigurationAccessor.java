package me.vadim.util.conf;

import me.vadim.util.conf.wrapper.PlaceholderMessage;

/**
 * Abstract wrapper to encapsulate different configuration APIs.
 *
 * @author vadim
 */
public interface ConfigurationAccessor {

	/**
	 * @return object by path split by '.'
	 */
	ConfigurationAccessor getPath(String path);

	/**
	 * @return object as defined by implementation (not guaranteed to be split by '.')
	 */
	ConfigurationAccessor getObject(String path);

	/**
	 * @return all child object nodes at the current level, or an empty array
	 */
	ConfigurationAccessor[] getChildren();

	/**
	 * @return whether or not the specified path exists and is not null
	 */
	boolean has(String path);

	/*
	 * Object getter methods will return null if the specified path references null or is nonexistent
	 */
	String currentPath();

}
