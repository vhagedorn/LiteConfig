package me.vadim.util.conf;

/**
 * Abstract wrapper to encapsulate access to different configuration APIs.
 *
 * @author vadim
 */
public interface ConfigurationAccessor extends ConfigurationReader, ConfigurationWriter {

	/**
	 * @return name or path denoting this current node
	 */
	public String currentPath();

}