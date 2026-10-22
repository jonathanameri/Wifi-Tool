import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	/** Vertical offset of the data window when shown beside a map. */
	private static final int DATA_WINDOW_Y = 270;

	public MainWindow() {
		super("WiFi Tool");
		setLayout(new FlowLayout());
		setResizable(false);

		JButton inputNew = new JButton("Input new data point");
		JButton viewCurrent = new JButton("View current data points");
		JButton viewDownload = new JButton("View download");
		JButton viewUpload = new JButton("View upload");

		inputNew.addActionListener(e -> showInputWindow());
		viewCurrent.addActionListener(e -> showDataWindow());
		viewDownload.addActionListener(e -> showMap(Metric.DOWNLOAD));
		viewUpload.addActionListener(e -> showMap(Metric.UPLOAD));

		for (JButton button : new JButton[] {inputNew, viewCurrent, viewDownload, viewUpload}) {
			button.setFocusable(false);
			add(button);
		}
	}

	private void showInputWindow() {
		InputWindow input = new InputWindow();
		input.pack();
		input.setLocationRelativeTo(null);
		input.setVisible(true);
	}

	private void showDataWindow() {
		DataWindow view = new DataWindow();
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}

	private void showMap(Metric metric) {
		MapWindow map;
		try {
			map = new MapWindow(metric);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Could not open map: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		map.setLocation(0, 0);
		map.setVisible(true);

		PointEntryWindow entry = map.getEntryWindow();
		entry.setLocation(map.getWidth(), 0);
		entry.setVisible(true);

		DataWindow data = new DataWindow();
		data.setLocation(map.getWidth(), DATA_WINDOW_Y);
		data.setVisible(true);
	}
}
