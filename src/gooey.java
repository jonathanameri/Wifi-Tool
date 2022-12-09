import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.*;

@SuppressWarnings("serial")
public class gooey extends JFrame{
	
	//!!!!VARIABLES!!!!
	//Create Text Fields to enter data
	private JTextField name;
	private JTextField download;
	private JTextField upload;
	
	//Un-editable text boxes
	private JLabel nameText;
	private JLabel downloadText;
	private JLabel uploadText;
	private JTextField lastInput;
	
	//Variables for speeds and name
	private String locationName;
	private String downloadSpeed;
	private String uploadSpeed;
	
	String currentData;
	
	//File stuff
	Formatter x;
	Scanner y;
	
	//Buttons
	private JButton submitButton;
	private JButton resetButton;
	
	boolean submit = false;
	
	public averager esskeetit = new averager();
	
	//!!!!CONSTRUCTOR!!!!
	public gooey() {
		
		//Default stuff
		super("New point");
		setLayout(null);
		setResizable(false);
		
		//Create Text Fields
		name = new JTextField();
		download = new JTextField();
		upload = new JTextField();
				
		nameText = new JLabel("Location name");
		nameText.setFocusable(false);
		
		downloadText = new JLabel("Download speed");
		downloadText.setFocusable(false);
		
		uploadText = new JLabel("Upload speed");
		uploadText.setFocusable(false);
		
		lastInput = new JTextField("Last input: ", 20);
		lastInput.setEditable(false);
		lastInput.setFocusable(false);
		
		//Create buttons
		submitButton = new JButton("Submit");
		submitButton.setBackground(Color.WHITE);
		submitButton.setOpaque(true);
		resetButton = new JButton("Reset");
		resetButton.setBackground(Color.WHITE);
		resetButton.setOpaque(true);
		
		int textAcross = 15;
		int textDown = 15;
		int inputAcross = 120;
		int buttonDown = 100;
		int buttonWidth = 100;
		int buttonHeight = 30;
		
		//Set boundaries
		nameText.setBounds(textAcross, textDown, 100, 18);
		downloadText.setBounds(textAcross, textDown + 30, 100, 18);
		uploadText.setBounds(textAcross, textDown + 60, 100, 18);
		lastInput.setBounds(textAcross, 138, 205, 18);
		
		name.setBounds(inputAcross, textDown, 100, 18);
		download.setBounds(inputAcross, textDown + 30, 100, 18);
		upload.setBounds(inputAcross, textDown + 60, 100, 18);
		
		submitButton.setBounds(15, buttonDown, buttonWidth, buttonHeight);
		resetButton.setBounds(120, buttonDown, buttonWidth, buttonHeight);
		
		//Add everything
		add(nameText);
		add(downloadText);
		add(uploadText);
		add(name);
		add(download);
		add(upload);
		add(lastInput);
		add(submitButton);
		add(resetButton);
		
		
		//Create HandlerClass
		HandlerClass handler = new HandlerClass();
		
		//Add handler
		submitButton.addActionListener(handler);
		resetButton.addActionListener(handler);
		
		esskeetit = new averager();
	}
	
	//Add stuff to the file
	public void addRecords() {
		try {
			y = new Scanner(new File("Wifi.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		readFile();
		try {
			x = new Formatter("Wifi.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		x.format("%s%s - %s - %s", currentData, locationName, downloadSpeed, uploadSpeed);
		x.close();
	}
	
	//Read file
	public void readFile() {
		currentData = "";
		while(y.hasNext()) {
			currentData += y.nextLine() + "\n";
		}
		y.close();
	}
	
	//Handler Class
	private class HandlerClass implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			
			if(event.getSource()==submitButton) {
				locationName = name.getText();
				downloadSpeed = download.getText();
				uploadSpeed = upload.getText();
				lastInput.setText("Last input: " + locationName + " - "+ downloadSpeed + " - " + uploadSpeed);
				addRecords();
				esskeetit = new averager();
				esskeetit.average();
			}
			if(event.getSource()==resetButton) {
				name.setText("");
				download.setText("");
				upload.setText("");
			}
		}

	}
}
