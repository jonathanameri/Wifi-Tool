import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Scanner;

import javax.swing.*;

public class mapView2 extends JFrame implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8390765595562296172L;
	private ImageIcon emptyMap;
	private int setWidth;
	private int setHeight;
	private int imageHeight;
	private int imageWidth;
	private BufferStrategy strategy;
	private ImageIcon OGMap;
	
	private String currentData;
	
	private newPoint2 p;
	
	private float max;
	private float min;
	
	//File stuff
	Formatter x;
	Scanner y;
	
	private int grid2Width = 1050, grid2Height = 750;
	private float[] grid2 = new float[grid2Width*grid2Height];
	private int[] grid2W = new int[grid2Width*grid2Height];
	private int[] grid2H = new int[grid2Width*grid2Height];
	
	public mapView2() {
		super("Add points to map");
		setResizable(false);
		
		imageWidth = 2100;
		imageHeight = 1500;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setWidth = screenSize.width;
		setHeight = screenSize.height;
		while(imageWidth > setWidth || imageHeight > setHeight) {
			imageWidth *= 9;
			imageHeight *= 9;
			imageWidth /= 10;
			imageHeight /= 10;
		}
		imageWidth *= 7;
		imageWidth /= 10;
		imageHeight *= 7;
		imageHeight /= 10;
		
		for (int i = 0; i < grid2.length; i++)
		{
			grid2[i] = 0;
		}
		for (int i = 0; i < grid2W.length; i++)
		{
			grid2W[i] = 50;
		}
		for (int i = 0; i < grid2H.length; i++)
		{
			grid2H[i] = 50;
		}
		
		p = new newPoint2(this);
		p.setSize(210,270);
		p.setLocation(imageWidth, 0);
		p.setVisible(true);
		
		this.processFile();
	}
	
	public void finalize() {
		emptyMap = new ImageIcon("School Map 2.png");
		OGMap = new ImageIcon("School Map 2.png");
		this.rerender();
		
		addMouseListener(this);
		setVisible(true);
	}
	
	public void render() {
		//creating buffer strategy, prerenders 1 frames
        this.strategy = getBufferStrategy();

        if (this.strategy == null)
        {
            createBufferStrategy(1);
            this.strategy = getBufferStrategy();
        }

        Graphics g = this.strategy.getDrawGraphics();
        update(g);
        g.drawImage(emptyMap.getImage(), 0, 11, imageWidth, imageHeight, null);
        g.dispose();
        this.strategy.show();
	}
	
	public void rerender() {
		Image image = OGMap.getImage(); // transform it 
		
		//STUPID STUFF
		BufferedImage before = new BufferedImage(2100, 1500, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = before.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		BufferedImage after = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2 = after.createGraphics();
		g2.drawImage(before.getScaledInstance(imageWidth, imageHeight, java.awt.Image.SCALE_DEFAULT), 0, 0, null);
		g2.dispose();
		
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//!!!!!!!!At this point, the original map image has been scaled and drawn as a buffered image!!!!!!!
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		for(int xx = 0; xx < grid2Width; xx++) {
			for(int yy = 0; yy < grid2Height; yy++) {
				if(getPoint(xx, yy) != 0) {
					//For each point in the grid2, if it has a value(download speed), draw a rectangle at the coordinates
					drawRectangle(xx*imageWidth/grid2Width, yy*imageHeight/grid2Height, (getPointWidth(xx, yy)*imageWidth/grid2Width)+4, (getPointHeight(xx, yy)*imageHeight/grid2Height)+4, after, gradient((getPoint(xx, yy) - min) / (max - min)));
				}
			}
		}
		emptyMap = new ImageIcon(after);  // transform it back
		add(new JLabel(emptyMap));
	}
	
	public void drawRectangle(int x, int y, int width, int height, BufferedImage img, int color) {
		for(int xx = 0; xx < width; xx++) {
			for(int yy = 0; yy < height; yy++) {
				img.setRGB(xx + x, yy + y, blend(color, img.getRGB(xx + x, yy + y)));
			}
		}
	}
	

	
	public static int gradient(float percent) 
    {
        float r = 1;
        float g = percent*2;
        float b = 0;
        if (percent >= .25 && percent < .5)
        {
            g = 1;
            r = 1-percent+.25f;
        }
        else if (percent >= .5 && percent < .75)
        {
            r = .4f;
            g = 1-percent+.5f;
            b = .4f;
        }
        else if (percent >= .75)
        {
            r = 0 ;
            g = .7f;
            b = 1-percent+.75f;
        }
        
        return 200<<24|(int)(255f*r)<<16|(int)(255f*g)<<8|(int)(255f*b);
    }
	
	//
	public static int blend(int fg, int bg)
    {
		//<<24 = Alpha, <<16 = Red, <<8 = Green, <<0 = Blue
        float a = getAlphaF(fg);
        return 255<<24|Math.round(a*getRed(fg)+(1-a)*getRed(bg))<<16|Math.round(a*getGreen(fg)+(1-a)*getGreen(bg))<<8|Math.round(a*getBlue(fg)+(1-a)*getBlue(bg));
    }
	
	public static int getRed(int color) {return color>>16&0xff;}
    public static int getGreen(int color) {return color>>8&0xff;}
    public static int getBlue(int color) {return color&0xff;}
    public static float getAlphaF(int color) {return (float)(color>>24&0xff)/255;}
	
	private void calcMax()
    {
        max = 1;
        for (int i = 0; i < this.grid2.length; i++)
        {
            if (grid2[i] > max)
            {
                max = grid2[i];
            }
        }
    }
	
	
	private void calcMin()
    {
        min = Float.MAX_VALUE;
        for (int i = 0; i < this.grid2.length; i++)
        {
            if (grid2[i] < min && grid2[i] !=0)
            {
                min = grid2[i];
            }
        }
    }
	
	public void savePoints() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File("Points2.txt"));
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		for(int i = 0; i < grid2.length; i++) {
			if(grid2[i] != 0) {
				try {
					y = new Scanner(new File("Points2.txt"));
				} catch (FileNotFoundException e) {
					File f = new File("Points2.txt");
					try {
						f.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				readFile();
				try {
					x = new Formatter("Points2.txt");
					x.format("%s%s %s %s %s %s", currentData, i%grid2Width, i/grid2Width, grid2W[i], grid2H[i], grid2[i]);
					x.close();	
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public void readFile() {
		currentData = "";
		while(y.hasNext()) {
			currentData += y.nextLine() + "\n";
		}
		y.close();
	}
	
	public void processFile() {
		try {
			Scanner s = new Scanner(new File("Points2.txt"));
			while(s.hasNextLine()) {
				setPoint(s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt(), s.nextFloat());
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Simple get methods
	public int getProperWidth(){return imageWidth;}
	public int getProperHeight(){return imageHeight;}
	
	//Point methods
	public void setPoint(int x, int y, int width, int height, float value)
	{
		//Add point to value grid2
		this.grid2[y*this.grid2Width+x] = value;
		calcMax();
		calcMin();
		
		//Add point to width grid2 and height grid2
		this.grid2W[y*this.grid2Width+x] = width;
		this.grid2H[y*this.grid2Width+x] = height;
		
		
	}
	public float getPoint(int x, int y)
	{
		return this.grid2[y*this.grid2Width+x];
	}
	
	public int getPointWidth(int x, int y)
	{
		return this.grid2W[y*this.grid2Width+x];
	}
	public int getPointHeight(int x, int y)
	{
		return this.grid2H[y*this.grid2Width+x];
	}

	//MouseListener
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		p.setXY((e.getX()) * grid2Width/(imageWidth), (e.getY()-11) * grid2Height/(imageHeight));
	}
	public void mouseReleased(MouseEvent e) {}
}