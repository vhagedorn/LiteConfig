package me.vadim.util.conf.wrapper;

/**
 * Represents a message that can be formatted via a {@link Placeholder}.
 *
 * @author vadim
 */
public interface PlaceholderMessage {

	public String raw();

	public String format(Placeholder placeholder);

}
