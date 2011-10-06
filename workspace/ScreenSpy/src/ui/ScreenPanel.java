package ui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Image;

public class ScreenPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5421943377039099877L;
	/**
	 * Create the panel.
	 */
	JLabel label_image;
	
	
	public ScreenPanel(String name) {
		this.setName(name);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		JScrollPane scrollPane_1 = new JScrollPane(panel);
		panel.setLayout(new BorderLayout(0, 0));
		add(scrollPane_1, BorderLayout.CENTER);
		
		label_image = new JLabel();
		panel.add(label_image, BorderLayout.CENTER);
	}
	public void setImage(Image image){
		label_image.setIcon(new ImageIcon(image));
	}
}
