package gui;

import model.WeatherData;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.LocalTime;

public class WeatherPanel extends JPanel {
    private JLabel cityLabel;
    private JLabel tempLabel;
    private JLabel descLabel;
    private JLabel humidityLabel;
    private JLabel windLabel;
    private JLabel iconLabel;
    
    private WeatherData currentData;

    private Color color1 = new Color(135, 206, 235);
    private Color color2 = Color.WHITE;

    public WeatherPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cityLabel = new JLabel("Enter a location to get weather", SwingConstants.CENTER);
        cityLabel.setFont(new Font("Arial", Font.BOLD, 24));
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconLabel = new JLabel();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tempLabel = new JLabel("--", SwingConstants.CENTER);
        tempLabel.setFont(new Font("Arial", Font.BOLD, 48));
        tempLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        descLabel = new JLabel("--", SwingConstants.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        humidityLabel = new JLabel("Humidity: --", SwingConstants.CENTER);
        humidityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        windLabel = new JLabel("Wind: --", SwingConstants.CENTER);
        windLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        windLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(cityLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(iconLabel);
        add(tempLabel);
        add(descLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(humidityLabel);
        add(windLabel);
        add(Box.createVerticalGlue());

        updateDynamicBackground(""); // default background
    }

    public void updateWeather(WeatherData data, boolean isCelsius) {
        this.currentData = data;
        cityLabel.setText(data.getCityName());
        descLabel.setText(data.getCondition());
        humidityLabel.setText("Humidity: " + data.getHumidity() + "%");
        
        refreshUnits(isCelsius);

        try {
            String iconUrl = "http://openweathermap.org/img/wn/" + data.getIconCode() + "@2x.png";
            ImageIcon icon = new ImageIcon(new URL(iconUrl));
            iconLabel.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateDynamicBackground(data.getIconCode());
        repaint();
    }

    public void refreshUnits(boolean isCelsius) {
        if (currentData == null) return;
        
        double temp = currentData.getTemperatureCelsius();
        if (!isCelsius) {
            temp = (temp * 9/5) + 32;
        }
        tempLabel.setText(String.format("%.1f °%s", temp, isCelsius ? "C" : "F"));
        
        double wind = currentData.getWindSpeedMetric();
        if (isCelsius) {
            windLabel.setText(String.format("Wind: %.1f m/s", wind));
        } else {
            wind = wind * 2.23694; // m/s to mph
            windLabel.setText(String.format("Wind: %.1f mph", wind));
        }
    }

    private void updateDynamicBackground(String iconCode) {
        int hour = LocalTime.now().getHour();
        Color startColor;
        Color endColor;
        boolean darkTheme = false;

        if (hour >= 6 && hour < 17) {
            // Day
            startColor = new Color(135, 206, 235); // Sky Blue
            endColor = new Color(255, 255, 255);
        } else if (hour >= 17 && hour < 20) {
            // Sunset
            startColor = new Color(255, 140, 0); // Dark Orange
            endColor = new Color(138, 43, 226);  // Blue Violet
            darkTheme = true;
        } else {
            // Night
            startColor = new Color(25, 25, 112); // Midnight Blue
            endColor = new Color(0, 0, 0);       // Black
            darkTheme = true;
        }

        this.color1 = startColor;
        this.color2 = endColor;
        
        Color fg = darkTheme ? Color.WHITE : Color.BLACK;
        cityLabel.setForeground(fg);
        tempLabel.setForeground(fg);
        descLabel.setForeground(fg);
        humidityLabel.setForeground(fg);
        windLabel.setForeground(fg);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
