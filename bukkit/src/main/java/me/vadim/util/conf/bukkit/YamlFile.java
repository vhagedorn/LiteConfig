package me.vadim.util.conf.bukkit;

import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.ConfigurationFile;
import me.vadim.util.conf.ResourceProvider;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * {@link org.bukkit.configuration.file.YamlConfiguration} config.
 * @author vadim
 */
public abstract class YamlFile extends ConfigurationFile<YamlConfiguration> {

    protected final YamlConfiguration yaml =  new YamlConfiguration().
            options()
            .parseComments(true)
            .configuration();

    protected final ConfigurationAccessor wrapper = new YamlConfigurationAccessor(null, this, yaml);

    public YamlFile(File file, ResourceProvider resourceProvider) {
        super(file, resourceProvider);
    }

    public YamlFile(String file, ResourceProvider resourceProvider) {
        super(file, resourceProvider);
    }

    @Override
    protected YamlConfiguration getConfiguration() { return yaml; }

    @Override
    protected ConfigurationAccessor getConfigurationAccessor() { return wrapper; }

    @Override
    protected void setTemplate(YamlConfiguration template) {
        yaml.options().copyDefaults(true).parseComments(true).configuration().addDefaults(template);
    }

    @Override
    protected void setDefaultTemplate() {
        String path = fromRoot.getPath();
        if(path.startsWith(File.separator)) path = path.substring(1);

        setTemplate(YamlConfiguration.loadConfiguration(new InputStreamReader(resourceProvider.getResource(path))));
    }

    @Override
    protected Void logError(Logger log, String path) {
        return logError(log, path, path);
    }

    @Override
    protected Void logError(Logger log, String path, String name) {
        String        invalid = Objects.toString(yaml.get(path));
        StringBuilder indent = new StringBuilder();
        StringBuilder error  = new StringBuilder();

        for(String p : path.split("\\.")){
            error.append('\n').append(indent).append(p).append(": ");
            indent.append(' ');
        }

        log.severe("Invalid " +
                   (!name.equals(path)
                        ? name + " in "
                        : "") + fromRoot.getPath() + ".");

        for(String line : (error + invalid).split("\n"))
            log.severe(line);

        String[] split = error.toString().split("\n");
        log.severe(" ".repeat(split[split.length - 1].length()) + "^".repeat(invalid.length()));

        sneaky(new InvalidConfigurationException(name));
        return null;
    }

    @Override
    public void reload() {
        load();
        save();
    }

    @Override
    protected void createConfiguration() throws Exception {
        yaml.loadFromString(getContent());
    }

    @Override
    public void save() {
        setContent(yaml.saveToString());
        super.save();
    }

}
