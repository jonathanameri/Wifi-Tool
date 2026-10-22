import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Raw speed readings live in Wifi.txt, one "name download upload" line per
 * reading. WiFiA.txt holds one averaged line per location in the same format.
 */
public final class Averager {

	public static final Path READINGS_FILE = Paths.get("Wifi.txt");
	public static final Path AVERAGES_FILE = Paths.get("WiFiA.txt");

	// Also accepts the older "name - download - upload" form.
	private static final Pattern READING =
			Pattern.compile("^(.+?)\\s+(?:-\\s+)?(\\d+(?:\\.\\d+)?)\\s+(?:-\\s+)?(\\d+(?:\\.\\d+)?)\\s*$");

	private Averager() {}

	/** Rewrites WiFiA.txt from Wifi.txt. Does nothing if there are no raw readings yet. */
	public static void averageReadings() {
		if (!Files.exists(READINGS_FILE)) {
			return;
		}
		try {
			Map<String, double[]> totals = new TreeMap<>();
			for (String line : Files.readAllLines(READINGS_FILE, StandardCharsets.UTF_8)) {
				if (line.trim().isEmpty()) {
					continue;
				}
				Matcher m = READING.matcher(line.trim());
				if (!m.matches()) {
					System.err.println("Skipping unreadable line in " + READINGS_FILE + ": " + line);
					continue;
				}
				double[] total = totals.computeIfAbsent(m.group(1), k -> new double[3]);
				total[0] += Double.parseDouble(m.group(2));
				total[1] += Double.parseDouble(m.group(3));
				total[2]++;
			}

			List<String> averages = new ArrayList<>();
			for (Map.Entry<String, double[]> e : totals.entrySet()) {
				double[] t = e.getValue();
				averages.add(formatReading(e.getKey(), t[0] / t[2], t[1] / t[2]));
			}
			Files.write(AVERAGES_FILE, averages, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Could not average readings: " + e.getMessage());
		}
	}

	/**
	 * Appends one reading to Wifi.txt. If Wifi.txt doesn't exist yet, it is first
	 * seeded from WiFiA.txt so the next averaging pass doesn't discard existing data.
	 */
	public static void appendReading(String name, double download, double upload) throws IOException {
		if (!Files.exists(READINGS_FILE) && Files.exists(AVERAGES_FILE)) {
			Files.write(READINGS_FILE, Files.readAllLines(AVERAGES_FILE, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
		}
		Files.write(READINGS_FILE, List.of(formatReading(name, download, upload)), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

	private static String formatReading(String name, double download, double upload) {
		return String.format(Locale.ROOT, "%s %.2f %.2f", name, download, upload);
	}
}
