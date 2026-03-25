package gui;

import model.ForecastData;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ForecastPanel extends JPanel {
    private List<DailyForecastPanel> dayPanels;
    private ForecastData currentData;

    public ForecastPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("5-Day Forecast"));
        setPreferredSize(new Dimension(800, 150));

        JPanel centerPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        dayPanels = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            DailyForecastPanel dp = new DailyForecastPanel();
            dayPanels.add(dp);
            centerPanel.add(dp);
        }

        add(centerPanel, BorderLayout.CENTER);
    }

    public void updateForecast(ForecastData data, boolean isCelsius) {
        this.currentData = data;
        List<ForecastData.DailyForecast> forecasts = data.getDailyForecasts();
        for (int i = 0; i < 5; i++) {
            if (i < forecasts.size()) {
                dayPanels.get(i).updateData(forecasts.get(i), isCelsius);
            } else {
                dayPanels.get(i).clear();
            }
        }
        revalidate();
        repaint();
    }

    public void refreshUnits(boolean isCelsius) {
        if (currentData == null) return;
        List<ForecastData.DailyForecast> forecasts = currentData.getDailyForecasts();
        for (int i = 0; i < 5; i++) {
            if (i < forecasts.size()) {
                dayPanels.get(i).refreshUnits(forecasts.get(i), isCelsius);
            }
        }
    }

    private static class DailyForecastPanel extends JPanel {
        private JLabel dateLabel;
        private JLabel iconLabel;
        private JLabel tempLabel;

        public DailyForecastPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            dateLabel = new JLabel("--");
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            iconLabel = new JLabel();
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            tempLabel = new JLabel("--");
            tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(Box.createVerticalGlue());
            add(dateLabel);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(iconLabel);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(tempLabel);
            add(Box.createVerticalGlue());
        }

        public void updateData(ForecastData.DailyForecast df, boolean isCelsius) {
            dateLabel.setText(df.getDateText());
            
            try {
                String iconUrl = "http://openweathermap.org/img/wn/" + df.getIconCode() + ".png";
                ImageIcon icon = new ImageIcon(new URL(iconUrl));
                iconLabel.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
            }

            refreshUnits(df, isCelsius);
        }

        public void refreshUnits(ForecastData.DailyForecast df, boolean isCelsius) {
            double temp = df.getTemp();
            if (!isCelsius) {
                temp = (temp * 9/5) + 32;
            }
            tempLabel.setText(String.format("%.1f °%s", temp, isCelsius ? "C" : "F"));
        }

        public void clear() {
            dateLabel.setText("--");
            iconLabel.setIcon(null);
            tempLabel.setText("--");
        }
    }
}
