import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		averager a = new averager();
		a.average();
		
		//Sets up GUI
		mainWindow main = new mainWindow();
		main.setSize(580,70);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
}
