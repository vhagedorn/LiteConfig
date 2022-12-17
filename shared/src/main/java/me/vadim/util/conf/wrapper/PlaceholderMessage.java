package me.vadim.util.conf.wrapper;

/**
 * @author vadim
 */
public interface PlaceholderMessage {

    String raw();

    String format(Placeholder placeholder);

}
