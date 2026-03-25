package api;

import model.ForecastData;
import model.WeatherData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherClient {
    private static final String API_KEY_FILE = "config.properties";
    private String apiKey;

    public WeatherClient() {
        loadApiKey();
    }

    private void loadApiKey() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(API_KEY_FILE)) {
            if (input == null) {
                // fallback for simple compilation
                try (InputStream fileInput = new java.io.FileInputStream(API_KEY_FILE)) {
                    Properties prop = new Properties();
                    prop.load(fileInput);
                    apiKey = prop.getProperty("api.key");
                }
            } else {
                Properties prop = new Properties();
                prop.load(input);
                apiKey = prop.getProperty("api.key");
            }
        } catch (Exception ex) {
            System.err.println("Could not load API key: " + ex.getMessage());
        }
    }

    public WeatherData getCurrentWeather(String location) throws Exception {
        if (apiKey == null || apiKey.equals("YOUR_API_KEY_HERE")) {
            throw new Exception("Invalid API Key. Please update config.properties");
        }

        String urlString = buildUrl("https://api.openweathermap.org/data/2.5/weather", location);
        String json = fetchJson(urlString);

        // Parse JSON using Regex (to avoid external dependencies for the assignment)
        WeatherData data = new WeatherData();
        data.setCityName(extractString(json, "\"name\":\"([^\"]+)\""));
        
        String mainObj = extractGroup(json, "\"main\":\\{([^}]+)\\}");
        if (mainObj != null) {
            data.setTemperatureCelsius(extractDouble(mainObj, "\"temp\":([\\d\\.-]+)"));
            data.setHumidity((int) extractDouble(mainObj, "\"humidity\":([\\d\\.-]+)"));
        }

        String windObj = extractGroup(json, "\"wind\":\\{([^}]+)\\}");
        if (windObj != null) {
            data.setWindSpeedMetric(extractDouble(windObj, "\"speed\":([\\d\\.-]+)"));
        }

        String weatherObj = extractGroup(json, "\"weather\":\\[\\{([^}]+)\\}\\]");
        if (weatherObj != null) {
            data.setCondition(extractString(weatherObj, "\"main\":\"([^\"]+)\""));
            data.setIconCode(extractString(weatherObj, "\"icon\":\"([^\"]+)\""));
        }
        
        data.setTimestamp(System.currentTimeMillis());

        return data;
    }

    public ForecastData getForecast(String location) throws Exception {
        if (apiKey == null || apiKey.equals("YOUR_API_KEY_HERE")) {
            throw new Exception("Invalid API Key. Please update config.properties");
        }

        String urlString = buildUrl("https://api.openweathermap.org/data/2.5/forecast", location);
        String json = fetchJson(urlString);

        ForecastData forecastData = new ForecastData();
        
        // Match the list array
        Pattern listPattern = Pattern.compile("\\{\"dt\":\\d+.*?(?=\\{\"dt\":|\\}\\]\\})");
        Matcher listMatcher = listPattern.matcher(json);
        
        // OpenWeatherMap returns forecasts every 3 hours. Let's pick one per day (e.g., around noon) 
        // Or simply take the first 5 entries that represent different days.
        String lastDate = "";
        while (listMatcher.find()) {
            String itemJson = listMatcher.group();
            String dtTxt = extractString(itemJson, "\"dt_txt\":\"([^\"]+)\""); // e.g., 2026-03-25 12:00:00
            if (dtTxt != null) {
                String dateOnly = dtTxt.substring(0, 10);
                if (!dateOnly.equals(lastDate) && dtTxt.contains("12:00:00")) {
                    ForecastData.DailyForecast df = new ForecastData.DailyForecast();
                    df.setDateText(dateOnly);
                    
                    String mainObj = extractGroup(itemJson, "\"main\":\\{([^}]+)\\}");
                    if (mainObj != null) {
                        df.setTemp(extractDouble(mainObj, "\"temp\":([\\d\\.-]+)"));
                    }
                    
                    String weatherObj = extractGroup(itemJson, "\"weather\":\\[\\{([^}]+)\\}\\]");
                    if (weatherObj != null) {
                        df.setCondition(extractString(weatherObj, "\"main\":\"([^\"]+)\""));
                        df.setIconCode(extractString(weatherObj, "\"icon\":\"([^\"]+)\""));
                    }
                    
                    forecastData.addForecast(df);
                    lastDate = dateOnly;
                    
                    if (forecastData.getDailyForecasts().size() >= 5) {
                        break;
                    }
                }
            }
        }
        
        return forecastData;
    }

    private String buildUrl(String baseUrl, String location) {
        // Simple check for coordinates (lat,lon)
        if (location.matches("^-?\\d+(\\.\\d+)?\\s*,\\s*-?\\d+(\\.\\d+)?$")) {
            String[] parts = location.split(",");
            return baseUrl + "?lat=" + parts[0].trim() + "&lon=" + parts[1].trim() + "&appid=" + apiKey + "&units=metric";
        } else {
            return baseUrl + "?q=" + location.replace(" ", "%20") + "&appid=" + apiKey + "&units=metric";
        }
    }

    private String fetchJson(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("API Request failed. Response code: " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    // Helper regex methods for rudimentary JSON parsing
    private String extractString(String json, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private double extractDouble(String json, String regex) {
        String val = extractString(json, regex);
        if (val != null) {
            try {
                return Double.parseDouble(val);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private String extractGroup(String json, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
