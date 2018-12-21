package com.mojo.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ButtonPerformActionListener implements ActionListener {
	JFrame frame;
	JButton btnSetting;
	JButton btnCreate;
	JButton btnApply;
	JButton btnDel;

	ButtonPerformActionListener(final JFrame frame, final JButton btn1,final JButton btn2, final JButton btn3, final JButton btn4) {
		this.frame = frame;
		this.btnSetting = btn1;
		this.btnCreate = btn2;
		this.btnApply = btn3;
		this.btnDel = btn4;
	}

	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource().equals(btnSetting)){
			MyDialog dialog = new MyDialog(frame, true);
			dialog.setVisible(true);
		}else if(ev.getSource().equals(btnCreate)){
			CreateDialog dialog = new CreateDialog(frame, true);
			dialog.setVisible(true);
		}else if(ev.getSource().equals(btnApply)){
			System.out.println("3");
		}else if(ev.getSource().equals(btnDel)){
			int i = JOptionPane.showConfirmDialog(frame, "确定删除吗?", "删除mock接口", JOptionPane.YES_NO_OPTION);
			//通过返回值判断进行删除
			System.out.println(i);
		}
	}
	}

class CreateDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	JLabel label1 = new JLabel("服务器地址：");
	JTextField server_name = new JTextField(50);
	JButton ok = new JButton("确定");
	JButton cancel = new JButton("取消");
	
	CreateDialog(JFrame parent, boolean modal){
		super(parent, modal);
		setTitle("创建");
		setSize(300,130);
		setResizable(false);
		setLayout(null);
		this.setLocationRelativeTo(parent);
//		this.setLocation((MockMain.leftWidth + MockMain.rightWidth)/4, (MockMain.height-100)/2);
		add(label1);
		label1.setBounds(20, 20, 100, 20);
		add(server_name);
		server_name.setBounds(120, 20, 150, 20);
		
		add(ok);
		add(cancel);
		cancel.setBounds(60, 60, 60, 25);
		ok.setBounds(180, 60, 60, 25);
		ok.addActionListener(this);
		cancel.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource()==ok){
		}

		dispose();
	}
}

class MyDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	JLabel label1 = new JLabel("服务器地址：");
	JLabel label2 = new JLabel("服务器端口：");
	JLabel label3 = new JLabel("项             目：");
	JTextField server_loc = new JTextField(50);
	JTextField server_port = new JTextField(50);
	JComboBox<String> cb = new JComboBox<String>(new String[]{"A", "B"});
	
	JButton ok = new JButton("确定");
	JButton cancel = new JButton("取消");
	
	MyDialog(JFrame parent, boolean modal){
		super(parent, modal);
		setTitle("设置");
		setSize(300,200);
		setResizable(false);
		setLayout(null);
		this.setLocationRelativeTo(parent);
//		this.setLocation((MockMain.leftWidth + MockMain.rightWidth)/4, (MockMain.height-100)/2);
		add(label1);
		add(label2);
		add(label3);
		label1.setBounds(20, 30, 100, 20);
		label2.setBounds(20, 60, 100, 20);
		label3.setBounds(20, 90, 100, 20);
		add(server_loc);
		add(server_port);
		add(cb);
		server_loc.setBounds(110, 30, 150, 20);
		server_port.setBounds(110, 60, 150, 20);
		cb.setBounds(110, 90, 150, 20);
		cb.setSelectedItem("B");
		
		add(ok);
		add(cancel);
		cancel.setBounds(60, 130, 60, 25);
		ok.setBounds(180, 130, 60, 25);
		ok.addActionListener(this);
		cancel.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e){
		if (e.getSource()==ok){
//			int row = Integer.parseInt(server_loc.getText());
//			int col = Integer.parseInt(server_port.getText());
		}

		dispose();
	}
}
