package me.vadim.util.conf.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.ConfigurationFile;
import me.vadim.util.conf.ResourceProvider;

import java.io.File;
import java.util.logging.Logger;

/**
 * {@link com.google.gson.Gson} config.
 *
 * @author vadim
 */
public abstract class JsonFile extends ConfigurationFile<JsonElement> {

	private JsonElement json;

	private ConfigurationAccessor wrapper;

	public JsonFile(File file, ResourceProvider resourceProvider) {
		super(file, resourceProvider);
	}

	public JsonFile(String file, ResourceProvider resourceProvider) {
		super(file, resourceProvider);
	}

	@Override
	protected JsonElement getConfiguration() { return json; }

	@Override
	protected ConfigurationAccessor getConfigurationAccessor() { return wrapper; }

	@Override
	protected Void logError(Logger log, String path) {
		return logError(log, path, path);
	}

	@Override
	protected Void logError(Logger log, String path, String name) {
		sneaky(new JsonParseException(name));
		return null;
	}

	@Override
	public void reload() {
		load();
		save();
	}

	@Override
	protected void createConfiguration() throws Exception {
		json = JsonParser.parseString(getContent());
		if (!json.isJsonObject()) throw new IllegalArgumentException(json.toString());//hmm
		wrapper = new JsonConfigurationAccessor(this, json.getAsJsonObject());
	}

	@Override
	public void save() {
		setContent(new GsonBuilder().setPrettyPrinting().create().toJson(json));
		super.save();
	}

}