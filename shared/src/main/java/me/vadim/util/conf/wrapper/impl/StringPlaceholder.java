package me.vadim.util.conf.wrapper.impl;

import me.vadim.util.conf.wrapper.Placeholder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a generic map-based placeholder for string values. Custom implementations are also possible, but this a straightforward and reusable example. <p>
 * The {@link #format} defines what the placeholders should look like in the user-inputted text. The default value is {@code "{%s}"},
 * therefore, in a configuration, a {@code "NAME"} placeholder would look like:
 * <ul><li> {@code value: "This is a string in a config. Hello, {NAME}!" } </li></ul>
 * And may be filled in like so:
 * <pre>
 * {@code
 * StringPlaceholder.builder().set("NAME", "World").build().format(placeholderMessageFromConfig);
 * }
 * </pre>
 * Which would yield the following message:
 * <ul><li>{@code "This is a string in a config. Hello, World!" } </li></ul>
 * <p>
 * Users wanting custom formats are advised to extend this class and provide a custom static method, e.g.:
 * <pre>{@code
 * public class PercentPlaceholder extends StringPlaceholder {
 *
 * 	// extra %s due to usage of String.format(message, key) in StringPlaceholder
 * 	private static final String format = "%%%s%%";
 *
 *    public PercentPlaceholder(String format, Map<String, String> placeholders) {
 * 		super(format, placeholders);
 *    }
 *
 *    public static Builder builder() {
 * 		return StringPlaceholder.builder().setFormat(format);
 *    }
 *
 *    public static StringPlaceholder of(String key, String value) {
 * 		return builder().set(key, value).build();
 *    }
 * }
 * }</pre>
 *
 * @author vadim
 */
public class StringPlaceholder implements Placeholder {

	public final String format;

	private final Map<String, String> placeholders;

	public StringPlaceholder(String format, Map<String, String> placeholders) {
		this.format       = format;
		this.placeholders = placeholders;
	}

	@Override
	public String format(String raw) {
		for (Map.Entry<String, String> entry : placeholders.entrySet())
			raw = raw.replace(String.format(format, entry.getKey()), entry.getValue());

		return raw;
	}

	@Override
	public Placeholder merge(Placeholder... other) {
		return new MergedPlaceholder(Arrays.asList(other));
	}

	public static Builder builder() { return new Builder(); }

	public static StringPlaceholder of(String key, String value) { return builder().set(key, value).build(); }

	public static final StringPlaceholder EMPTY = builder().build();

	public static final class Builder {

		private final Map<String, String> map = new HashMap<>();
		private String format = "{%s}";

		private Builder() { }

		public Builder set(String key, String value) {
			map.put(key, value);
			return this;
		}

		public Builder setFormat(String format) {
			this.format = format;
			return this;
		}

		public StringPlaceholder build() { return new StringPlaceholder(format, map); }

	}

}
