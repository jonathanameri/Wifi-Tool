# WiFi Tool

A Swing app for recording WiFi speeds around the school and showing them as a
heat map over the floor plan.

## Build and run

Requires JDK 11+. Run from the repository root (data files are read from the
working directory):

```sh
javac -d bin src/*.java
java -cp bin Main
```

## Using it

- **Input new data point**: record a location name with its download/upload
  speed. Each reading is appended to `Wifi.txt`, and `WiFiA.txt` is rebuilt
  with the average per location.
- **View current data points**: show the averages in `WiFiA.txt`.
- **View download / View upload**: open the heat map. Click the map, press
  *Top-left*, click the opposite corner, press *Bottom-right*, enter a speed
  and *Submit* to shade that area. Colours run red (slowest) to blue (fastest).

## Data files

| File | Format (space-separated, one per line) |
| --- | --- |
| `Wifi.txt` | `name download upload`: raw readings (created on first input) |
| `WiFiA.txt` | `name download upload`: averages, generated from `Wifi.txt` |
| `Points.txt` / `Points2.txt` | `x y width height speed`: download / upload map areas, in a 1050×750 grid |
| `School Map.png` | floor plan the areas are drawn over |
