package com.stock;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

public class StockMain extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String[] columnNames = new String[] { "股票名称", "价格", "涨跌","涨跌幅", "成交量(万手)", "成交额(亿元)" };
	@SuppressWarnings("serial")
	public static List<String> stocks= new ArrayList<String>(){{
		add("sh000001");add("sz399006");add("sh600308");add("sz000726");add("sz002958");
	}};//,"sz399001" 深综
	public static String stock_url = "https://hq.sinajs.cn/etag.php?list=";
	
	static JPanel jpAdd;
	static JButton jbn;
	JLabel jbl;
	static JTextField jtx;
	
	public StockMain() {
		this.setUndecorated(true); // 去掉窗口的装饰 
		this.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getRootPane().setBorder(BorderFactory.createEmptyBorder());
		
		jbl = new JLabel("市场代码：");
		jbl.setPreferredSize(new Dimension(70, 20));
		jtx = new JTextField(10);
		jbn = new JButton("Add");
		jbn.setPreferredSize(new Dimension(60, 20));
		jpAdd = new JPanel();
		jpAdd.setPreferredSize(new Dimension(366, 30));
		jpAdd.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		jpAdd.add(jbl);
		jpAdd.add(jtx);
		jpAdd.add(jbn);
	}
	
	public static void main(String args[]) {
		final StockMain frame = new StockMain();
		final StockTablePanel stockMain = new StockTablePanel();
		frame.setLayout(new BorderLayout());
		frame.add(jpAdd, BorderLayout.NORTH);
		frame.add(stockMain, BorderLayout.CENTER);
		frame.setSize(new Dimension(368, 175));//366
		frame.setLocation(1600, 100);
		frame.setVisible(true);
		//frame.setAlwaysOnTop(true);
		jbn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = jtx.getText();
				System.out.println("text:" + text.toLowerCase());
				if(stocks.contains(text)) {
					JOptionPane.showMessageDialog(frame, "已存在该条信息！", "添加Stock", JOptionPane.INFORMATION_MESSAGE);
				}else if(text.equals("")){
					JOptionPane.showMessageDialog(frame, "证券代码不能为空！", "添加Stock", JOptionPane.INFORMATION_MESSAGE);
				}else if(!(text.toLowerCase().startsWith("sz") || text.toLowerCase().startsWith("sh"))){
					JOptionPane.showMessageDialog(frame, "证券代码不合法，sh/sz+6位代码！", "添加Stock", JOptionPane.INFORMATION_MESSAGE);
				}else {
					if(text.length()!=8) {
						JOptionPane.showMessageDialog(frame, "证券代码不合法(8位)，sh/sz+6位代码！", "添加Stock", JOptionPane.INFORMATION_MESSAGE);
					}else {
						stocks.add(text.toLowerCase());
						stockMain.refreshUI();
					}
				}
			}
		});
	}
	
}
