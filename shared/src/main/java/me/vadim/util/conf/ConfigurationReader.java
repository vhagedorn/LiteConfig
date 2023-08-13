package me.vadim.util.conf;

import me.vadim.util.conf.wrapper.PlaceholderMessage;

/**
 * Provides read access to a configuration.
 *
 * @author vadim
 */
public interface ConfigurationReader {

	/**
	 * @return object by path split by '.'
	 */
	public ConfigurationAccessor getPath(String path);

	/**
	 * @return object as defined by implementation (not guaranteed to be split by '.')
	 */
	public ConfigurationAccessor getObject(String path);

	/**
	 * @return all child object nodes at the current level, or an empty array
	 */
	public ConfigurationAccessor[] getChildren();

	/**
	 * @return whether or not the specified path exists and is not null
	 */
	public boolean has(String path);

	/*
	 * Object getter methods will return null if the specified path references null or is nonexistent
	 */

	public String getString(String path);

	public char getChar(String path);

	public PlaceholderMessage getPlaceholder(String path);

	/*
	 * primitive getter methods will return zero (or false) if the specified path references a null or is nonexistent
	 */

	public boolean getBoolean(String path);

	public byte getByte(String path);

	public short getShort(String path);

	public int getInt(String path);

	public long getLong(String path);

	public float getFloat(String path);

	public double getDouble(String path);

	public String[] getStringArray(String path);

	public ConfigurationAccessor[] getObjectArray(String path);

	//todo: primitive array? number array? bigdecimal/biginteger array?

}