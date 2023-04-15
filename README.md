# LiteConfig

A lightweight configuration wrapper. Currently supports Spigot's ConfigurationSection API, and GSON from Google.

## Dependency

To use:

### Install
1. Clone this repo into a new folder
2. Open the folder
3. Build & publish

```bash
git clone https://github.com/RuthlessJailer/LiteConfig/
cd LiteConfig
./gradlew build test publishToMavenLocal
```

### Apply

#### Gradle
```gradle
repositories {
  mavenLocal()
  // ...
}

dependencies {
    implementation 'me.vadim.util.conf:LiteConfig-shared:1.1'
    implementation 'me.vadim.util.conf:LiteConfig-<dist>:1.1' // replace <dist> with 'bukkit' or 'gson'
    // ...
}
```

### Maven
```xml
<dependencies>
	<dependency>
		<groupId>me.vadim.util.conf</groupId>
		<artifactId>LiteConfig-shared</artifactId>
		<version>1.1</version>
		<scope>compile</scope>
	</dependency>
  
	<!-- Replace <dist> with 'bukkit' or 'gson' -->
  <dependency>
		<groupId>me.vadim.util.conf</groupId>
		<artifactId>LiteConfig-<dist></artifactId>
		<version>1.1</version>
		<scope>compile</scope>
	</dependency>
	    
	<!-- ... -->
</dependencies>
```

## Usage

Main class:
```java
public class Test implements ResourceProvider {
    private final ConfigurationManager configMgr = new LiteConfig(this);
    
    public void start(){
       configMgr.register(ConfigMini.class, ConfigMini::new);
        
       ConfigMini mini = configMgr.open(ConfigMini.class);
        
       //access to config class
       mini.shopkeeper = 69;
    }

    public void reload(){
        configMgr.reload();
    }
    
    // implement methods from ResourceProvider
    
}
```

Config class:
```java

public class ConfigMini extends YamlFile {

    public static final String SK_ID = "shopkeeper_id";
    
    public ConfigMini(ResourceProvider rp) {
        super("config.yml", rp);
    }
  
    public int shopkeeper;
  
    //after this method is called, the configuration is cached and can be accessed anywhere
    //this example simply syncs the shopkeeper field to the config upon save/load 
    @Override
    public void load() {
        super.load();
    
        //load values from configuration
        shopkeeper = yaml.getInt(SK_ID);
    }

    //same here. the configuration is not cleared after this method is called
    //we're just syncing the shopkeeper field in this example
    @Override
    public void save() {
        //write values to configuration
        yaml.set(SK_ID, shopkeeper);
        
        super.save();
    }
    
    //you can also use the ConfigurationAccessor, which encapsulates the underlying APIs
    public String getShopkeeperGreeting(){ return getConfigurationAccessor().getString("messages.shopkeeper-greeting"); }
}
```
