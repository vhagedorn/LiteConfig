package me.vadim.util.conf.io;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Utility wrapper for a file on the disk. It may not yet exist
 * @author vadim
 */
public class BufferedFile {

    public final File file;

    private byte[] buffer;

    protected byte[] getBuffer(){ return Arrays.copyOf(buffer, buffer.length); }

    public BufferedFile(File file) {
        if(file.isDirectory()) throw new IllegalArgumentException("Provided file is a directory.");
        this.file = file;
    }

    public BufferedFile(String file) {
        this(new File(file));
    }

    /**
     * @return a direct copy of the buffer
     */
    public byte[] getRawContent(){ return Arrays.copyOf(buffer, buffer.length); }

    /**
     * Directly set the buffer with a copy.
     */
    public void setRawContent(byte[] buf){ buffer = Arrays.copyOf(buf, buf.length); }

    /**
     * Retrieve the buffer. Decodes the string with the default charset.
     * @return the current buffer
     */
    public String getContent(){
        return new String(buffer);
    }

    /**
     * Retrieve the buffer.
     * @param charset the charset to decode the buffer with
     * @return the current buffer
     */
    public String getContent(Charset charset){
        return new String(buffer, charset);
    }

    /**
     * Update the buffer. Encodes the string with the default charset.
     * @param string the new content
     */
    public void setContent(String string){
        buffer = string.getBytes();
    }

    /**
     * Update the buffer.
     * @param string the new content
     * @param charset the charset to encode the string with
     */
    public void setContent(String string, Charset charset){
        buffer = string.getBytes(charset);
    }

    /**
     * Replace the contents of the buffer with the file's.
     */
    public void load() {
        create();
        try(InputStream stream = getInputStream()) {
            buffer = stream.readAllBytes();
        } catch (IOException e){
            sneaky(e);
        }
    }

    /**
     * Replace the file with the contents of the buffer.
     */
    public void save(){
        create();
        try(OutputStream stream = getOutputStream()) {
            stream.write(buffer);
        } catch (IOException e){
            sneaky(e);
        }
    }

    /**
     * Creates the file and any parent files if it does not yet exist.
     */
    public void create(){
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();

                if (!parent.exists())
                    parent.mkdirs();

                file.createNewFile();
            }
        } catch (IOException e){
            sneaky(e);
        }
    }

    public FileInputStream getInputStream() throws FileNotFoundException { return new FileInputStream(this.file); }

    public FileOutputStream getOutputStream() throws FileNotFoundException { return new FileOutputStream(this.file); }

    @SuppressWarnings("unchecked")
    protected static <T extends Throwable> void sneaky(Throwable t) throws T {
        throw (T) t;
    }

}
