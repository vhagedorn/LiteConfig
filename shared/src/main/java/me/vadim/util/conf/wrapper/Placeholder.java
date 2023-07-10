package me.vadim.util.conf.wrapper;

/**
 * Represents a value-holding object that can format messages.
 *
 * @author vadim
 */
public interface Placeholder {

	String format(String raw);

	Placeholder merge(Placeholder... other);

}