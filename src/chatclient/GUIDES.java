package chatclient;

import java.awt.Dimension;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

import java.awt.Color;
import java.awt.Font;

public class GUIDES {

	private static final int WIDTH_MAIN = 700;
	private final static int HEIGHT_MAIN = 500;
private final static double ver = 1.0;

private static JFrame framePre;
private static JFrame frame;
private static JPanel panelPre;
private static JPanel panel;
private static JTextField input_1;
private JTextField ipSet;
private JTextField portSet;
private static JEditorPane outputText;
private static JTextField input;
private static JScrollPane output;
private JScrollPane userList;
private JLabel userTitle,
		   userNameLabel,
		   ipLabel,
		   portLabel;
private static JButton btnSend;
private JButton btnConnect;
private Box horizontalBox;
private static JScrollPane scrollPane;

public static void main(String[] args) {
	frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.setSize(700, 432);
	
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(700, 500));
	
	input = new JTextField(30);
	input.setBounds(10, 373, 591, 23);
	input.setFont(new Font("Arial", Font.PLAIN, 14));
	
	output = new JScrollPane();
	output.setBounds(0, 0, 694, 367);
	output.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
	GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
	groupLayout.setHorizontalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
	);
	groupLayout.setVerticalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
	);
	
	outputText = new JEditorPane();
	outputText.setFont(new Font("Arial", Font.PLAIN, 14));
	output.setViewportView(outputText);
	output.setFocusable(false);
	DefaultCaret caret = (DefaultCaret) outputText.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
	btnSend = new JButton("Send");
	btnSend.setBounds(611, 373, 73, 24);
	panel.setLayout(null);
	panel.add(input);
	panel.add(btnSend);
	panel.add(output);
	frame.getContentPane().setLayout(groupLayout);
}
}
