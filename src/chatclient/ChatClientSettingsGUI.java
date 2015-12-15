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

public class ChatClientSettingsGUI {
	
	private static final String ver = ChatClient.ver;
	private static JFrame frame;
	private static JPanel panel;
	private static JLabel label_1;
	private static JLabel errorLabel;
	private static JLabel lblServerName;
	private static JTextField inputName;
	private static JTextField inputIp;
	private static JButton btnAddServer;
	private static JButton btnRefreshList;
	private static JTextField userNameSet;
	private boolean doneSettings = false;
	
	public ChatClientSettingsGUI() {
		frame = new JFrame("ChatClient v" + ver + " - Setup");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(ChatClient.WIDTH, ChatClient.HEIGHT);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(ChatClient.WIDTH, ChatClient.HEIGHT));
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
		
		userNameSet = new JTextField("name");
		userNameSet.setBounds(77, 356, 148, 20);
		userNameSet.setToolTipText("Server Name");
		userNameSet.setColumns(10);
		
		inputIp = new JTextField("localhost");
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
		
		errorLabel = new JLabel("");
		errorLabel.setBounds(10, 334, 327, 14);
		errorLabel.setForeground(new Color(255, 0, 0));
		
		panel.add(errorLabel);
		panel.add(lblUsername);
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(586, 334, 108, 23);
		btnConnect.addActionListener(new SettingsListener());
		btnConnect.requestFocus();
		panel.add(btnConnect);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.setBounds(507, 0, 89, 23);
		panel.add(btnDelete);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setEnabled(false);
		btnEdit.setBounds(417, 0, 89, 23);
		panel.add(btnEdit);
		//frame.getContentPane().setLayout(groupLayout);
		//frame.pack();
		//frame.setVisible(true);
	}
	public JPanel getPanel() {
		return panel;
	}
	public String getName() {
		return (userNameSet.getText().length() > 0) ? userNameSet.getText() : "Anon";
	}
	public String getIp() {
		String ip = inputIp.getText().trim();
		if (ip.length() > 0) {
			if (ip.indexOf(":") > -1) {
				System.out.println(ip.split(":"));
				return ip.split(":")[0];
			}
		}
		return "localhost";
	}
	public int getPort() {
		String ip = inputIp.getText().trim();
		if (ip.length() > 0) {
			if (ip.indexOf(":") > -1) {
				return Integer.parseInt(ip.split(":")[1]);
			}
		}
		return 25565;
	}
	public boolean isDone() {
		return doneSettings;
	}
	public void setDone(boolean done) {
		doneSettings = done;
	}
	private class SettingsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("settingsListener");
			if (getName().length() < 1) {
				errorLabel.setText("Set a username.");
			} else if (getName().contains(" ")) {
				errorLabel.setText("Username cannot contain spaces");
			} else {
				errorLabel.setText("");
				ChatClient.host = getIp();
				ChatClient.port = getPort();
				ChatClient.user = getName();
				doneSettings = true;
				ChatClient.needsReset = false;
				ChatClient.connected = true;
				//ChatClient.gotSettings = true;
				System.out.println("gotSettings");
			}
		}
	}
}
