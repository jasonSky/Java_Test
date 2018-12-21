package com.mojo.gui;

import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ImageButton extends JButton {

	private static final long serialVersionUID = 1L;

	public ImageButton(String text, String imagePath){
		ImageIcon ico = new ImageIcon(imagePath);
		Image temp = ico.getImage().getScaledInstance(20,20, Image.SCALE_DEFAULT);
		ico=new ImageIcon(temp); 
		this.setFocusPainted(false);
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setHorizontalTextPosition(SwingConstants.RIGHT);
		this.setText(text);
		this.setIcon(ico);
	}
	
}
