package me.vadim.util.conf.bukkit;

import me.vadim.util.conf.wrapper.Placeholder;
import me.vadim.util.conf.wrapper.Placeholders;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

/**
 * An extension of {@link me.vadim.util.conf.wrapper.Placeholders} that deals specificly with bukkit scenarios.
 * @author vadim
 */
public final class BukkitPlaceholders {

	/**
	 * Formats {@linkplain ItemMeta#getDisplayName() display name} and {@linkplain ItemMeta#getLore() lore} of an item using {@code placeholder}.
	 * @param placeholder the {@link Placeholder} to apply
	 * @param item the {@link ItemStack} to edit
	 * @return {@code item}, mutated
	 */
	@Contract(mutates = "param2")
	public static ItemStack format(@NotNull Placeholder placeholder, @Nullable ItemStack item) {
		if(item == null)
			return null;
		if(!item.hasItemMeta())
			return item;
		ItemMeta meta = item.getItemMeta();
		if(meta == null)
			return item;

		String displayName = meta.getDisplayName();
		displayName = placeholder.format(displayName);
		meta.setDisplayName(displayName);

		List<String> lore = meta.getLore();
		if(lore == null)
			lore = new ArrayList<>();
		else
			lore = new ArrayList<>(lore);
		lore = Placeholders.reformat(placeholder, lore);
		meta.setLore(lore);

		item.setItemMeta(meta);

		return item;
	}

	private static final Pattern HEX = Pattern.compile("&#([A-Fa-f0-9]{6})");

	/**
	 * Colorize a string using {@link ChatColor} ({@code '&'}) codes and hex color codes in the format {@code '&#rrggbb'}.
	 */
	public static String colorize(String string) {
		return string == null ? null : ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(string));
	}

	private static final Pattern STRIP_AMP = Pattern.compile("(?i)[&" + COLOR_CHAR + "][0-9A-FK-ORX]");
	private static final Pattern STRIP_HEX = Pattern.compile("(?i)&#([A-Fa-f0-9]{6})");

	/**
	 * Performs the following on {@code string}:
	 * <ul>
	 *     <li>strips {@link ChatColor} codes (for both {@code '&'} and {@code '§'})</li>
	 *     <li>strips hex color codes in the format {@code 'X#rrggbb'}, where X is either {@code '&'} or {@code '§'}</li>
	 *     <li>any sanitization that {@link ChatColor#stripColor(String)} performs</li>
	 * </ul>
	 */
	public static String stripColor(String string) {
		if (string == null) return null;
		string = STRIP_HEX.matcher(string).replaceAll("");
		string = STRIP_AMP.matcher(string).replaceAll("");
		return ChatColor.stripColor(string);
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
