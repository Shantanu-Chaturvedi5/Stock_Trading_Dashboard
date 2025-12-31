import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PriceFetcher {

    private static final String BASE_URL = "https://military-jobye-haiqstudios-14f59639.koyeb.app";

    public static double getPrice(String symbol) {
        try {
            String url = BASE_URL + "/stock?symbol=" + symbol + "&res=num";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject obj = new JSONObject(response.toString());
            if (obj.getString("status").equals("success")) {
                JSONObject data = obj.getJSONObject("data");
                return data.getDouble("last_price");
            } else {
                System.out.println("API error: " + obj.getString("message"));
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}