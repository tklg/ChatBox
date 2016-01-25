package chatclient;

import java.awt.Dimension;

import javax.swing.*;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Component;

public class GUI_SERVLIST {

private static JFrame frame;
private static JPanel panel;
private static JLabel lblServerName;
private static JLabel label_1;
private static JTextField userNameSet;
private static JTextField inputIp;
private static JButton btnAddServer;
private static JButton btnRefreshList;
private static JTextField inputName;
private static Component horizontalGlue;

public static void main(String[] args) {
	frame = new JFrame();
	frame.setResizable(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(ChatClient.WIDTH, ChatClient.HEIGHT);
	
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(ChatClient.WIDTH, ChatClient.HEIGHT));
	GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
	groupLayout.setHorizontalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 704, Short.MAX_VALUE)
	);
	groupLayout.setVerticalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
			.addGroup(groupLayout.createSequentialGroup()
				.addComponent(panel, GroupLayout.PREFERRED_SIZE, 402, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(63, Short.MAX_VALUE))
	);
	
	JScrollPane serverList = new JScrollPane();
	serverList.setViewportBorder(null);
	
	Box horizontalBox_1 = Box.createHorizontalBox();
	
	JLabel lblUsername = new JLabel("Username");
	lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
	
	userNameSet = new JTextField();
	userNameSet.setToolTipText("Server Name");
	userNameSet.setColumns(10);
	
	JLabel errorLabel = new JLabel("");
	
	lblServerName = new JLabel("Server Name");
	lblServerName.setHorizontalAlignment(SwingConstants.RIGHT);
	
	inputName = new JTextField();
	inputName.setColumns(10);
	
	inputIp = new JTextField();
	inputIp.setToolTipText("Server IP");
	inputIp.setColumns(10);
	
	label_1 = new JLabel("IP");
	label_1.setHorizontalAlignment(SwingConstants.RIGHT);
	
	btnAddServer = new JButton("Add Server");
	GroupLayout gl_panel = new GroupLayout(panel);
	gl_panel.setHorizontalGroup(
		gl_panel.createParallelGroup(Alignment.LEADING)
			.addGroup(gl_panel.createSequentialGroup()
				.addGap(10)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
					.addComponent(userNameSet, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
				.addGap(61)
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
					.addComponent(lblServerName, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
					.addComponent(inputName, 157, 182, Short.MAX_VALUE)
					.addComponent(inputIp, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(btnAddServer, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
				.addGap(9))
			.addComponent(horizontalBox_1, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
			.addGroup(gl_panel.createSequentialGroup()
				.addComponent(serverList, GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
				.addGap(1))
	);
	gl_panel.setVerticalGroup(
		gl_panel.createParallelGroup(Alignment.LEADING)
			.addGroup(gl_panel.createSequentialGroup()
				.addComponent(horizontalBox_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(serverList, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
					.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
							.addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblUsername)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblServerName)
								.addComponent(inputName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(inputIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label_1)
							.addComponent(userNameSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(12))
					.addGroup(gl_panel.createSequentialGroup()
						.addComponent(btnAddServer)
						.addGap(22))))
	);
	
	horizontalGlue = Box.createHorizontalGlue();
	horizontalBox_1.add(horizontalGlue);
	
	JButton btnEdit = new JButton("Edit");
	btnEdit.setPreferredSize(new Dimension(100, 20));
	horizontalBox_1.add(btnEdit);
	btnEdit.setEnabled(false);
	
	JButton btnDelete = new JButton("Delete");
	btnDelete.setPreferredSize(new Dimension(100, 20));
	horizontalBox_1.add(btnDelete);
	btnDelete.setEnabled(false);
	
	btnRefreshList = new JButton("Refresh");
	btnRefreshList.setPreferredSize(new Dimension(100, 20));
	horizontalBox_1.add(btnRefreshList);
	panel.setLayout(gl_panel);
	frame.getContentPane().setLayout(groupLayout);
	frame.pack();
	frame.setVisible(true);
}
}
