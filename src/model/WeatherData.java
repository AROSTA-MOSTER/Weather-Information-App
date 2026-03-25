package model;

public class WeatherData {
    private String cityName;
    private double temperatureCelsius;
    private int humidity;
    private double windSpeedMetric; // m/s
    private String condition;
    private String iconCode;
    private long timestamp;

    public WeatherData() {}

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public double getTemperatureCelsius() { return temperatureCelsius; }
    public void setTemperatureCelsius(double temperatureCelsius) { this.temperatureCelsius = temperatureCelsius; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeedMetric() { return windSpeedMetric; }
    public void setWindSpeedMetric(double windSpeedMetric) { this.windSpeedMetric = windSpeedMetric; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getIconCode() { return iconCode; }
    public void setIconCode(String iconCode) { this.iconCode = iconCode; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
