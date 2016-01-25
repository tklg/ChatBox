package chatclient;

import java.awt.Dimension;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import common.p;

import java.awt.Color;
import java.awt.Component;

public class ChatClientSettingsGUI {
	
	private static final String ver = ChatClient.ver;
	private static JFrame frame;
	private static JPanel panel;
	private static ServerListItem activePanel;
	private static JLabel label_1;
	private static JLabel errorLabel;
	private static JLabel lblServerName;
	private static JTextField inputName;
	private static JTextField inputIp;
	private static JButton btnAddServer, btnDelete, btnEdit;
	private static JButton btnRefreshList;
	private static JTextField userNameSet;
	private static JScrollPane serverList;
	private static ServerList sl = new ServerList();
	private boolean editing = false;
	
	public ChatClientSettingsGUI() {
		frame = new JFrame("ChatClient v" + ver + " - Setup");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(ChatClient.WIDTH, ChatClient.HEIGHT);
		
		panel = new JPanel();
		Box horizontalBox_1 = Box.createHorizontalBox();
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
		
		serverList = sl.getServerPanels();
		//serverList.setBounds(0, 23, 704, 304);
		serverList.setViewportBorder(null);
		
		serverList.addMouseListener(new DeselectListener());
		
		lblServerName = new JLabel("Server Name");
		lblServerName.setHorizontalAlignment(SwingConstants.RIGHT);
		//lblServerName.setBounds(270, 341, 148, 14);
		
		label_1 = new JLabel("IP");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		//label_1.setBounds(270, 372, 148, 14);
		
		userNameSet = new JTextField();
		//userNameSet.setBounds(77, 356, 148, 20);
		userNameSet.setToolTipText("Server Name");
		userNameSet.setColumns(10);
		userNameSet.requestFocus();
		inputIp = new JTextField();
		//inputIp.setBounds(428, 369, 148, 20);
		inputIp.setToolTipText("Server IP");
		inputIp.setColumns(10);
		
		btnAddServer = new JButton("Add Server");
		//btnAddServer.setBounds(586, 352, 108, 23);
		btnAddServer.addActionListener(new AddServerListener());
		
		btnRefreshList = new JButton("Refresh");
		btnRefreshList.setPreferredSize(new Dimension(100, 20));
		//btnRefreshList.setBounds(596, 0, 108, 23);
		btnRefreshList.addActionListener(new RefreshListener());
		panel.setLayout(null);
		panel.add(lblServerName);
		panel.add(userNameSet);
		panel.add(inputIp);
		//panel.add(btnRefreshList);
		panel.add(label_1);
		panel.add(btnAddServer);
		panel.add(serverList);
		
		inputName = new JTextField();
		//inputName.setBounds(428, 338, 148, 20);
		panel.add(inputName);
		inputName.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		//lblUsername.setBounds(10, 359, 61, 14);
		
		errorLabel = new JLabel();
		//errorLabel.setBounds(10, 334, 327, 14);
		errorLabel.setForeground(new Color(255, 0, 0));
		
		panel.add(errorLabel);
		panel.add(lblUsername);
		
		btnDelete = new JButton("Delete");
		btnDelete.setPreferredSize(new Dimension(100, 20));
		btnDelete.setEnabled(false);
		//btnDelete.setBounds(506, 0, 89, 23);
		btnDelete.addActionListener(new RemoveServerListener());
		panel.add(btnDelete);
		
		btnEdit = new JButton("Edit");
		btnEdit.setEnabled(false);
		btnEdit.setPreferredSize(new Dimension(100, 20));
		//btnEdit.setBounds(416, 0, 89, 23);
		btnEdit.addActionListener(new EditServerListener());
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue);
		horizontalBox_1.add(btnEdit);
		horizontalBox_1.add(btnDelete);
		horizontalBox_1.add(btnRefreshList);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel.createSequentialGroup()
										.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
									.addComponent(userNameSet, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
								.addGap(35)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addComponent(lblServerName, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
									.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addComponent(inputIp, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
									.addComponent(inputName, 157, 157, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnAddServer, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
								.addGap(8))
							.addComponent(serverList, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
					.addComponent(horizontalBox_1, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
			);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addComponent(horizontalBox_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						//.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(serverList, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_panel.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblUsername)
										.addComponent(lblServerName)
										.addComponent(inputName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(userNameSet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
									.addGroup(gl_panel.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
											.addComponent(inputIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(label_1))
										.addContainerGap())))
							.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnAddServer)
								.addGap(22))))
			);
		panel.setLayout(gl_panel);
		//frame.getContentPane().setLayout(groupLayout);
		//frame.pack();
		//frame.setVisible(true);
	}
	public static ArrayList<ServerListItem> getServers() {
		return sl.getServers();
	}
	public JPanel getPanel() {
		return panel;
	}
	public String getName() {
		return userNameSet.getText().trim();
		//return (userNameSet.getText().trim().length() > 0) ? userNameSet.getText().trim() : "Anon";
	}
	public static String getUserName() {
		return (userNameSet.getText().trim().length() > 0) ? userNameSet.getText().trim() : "Anon";
	}
	private String getServerName() {
		return inputName.getText().trim();
	}
	public String getIp() {
		String ip = inputIp.getText().trim();
		if (ip.length() > 0) {
			if (ip.indexOf(":") > -1) {
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
	public void setErrorMessage(String msg) {
		errorLabel.setText(msg);
	}
	public void updateMessages() {
		ArrayList<ServerListItem> servers = getServers();
		for (ServerListItem s : servers) {
			Thread pingThread = new Thread(() -> {
				s.pingLabel().setText("querying...");
				s.pingLabel().setForeground(Color.BLACK);
				String message = s.pingMessage();
				boolean ping = (message != null);
				s.pingLabel().setText((ping ? "online" : "offline"));
				s.pingLabel().setForeground((ping ? new Color(0, 200, 0) : new Color(255, 0, 0)));
				s.messageLabel().setText(message);
			});
			pingThread.start();
		}
	}
	public static void selectPanel(ServerListItem item) {
		deselectPanels();
		activePanel = item;
		item.setBackground(new Color(220, 220, 220));
		item.setBorder(false);
		
		btnDelete.setEnabled(true);
		btnEdit.setEnabled(true);
	}
	public static void deselectPanels() {
		for (ServerListItem s : getServers()) {
			s.setBackground(new Color(240, 240, 240));
			s.setBorder(true);
		}
		btnDelete.setEnabled(false);
		btnEdit.setEnabled(false);
	}
	private class AddServerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (getIp().length() > 0 && getPort() > -1) {
				if (!editing) {
					sl.addServer(getServerName(), getIp() + ":" + getPort());
				} else {
					sl.editServer(activePanel, getServerName(), getIp() + ":" + getPort());
					editing = false;
				}
				//panel.remove(serverList);
				//serverList = sl.getServerPanels();
				serverList.setViewportView(sl.getViewportView());
				//panel.add(serverList);
				inputName.setText("");
				inputIp.setText("");
				deselectPanels();
				updateMessages();
				panel.revalidate();
				panel.repaint();
			} else {
				p.nl("needs ip");
			}
		}
	}
	private class RemoveServerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sl.removeServer(activePanel);
			serverList.setViewportView(sl.getViewportView());
			deselectPanels();
			updateMessages();
			panel.revalidate();
			panel.repaint();
		}
	}
	private class EditServerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			inputName.setText(activePanel.getName());
			inputIp.setText(activePanel.getIp());
			editing = true;
		}
	}
	private class DeselectListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			deselectPanels();
		}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	private class RefreshListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			updateMessages();
		}
	}
}
