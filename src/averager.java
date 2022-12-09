import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class averager {
	
	Scanner y;
	Formatter x;
	String currentData;
	
	public averager() {}
	
	public void average() {
		try {
			y = new Scanner(new File("Wifi.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		readFile();
		try {
			x = new Formatter("WiFiA.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		x.format("%s", currentData);
		x.close();
	}
	
	//Read file
	public void readFile() {
		currentData = "";
		String lastLine = "";
		String currentLine = "";
		double averageD = 0;
		double averageU = 0;
		String name = "";
		int amount = 0;
		while(y.hasNextLine()) {
			lastLine = currentLine;
			currentLine = y.nextLine();
			for(int i = 0; i < lastLine.length(); i++) {
				if(lastLine.substring(i, i+1).equals(" ") && isNum(lastLine.substring(i+1, i+2))) {
					name = lastLine.substring(0, i);
					break;
				}
			}
			for(int t = 0; t < currentLine.length(); t++) {
				if(currentLine.substring(t, t+1).equals(" ") && isNum(currentLine.substring(t+1, t+2))) {
					
					if(currentLine.substring(0,t).equals(name)) {
						averageD += Float.parseFloat(lastLine.substring(lastLine.length()-11, lastLine.length()-6));
						averageU += Float.parseFloat(lastLine.substring(lastLine.length()-5,lastLine.length()));
						amount++;
					}
					else if(lastLine.length() >= 12){
						averageD += Float.parseFloat(lastLine.substring(lastLine.length()-11, lastLine.length()-6));
						averageU += Float.parseFloat(lastLine.substring(lastLine.length()-5,lastLine.length()));
						amount++;
						averageD/=amount;
						averageU/=amount;
						
						String formatD = String.format("%.2f", averageD);
						String formatU = String.format("%.2f", averageU);
						
						
						currentData += name + " " + formatD + " " + formatU + "\n";
						averageD = 0;
						averageU = 0;
						name = "";
						amount = 0;
					}
					break;
				}
			}
		}
		y.close();
	}
		
	public boolean isNum(String x) {
		if(x.equals("0")||x.equals("1")||x.equals("2")||x.equals("3")||x.equals("4")||x.equals("5")||x.equals("6")||x.equals("7")||x.equals("8")||x.equals("9")) {
			return true;
		}
		return false;
	}
}
