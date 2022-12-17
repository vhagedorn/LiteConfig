package me.vadim.util.conf.gson;

import me.vadim.util.conf.ConfigurationAccessor;
import me.vadim.util.conf.LiteConfig;
import me.vadim.util.conf.ResourceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

/**
 * @author vadim
 */
class GsonTest {

	private        LiteConfig       lite;
	private static ResourceProvider resourceProvider;

	@BeforeAll
	static void setUp() {
		resourceProvider = new ResourceProvider() {
			@Override
			public File getDataFolder() {
				return new File("target/test");
			}

			@Override
			public InputStream getResource(String name) {
				return getClass().getResourceAsStream(name);
			}

			@Override
			public Logger getLogger() {
				return Logger.getLogger(GsonTest.class.getSimpleName());
			}
		};
	}

	@Test
	void resourcesTest() {
		Assertions.assertNotNull(resourceProvider.getResource('/' + new ConfigA(resourceProvider).fromRoot.getPath()));
		Assertions.assertNotNull(resourceProvider.getLogger());
		Assertions.assertEquals(resourceProvider.getDataFolder(), new File("target/test"));
		resourceProvider.getLogger().info("Testing the resource provider's logger.");
	}

	@Test
	void firstLoadTest() throws IOException {
		//initialize
		LiteConfig config = new LiteConfig(resourceProvider);
		config.register(ConfigA.class, ConfigA::new);

		//access
		ConfigA a = config.open(ConfigA.class);
		if(a.file.exists()) Files.delete(a.file.toPath());
		a.load();

		ConfigurationAccessor ca = a.getConfigurationAccessor();

		Assertions.assertEquals(-2, ca.getInt("int"));

		Assertions.assertEquals("Hello, world!", ca.getString("string"));

		ConfigurationAccessor o = ca.getObject("object");
		Assertions.assertNotNull(o);
		Assertions.assertEquals(3.14, o.getDouble("double"));

		Assertions.assertEquals(0, o.getChildren().length);
		Assertions.assertEquals(1, ca.getChildren().length);
//		Assertions.assertArrayEquals(ca.getList("list"), new String[]{ "a", "b", "c" });//todo lists

		Assertions.assertNull(ca.getString("null"));
		Assertions.assertNull(ca.getObject("null"));
		Assertions.assertEquals(0, ca.getInt("null"));
		Assertions.assertFalse(ca.getBoolean("null"));
	}

}