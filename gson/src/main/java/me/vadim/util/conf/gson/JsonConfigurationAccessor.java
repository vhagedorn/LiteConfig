package me.vadim.util.conf.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.vadim.util.conf.AbstractConfigurationAccessor;
import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.wrapper.PlaceholderMessage;
import me.vadim.util.conf.wrapper.impl.UnformattedMessage;

import java.util.Map;
import java.util.Optional;

/**
 * @author vadim
 */
public class JsonConfigurationAccessor extends AbstractConfigurationAccessor<JsonFile> {

	private final JsonObject object;
	private final String key;

	public JsonConfigurationAccessor(String key, JsonFile file, JsonObject object) {
		super(file);
		this.key    = key;
		this.object = object;
	}

	@Override
	public String currentPath() {
		return key;
	}

	private Optional<JsonPrimitive> getPrimitive(String path) {
		return has(path) ? Optional.of(object.getAsJsonPrimitive(path)) : Optional.empty();
	}

	@Override
	public String getString(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsString).orElse(null);
	}

	@Override
	public char getChar(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsCharacter).orElse((char) 0);
	}

	@Override
	public PlaceholderMessage getPlaceholder(String path) {
		return new UnformattedMessage(getString(path));
	}

	@Override
	public ConfigurationAccessor getPath(String path) {
		ConfigurationAccessor child = this;
		for (String s : path.split("\\."))
			child = child.getObject(s);
		return child;
	}

	@Override
	public ConfigurationAccessor getObject(String path) {
		return has(path) ? new JsonConfigurationAccessor(path, file, object.getAsJsonObject(path)) : null;
	}

	@Override
	public ConfigurationAccessor[] getChildren() {
		return object.entrySet().stream()
					 .filter(e -> e.getValue().isJsonObject())
					 .map(e -> Map.entry(e.getKey(), e.getValue().getAsJsonObject()))
					 .map(e -> new JsonConfigurationAccessor(e.getKey(), file, e.getValue()))
					 .toArray(ConfigurationAccessor[]::new);
	}

	@Override
	public boolean has(String path) {
		return object.has(path) && !object.get(path).isJsonNull();
	}

	@Override
	public boolean getBoolean(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsBoolean).orElse(false);
	}

	@Override
	public byte getByte(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsByte).orElse((byte) 0);
	}

	@Override
	public short getShort(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsShort).orElse((short) 0);
	}

	@Override
	public int getInt(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsInt).orElse(0);
	}

	@Override
	public long getLong(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsLong).orElse(0L);
	}

	@Override
	public float getFloat(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsFloat).orElse(0f);
	}

	@Override
	public double getDouble(String path) {
		return getPrimitive(path).map(JsonPrimitive::getAsDouble).orElse(0.0);
	}

}
