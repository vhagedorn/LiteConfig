package me.vadim.util.conf.gson;

import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.ResourceProvider;

/**
 * @author vadim
 */
class ConfigA extends JsonFile {

	ConfigA(ResourceProvider resourceProvider) {
		super("a.json", resourceProvider);
	}

	public ConfigurationAccessor getConfigurationAccessor(){ return super.getConfigurationAccessor(); }

}