package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

/**
 * Utils
 */
public class Utils {

  public static JSONObject readJsonFile(String filePath) {
    JSONObject jsonObject = null;
    try {
      InputStream is = new FileInputStream(filePath);
      BufferedReader buf = new BufferedReader(new InputStreamReader(is));
      String line = null;
      StringBuffer sBuf = new StringBuffer();
      while ((line = buf.readLine()) != null) {
        sBuf.append(line);
      }

      buf.close();

      jsonObject = new JSONObject(sBuf.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return jsonObject;
  }
}