# Weather Information App

This is a real-time Weather Information App built with Java Swing. It fetches current weather data and short-term (5-day) forecasts using the OpenWeatherMap API.

## Features Included
- **API Integration**: Fetches real-time weather and forecasts using OpenWeatherMap.
- **GUI Design**: User-friendly Java Swing interface with panels for search, current weather, and forecast.
- **Weather Information**: Displays temperature, humidity, wind speed, and conditions.
- **Dynamic Icons**: Shows OpenWeatherMap icons indicating the weather visually.
- **Forecast Display**: 5-Day forecast panel.
- **Unit Conversion**: Quickly switch between Celsius and Fahrenheit.
- **Error Handling**: Validates input, gracefully reports missing API keys or invalid locations.
- **History Tracking**: Keeps a history of the last 10 searches with timestamps, selectable from a dropdown.
- **Dynamic Backgrounds**: The background of the weather panel automatically changes based on the time of day (sky blue for day, orange/purple gradient for sunset, dark blue for night).

## How to Set Up the API Key
In order to retrieve actual live weather data, you need an OpenWeatherMap API Key.
1. Sign up for a free API key at [OpenWeatherMap](https://openweathermap.org/api).
2. Open the `config.properties` file in the root directory.
3. Replace `YOUR_API_KEY_HERE` with your actual API key:
   ```properties
   api.key=1a2b3c4d5e6f7g8h9i0j
   ```
4. Save the file.

## How to Run the Application
### Option 1: Using the provided Batch script (Windows)
1. Double click the `compile_and_run.bat` file.
2. The script will automatically compile all `.java` files into a `bin` folder and launch the application.

### Option 2: Command Line (Manual)
1. Open a terminal or command prompt in the `ASSIGNMENT 8` folder.
2. Compile the source code:
   ```bash
   mkdir bin
   javac -d bin src/model/*.java src/util/*.java src/api/*.java src/gui/*.java src/WeatherApp.java
   ```
3. Run the application:
   ```bash
   java -cp bin WeatherApp
   ```

## Implementation Details
- **`model`**: Contains POJOs (`WeatherData.java`, `ForecastData.java`) holding the state of grabbed JSON information cleanly.
- **`api.WeatherClient`**: Manages HTTP calls to the OpenWeatherMap endpoints using native `HttpURLConnection`. Since external dependencies like `org.json` might be hard to set up for a school evaluator, basic RegEx-based JSON parsing is used for pure standard Java library compatibility.
- **`gui`**: Break-down of complex Swing logic into component panels (`SearchPanel`, `WeatherPanel`, `ForecastPanel`, `MainFrame`) for high maintainability.
- **`util.SearchHistoryManager`**: Handles saving and retrieving timestamped searches securely to a relative `history.txt` flat file. 

Enjoy your Weather App!

# Weather-Information-App
