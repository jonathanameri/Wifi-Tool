import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Companion to a {@link MapWindow}: click the map, mark the clicked spot as one
 * corner of an area, mark the opposite corner, enter a speed and submit.
 */
@SuppressWarnings("serial")
public class PointEntryWindow extends JFrame {

	private final MapWindow map;
	private final JTextField input = new JTextField(8);
	private final JLabel clickedLabel = new JLabel();
	private final JLabel firstCornerLabel = new JLabel();
	private final JLabel secondCornerLabel = new JLabel();
	private final JLabel sizeLabel = new JLabel();

	private Point clicked;
	private Point firstCorner;
	private Point secondCorner;

	public PointEntryWindow(MapWindow map, Metric metric) {
		super("Enter point");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.map = map;

		JButton firstCornerButton = new JButton("Top-left");
		JButton secondCornerButton = new JButton("Bottom-right");
		JButton submitButton = new JButton("Submit");
		firstCornerButton.addActionListener(e -> {
			firstCorner = clicked;
			updateLabels();
		});
		secondCornerButton.addActionListener(e -> {
			secondCorner = clicked;
			updateLabels();
		});
		submitButton.addActionListener(e -> submit());

		JPanel form = new JPanel(new GridLayout(0, 2, 5, 8));
		form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		form.add(new JLabel(metric.getLabel() + " speed:"));
		form.add(new JLabel());
		form.add(input);
		form.add(clickedLabel);
		form.add(firstCornerButton);
		form.add(firstCornerLabel);
		form.add(secondCornerButton);
		form.add(secondCornerLabel);
		form.add(sizeLabel);
		form.add(submitButton);

		add(form, BorderLayout.CENTER);
		getRootPane().setDefaultButton(submitButton);
		updateLabels();
		pack();
	}

	/** Called by the map with the clicked position in grid coordinates. */
	public void setXY(int x, int y) {
		clicked = new Point(x, y);
		updateLabels();
	}

	private void updateLabels() {
		clickedLabel.setText(describe(clicked));
		firstCornerLabel.setText(describe(firstCorner));
		secondCornerLabel.setText(describe(secondCorner));
		if (firstCorner != null && secondCorner != null) {
			sizeLabel.setText("W: " + Math.abs(secondCorner.x - firstCorner.x)
					+ "  H: " + Math.abs(secondCorner.y - firstCorner.y));
		} else {
			sizeLabel.setText("W: -  H: -");
		}
	}

	private static String describe(Point p) {
		return p == null ? "X: -  Y: -" : "X: " + p.x + "  Y: " + p.y;
	}

	private void submit() {
		if (firstCorner == null || secondCorner == null) {
			showError("Click the map and set both corners first.");
			return;
		}
		float speed;
		try {
			speed = Float.parseFloat(input.getText().trim());
		} catch (NumberFormatException e) {
			showError("Speed must be a number.");
			return;
		}
		if (speed < 0) {
			showError("Speed can't be negative.");
			return;
		}

		// Corners may have been set in either order.
		int x = Math.min(firstCorner.x, secondCorner.x);
		int y = Math.min(firstCorner.y, secondCorner.y);
		int width = Math.abs(secondCorner.x - firstCorner.x);
		int height = Math.abs(secondCorner.y - firstCorner.y);
		map.addArea(new MeasuredArea(x, y, width, height, speed));
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Invalid input", JOptionPane.ERROR_MESSAGE);
	}
}
