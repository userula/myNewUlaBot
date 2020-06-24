import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    //http://api.openweathermap.org/data/2.5/weather?q=Nur-Sultan&appid=d950c07bf8ee62a158c511abe61e08b3
    //d950c07bf8ee62a158c511abe61e08b3
    private Model m;

    public String getWeather(String city, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=d950c07bf8ee62a158c511abe61e08b3");

        Scanner sc = new Scanner((InputStream) url.getContent());
        String res = "";
        while (sc.hasNext())
        {
            res += sc.nextLine();
        }

        JSONObject js = new JSONObject(res);
        model.setCityName(js.getString("name"));

        JSONObject main = js.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray jsarr = js.getJSONArray("weather");
        JSONObject jsobj = jsarr.getJSONObject(0);
        model.setIcon((String) jsobj.get("icon"));
        model.setMain((String) jsobj.get("main"));

        setM(model);

        return "City: " + model.getCityName() + "\n" + "Temperature: " + model.getTemp() + "C\n" + "Main: " + model.getMain() + "\n"
                + "Humidity: " + model.getHumidity() + "%\n";

    }

    public Model getM() {
        return m;
    }

    public void setM(Model m) {
        this.m = m;
    }
}
