package express.japanese.botto.misc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Old Code.
 * May be updated, may not be, dunno yet.
 */
public class Images {
    public Boolean download(String url, String fileName) {
        try {
            int count;
            URLConnection con = new URL(url).openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            InputStream in = con.getInputStream();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileName + ".png"));
            while ((count = in.read()) != -1) {
                out.write(count);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteAfterDelay(int seconds, final String fileName, Boolean deleteOnExit) {
        if (deleteOnExit)
            new File(fileName + ".png").deleteOnExit();
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                new File(fileName + ".png").delete();
            }
        }, 1000 * seconds);
    }
}

