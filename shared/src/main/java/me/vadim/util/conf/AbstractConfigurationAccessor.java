package me.vadim.util.conf;

import me.vadim.util.conf.wrapper.PlaceholderMessage;
import me.vadim.util.conf.wrapper.impl.UnformattedMessage;

/**
 * @author vadim
 */
public abstract class AbstractConfigurationAccessor<C extends ConfigurationFile<?>> implements ConfigurationAccessor {

	protected final C file;

	public AbstractConfigurationAccessor(C file) {
		this.file = file;
	}

	@SuppressWarnings("unchecked") // use Void to trick compiler (read: kotlin's Nothing)
	protected <T> T nonNull(T nullable, String path) {
		return nullable == null ? (T) file.logError(path) : nullable;
	}

	/* default impls in case a parser does not have native primitives */

	@Override
	public char getChar(String path) {
		return getString(path).charAt(0);
	}

	@Override
	public PlaceholderMessage getPlaceholder(String path) {
		return new UnformattedMessage(getString(path));
	}

	@Override
	public byte getByte(String path) {
		try {
			return Byte.parseByte(getString(path));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public short getShort(String path) {
		try {
			return Short.parseShort(getString(path));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public int getInt(String path) {
		try {
			return Integer.parseInt(getString(path));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public long getLong(String path) {
		try {
			return Long.parseLong(getString(path));
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

	@Override
	public float getFloat(String path) {
		try {
			return Float.parseFloat(getString(path));
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	@Override
	public double getDouble(String path) {
		try {
			return Double.parseDouble(getString(path));
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

}
