import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Form for recording a location's measured download and upload speed. */
@SuppressWarnings("serial")
public class InputWindow extends JFrame {

	private final JTextField name = new JTextField(10);
	private final JTextField download = new JTextField(10);
	private final JTextField upload = new JTextField(10);
	private final JLabel lastInput = new JLabel("Last input: ");

	public InputWindow() {
		super("New point");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);

		JButton submitButton = new JButton("Submit");
		JButton resetButton = new JButton("Reset");
		submitButton.addActionListener(e -> submit());
		resetButton.addActionListener(e -> reset());

		JPanel form = new JPanel(new GridLayout(0, 2, 5, 8));
		form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		form.add(new JLabel("Location name"));
		form.add(name);
		form.add(new JLabel("Download speed"));
		form.add(download);
		form.add(new JLabel("Upload speed"));
		form.add(upload);
		form.add(submitButton);
		form.add(resetButton);

		lastInput.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

		add(form, BorderLayout.CENTER);
		add(lastInput, BorderLayout.SOUTH);
		getRootPane().setDefaultButton(submitButton);
	}

	private void submit() {
		String locationName = name.getText().trim();
		if (locationName.isEmpty()) {
			showError("Enter a location name.");
			return;
		}
		double downloadSpeed;
		double uploadSpeed;
		try {
			downloadSpeed = Double.parseDouble(download.getText().trim());
			uploadSpeed = Double.parseDouble(upload.getText().trim());
		} catch (NumberFormatException e) {
			showError("Download and upload speeds must be numbers.");
			return;
		}
		if (downloadSpeed < 0 || uploadSpeed < 0) {
			showError("Speeds can't be negative.");
			return;
		}

		try {
			Averager.appendReading(locationName, downloadSpeed, uploadSpeed);
		} catch (IOException e) {
			showError("Could not save reading: " + e.getMessage());
			return;
		}
		Averager.averageReadings();
		lastInput.setText("Last input: " + locationName + " - " + downloadSpeed + " - " + uploadSpeed);
	}

	private void reset() {
		name.setText("");
		download.setText("");
		upload.setText("");
	}

	private void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Invalid input", JOptionPane.ERROR_MESSAGE);
	}
}
