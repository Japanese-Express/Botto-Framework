package express.japanese.botto.misc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.json.simple.parser.JSONParser;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BotJsonReader {
    private static final Gson gson = new Gson();
    private BotJsonReader() {}
    @Nullable
    public static JsonObject readFromFile(File file) {
        try {
            JsonReader reader = new JsonReader(new FileReader(file.toPath().toString()));
            return gson.fromJson(reader, JsonObject.class);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Nullable
    public static JsonObject readFromFile(String fileLoc) {
        try {
            JsonReader reader = new JsonReader(new FileReader(fileLoc));
            return gson.fromJson(reader, JsonObject.class);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static Object readFromURL(String url) {
        try {
            URLConnection uc = new URL(url).openConnection();
            uc.setRequestProperty("User-Agent", "");
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            JSONParser parser = new JSONParser();
            return parser.parse(br.readLine());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
