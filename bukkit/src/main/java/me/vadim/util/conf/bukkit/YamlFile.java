package me.vadim.util.conf.bukkit;

import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.ConfigurationFile;
import me.vadim.util.conf.ResourceProvider;
import me.vadim.util.conf.bukkit.wrapper.EffectGroup;
import me.vadim.util.conf.bukkit.wrapper.EffectParticle;
import me.vadim.util.conf.bukkit.wrapper.EffectSound;
import me.vadim.util.conf.bukkit.wrapper.OptionalMessage;
import me.vadim.util.conf.wrapper.PlaceholderMessage;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * {@link org.bukkit.configuration.file.YamlConfiguration} config.
 *
 * @author vadim
 */
public abstract class YamlFile extends ConfigurationFile<YamlConfiguration> {

	protected final YamlConfiguration yaml = new YamlConfiguration().
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
		if (path.startsWith(File.separator)) path = path.substring(1);

		setTemplate(YamlConfiguration.loadConfiguration(new InputStreamReader(resourceProvider.getResource(path))));
	}

	@Override
	public Void logError(Logger log, String path, String name) {
		String        invalid = Objects.toString(yaml.get(path));
		StringBuilder indent  = new StringBuilder();
		StringBuilder error   = new StringBuilder();

		for (String p : path.split("\\.")) {
			error.append('\n').append(indent).append(p).append(": ");
			indent.append(' ');
		}

		log.severe("Invalid " +
				   (!name.equals(path)
					? name + " in "
					: "") + fromRoot.getPath() + ".");

		for (String line : (error + invalid).split("\n"))
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

    /* Convenience getters. */

	protected ItemStack getItem(String path) {
		ConfigurationAccessor conf = getConfigurationAccessor().getPath(path);

		String   name = conf.getString("name");
		String[] lore = conf.getStringArray("lore");
		Material type = Material.matchMaterial(conf.getString("type"));

		if (name == null)
			logError(resourceProvider.getLogger(), path + ".name", "item name");
		assert name != null;
		if (lore == null)
			logError(resourceProvider.getLogger(), path + ".lore", "item lore");
		assert lore != null;
		if (type == null)
			logError(resourceProvider.getLogger(), path + ".type", "item type");
		assert type != null;

		ItemStack item = new ItemStack(type);
		ItemMeta meta = item.getItemMeta();
		if(!item.hasItemMeta() || meta == null)
			return item;

		meta.setDisplayName(BukkitPlaceholders.colorize(name));
		meta.setLore(Arrays.stream(lore).map(BukkitPlaceholders::colorize).collect(Collectors.toList()));

		item.setItemMeta(meta);

		return item;
	}

    protected static String[] OPTIONAL_DISABLED = { "null", "disable", "disabled", "off", "" };

    protected OptionalMessage getOptional(String path) {
        ConfigurationAccessor conf = getConfigurationAccessor();

        PlaceholderMessage msg = null;

        boolean disabled = false;
        if (conf.has(path))
            for (String m : OPTIONAL_DISABLED)
                disabled |= m.equalsIgnoreCase(conf.getString(path));

        if(!disabled)
            msg = conf.getPlaceholder(path);

        return new OptionalMessage(msg);
    }

    protected EffectSound getSound(String path) {
        ConfigurationAccessor conf = getConfigurationAccessor().getPath(path);

        float volume = 1.0f;
        if (conf.has("volume"))
            volume = conf.getFloat("volume");

        float pitch = 1.0f;
        if (conf.has("pitch"))
            pitch = conf.getFloat("pitch");

        Sound sound = null;
        if (conf.has("sound")) {
            sound = getEnumExact(Sound.class, conf.getString("sound"));
            if (sound == null)
                logError(resourceProvider.getLogger(), path + ".sound", "sound");
        }

        return new EffectSound(sound, volume, pitch);
    }

    protected EffectParticle getParticle(String path) {
        ConfigurationAccessor conf = getConfigurationAccessor().getPath(path);

        int count = 1;
        if (conf.has("count"))
            count = conf.getInt("count");

        Particle particle = null;
        if (conf.has("particle")) {
            particle = getEnumExact(Particle.class, conf.getString("particle"));
            if (particle == null)
                logError(resourceProvider.getLogger(), path + ".particle", "particle");
        }

        return new EffectParticle(particle, count);
    }

    protected EffectGroup getEffect(String path) {
        ConfigurationAccessor conf = getConfigurationAccessor().getPath(path);

        EffectSound sound = EffectSound.EMPTY;
        if (conf.has("sound"))
            sound = getSound(path);

        EffectParticle particle = EffectParticle.EMPTY;
        if (conf.has("particle"))
            particle = getParticle(path);

        return new EffectGroup(sound, particle);
    }

    protected <E extends Enum<E>> @Nullable E getEnum(Class<E> clazz, String path) {
        try {
            String name = getConfigurationAccessor().getString(path);
            if(name == null)
                return null;
            return getEnumExact(clazz, name.trim().replace(' ', '_').toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    protected static <E extends Enum<E>> @Nullable E getEnumExact(Class<E> clazz, String name) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
