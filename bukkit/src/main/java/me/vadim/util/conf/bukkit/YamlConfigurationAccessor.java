package me.vadim.util.conf.bukkit;

import me.vadim.util.conf.AbstractConfigurationAccessor;
import me.vadim.util.conf.ConfigurationAccessor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

/**
 * @author vadim
 */
public class YamlConfigurationAccessor extends AbstractConfigurationAccessor<YamlFile> {

	private final ConfigurationSection section;
	private final YamlConfigurationAccessor parent;
	private final String path;

	public YamlConfigurationAccessor(String subpath, YamlFile file, ConfigurationSection section) {
		this(subpath, null, file, section);
	}

	public YamlConfigurationAccessor(String subpath, YamlConfigurationAccessor parent, YamlFile file, ConfigurationSection section) {
		super(file);
		this.path    = subpath;
		this.parent  = parent;
		this.section = section;
	}

	private String fullPathTo(String subpath) {
		String fp = fullPath(this);
		return fp == null
			   ? subpath
			   : fp + '.' + subpath;
	}

	private String fullPath(YamlConfigurationAccessor parent) {
		return (parent.parent != null
				? fullPath(parent.parent) + '.' + parent.path
				: null);
	}

	@Override
	public String currentPath() {
		return path;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> T nonNull(T nullable, String path) {
		return nullable == null ? (T) file.logError(fullPathTo(path)) : nullable;
	}

	@Override
	public String getString(String path) {
		return nonNull(section.getString(path), path);
	}

	@Override
	public ConfigurationAccessor getPath(String path) {
		return getObject(path); // syntax is the same for yaml
	}

	@Override
	public ConfigurationAccessor getObject(String path) {
		return has(path) ? new YamlConfigurationAccessor(path, this, file, section.getConfigurationSection(path)) : null;
	}

	@Override
	public ConfigurationAccessor[] getChildren() {
		return section.getKeys(false).stream()
					  .map(section::getConfigurationSection).filter(Objects::nonNull)
					  .map(s -> new YamlConfigurationAccessor(s.getCurrentPath(), this, file, s))
					  .toArray(ConfigurationAccessor[]::new);
	}

	@Override
	public boolean has(String path) {
		return section.contains(path);
	}

	@Override
	public boolean getBoolean(String path) {
		return section.getBoolean(path);
	}

	@Override
	public int getInt(String path) {
		return section.getInt(path);
	}

	@Override
	public long getLong(String path) {
		return section.getLong(path);
	}

	@Override
	public double getDouble(String path) {
		return section.getDouble(path);
	}

	@Override
	public String[] getStringArray(String path) {
		return section.getStringList(path).toArray(String[]::new);
	}

	@Override
	public ConfigurationAccessor[] getObjectArray(String path) {
		throw new UnsupportedOperationException("YAML does not support object lists.");
	}

}
