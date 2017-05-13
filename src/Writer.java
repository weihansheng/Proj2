import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Johan007 on 17/5/11.
 */
public class Writer {
    public static boolean write(String fileName, StringBuffer sBuffer) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(file, true);// append mod
            out.write(sBuffer.toString().getBytes("utf-8"));
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }
}
