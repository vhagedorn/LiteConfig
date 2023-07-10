package me.vadim.util.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author vadim
 */
public class LiteConfig implements ConfigurationManager {

	private final ResourceProvider resourceProvider;

	private final Map<Class<?>, ConfigurationFile<?>> configs = new HashMap<>();

	public LiteConfig(ResourceProvider resourceProvider) {
		this.resourceProvider = resourceProvider;
	}

	@Override
	public <T extends ConfigurationFile<?>> LiteConfig register(Class<T> clazz, Function<ResourceProvider, T> factory) {
		configs.put(clazz, factory.apply(resourceProvider));
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ConfigurationFile<?>> T open(Class<T> clazz) {
		String canonical = clazz.getCanonicalName();
		return (T) Optional.ofNullable(configs.get(clazz)).orElseThrow(() -> new IllegalArgumentException("Configuration class '" + canonical + "' not registered."));
	}

	@Override
	public void reload() {
		configs.values().forEach(ConfigurationFile::reload);
	}

	@Override
	public void save() {
		configs.values().forEach(ConfigurationFile::save);
	}

}