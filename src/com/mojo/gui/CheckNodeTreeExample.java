package com.mojo.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class CheckNodeTreeExample extends JFrame {

  public CheckNodeTreeExample() {
    super("CheckNode TreeExample");
    String[] strs = {"swing",     // 0
         "platf",     // 1
         "basic",     // 2
         "metal",     // 3
         "JTree"};    // 4

    CheckNode[] nodes = new CheckNode[strs.length];
    for (int i=0;i<strs.length;i++) {
      nodes[i] = new CheckNode(strs[i]);
    }
    nodes[0].add(nodes[1]);
    nodes[1].add(nodes[2]);
    nodes[1].add(nodes[3]);
    nodes[0].add(nodes[4]);
    nodes[3].setSelected(true);
    JTree tree = new JTree( nodes[0] );
    tree.setCellRenderer(new CheckRenderer());
    tree.getSelectionModel().setSelectionMode(
      TreeSelectionModel.SINGLE_TREE_SELECTION
    );
    //nodes[i].setSelectionMode(mode);
    tree.putClientProperty("JTree.lineStyle", "Angled");
    tree.addMouseListener(new NodeSelectionListener(tree));
    JScrollPane sp = new JScrollPane(tree);
    
    JTextArea textArea = new JTextArea(3,10);
    JScrollPane textPanel = new JScrollPane(textArea);
    JButton button = new JButton("print");
    button.addActionListener(new ButtonActionListener(nodes[0], textArea));
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(button, BorderLayout.SOUTH);

    getContentPane().add(sp,    BorderLayout.CENTER);
    getContentPane().add(panel, BorderLayout.EAST);
    getContentPane().add(textPanel, BorderLayout.SOUTH);
  }

  public static void main(String args[]) {
    CheckNodeTreeExample frame = new CheckNodeTreeExample();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}