package express.japanese.botto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import express.japanese.botto.misc.BotJsonReader;
import express.japanese.botto.misc.ReaderResponse;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class StaticConfig {
    //private static final JSONParser parser = new JSONParser();
    private static JsonObject jsonObject;

    static boolean debug = false;
    static boolean willPassWithoutToken = false;

    private static final String prefix;
    private static final String token;
    static String defaultAuthor;
    static String ownerId;

    public static Boolean contains(String s) {
        return jsonObject.has(s);
    }

    public static boolean has(String key) {
        return jsonObject.has(key);
    }
    @Nullable
    public static String getStr(String key) {
        return has(key) ? jsonObject.get(key).getAsString() : null;
    }
    @Nullable
    public static JsonElement get(String key) {
        return has(key) ? jsonObject.get(key) : null;
    }


    public static boolean isDebug() {
        return debug;
    }
    static String getToken() {
        return token;
    }
    public static String getOwnerId() {
        return ownerId;
    }
    public static String getPrefix() {
        return prefix;
    }
    public static String getDefaultAuthor() {
        return defaultAuthor;
    }

    public static void reload() {
        File conf = new File("config.json");
        if(!conf.exists()) {
            try {
                boolean b = conf.createNewFile();
                if(!b)
                    throw new RuntimeException("Failed to create config.json, possibly due to permissions");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ReaderResponse<JsonObject> mbJson = BotJsonReader.readFromFile(conf, JsonObject.class);
        if(!mbJson.responseCodeIsError())
            jsonObject = mbJson.getReturned();
    }

    static {
        StaticConfig.reload();
        if(jsonObject == null)
            throw new RuntimeException("JsonObject was null for config.json, stopping...");
        if(!jsonObject.has("prefix")) {
            jsonObject.addProperty("prefix", "!");
            System.out.println("\"prefix\" is not in config.json, so it's been created");
        }
        if(!StaticConfig.contains("token")) {
            jsonObject.addProperty("token", "?");
            System.out.println("\"token\" is not in config.json, so it's been created");
            System.out.println("   - NOTE: YOU MUST SET THE TOKEN YOURSELF!");
        }
        prefix = StaticConfig.getStr("prefix");
        token = StaticConfig.getStr("token");
    }
}

