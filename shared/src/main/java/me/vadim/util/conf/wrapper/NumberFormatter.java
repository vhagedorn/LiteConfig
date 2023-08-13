package me.vadim.util.conf.wrapper;

import org.jetbrains.annotations.NotNull;

/**
 * @author vadim
 */
@FunctionalInterface
public interface NumberFormatter {

	public static NumberFormatter valueOf = String::valueOf;

	public String formatNumber(@NotNull Number number);

}
