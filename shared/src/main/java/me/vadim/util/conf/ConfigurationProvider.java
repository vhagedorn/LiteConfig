package me.vadim.util.conf;

import java.util.function.Function;

/**
 * Defines a class capable of providing {@link ConfigurationFile} instances.
 *
 * @author vadim
 */
public interface ConfigurationProvider {

	/**
	 * Retrieve a {@link ConfigurationManager#register(Class, Function) registered} config.
	 *
	 * @param clazz the target class (inherits from {@link T})
	 * @param <T>   target class type
	 *
	 * @return the instance of the registered config
	 *
	 * @throws IllegalArgumentException if {@code clazz} is not {@link ConfigurationManager#register(Class, Function) registered}
	 */
	public <T extends ConfigurationFile<?>> T open(Class<T> clazz);

}
