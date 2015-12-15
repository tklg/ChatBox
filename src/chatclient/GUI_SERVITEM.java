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
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class GUI_SERVITEM {

	private final int WIDTH_SETTINGS = 400,
	  		  HEIGHT_SETTINGS = 200;
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
private JScrollPane userList;
private JLabel userTitle,
		   userNameLabel,
		   ipLabel,
		   portLabel;
private JButton btnConnect;
private Box horizontalBox;
private static JScrollPane scrollPane;
private static JLabel lblServerName;
private static JLabel lblServerIp;
private static JLabel lblPing;
private static JButton btnConnect_1;

public static void main(String[] args) {
	frame = new JFrame();
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	panel = new JPanel();
	panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
	panel.setPreferredSize(new Dimension(WIDTH_MAIN, HEIGHT_MAIN));
	GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
	groupLayout.setHorizontalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
		.addComponent(panel, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
	);
	groupLayout.setVerticalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
			.addGroup(groupLayout.createSequentialGroup()
				.addComponent(panel, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(248, Short.MAX_VALUE))
	);
	panel.setLayout(null);
	
	lblServerName = new JLabel("Server Name");
	lblServerName.setBounds(10, 11, 209, 14);
	panel.add(lblServerName);
	
	lblServerIp = new JLabel("Server IP");
	lblServerIp.setBounds(10, 24, 209, 14);
	panel.add(lblServerIp);
	
	lblPing = new JLabel("Ping");
	lblPing.setBounds(505, 11, 88, 27);
	panel.add(lblPing);
	
	btnConnect_1 = new JButton("Connect");
	btnConnect_1.setBounds(605, 11, 89, 27);
	panel.add(btnConnect_1);
	
	JLabel lblMessage = new JLabel("Message");
	lblMessage.setBounds(161, 11, 334, 27);
	panel.add(lblMessage);
	frame.getContentPane().setLayout(groupLayout);
	frame.pack();
	frame.setVisible(true);
}
}
