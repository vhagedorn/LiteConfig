package me.vadim.util.conf;

import java.util.function.Function;

/**
 * Defines a class capable of managing {@link ConfigurationFile} instances.
 *
 * @author vadim
 */
public interface ConfigurationManager extends ConfigurationProvider {

	/**
	 * Register and load a config.
	 *
	 * @param clazz   the target class (inherits from {@link T})
	 * @param factory generator function to create a new instance of the target class, given a {@link ResourceProvider}
	 * @param <T>     target class type
	 *
	 * @return {@code this} instance, for chaining
	 */
	public <T extends ConfigurationFile<?>> ConfigurationManager register(Class<T> clazz, Function<ResourceProvider, T> factory);

	/**
	 * {@link ConfigurationFile#reload() Reloads} all currently registered configs.
	 */
	public void reload();

	/**
	 * {@link ConfigurationFile#save() Saves} all currently registered configs.
	 */
	public void save();

}
