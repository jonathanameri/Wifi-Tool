import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class newPoint2 extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 310030898295533345L;
	private JButton submitButton;
	private JButton topLeftButton;
	private JButton bottomRightButton;
	private JTextField input;
	private JLabel enter;
	private JLabel xy;
	private JLabel xytop;
	private JLabel xybottom;
	private JLabel Dwidthheight;
	private int x = 0;
	private int y = 0;
	private int xtop = 0;
	private int ytop = 0;
	private int xbottom = 0;
	private int ybottom = 0;
	private int width = 0;
	private int height = 0;
	
	public mapView2 map;
	
	public newPoint2(mapView2 m) {
		super("Enter point");
		setResizable(false);
		setLayout(null);
		
		map = m;
		
		enter = new JLabel("Upload speed:");
		enter.setBounds(15, 15, 110, 20);
		
		input = new JTextField();
		input.setBounds(15, 40, 110, 30);
		
		//Submit button
		submitButton = new JButton("Submit");
		submitButton.setBounds(55, 200, 100, 30);
		submitButton.setBackground(Color.WHITE);
		submitButton.setOpaque(false);
		
		//Top left button
		topLeftButton = new JButton("Top-left");
		topLeftButton.setBounds(15, 80, 110, 30);
		topLeftButton.setBackground(Color.WHITE);
		topLeftButton.setOpaque(false);
		
		//Bottom right button
		bottomRightButton = new JButton("Bottom-right");
		bottomRightButton.setBounds(15, 120, 110, 30);
		bottomRightButton.setBackground(Color.WHITE);
		bottomRightButton.setOpaque(false);
		
		xy = new JLabel("X: " + x + " Y: " + y);
		xy.setBounds(130, 40, 100, 30);
		
		xytop = new JLabel("X: " + xtop + " Y: " + ytop);
		xytop.setBounds(130, 80, 100, 30);
		
		xybottom = new JLabel("X: " + xbottom + " Y: " + ybottom);
		xybottom.setBounds(130, 120, 100, 30);
		
		Dwidthheight = new JLabel("Width: " + width + "     Height: " + height);
		Dwidthheight.setBounds(15, 160, 180, 30);
		
		add(enter);
		add(input);
		add(xy);
		add(submitButton);
		add(topLeftButton);
		add(bottomRightButton);
		add(xytop);
		add(xybottom);
		add(Dwidthheight);
		
		submitButton.addActionListener(this);
		topLeftButton.addActionListener(this);
		bottomRightButton.addActionListener(this);
	}
	public void setXY(int x1, int y1) {
		x = x1;
		y = y1;
		xy.setText("X: " + x + " Y: " + y);
	}
	
	public void updateWH() {
		width = xbottom - xtop;
		height = ybottom - ytop;
		Dwidthheight.setText("Width: " + width + "     Height: " + height);

	}
	//Handler Class
	public void actionPerformed(ActionEvent event) {
		if(event.getSource()==submitButton) {
			//THIS HAS TO CHANGE VVVVVVVVV
			map.setPoint(xtop, ytop, width, height, Float.parseFloat(input.getText()));
			//THIS HAS TO CHANGE ^^^^^^^^^^
			map.savePoints();
			map.rerender();
			map.render();
		}
		if(event.getSource()==topLeftButton) {
			xtop = x;
			ytop = y;
			xytop.setText("X: " + xtop + " Y: " + ytop);
			if(xtop != 0 && ytop != 0 && xbottom != 0 && ybottom != 0) {
				updateWH();
			}
		}
		if(event.getSource()==bottomRightButton) {
			xbottom = x;
			ybottom = y;
			xybottom.setText("X: " + xbottom + " Y: " + ybottom);
			if(xtop != 0 && ytop != 0 && xbottom != 0 && ybottom != 0) {
				updateWH();
			}
		}
	}
}