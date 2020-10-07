package express.japanese.botto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import express.japanese.botto.misc.UserConfig;
import express.japanese.botto.misc.modulistic.BotJsonReader;
import express.japanese.botto.misc.modulistic.ReaderResponse;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * <h2>Static Configuration File Reader</h1>
 *
 * <p>Static Configuration, used for global bot
 * definitions and variables such as global
 * prefix and other things, based on JSON for
 * <b>./config.json</b> file on users bot</p>
 *
 * <p>StaticConfig cannot be written to but, if
 * the file is manually edited you can
 * expect to get global variables from this
 * file if you wish to.</p><br/>
 *
 * <p>If you're looking to write and read on the
 * fly, you can also use the UserConfig file</p>
 * @see UserConfig
 */
public class StaticConfig {
    //private static final JSONParser parser = new JSONParser();
    private static JsonObject jsonObject;

    static boolean debug = false;
    static boolean willPassWithoutToken = false;

    private static final boolean shouldReadDefaults = false;

    private static final String prefix;
    private static final String token;
    static String defaultAuthor;
    static String ownerId;

    /**
     * Does this module contain a key value
     * @param s key value for check
     * @return boolean
     * @see StaticConfig
     */
    public static Boolean contains(String s) {
        return jsonObject.has(s);
    }

    /**
     * Same as contains, but for JSON styling
     * @param key key value for check
     * @return boolean
     * @see StaticConfig
     */
    public static boolean has(String key) {
        return jsonObject.has(key);
    }

    /**
     * Get value as string from map with key
     * @param key key value for map
     * @return Possibly nulled String value if invalid
     * @see StaticConfig
     */
    @Nullable
    public static String getStr(String key) {
        return has(key) ? jsonObject.get(key).getAsString() : null;
    }
    /**
     * Get full JsonElement from map with key
     * @param key key value for map
     * @return Possibly nulled String value if invalid
     * @see StaticConfig
     */
    @Nullable
    public static JsonElement get(String key) {
        return has(key) ? jsonObject.get(key) : null;
    }


    /**
     * Check if Botto-Framework is in Debug mode
     * @return boolean
     * @see StaticConfig
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * <b>Not public</b>, should not be used in a
     * production environment.<br/><br/>
     *
     * Grabs global value from JSON config of
     * the users global token, which is used for
     * all bots created by default.
     * @return String of bot token
     * @see StaticConfig
     */
    static String getToken() {
        return token;
    }

    /**
     * Get global bot owner ID
     * @return String of owner ID
     * @see StaticConfig
     */
    public static String getOwnerId() {
        return ownerId;
    }

    /**
     * Get global bot prefix from JSON config
     * @return String value of prefix
     * @see StaticConfig
     */
    public static String getPrefix() {
        return prefix;
    }

    /**
     * Get global default author for all controllers
     * @return String of author
     * @see StaticConfig
     */
    public static String getDefaultAuthor() {
        return defaultAuthor;
    }

    /**
     * Fully reload entire JSON file of ./config.json
     * @see StaticConfig
     */
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
            jsonObject = mbJson.getResponse();
    }

    static {
        StaticConfig.reload();
        if(shouldReadDefaults) {
            if (jsonObject == null)
                throw new RuntimeException("JsonObject was null for config.json, stopping...");
            if (!jsonObject.has("prefix")) {
                jsonObject.addProperty("prefix", "!");
                System.out.println("\"prefix\" is not in config.json, so it's been created");
            }
            if (!StaticConfig.contains("token")) {
                jsonObject.addProperty("token", "?");
                System.out.println("\"token\" is not in config.json, so it's been created");
                System.out.println("   - NOTE: YOU MUST SET THE TOKEN YOURSELF!");
            }
            prefix = StaticConfig.getStr("prefix");
            token = StaticConfig.getStr("token");
        } else {
            prefix = null;
            token = null;
        }
    }
}

