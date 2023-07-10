package me.vadim.util.conf.wrapper;

import me.vadim.util.conf.wrapper.impl.MergedPlaceholder;

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

}
