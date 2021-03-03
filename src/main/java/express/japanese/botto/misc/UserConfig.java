package express.japanese.botto.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import express.japanese.botto.misc.modulistic.BotJsonReader;
import express.japanese.botto.misc.modulistic.ReaderResponse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * This file is not final and will change.
 */
public class UserConfig {
    public static HashMap<String/*cfg Name*/, UserConfig> configHashMap;
    public static UserConfig initializeConfig(String cfgLocation) {
        if(configHashMap.containsKey(cfgLocation))
            return configHashMap.get(cfgLocation);
        UserConfig userConfig = new UserConfig(cfgLocation);
        configHashMap.put(cfgLocation, userConfig);
        return userConfig;
    }
    public static boolean configExists(String cfgLocation) {
        File f = new File(cfgLocation);
        return f.exists() && !f.isDirectory();
    } static {
        configHashMap = new HashMap<>();
    }

    private boolean hasErrors = false;
    private final String cfgLocation;
    private Charset charset;
    private JsonObject jsonObject;
    private File file;
    UserConfig(String cfgLocation) {
        this.charset = StandardCharsets.UTF_8;
        this.cfgLocation = cfgLocation;
        this.file = new File(cfgLocation);
        if(!file.exists()) {
            try {
                boolean b = file.createNewFile();
                if(!b) {
                    System.err.println("Failed to create config at location");
                    System.err.println(cfgLocation);
                    return;
                }
                this.jsonObject = new JsonObject();
                if(this.file.canWrite())
                    this.write();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if(this.file.exists() && this.file.canRead())
            this.reload();
    }

    public final boolean has(String key) {
        return jsonObject.has(key);
    }
    public final JsonElement get(String key) {
        return jsonObject.get(key);
    }

    public final void add(String key, Number value) {
        jsonObject.addProperty(key, value);
    }
    public final void add(String key, char value) {
        jsonObject.addProperty(key, value);
    }
    public final void add(String key, boolean value) {
        jsonObject.addProperty(key, value);
    }
    public final void add(String key, String value) {
        jsonObject.addProperty(key, value);
    }
    public final void add(String key, JsonElement value) {
        jsonObject.add(key, value);
    }

    public JsonObject getJsonObj() {
        return jsonObject;
    }
    public void updateJsonObj(JsonObject jsonObj) {
        this.jsonObject = jsonObj;
        this.write();
        this.reload();
    }
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
    public void write() {
        List<String> lines = Collections.singletonList(this.jsonObject.toString());
        Path path = Paths.get(this.file.getAbsolutePath());
        try {
            Files.write(path, lines, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.reload();
    }
    public void reload() {
        ReaderResponse<JsonObject> mbJson = BotJsonReader.readFromFile(this.file, JsonObject.class);
        if(!mbJson.responseCodeIsError()) {
            this.jsonObject = mbJson.getResponse();
        } else {
            System.err.println(mbJson.getMessage());
        }
    }

    public String getCfgLocation() {
        return cfgLocation;
    }
}
