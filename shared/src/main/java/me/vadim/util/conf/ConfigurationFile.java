package me.vadim.util.conf;

import me.vadim.util.conf.io.BufferedFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

/**
 * Base class for configs. Default implementations are provided by submodules of this project.
 * @author vadim
 */
public abstract class ConfigurationFile<C> extends BufferedFile {

    protected final ResourceProvider resourceProvider;
    public final File                fromRoot;
    public ConfigurationFile(File file, ResourceProvider resourceProvider) {
        super(new File(resourceProvider.getDataFolder(), file.getPath()));
        this.fromRoot         = file;
        this.resourceProvider = resourceProvider;
    }

    public ConfigurationFile(String file, ResourceProvider resourceProvider) { this(new File(file), resourceProvider); }

    protected abstract C getConfiguration();
    protected abstract ConfigurationAccessor getConfigurationAccessor();

    //merge methods
    protected void setTemplate(C template){
        throw new UnsupportedOperationException();
    }

    protected void setDefaultTemplate(){
        throw new UnsupportedOperationException();
    }

    protected abstract Void logError(Logger log, String path);
    protected abstract Void logError(Logger log, String path, String name);

    public abstract void reload();

    protected abstract void createConfiguration() throws Exception;

    @Override
    public void load() {
        super.load();
        try {
            if(getBuffer().length == 0) {//empty (first load)
                String path = fromRoot.getPath();
                if(path.startsWith("/") || path.startsWith("\\") || path.startsWith(File.separator)) path = path.substring(1);
                path = '/' + path;//getResource is funky and wants the filename prepended with a slash ('/')
                InputStream resource = resourceProvider.getResource(path);
                if(resource != null)
                    Files.copy(resource, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                else if(!file.exists())
                    Files.createFile(file.toPath());
                super.load();
            }
            createConfiguration();
        } catch (Exception e) {
            sneaky(e);
        }
    }

}