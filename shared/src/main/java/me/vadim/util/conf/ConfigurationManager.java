package me.vadim.util.conf;

import java.util.function.Function;

/**
 * Defines a class capable of managing {@link ConfigurationFile} instances.
 * @author vadim
 */
public interface ConfigurationManager {

	/**
	 * Register and load a config.
	 * @param clazz the target class (inherits from {@link T})
	 * @param factory generator function to create a new instance of the target class, given a {@link ResourceProvider}
	 * @param <T> target class type
	 * @return {@code this} instance, for chaining
	 */
	<T extends ConfigurationFile<?>> ConfigurationManager register(Class<T> clazz, Function<ResourceProvider, T> factory);

	/**
	 * Retrieve a {@link #register(Class, Function) registered} config.
	 * @param clazz the target class (inherits from {@link T})
	 * @param <T> target class type
	 * @return the instance of the registered config
	 * @throws IllegalArgumentException if {@code clazz} is not {@link #register(Class, Function) registered}
	 */
	<T extends ConfigurationFile<?>> T open(Class<T> clazz);

	/**
	 * Reloads all currently registered configs.
	 */
	void reload();

}
