package ps.hell.ml.patternRecognition.algorithm.base;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	 
	   private BufferedImage image3;
	 
	    public ImagePanel(BufferedImage image3) {
	    	this.image3=image3;
	   //     try {
	            //image = ImageIO.read(new File(file));
	  //      } catch (IOException ex) {
	            // handle exception...
	   //     }
	    }
	 
		@Override
	    public void paintComponent(Graphics g) {
	        g.drawImage(image3, 0, 0, null); 
	    }
	 
	}
