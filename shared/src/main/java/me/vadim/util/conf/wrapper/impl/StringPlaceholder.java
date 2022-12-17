package me.vadim.util.conf.wrapper.impl;

import me.vadim.util.conf.wrapper.Placeholder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vadim
 */
public class StringPlaceholder implements Placeholder {

	@SuppressWarnings("StaticNonFinalField")
	public static String              format = "{%s}";

	private final Map<String, String> placeholders;

	public StringPlaceholder(Map<String, String> placeholders) {
		this.placeholders = placeholders;
	}

	@Override
	public String format(String raw) {
		for (Map.Entry<String, String> entry : placeholders.entrySet())
			raw = raw.replace(String.format(format, entry.getKey()), entry.getValue());

		return raw;
	}

	public static Builder builder()                              { return new Builder(); }
	public static StringPlaceholder of(String key, String value) { return builder().set(key, value).build(); }

	public static final StringPlaceholder EMPTY = builder().build();

	public static final class Builder {
		private final Map<String, String> map = new HashMap<>();

		private Builder() {}

		public Builder set(String key, String value){
			map.put(key, value);
			return this;
		}

		public StringPlaceholder build() { return new StringPlaceholder(map); }
	}
}
