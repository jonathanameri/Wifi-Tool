import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class mainWindow extends JFrame{
	
	//Buttons
	private JButton inputNew;
	private JButton viewCurrent;
	private JButton viewDownload;
	private JButton viewUpload;
	
	
	public mainWindow() {
		
		
		
		super("WiFi Tool");
		setLayout(new FlowLayout());
		setResizable(false);
		
		inputNew = new JButton("Input new data point");
		inputNew.setFocusable(false);
		inputNew.setBackground(Color.WHITE);
		inputNew.setOpaque(true);
		
		viewCurrent = new JButton("View current data points");
		viewCurrent.setFocusable(false);
		viewCurrent.setBackground(Color.WHITE);
		viewCurrent.setOpaque(true);
		
		viewDownload = new JButton("View download");
		viewDownload.setFocusable(false);
		viewDownload.setBackground(Color.WHITE);
		viewDownload.setOpaque(true);
		
		viewUpload = new JButton("View upload");
		viewUpload.setFocusable(false);
		viewUpload.setBackground(Color.WHITE);
		viewUpload.setOpaque(true);
		
		add(inputNew);
		add(viewCurrent);
		add(viewDownload);
		add(viewUpload);
		
		HandlerClass handler = new HandlerClass();
		
		inputNew.addActionListener(handler);
		viewCurrent.addActionListener(handler);
		viewDownload.addActionListener(handler);
		viewUpload.addActionListener(handler);
		
	}
	
	private class HandlerClass implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			
			//If inputNew Button is pressed
			if(event.getSource()==inputNew) {
				gooey gui = new gooey();
				gui.setSize(240, 203);
			    gui.setLocationRelativeTo(null);
				gui.setVisible(true);
			}
			
			//If viewCurrent Button is pressed
			else if(event.getSource()==viewCurrent) {
				viewData view = new viewData();
				view.setSize(260,300);
				view.setLocationRelativeTo(null);
				view.setVisible(true);
			}
			
			//If viewDownload Button is pressed
			else if(event.getSource()==viewDownload) {
				mapView map = new mapView();
				int properWidth = map.getProperWidth();
				int properHeight = map.getProperHeight();
				map.setSize(properWidth, properHeight);
				map.finalize();
				map.setVisible(true);
				map.rerender();
				map.render();
				map.render();
				
				viewData view = new viewData();
				view.setSize(260,300);
				view.setLocation(properWidth, 270);
				view.setVisible(true);
			}
			
			//If viewUpload Button is pressed
			else if(event.getSource()==viewUpload) {
				mapView2 map2 = new mapView2();
				int properWidth2 = map2.getProperWidth();
				int properHeight2 = map2.getProperHeight();
				map2.setSize(properWidth2, properHeight2);
				map2.finalize();
				map2.setVisible(true);
				map2.rerender();
				map2.render();
				map2.render();
				
				viewData view2 = new viewData();
				view2.setSize(260,300);
				view2.setLocation(properWidth2, 270);
				view2.setVisible(true);
			}
			
		}
}
}