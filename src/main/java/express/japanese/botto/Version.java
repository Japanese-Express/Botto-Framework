package express.japanese.botto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Version {
    public static final int[] current = new int[]{1, 0, 1};
    public static String latest;
    private final String url = "https://api.github.com/repos/Japanese-Express/Botto-Framework/releases";

    static void CompareVersion() {
        JSONParser parser = new JSONParser();
        try {
            int[] tag;
            URL oracle = new URL("https://api.github.com/repos/Japanese-Express/Botto-Framework/releases");
            URLConnection uc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = in.readLine();
            Object a = parser.parse(inputLine);
            if(((JSONArray)a).size() < 1)
                return;
            JSONObject ready = (JSONObject)((JSONArray)a).get(0);
            String tagSuper = latest = ((String)ready.get("tag_name")).replaceAll("[^\\d.]", "");
            boolean containsMinors = false;
            if (tagSuper.contains(".")) {
                containsMinors = true;
                tag = Arrays.stream(tagSuper.split("\\.")).mapToInt(Integer::parseInt).toArray();
            } else
                tag = new int[]{Integer.parseInt(tagSuper)};
            System.out.println("Comparing versions between " + current[0] + "." + current[1] + "." + current[2] + " and " + tagSuper + "...");
            Object pub = ready.get("published_at");
            if (!containsMinors) {
                if (current[0] > tag[0]) {
                    System.out.println("\nA new version of Botto-Framework was released " + pub);
                    System.out.println("  You have version " + current[0] + "." + current[1] + "." + current[2] + ", the new version is " + tagSuper);
                    System.out.println("You can find it at https://github.com/Japanese-Express/Botto-Framework/releases/latest\n");
                    return;
                }
            } else {
                for (int i = 0; i < tag.length; ++i) {
                    if (current[i] >= tag[i]) continue;
                    System.out.println("\nA new version of Bot_Ant-Bot-Core was released at " + pub);
                    System.out.println("  You have version " + current[0] + "." + current[1] + "." + current[2] + ", the new version is " + tagSuper);
                    System.out.println("You can find it at https://github.com/Japanese-Express/Botto-Framework/releases/latest\n");
                    return;
                }
            }
            if(StaticConfig.debug)
                System.out.println("Version is fine. Moving on\n");
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}

