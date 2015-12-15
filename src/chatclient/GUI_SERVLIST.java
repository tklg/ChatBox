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
import javax.swing.border.TitledBorder;

public class GUI_SERVLIST {

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
private Box horizontalBox;
private static JScrollPane scrollPane;
private static JLabel lblServerName;
private static JLabel label_1;
private static JTextField userNameSet;
private static JTextField inputIp;
private static JButton btnAddServer;
private static JButton btnRefreshList;
private static JTextField inputName;

public static void main(String[] args) {
	frame = new JFrame();
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(WIDTH_MAIN, HEIGHT_MAIN);
	
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(WIDTH_MAIN, HEIGHT_MAIN));
	GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
	groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
		);
	
	JScrollPane serverList = new JScrollPane();
	serverList.setBounds(0, 23, 704, 304);
	serverList.setViewportBorder(null);
	
	lblServerName = new JLabel("Server Name");
	lblServerName.setHorizontalAlignment(SwingConstants.RIGHT);
	lblServerName.setBounds(270, 341, 148, 14);
	
	label_1 = new JLabel("IP");
	label_1.setHorizontalAlignment(SwingConstants.RIGHT);
	label_1.setBounds(270, 372, 148, 14);
	
	userNameSet = new JTextField();
	userNameSet.setBounds(77, 356, 148, 20);
	userNameSet.setToolTipText("Server Name");
	userNameSet.setColumns(10);
	
	inputIp = new JTextField();
	inputIp.setBounds(428, 369, 148, 20);
	inputIp.setToolTipText("Server IP");
	inputIp.setColumns(10);
	
	btnAddServer = new JButton("Add Server");
	btnAddServer.setBounds(586, 368, 108, 23);
	
	btnRefreshList = new JButton("Refresh List");
	btnRefreshList.setBounds(596, 0, 108, 23);
	panel.setLayout(null);
	panel.add(lblServerName);
	panel.add(userNameSet);
	panel.add(inputIp);
	panel.add(btnRefreshList);
	panel.add(label_1);
	panel.add(btnAddServer);
	panel.add(serverList);
	
	inputName = new JTextField();
	inputName.setBounds(428, 338, 148, 20);
	panel.add(inputName);
	inputName.setColumns(10);
	
	JLabel lblUsername = new JLabel("Username");
	lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
	lblUsername.setBounds(10, 359, 61, 14);
	panel.add(lblUsername);
	
	JLabel lblError = new JLabel("");
	lblError.setBounds(10, 334, 327, 14);
	panel.add(lblError);
	
	JButton btnConnect = new JButton("Connect");
	btnConnect.setBounds(586, 334, 108, 23);
	panel.add(btnConnect);
	
	JButton btnDelete = new JButton("Delete");
	btnDelete.setEnabled(false);
	btnDelete.setBounds(507, 0, 89, 23);
	panel.add(btnDelete);
	
	JButton btnEdit = new JButton("Edit");
	btnEdit.setEnabled(false);
	btnEdit.setBounds(417, 0, 89, 23);
	panel.add(btnEdit);
	frame.getContentPane().setLayout(groupLayout);
	frame.pack();
	frame.setVisible(true);
}
}
