package me.vadim.util.conf.wrapper;

import me.vadim.util.conf.wrapper.impl.MergedPlaceholder;
import me.vadim.util.conf.wrapper.impl.StringPlaceholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility methods pertaining to {@link Placeholder} and {@link PlaceholderMessage} objects, especially in relation to collections.
 *
 * @author vadim
 */
public final class Placeholders {

	/**
	 * Formats a list of messages using the provided {@code placeholder}.
	 */
	public static List<String> format(Placeholder placeholder, List<PlaceholderMessage> messages) {
		return messages.stream().map(msg -> msg.format(placeholder)).collect(Collectors.toList());
	}

	/**
	 * Re-formats a list of strings using the provided {@code placeholder}.
	 */
	public static List<String> reformat(Placeholder placeholder, List<String> formatted) {
		return formatted.stream().map(placeholder::format).collect(Collectors.toList());
	}

	/**
	 * Constructs a new {@link Placeholder} that will format messages using all the provided {@code placeholders}.
	 */
	public static Placeholder merge(Placeholder... placeholders) {
		return new MergedPlaceholder(Arrays.asList(placeholders));
	}
	
	/* 
	 * Overloaded StringPlaceholder factory methods. 
	 * (having them in Builder is quite enough for a "straightforward and reusable example")
	 */

	public static StringPlaceholder of(@NotNull String key, byte value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, short value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, int value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, long value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, float value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, double value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, boolean value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, char value) { return StringPlaceholder.builder().set(key, value).build(); }

	public static StringPlaceholder of(@NotNull String key, @Nullable Object value) { return StringPlaceholder.builder().set(key, value).build(); }

}
