package com.mojo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.TreeSelectionModel;

public class MockMain {
	
	//
	static final int btn_width = 60;
	static final int btn_height = 26;
	static final int leftWidth = 200;
	static final int rightWidth = 480;
	static final int centerheight = 620;
	static final int height = 688;
	
	JFrame frame = new JFrame(); 
	JButton setting, create, app, del;
	JPanel settingPanel = new JPanel();
	JPanel createPanel = new JPanel();
	JPanel appPanel = new JPanel();
	JPanel topPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	JSplitPane splitPane = new JSplitPane ();//创建一个分隔容器类
	private JPanel buttonPane = new JPanel();//单选框组容器
	private ButtonGroup group = new ButtonGroup();
	public static CheckNode rootNode; //tree的根节点
	
	public MockMain() {
		frame.setSize(leftWidth+rightWidth, height);// 设置窗体大小 
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 当点击窗体关闭按钮的时候，就退出程
		frame.addWindowListener(new WindowAdapter()  //为了关闭窗口
		{
			public void windowClosing(WindowEvent e)
			{
				JOptionPane.showMessageDialog(frame, "ok");
				 System.exit(0);
			}
		});
		Container container = frame.getContentPane();// 在这里获取容器面板 
		//
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT,5,5);
		topPanel.setLayout(fl);
		setting = new ImageButton("设置", "res/setting.png");
		create = new ImageButton("创建", "res/add.png");
		app = new ImageButton("应用", "res/apply.png");
		del = new ImageButton("删除", "res/del.png");
		ButtonPerformActionListener btnlistener = new ButtonPerformActionListener(frame, setting, create, app, del);
		setting.addActionListener(btnlistener);
		create.addActionListener(btnlistener);
		app.addActionListener(btnlistener);
		del.addActionListener(btnlistener);
		topPanel.add(setting);
		topPanel.add(create);
		topPanel.add(del);
		JPanel top_right = new JPanel();
		top_right.setLayout(null);
		top_right.setPreferredSize(new Dimension(410+btn_width, btn_height+2));
		app.setBounds(410, 2, btn_width, btn_height);
		top_right.add(app);
		topPanel.add(top_right);
		container.add(topPanel, BorderLayout.NORTH);
		//
		splitPane.setOneTouchExpandable (true); //让分割线显示出箭头
		splitPane.setContinuousLayout (true); //当用户操作分割线箭头时，系统会重绘图形
		splitPane.setPreferredSize (new Dimension (leftWidth + rightWidth, centerheight));
		splitPane.setOrientation (JSplitPane.HORIZONTAL_SPLIT); //设置分割线为水平分割线
		centerPanel.add(splitPane);
		//
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		initLeftPanel(leftPanel, new String[]{"接口1","接口2","接口3"});
		initRightPanel(rightPanel);
		container.add(centerPanel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	/**
	 * tree
	 * @param data
	 */
	public void initLeftPanel(JPanel p,String[] data){
		p.setLayout(null);  
		CheckNode[] nodes = new CheckNode[data.length];
		rootNode = new CheckNode("Mock列表");
		for(int i=0;i<nodes.length;i++){
			nodes[i] = new CheckNode(data[i]);
			rootNode.add(nodes[i]);
		}
		JTree tree = new JTree(rootNode);
		tree.setCellRenderer(new CheckRenderer());
		tree.getSelectionModel().setSelectionMode(
		  TreeSelectionModel.SINGLE_TREE_SELECTION
		);
		//nodes[i].setSelectionMode(mode);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		tree.addMouseListener(new NodeSelectionListener(tree));
		
		MockMain.rootNode.add(new CheckNode("111"));
		tree.updateUI();
		
		JScrollPane sp = new JScrollPane(tree);
		sp.setBounds(2, 0, leftWidth, centerheight);  
		p.add(sp);
		splitPane.setDividerLocation(leftWidth);
		splitPane.setLeftComponent (p); //分割线左边
	}
	
	public void initRightPanel(JPanel parent){
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		parent.setLayout(gridbag);
//		parent.setBorder(BorderFactory.createLineBorder(Color.red));
		
		JLabel label = new JLabel("Request：", null, SwingConstants.LEFT); 
		label.setFont(new Font(Font.DIALOG,Font.BOLD,18));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(label, gbc);
		parent.add(label);
		parent.add(addSplitHr(gridbag, gbc));
		
		JLabel info1 = new JLabel("地址：", SwingConstants.LEFT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(info1, gbc);
		parent.add(info1);
		parent.add(addSpace(gridbag, gbc));
		JLabel urlBase = new JLabel("http://10.10.127.63:8081/", SwingConstants.LEFT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(urlBase, gbc);
		parent.add(urlBase);
		JTextField requst_param = new JTextField(10);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(requst_param, gbc);
		parent.add(requst_param);
		
		JLabel info2 = new JLabel("类型：");
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(info2, gbc);
		parent.add(info2);
		addBtn("URL", true);addBtn("UrlPattern", false);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, -10, 0, 0);
		gridbag.setConstraints(buttonPane, gbc);
		parent.add(buttonPane);
		parent.add(addEndSpace(gridbag, gbc));
		
		JLabel info3 = new JLabel("方法：");
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gridbag.setConstraints(info3, gbc);
		parent.add(info3);
		parent.add(addSpace(gridbag, gbc));
		JLabel info3_right = new JLabel("POST");
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gridbag.setConstraints(info3_right, gbc);
		parent.add(info3_right);
		parent.add(addEndSpace(gridbag, gbc));
		
		JLabel info4 = new JLabel("Body：");
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(info4, gbc);
		parent.add(info4);
		parent.add(addSpace(gridbag, gbc));
		JTextArea jta1 = new JTextArea(5,30);
		JScrollPane scroll = new JScrollPane(jta1);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0.1;
		gridbag.setConstraints(scroll, gbc);
		parent.add(scroll);
		
		//response
		JLabel responst = new JLabel("Response：", null, SwingConstants.LEFT); 
		responst.setFont(new Font(Font.DIALOG,Font.BOLD,18));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(responst, gbc);
		parent.add(responst);
		parent.add(addSplitHr(gridbag, gbc));
		
		JLabel rinfo = new JLabel("延迟：", SwingConstants.LEFT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(rinfo, gbc);
		parent.add(rinfo);
		parent.add(addSpace(gridbag, gbc));
		JTextField input_time = new JTextField(10);
		input_time.setText("0");
		//input_time.setEditable(false);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(input_time, gbc);
		parent.add(input_time);
		JLabel mills_str = new JLabel("毫秒", SwingConstants.LEFT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(mills_str, gbc);
		parent.add(mills_str);
		
		JLabel rinfo2 = new JLabel("状态：", SwingConstants.LEFT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(rinfo2, gbc);
		parent.add(rinfo2);
		parent.add(addSpace(gridbag, gbc));
		JTextField input_status = new JTextField(10);
		input_status.setText("200");
		//input_status.setEditable(false);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(input_status, gbc);
		parent.add(input_status);
		parent.add(addEndSpace(gridbag, gbc));
		
		JLabel returnVal = new JLabel("返回值：");
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(returnVal, gbc);
		parent.add(returnVal);
		parent.add(addSpace(gridbag, gbc));
		JTextArea jta2 = new JTextArea(10,30);
		//jta2.setEditable(false);
		JScrollPane scroll2 = new JScrollPane(jta2);
		scroll2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0.1;
		gridbag.setConstraints(scroll2, gbc);
		parent.add(scroll2);
		
		//取消， 保存
		JButton ok_btn = new JButton("确定");
		ok_btn.setBounds(rightWidth-80-btn_width, 2, btn_width, btn_height);
		JPanel ok_panel = new JPanel();
		ok_panel.setLayout(null);
		ok_panel.setPreferredSize(new Dimension(rightWidth-80, 30));
		ok_panel.add(ok_btn);
		gbc.fill = GridBagConstraints.WEST;
		gbc.gridwidth = 8;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(5, 0, 0, 0);
		gridbag.setConstraints(ok_panel, gbc);
		parent.add(ok_panel);
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//保存,删除按钮
			}
		};
		ok_btn.addActionListener(listener);
		
		JPanel p  = new JPanel();
		p.setLayout(null);
		p.setPreferredSize(new Dimension(rightWidth, centerheight));
		parent.setBounds(-20, 10, rightWidth, centerheight-30);
		p.add(parent);
		JScrollPane scroll3 = new JScrollPane(p);
		scroll3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		splitPane.setRightComponent (scroll3);//分割线右边
	}
	
	public JSeparator addSplitHr(GridBagLayout gridbag, GridBagConstraints gbc){
		JSeparator s1 = new JSeparator();
		s1.setForeground(Color.black);
		s1.setOrientation(SwingConstants.HORIZONTAL ); 
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0.01;
		gridbag.setConstraints(s1, gbc);
		return s1;
	}
	
	public JLabel addSpace(GridBagLayout gridbag, GridBagConstraints gbc){
		JLabel space = new JLabel("	", SwingConstants.LEFT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(space, gbc);
		return space;
	}
	
	public JLabel addEndSpace(GridBagLayout gridbag, GridBagConstraints gbc){
		JLabel spaceEnd = new JLabel("", SwingConstants.LEFT);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gridbag.setConstraints(spaceEnd, gbc);
		return spaceEnd;
	}
	
	public void addBtn(String name, boolean flag) {
		JRadioButton newBtn = new JRadioButton(name, flag);
		group.add(newBtn);
		buttonPane.add(newBtn);
		
		//this listener sets the label font size
		ActionListener listener = new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					
				}
		};
		
		newBtn.addActionListener(listener);
	}
	
	public static void main(String args[]){
		new MockMain();
	}
	
}