package express.japanese.botto.misc;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class BotJsonReader {
    private static final Gson gson = new Gson();
    private BotJsonReader() {}

    public static <T> ReaderResponse<T> readFromFile(File file, Type typeOf) {
        try {
            JsonReader reader = new JsonReader(new FileReader(file.toPath().toString()));
            return new ReaderResponse<>(gson.fromJson(reader, typeOf), "Success", 1);
        } catch(Exception e) {
            return new ReaderResponse<>(e.getMessage(), 0);
        }
    }
    public static <T> ReaderResponse<T> readFromFile(String fileLoc, Type typeOf) {
        try {
            JsonReader reader = new JsonReader(new FileReader(fileLoc));
            return new ReaderResponse<>(gson.fromJson(reader, typeOf), "Success", 1);
        } catch(Exception e) {
            return new ReaderResponse<>(e.getMessage(), 0);
        }
    }

    public static <T> ReaderResponse<T> readFromURL(String url, Type typeOf) {
        try {
            HttpURLConnection uc = (HttpURLConnection) new URL(url).openConnection();
            uc.setRequestProperty("User-Agent", "");
            if(uc.getResponseCode() != 200)
                return new ReaderResponse<>(uc.getResponseMessage(), uc.getResponseCode());
            System.out.println(uc.getResponseCode() + ": " + uc.getResponseMessage());
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            JsonReader reader = new JsonReader(br);
            return new ReaderResponse<>(gson.fromJson(reader, typeOf), uc.getResponseMessage(), uc.getResponseCode());
        } catch(Exception e) {
            return new ReaderResponse<>(e.getMessage(), 0);
        }
    }
}
