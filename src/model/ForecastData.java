package model;

import java.util.ArrayList;
import java.util.List;

public class ForecastData {
    private List<DailyForecast> dailyForecasts;

    public ForecastData() {
        dailyForecasts = new ArrayList<>();
    }

    public List<DailyForecast> getDailyForecasts() { return dailyForecasts; }
    public void setDailyForecasts(List<DailyForecast> dailyForecasts) { this.dailyForecasts = dailyForecasts; }
    public void addForecast(DailyForecast df) { this.dailyForecasts.add(df); }

    public static class DailyForecast {
        private String dateText;
        private double temp;
        private String condition;
        private String iconCode;

        public String getDateText() { return dateText; }
        public void setDateText(String dateText) { this.dateText = dateText; }

        public double getTemp() { return temp; }
        public void setTemp(double temp) { this.temp = temp; }

        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }

        public String getIconCode() { return iconCode; }
        public void setIconCode(String iconCode) { this.iconCode = iconCode; }
    }
}
