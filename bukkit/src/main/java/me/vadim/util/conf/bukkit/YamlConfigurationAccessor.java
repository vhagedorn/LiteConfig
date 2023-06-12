package me.vadim.util.conf.bukkit;

import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.wrapper.PlaceholderMessage;
import me.vadim.util.conf.wrapper.impl.UnformattedMessage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author vadim
 */
public class YamlConfigurationAccessor implements ConfigurationAccessor {

    private final YamlFile                  file;
    private final ConfigurationSection      section;
    private final YamlConfigurationAccessor parent;
    private final String                    path;

    public YamlConfigurationAccessor(String subpath, YamlFile file, ConfigurationSection section) {
        this(subpath, null, file, section);
    }

	public YamlConfigurationAccessor(String subpath, YamlConfigurationAccessor parent, YamlFile file, ConfigurationSection section) {
		this.path    = subpath;
		this.parent  = parent;
		this.file    = file;
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

    private <T> T nonNull(T nullable, String path){
        return nullable == null ? (T) file.logError(Logger.getGlobal(), fullPathTo(path)) : nullable;
    }

    @Override
    public String getString(String path) {
        return nonNull(section.getString(path), path);
    }

    @Override
    public char getChar(String path) {
        return getString(path).charAt(0);
    }

    @Override
    public PlaceholderMessage getPlaceholder(String path) {
        return new UnformattedMessage(getString(path));
    }

    @Override
    public ConfigurationAccessor getPath(String path) {
        return getObject(path);//syntax is the same for yaml
    }

    @Override
    public ConfigurationAccessor getObject(String path) {
        return new YamlConfigurationAccessor(path, this, file, section.getConfigurationSection(path));
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
    public byte getByte(String path) {
        return (byte) getInt(path);
    }

    @Override
    public short getShort(String path) {
        return (short) getInt(path);
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
    public float getFloat(String path) {
        return (float) getDouble(path);
    }

    @Override
    public double getDouble(String path) {
        return section.getDouble(path);
    }

}
