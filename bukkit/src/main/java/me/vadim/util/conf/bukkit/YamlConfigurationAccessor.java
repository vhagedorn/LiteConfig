package me.vadim.util.conf.bukkit;

import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.wrapper.PlaceholderMessage;
import me.vadim.util.conf.wrapper.impl.UnformattedMessage;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

/**
 * @author vadim
 */
public class YamlConfigurationAccessor implements ConfigurationAccessor {

    private final YamlFile             file;
    private final ConfigurationSection section;

    public YamlConfigurationAccessor(YamlFile file, ConfigurationSection section) {
        this.file = file;
        this.section = section;
    }

    private <T> T nonNull(T nullable, String path){
        return nullable == null ? (T) file.logError(Logger.getGlobal(), path) : nullable;
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
        return new YamlConfigurationAccessor(file, section.getConfigurationSection(path));
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
