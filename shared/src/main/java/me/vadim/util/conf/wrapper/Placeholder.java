package me.vadim.util.conf.wrapper;

/**
 * Represents a value-holding object that can format messages.
 *
 * @author vadim
 */
public interface Placeholder {

	public String format(String raw);

	public Placeholder merge(Placeholder... other);

}