import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		Averager.averageReadings();

		SwingUtilities.invokeLater(() -> {
			MainWindow main = new MainWindow();
			main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			main.pack();
			main.setLocationRelativeTo(null);
			main.setVisible(true);
		});
	}
}
