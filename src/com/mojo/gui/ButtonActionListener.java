package com.mojo.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JTextArea;
import javax.swing.tree.TreeNode;

public class ButtonActionListener implements ActionListener {
    CheckNode root;

    JTextArea textArea;

    ButtonActionListener(final CheckNode root, final JTextArea textArea) {
      this.root = root;
      this.textArea = textArea;
    }

    public void actionPerformed(ActionEvent ev) {
      Enumeration e = root.breadthFirstEnumeration();
      while (e.hasMoreElements()) {
        CheckNode node = (CheckNode) e.nextElement();
        if (node.isSelected()) {
          TreeNode[] nodes = node.getPath();
          textArea.append("\n" + nodes[0].toString());
          for (int i = 1; i < nodes.length; i++) {
            textArea.append("/" + nodes[i].toString());
          }
        }
      }
    }
  }