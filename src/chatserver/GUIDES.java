package chatserver;

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

	private final int WIDTH_SETTINGS = 400,
	  		  HEIGHT_SETTINGS = 200;
private final static double ver = 1.0;

private static JFrame framePre;
private static JFrame frame;
private static JPanel panelPre;
private static JPanel panel;
private static JTextField input_1;
private JTextField ipSet;
private JTextField portSet;
private static JTextArea outputText;
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
private static JScrollPane users;

public static void main(String[] args) {
	frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	frame.setSize(ChatServer.WIDTH, ChatServer.HEIGHT);
	
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(ChatServer.WIDTH, ChatServer.HEIGHT));
	
	input = new JTextField(30);
	input.setFont(new Font("Arial", Font.PLAIN, 14));
	
	output = new JScrollPane();
	output.setViewportBorder(null);
	GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
	groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
		);
	
	outputText = new JTextArea();
	outputText.setEditable(false);
	outputText.setFont(new Font("Arial", Font.PLAIN, 14));
	outputText.setBackground(new Color(30, 30, 30));
	output.setViewportView(outputText);
	output.setFocusable(false);
	DefaultCaret caret = (DefaultCaret) outputText.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
	btnSend = new JButton("Send");
	
	users = new JScrollPane();
	users.setViewportBorder(null);
	//btnSend.addActionListener(new SendListener());
	GroupLayout gl_panel = new GroupLayout(panel);
	gl_panel.setHorizontalGroup(
		gl_panel.createParallelGroup(Alignment.TRAILING)
			.addGroup(gl_panel.createSequentialGroup()
				.addContainerGap()
				.addComponent(input, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(btnSend)
				.addGap(4))
			.addGroup(gl_panel.createSequentialGroup()
				.addComponent(output, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(users, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
	);
	gl_panel.setVerticalGroup(
		gl_panel.createParallelGroup(Alignment.TRAILING)
			.addGroup(gl_panel.createSequentialGroup()
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
					.addComponent(output, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
					.addComponent(users, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
					.addComponent(input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
				.addGap(6))
	);
	
	JEditorPane usersText = new JEditorPane();
	usersText.setBackground(new Color(20, 20, 20));
	users.setViewportView(usersText);
	panel.setLayout(gl_panel);
	frame.getContentPane().setLayout(groupLayout);
	frame.pack();
	frame.setVisible(true);
}
}
