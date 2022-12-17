package me.vadim.util.conf.wrapper.impl;

import me.vadim.util.conf.wrapper.Placeholder;
import me.vadim.util.conf.wrapper.PlaceholderMessage;

/**
 * @author vadim
 */
public class UnformattedMessage implements PlaceholderMessage {

	private final String value;

	public UnformattedMessage(String value){
		this.value = value;
	}

	@Override
	public String raw() {
		return value;
	}

	@Override
	public String format(Placeholder placeholder) {
		return placeholder.format(value);
	}

}