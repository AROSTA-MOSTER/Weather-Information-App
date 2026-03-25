package gui;

import api.WeatherClient;
import model.ForecastData;
import model.WeatherData;
import util.SearchHistoryManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private SearchPanel searchPanel;
    private WeatherPanel weatherPanel;
    private ForecastPanel forecastPanel;
    
    private WeatherClient weatherClient;
    private SearchHistoryManager historyManager;

    private boolean isCelsius = true;

    public MainFrame() {
        setTitle("Weather Information App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        weatherClient = new WeatherClient();
        historyManager = new SearchHistoryManager();

        searchPanel = new SearchPanel(this, historyManager);
        weatherPanel = new WeatherPanel();
        forecastPanel = new ForecastPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(weatherPanel, BorderLayout.CENTER);
        add(forecastPanel, BorderLayout.SOUTH);
    }

    public void fetchWeather(String location) {
        // Run network request in background
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            WeatherData current;
            ForecastData forecast;
            Exception error;

            @Override
            protected Void doInBackground() {
                try {
                    current = weatherClient.getCurrentWeather(location);
                    forecast = weatherClient.getForecast(location);
                } catch (Exception ex) {
                    error = ex;
                }
                return null;
            }

            @Override
            protected void done() {
                if (error != null) {
                    JOptionPane.showMessageDialog(MainFrame.this, 
                        "Error fetching weather: " + error.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    weatherPanel.updateWeather(current, isCelsius);
                    forecastPanel.updateForecast(forecast, isCelsius);
                    historyManager.addSearch(location);
                    searchPanel.updateHistoryDropdown();
                }
            }
        };
        worker.execute();
    }

    public void toggleUnits() {
        isCelsius = !isCelsius;
        weatherPanel.refreshUnits(isCelsius);
        forecastPanel.refreshUnits(isCelsius);
    }

    public boolean isCelsius() {
        return isCelsius;
    }
}
