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
	 * @return whether or not the specified path exists and is not null
	 */
	boolean has(String path);

	/*
	 * Object getter methods will return null if the specified path references null or is nonexistent
	 */

	String getString(String path);

	char getChar(String path);

	PlaceholderMessage getPlaceholder(String path);

	/*
	 * primitive getter methods will return zero (or false) if the specified path references a null or is nonexistent
	 */

	boolean getBoolean(String path);

	byte getByte(String path);

	short getShort(String path);

	int getInt(String path);

	long getLong(String path);

	float getFloat(String path);

	double getDouble(String path);
}
