package me.vadim.util.conf.bukkit.wrapper;

import me.vadim.util.conf.bukkit.BukkitPlaceholders;
import me.vadim.util.conf.wrapper.Placeholder;
import me.vadim.util.conf.wrapper.PlaceholderMessage;
import me.vadim.util.conf.wrapper.impl.UnformattedMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A {@link PlaceholderMessage} that is optional. If no message is present, then nothing will be send to receivers.
 *
 * @author vadim
 */
public class OptionalMessage extends UnformattedMessage {

	private final boolean send;

	public OptionalMessage(@Nullable String value) {
		super(Optional.ofNullable(value).orElse(""));
		this.send = value != null && !value.trim().isBlank();
	}

	public OptionalMessage(@Nullable PlaceholderMessage value) {
		this(Optional.ofNullable(value).map(PlaceholderMessage::raw).orElse(null));
	}

	/**
	 * Send this {@link PlaceholderMessage} to {@code sender}, only if this message has content.
	 */
	public void sendTo(@NotNull CommandSender sender, @NotNull Placeholder placeholder) {
		if (send)
			sender.sendMessage(BukkitPlaceholders.colorize(format(placeholder)));
	}

}
