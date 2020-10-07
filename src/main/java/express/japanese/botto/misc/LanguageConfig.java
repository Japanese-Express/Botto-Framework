package express.japanese.botto.misc;

import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LanguageConfig {
    public static String exampleConfig =
            "language.translator=Unknown\n" +
            "language.name=English\n" +
            "language.region=US\n" +
            "language.code=en_us\n\n" +
            "example.thing=Test1\n" +
            "example.another=Test2\n\n" +
            "How to use:\n" +
            "Inside a module there are language utilities\n" +
            "Inside a cmd: \n" +
            "  Language lang = this.getUserLang(user);\n" +
            "  LanguageConfig config = getLang(lang);" +
            "  String translator = config.get(\"language.translator\");\n" +
            "  msg.getChannel().sendMessage(translator).queue();\n\n" +
            "This is basically all you need. Update your lang files accordingly!";

    public String langTranslator;
    public String langRegion;
    public String langCode;
    public String langName;

    private Map<String, String> lang;
    public LanguageConfig(File file) {
        if(!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                boolean b = file.createNewFile();
                if(!b)
                    throw new IOException("Can't create file " + file.getAbsolutePath());
                FileWriter myWriter = new FileWriter(file);
                myWriter.write(exampleConfig);
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        lang = new HashMap<>();
        try {
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            while((line = reader.readLine()) != null) {
                if(line.isEmpty())
                    continue;
                String[] split = line.split("=");
                if(split.length < 2)
                    continue;
                if(split[0].startsWith("language.")) {
                    String[] dot = split[0].split("\\.");
                    switch(dot[1]) {
                        case "name":
                            langName = split[1];break;
                        case "region":
                            langRegion = split[1];break;
                        case "code":
                            langCode = split[1];break;
                        case "translator":
                            langTranslator = split[1];break;
                        default:
                            lang.put(split[0], split[1]);break;
                    }
                } else
                    lang.put(split[0], split[1]);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        if(!lang.containsKey(key))
            return "TRANSLATION ERROR!";
        return lang.get(key);
    }
    public String get(String key, User user) {
        String value = get(key);
        value = value.replaceAll("%UserName%", user.getName());
        value = value.replaceAll("%UserId%", user.getId());
        value = value.replaceAll("%UserMention%", user.getAsMention());
        return value;
    }
}
