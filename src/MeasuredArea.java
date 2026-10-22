import java.util.Locale;

/**
 * A rectangle on the map with one measured speed. Coordinates are in map grid
 * units (see {@link MapWindow#GRID_WIDTH}), not screen pixels.
 */
public final class MeasuredArea {

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final float value;

	public MeasuredArea(int x, int y, int width, int height, float value) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.value = value;
	}

	/** Parses a "x y width height value" line as stored in the points files. */
	public static MeasuredArea parse(String line) {
		String[] parts = line.trim().split("\\s+");
		if (parts.length != 5) {
			throw new IllegalArgumentException("Expected 5 fields: " + line);
		}
		return new MeasuredArea(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
				Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Float.parseFloat(parts[4]));
	}

	public String format() {
		return String.format(Locale.ROOT, "%d %d %d %d %s", x, y, width, height, value);
	}

	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public float getValue() {return value;}
}
