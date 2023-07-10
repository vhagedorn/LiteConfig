package me.vadim.util.conf.bukkit.wrapper;

import me.vadim.util.conf.wrapper.Placeholder;
import me.vadim.util.conf.wrapper.PlaceholderMessage;
import me.vadim.util.conf.wrapper.impl.UnformattedMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

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
			sender.sendMessage(colorize(format(placeholder)));
	}

	private static final Pattern HEX = Pattern.compile("&#([A-Fa-f0-9]{6})");

	public static String colorize(String string) {
		return string == null ? null : ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(string));
	}

	private static String translateHexColorCodes(String message) {
		Matcher       matcher = HEX.matcher(message);
		StringBuilder builder = new StringBuilder(message.length() + 4 * 8);
		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(builder, COLOR_CHAR + "x"
											   + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
											   + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
											   + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
									 );
		}
		return matcher.appendTail(builder).toString();
	}

}
