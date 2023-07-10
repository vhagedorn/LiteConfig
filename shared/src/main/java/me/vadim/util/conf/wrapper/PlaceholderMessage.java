package me.vadim.util.conf.wrapper;

/**
 * Represents a message that can be formatted via a {@link Placeholder}.
 *
 * @author vadim
 */
public interface PlaceholderMessage {

	String raw();

	String format(Placeholder placeholder);

}
