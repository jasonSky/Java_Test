package com.screen.advert;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainAd implements ActionListener {

	JFrame frame = new JFrame(); 
	JPanel centerPanel = new JPanel();
	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	JSplitPane splitPane = new JSplitPane ();//创建一个分隔容器类
	static final int leftWidth = 200;
	static final int rightWidth = 200;
	static final int height = 268;
	JButton jb = new JButton("send");
	final JButton jb1 = new JButton("+");
	final JButton jb2 = new JButton(" - ");
	String titles[] = { "Single dogs" };
	final JComboBox comboBox = new JComboBox(titles);
	private ButtonGroup group = new ButtonGroup();
	final JRadioButton mode1 = new JRadioButton("显示");
	final JRadioButton mode2 = new JRadioButton("分割");
	final JTextField tx3 = new JTextField(30);
	
	static{
		//autoit
		File ait = VV.getFile("autoit", "/lib/AutoItX3.dll");
		if(ait == null)
			ait = new File("lib", "AutoItX3.dll");
		//reg auto.dll
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("regsvr32 /s" + ait.getAbsolutePath());
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		new Runnable(){
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				VV.enter(KeyEvent.VK_ENTER);
//				
//			}
//			
//		}.run();
		
	}
	
	public MainAd(){
		frame.setSize(leftWidth+rightWidth, height);// 设置窗体大小 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 当点击窗体关闭按钮的时候，就退出程
		Container container = frame.getContentPane();// 在这里获取容器面板
		
		initRightGUI(rightPanel);
		splitPane.setLeftComponent (leftPanel);
		splitPane.setRightComponent(rightPanel);
		//
		splitPane.setOneTouchExpandable (true); //让分割线显示出箭头
		splitPane.setContinuousLayout (true); //当用户操作分割线箭头时，系统会重绘图形
		splitPane.setPreferredSize (new Dimension (leftWidth + rightWidth, height));
		splitPane.setOrientation (JSplitPane.HORIZONTAL_SPLIT); //设置分割线为水平分割线
		
		container.add(splitPane, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	public void initRightGUI(JPanel panel){
		panel.setLayout(null);
		panel.setSize(rightWidth, height);
		JLabel lb1 = new JLabel("Swing: ");
		lb1.setBounds(10, 10, 50, 20);
	    comboBox.setEditable(true);
	    comboBox.setBounds(65, 10, 200, 20);
		panel.add(lb1);
		panel.add(comboBox);
		jb1.setBounds(270, 10, 45, 20);
		jb2.setBounds(315, 10, 45, 20);
		panel.add(jb1);
		panel.add(jb2);
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		JLabel lb2 = new JLabel("Text: ");
		lb2.setBounds(10, 35, 50, 20);
		final JTextArea tx2 = new JTextArea(100, 100);
		JScrollPane scroll = new JScrollPane(tx2);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		scroll.setBounds(65, 35, 200, 100);
		panel.add(lb2);
		panel.add(scroll);
		//model
		mode1.setBounds(50, 140, 80, 20);
		mode2.setBounds(135, 140, 80, 20);
		mode1.addActionListener(this);
		mode2.addActionListener(this);
		group.add(mode1);
		group.add(mode2);
		group.setSelected(mode1.getModel(), true);
		panel.add(mode1);
		panel.add(mode2);
		JLabel lb3 = new JLabel("Times: ");
		lb3.setBounds(10, 170, 50, 20);
		tx3.setBounds(65, 170, 200, 20);
		panel.add(lb3);
		panel.add(tx3);
		
		jb.setBounds(125, 200, 70, 20);
		panel.add(jb);
		
		jb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(mode1.isSelected()){
					int times = 0;
					try{
						times = Integer.parseInt(tx3.getText());
					}catch(Exception ex){
						JOptionPane.showMessageDialog(frame, ex.getMessage());
					}
					new VV().send(tx2.getText(), times, comboBox.getItemAt(comboBox.getSelectedIndex()).toString());
				}else{
					new VV().sendC(tx2.getText(), comboBox.getItemAt(comboBox.getSelectedIndex()).toString());
				}
			}
			
		});
	}
	
	public static void main(String args[]){
		new MainAd();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == jb1){
			String text = JOptionPane.showInputDialog("Swing Title:");
			if(text != null && !text.equals(""))
				comboBox.addItem(text);
		}else if(e.getSource() == jb2){
			comboBox.removeItemAt(comboBox.getSelectedIndex());
		}else if(e.getSource() == mode1){
			tx3.setEditable(true);
		}else if(e.getSource() == mode2){
			tx3.setText("");
			tx3.setEditable(false);
		}
	}
	
}
