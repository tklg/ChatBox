package chatclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class ServerListItem {

	private String name;
	private String ip;
	private JPanel panel;
	private JLabel lblPing, lblMessage;
	private JButton btnConnect_1;
	private boolean connectable = false;
	public static final int HEIGHT = 49;
	
	public ServerListItem(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public String getIp() {
		return ip;
	}
	public String getHost() {
		return ip.split(":")[0];
	}
	public String getPort() {
		return ip.split(":")[1];
	}
	public boolean ping() {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(getHost(), Integer.parseInt(getPort())), 4000);
			socket.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public String pingMessage() {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(getHost(), Integer.parseInt(getPort())), 4000);
			PrintWriter msgOut = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader msgIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			msgOut.println(ChatClient.REQ + "/message");
			String msgFrom;
			while ((msgFrom = msgIn.readLine()) != null) { //read response from server
				socket.close();
				btnConnect_1.setEnabled(true);
				connectable = true;
				return msgFrom;
			}
			socket.close();
		} catch (Exception e) {
			
		}
		btnConnect_1.setEnabled(false);
		connectable = false;
		return null;
	}
	public JPanel panel() {
		return updatePanel();
	}
	public JPanel updatePanel() {
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(ChatClient.WIDTH, ServerListItem.HEIGHT);
		panel = new JPanel();
		
		panel.addMouseListener(new ServerSelectListener());
		
		panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		//panel.setPreferredSize(new Dimension(ChatClient.WIDTH, ServerListItem.HEIGHT));
		panel.setMaximumSize(new Dimension(Short.MAX_VALUE, ServerListItem.HEIGHT));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane()); //frame.getContentPane()
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, ServerListItem.HEIGHT, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(248, Short.MAX_VALUE))
		);
		panel.setLayout(null);
		
		JLabel lblServerName = new JLabel(getName());
		//lblServerName.setBounds(10, 11, 209, 14);
		panel.add(lblServerName);
		
		JLabel lblServerIp = new JLabel(getIp());
		//lblServerIp.setBounds(10, 24, 209, 14);
		lblServerIp.setForeground(Color.GRAY);
		panel.add(lblServerIp);
		
		lblPing = new JLabel("querying...");
		//pingThread.start();
		//lblPing.setBounds(505, 11, 88, 27);
		panel.add(lblPing);
		
		btnConnect_1 = new JButton("Connect");
		//btnConnect_1.setBounds(605, 11, 89, 27);
		btnConnect_1.addActionListener(new SettingsListener());
		btnConnect_1.setEnabled(false);
		panel.add(btnConnect_1);
		
		//lblMessage = new JLabel(getMessage());
		lblMessage = new JLabel();
		//lblMessage.setBounds(161, 11, 334, 27);
		panel.add(lblMessage);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblServerIp, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblServerName, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(151)
							.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)))
					.addGap(10)
					.addComponent(lblPing, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnConnect_1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addGap(9))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(8)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(13)
							.addComponent(lblServerIp))
						.addComponent(lblServerName)
						.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblPing, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnConnect_1, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))))
		);
		panel.setLayout(gl_panel);
		return panel;
	}
	public JLabel pingLabel() {
		return lblPing;
	}
	public JLabel messageLabel() {
		return lblMessage;
	}
	public String toString() {
		return name + " : " + ip;
	}
	private ServerListItem self() {
		return this;
	}
	public void setBackground(Color color) {
		panel.setBackground(color);
	}
	public void setBorder(boolean raised) {
		panel.setBorder(new SoftBevelBorder(raised ? BevelBorder.RAISED : BevelBorder.LOWERED, null, null, null, null));
	}
	private void login() {
		if (connectable) {
			ChatClient.host = getHost();
			ChatClient.port = Integer.parseInt(getPort());
			ChatClient.user = ChatClientSettingsGUI.getUserName();
			ChatClient.displayMain();
		}
	}
	private class SettingsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			login();
		}
	}
	private class ServerSelectListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				ChatClientSettingsGUI.selectPanel(self());
			} else if (e.getClickCount() == 2) {
				login();
			}
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
}
