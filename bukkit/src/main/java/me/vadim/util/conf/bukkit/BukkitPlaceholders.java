package me.vadim.util.conf.bukkit;

import me.vadim.util.conf.wrapper.Placeholder;
import me.vadim.util.conf.wrapper.Placeholders;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

}
