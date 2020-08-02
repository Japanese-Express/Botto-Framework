package express.japanese.botto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import express.japanese.botto.misc.BotJsonReader;

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

    @Nullable
    public static String getStr(String s) {
        return jsonObject.has(s) ? jsonObject.get(s).getAsString() : null;
    }
    @Nullable
    public static JsonElement get(String s) {
        return jsonObject.has(s) ? jsonObject.get(s) : null;
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
        Object mbJson = BotJsonReader.readFromFile(conf);
        if(mbJson != null)
            jsonObject = (JsonObject) mbJson;
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

