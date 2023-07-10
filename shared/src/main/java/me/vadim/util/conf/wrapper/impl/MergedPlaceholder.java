package me.vadim.util.conf.wrapper.impl;

import me.vadim.util.conf.wrapper.Placeholder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a combined placeholder that formats messages with all backing placeholders.
 *
 * @author vadim
 */
public class MergedPlaceholder implements Placeholder {

	private final List<Placeholder> placeholders;

	public MergedPlaceholder(List<Placeholder> placeholders) {
		this.placeholders = placeholders.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public String format(String raw) {
		for (Placeholder placeholder : placeholders)
			raw = placeholder.format(raw);
		return raw;
	}

	@Override
	public Placeholder merge(Placeholder... other) {
		return new MergedPlaceholder(Stream.concat(placeholders.stream(), Arrays.stream(other)).collect(Collectors.toList()));
	}

}
