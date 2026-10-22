import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** School map with measured areas shaded by speed for one {@link Metric}. */
@SuppressWarnings("serial")
public class MapWindow extends JFrame {

	public static final Path MAP_IMAGE = Paths.get("School Map.png");

	/** Coordinate space the points files are stored in, independent of screen size. */
	public static final int GRID_WIDTH = 1050;
	public static final int GRID_HEIGHT = 750;

	/** Extra pixels added to each drawn area so neighbouring areas don't leave gaps. */
	private static final int AREA_PADDING = 4;
	private static final double SCREEN_FRACTION = 0.7;

	private final Metric metric;
	private final List<MeasuredArea> areas;
	private final BufferedImage mapImage;
	private final MapPanel panel;
	private final PointEntryWindow entryWindow;

	public MapWindow(Metric metric) throws IOException {
		super(metric.getLabel() + " map");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.metric = metric;
		this.areas = loadAreas(metric.getPointsFile());
		this.mapImage = ImageIO.read(MAP_IMAGE.toFile());
		if (mapImage == null) {
			throw new IOException("Unsupported image format: " + MAP_IMAGE);
		}

		panel = new MapPanel(fitToScreen(mapImage.getWidth(), mapImage.getHeight()));
		add(panel);
		pack();

		entryWindow = new PointEntryWindow(this, metric);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				entryWindow.dispose();
			}
		});
	}

	public PointEntryWindow getEntryWindow() {return entryWindow;}

	/** Adds an area, replacing any existing one with the same top-left corner, and saves. */
	public void addArea(MeasuredArea area) {
		areas.removeIf(a -> a.getX() == area.getX() && a.getY() == area.getY());
		areas.add(area);
		try {
			saveAreas();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Could not save " + metric.getPointsFile() + ": " + e.getMessage(),
					"Save failed", JOptionPane.ERROR_MESSAGE);
		}
		panel.repaint();
	}

	private static List<MeasuredArea> loadAreas(Path file) throws IOException {
		List<MeasuredArea> loaded = new ArrayList<>();
		if (!Files.exists(file)) {
			return loaded;
		}
		for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
			if (line.trim().isEmpty()) {
				continue;
			}
			try {
				loaded.add(MeasuredArea.parse(line));
			} catch (IllegalArgumentException e) {
				System.err.println("Skipping unreadable line in " + file + ": " + line);
			}
		}
		return loaded;
	}

	private void saveAreas() throws IOException {
		List<String> lines = new ArrayList<>();
		areas.stream()
				.sorted(Comparator.comparingInt(MeasuredArea::getY).thenComparingInt(MeasuredArea::getX))
				.forEach(a -> lines.add(a.format()));
		Files.write(metric.getPointsFile(), lines, StandardCharsets.UTF_8);
	}

	private static Dimension fitToScreen(int width, int height) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		double scale = Math.min(1.0, Math.min((double) screen.width / width, (double) screen.height / height));
		scale *= SCREEN_FRACTION;
		return new Dimension((int) (width * scale), (int) (height * scale));
	}

	/** Maps a speed's position between the slowest and fastest area to a red→green→blue colour. */
	static int gradient(float percent) {
		float r = 1;
		float g = percent * 2;
		float b = 0;
		if (percent >= .25 && percent < .5) {
			g = 1;
			r = 1 - percent + .25f;
		} else if (percent >= .5 && percent < .75) {
			r = .4f;
			g = 1 - percent + .5f;
			b = .4f;
		} else if (percent >= .75) {
			r = 0;
			g = .7f;
			b = 1 - percent + .75f;
		}
		return 200 << 24 | (int) (255f * r) << 16 | (int) (255f * g) << 8 | (int) (255f * b);
	}

	private class MapPanel extends JPanel {

		MapPanel(Dimension size) {
			setPreferredSize(size);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int x = clamp(e.getX() * GRID_WIDTH / getWidth(), GRID_WIDTH - 1);
					int y = clamp(e.getY() * GRID_HEIGHT / getHeight(), GRID_HEIGHT - 1);
					entryWindow.setXY(x, y);
				}
			});
		}

		@Override
		protected void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			Graphics2D g = (Graphics2D) graphics;
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), null);

			if (areas.isEmpty()) {
				return;
			}
			float min = Float.MAX_VALUE;
			float max = -Float.MAX_VALUE;
			for (MeasuredArea a : areas) {
				min = Math.min(min, a.getValue());
				max = Math.max(max, a.getValue());
			}
			float range = max - min;

			for (MeasuredArea a : areas) {
				float percent = range > 0 ? (a.getValue() - min) / range : 1f;
				g.setColor(new Color(gradient(percent), true));
				g.fillRect(a.getX() * getWidth() / GRID_WIDTH, a.getY() * getHeight() / GRID_HEIGHT,
						a.getWidth() * getWidth() / GRID_WIDTH + AREA_PADDING,
						a.getHeight() * getHeight() / GRID_HEIGHT + AREA_PADDING);
			}
		}

		private int clamp(int value, int max) {
			return Math.max(0, Math.min(value, max));
		}
	}
}
