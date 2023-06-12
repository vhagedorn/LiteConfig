package me.vadim.util.conf.gson;

import com.google.gson.*;
import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.ConfigurationFile;
import me.vadim.util.conf.ResourceProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
	protected void setTemplate(JsonElement template) {
		super.setTemplate(template);
	}

	@Override
	protected void setDefaultTemplate() {
		String path = fromRoot.getPath();
		if (path.startsWith(File.separator)) path = path.substring(1);

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			resourceProvider.getResource(path).transferTo(baos);
			setTemplate(JsonParser.parseString(baos.toString(StandardCharsets.UTF_8)));
		} catch (IOException e){
			sneaky(e);
		}
	}

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
		if(getContent().length() > 0)
			json = JsonParser.parseString(getContent());
		else
			json = new JsonObject();
		if (!json.isJsonObject()) throw new IllegalArgumentException(json.toString());//hmm
		wrapper = new JsonConfigurationAccessor(null, this, json.getAsJsonObject());
	}

	@Override
	public void save() {
		setContent(new GsonBuilder().setPrettyPrinting().create().toJson(json));
		super.save();
	}

}