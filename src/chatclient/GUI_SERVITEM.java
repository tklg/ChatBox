package chatclient;

import java.awt.Dimension;

import javax.swing.*;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class GUI_SERVITEM {

private static JFrame frame;
private static JPanel panel;
private static JLabel lblServerName;
private static JLabel lblServerIp;
private static JLabel lblPing;
private static JButton btnConnect_1;

public static void main(String[] args) {
	frame = new JFrame();
	frame.setResizable(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	panel = new JPanel();
	panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
	panel.setPreferredSize(new Dimension(ChatClient.WIDTH, ServerListItem.HEIGHT));
	GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
	groupLayout.setHorizontalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
		.addComponent(panel, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
	);
	groupLayout.setVerticalGroup(
		groupLayout.createParallelGroup(Alignment.LEADING)
			.addGroup(groupLayout.createSequentialGroup()
				.addComponent(panel, GroupLayout.PREFERRED_SIZE, ServerListItem.HEIGHT, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(248, Short.MAX_VALUE))
	);
	
	lblServerName = new JLabel("Server Name");
	lblServerName.setFont(new Font("Tahoma", Font.BOLD, 11));
	
	lblServerIp = new JLabel("Server IP");
	lblServerIp.setForeground(Color.GRAY);
	
	lblPing = new JLabel("Ping");
	
	btnConnect_1 = new JButton("Connect");
	
	JLabel lblMessage = new JLabel("Message");
	GroupLayout gl_panel = new GroupLayout(panel);
	gl_panel.setHorizontalGroup(
		gl_panel.createParallelGroup(Alignment.TRAILING)
			.addGroup(gl_panel.createSequentialGroup()
				.addGap(7)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
					.addComponent(lblServerIp, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
					.addComponent(lblServerName, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(151)
						.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 334, Short.MAX_VALUE)))
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
	frame.getContentPane().setLayout(groupLayout);
	frame.pack();
	frame.setVisible(true);
}
}
