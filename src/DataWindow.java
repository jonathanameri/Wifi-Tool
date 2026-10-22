import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** Read-only view of the averaged speeds in WiFiA.txt. */
@SuppressWarnings("serial")
public class DataWindow extends JFrame {

	public DataWindow() {
		super("Current Data");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JTextArea data = new JTextArea(15, 20);
		data.setEditable(false);
		data.setText(readAverages());
		data.setCaretPosition(0);

		add(new JScrollPane(data));
		setSize(260, 300);
	}

	private static String readAverages() {
		try {
			return new String(Files.readAllBytes(Averager.AVERAGES_FILE), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return "No data yet (" + Averager.AVERAGES_FILE + " not found).";
		}
	}
}
