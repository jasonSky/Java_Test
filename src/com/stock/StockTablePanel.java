package com.stock;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class StockTablePanel extends JPanel{// implements Runnable

	private static final long serialVersionUID = 1L;
	TableModel dataModel;
	JScrollPane scrollpane;
	JTable table;
	JPopupMenu popup;
	Timer timer;
	List<String[]> list;
	
	private static double[] prePrice;
	String substr="";
	
	public StockTablePanel(){
		list = getData(StockMain.stocks);
		prePrice= new double[StockMain.stocks.size()+2];
		dataModel = getTableModel();
		table = new JTable(dataModel);
		//隐藏默认表头
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setPreferredSize(new Dimension(0, 0));
		table.getTableHeader().setDefaultRenderer(renderer);
		//cell
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
		cr.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, cr);
		scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(360, 115));//设置大小
		this.add(scrollpane);
		
		timer = new Timer(1000,new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				refreshUIData();
			}
		});
		timer.start();
		
		popup = new JPopupMenu();
		JMenuItem menu = new JMenuItem("删除");
		menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				StockMain.stocks.remove(selectedRow-2);
				refreshUI();
			}
			
		});
		popup.add(menu);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				//判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键
				if (evt.getButton() == MouseEvent.BUTTON3) {
					//通过点击位置找到点击为表格中的行
					int focusedRowIndex = table.rowAtPoint(evt.getPoint());
					if (focusedRowIndex>= -1 && focusedRowIndex<=3) {
						return;
					}
					//将表格所选项设为当前右键点击的行
					table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
					//弹出菜单
					popup.show(table, evt.getX(), evt.getY());
				}
			}
		});
	}
	
	public void refreshUI() {
		list=getData(StockMain.stocks);
		prePrice= new double[StockMain.stocks.size()+2];
		table.validate();
		table.updateUI();
	}
	
	public void refreshUIData() {
		list=getData(StockMain.stocks);
		table.validate();
		table.updateUI();
	}
	
	public List<String[]> getData(List<String> stocks) {
		List<String[]> resList = new ArrayList<String[]>();
		resList.add(StockMain.columnNames);
		try {
			String result = HttpUtils.sendGet(StockMain.stock_url + "hf_CHA50CFD", null);
			String[] strs = result.substring(result.indexOf("\"")+1, result.length()-3).split(",");
			double fixVal = Double.parseDouble(strs[0])-Double.parseDouble(strs[7]);
			String[] a50 = new String[] {"A50", strs[0], String.valueOf(fixVal), String.valueOf(fixVal/Double.parseDouble(strs[7])), strs[9], ""};
			resList.add(a50);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Iterator<String> it=stocks.iterator();it.hasNext();) {
			String result="";
			try {
				result = HttpUtils.sendGet(StockMain.stock_url+it.next(), null);
				//System.out.println(result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] strs = result.substring(result.indexOf("\"")+1, result.length()-3).split(",");
			double fixVal = Double.parseDouble(strs[3])-Double.parseDouble(strs[2]);
			String[] item = new String[] {strs[0], strs[3], String.valueOf(fixVal), String.valueOf(fixVal/Double.parseDouble(strs[2])), strs[8], strs[9]};
			resList.add(item);
		}
		
		return resList;
	}  
	
	public AbstractTableModel getTableModel() {  
		
		return new AbstractTableModel() {
			
			public int getColumnCount() {  
				return 5;  
			}  
			public int getRowCount() {
				return list.size();  
			}  
			public Object getValueAt(int row, int col) {
				switch (col) {  
				case (0): {  
					return list.get(row)[0];  
				}  
				case (1): {  
					if (row==0) {
						return list.get(row)[1];
					}else {
						return String.format("%.2f", Double.parseDouble(list.get(row)[1]));
					}
				}  
				case (2): {
//					return list.get(row)[2];
					if (row==0) {
						return list.get(row)[2];
					}else {
						
//						boolean flag = list.get(row)[2].indexOf("-") != -1;
//						if(flag) {
//							return "-" + String.format("%.2f", Double.parseDouble(list.get(row)[2].substring(1)));
//						}else {
						final int rowx = row;
						//System.out.println("xx:" + prePrice[row]);
						if(prePrice[row] != 0d) {
							if(Double.parseDouble(list.get(row)[2]) < prePrice[row]) {
								substr = "↓";
							}else if(Double.parseDouble(list.get(row)[2]) > prePrice[row]) {
								new Thread(
										new Runnable() { 
											@Override 
											public void run() {
												if((Double.parseDouble(list.get(rowx)[2]))/(Double.parseDouble(list.get(rowx)[1])-Double.parseDouble(list.get(rowx)[2])) > 0.03){//快速拉升1%
													JOptionPane.showMessageDialog(null, list.get(rowx)[0]+"快速拉升, 当前涨跌："+String.format("%.2f", Double.parseDouble(list.get(rowx)[2])), "拉升", JOptionPane.INFORMATION_MESSAGE); 
												}
											} 
								}).start();
								substr = "↑";
							}
							else {
								substr = "";
							}
						}
						prePrice[row] = Double.parseDouble(list.get(row)[2]);
						return (list.get(row)[2].indexOf("-")==-1?"+":"") + String.format("%.2f", Double.parseDouble(list.get(row)[2])) + substr;
//						}
					}
				}
				case (3): {
					if (row==0)
						return list.get(row)[4];
					else if(row==2 || row==1)
						return String.format("%.2f", Double.parseDouble(list.get(row)[4])/10000);
					else
						return String.format("%.2f", Double.parseDouble(list.get(row)[4])/100/10000);  
				} 
				default:
					if (row==0)
						return list.get(row)[5];
					else if(row==1)
						return "--";
					else
						return String.format("%.2f", Double.parseDouble(list.get(row)[5])/10000/10000);  
				}  
			}  
		};  
	}
	
	
	
}
