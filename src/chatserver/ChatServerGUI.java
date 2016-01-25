package chatserver;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import common.Colors;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

public class ChatServerGUI {
	
	private static final String ver = ChatServer.ver;
	
	private static JFrame frame;
	private static JPanel panel;
	private static JTextField userNameSet,
					   ipSet,
					   portSet,
					   input;
	private static JEditorPane outputText;
	private static JScrollPane output;
	private static JScrollPane users;
	private static JButton btnSend;
	private static JEditorPane usersText;
	public ChatServerGUI(ChatServer server) {
		
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setSize(ChatServer.WIDTH, ChatServer.HEIGHT);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(ChatServer.WIDTH, ChatServer.HEIGHT));
		
		input = new JTextField(30);
		input.setFont(new Font("Arial", Font.PLAIN, 14));
		input.addActionListener(new SendListener());
		
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
		
		//outputText = new JTextArea();
		outputText = new JEditorPane();
		//outputText.setFont(new Font("Arial", Font.PLAIN, 14));
		output.setViewportView(outputText);
		outputText.setEditable(false);
		outputText.setContentType("text/html");
		outputText.setBackground(new Color(20, 20, 20));
		output.setAutoscrolls(true);
		DefaultCaret caret = (DefaultCaret) outputText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		HTMLEditorKit kit = new HTMLEditorKit();
		outputText.setEditorKit(kit);
		Font font = new Font("Segoe UI", Font.PLAIN, 14);
	    String stylesheet = "body { font-family: " + font.getFamily() + "; " +
	            "font-size: " + font.getSize() + "pt; color: white;}";
		((HTMLDocument) outputText.getDocument()).getStyleSheet().addRule(stylesheet);
		//outputText.setLineWrap(true);
		//outputText.setWrapStyleWord(true);
				
		btnSend = new JButton("Send");
		btnSend.addActionListener(new SendListener());
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
		
		usersText = new JEditorPane();
		HTMLEditorKit kit2 = new HTMLEditorKit();
		usersText.setEditorKit(kit2);
		usersText.setEditable(false);
		users.setViewportView(usersText);
		usersText.setFont(new Font("Arial", Font.PLAIN, 13));
		usersText.setBackground(new Color(20, 20, 20));
		((HTMLDocument) usersText.getDocument()).getStyleSheet().addRule(stylesheet);
		users.setViewportView(usersText);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
		
	}
	public void displayMain() {
		frame.pack();
		frame.setTitle("ChatServer v" + ver + " running on port " + ChatServer.port);
		frame.setVisible(true);
	}
	public String getName() {
		return (userNameSet.getText().length() > 0) ? userNameSet.getText() : "";
	}
	public String getIp() {
		return (ipSet.getText().length() > 0) ? ipSet.getText() : "";
	}
	public int getPort() {
		return (portSet.getText().length() > 0) ? Integer.parseInt(portSet.getText()) : 0;
	}
	public void pushToChat(String msg) {
		append(ChatServerFunctions.parseColor(msg) + "\n");
	}
	public void append(String s) {
		   try {
		      HTMLDocument doc = (HTMLDocument) outputText.getDocument();
		      //doc.insertString(doc.getLength(), s, null);
		      HTMLEditorKit kit = (HTMLEditorKit) outputText.getEditorKit();
		      
		      StringReader reader = new StringReader(s);
		      try {
				kit.read(reader, doc, doc.getLength());
			} catch (IOException e) {
				e.printStackTrace();
			}
		      outputText.setCaretPosition(outputText.getDocument().getLength()); //3rd attempt
		   } catch (BadLocationException e) {
		      e.printStackTrace();
		   }
		}
	public void setUsersList(String s) {
		try {
		      HTMLDocument doc = (HTMLDocument) usersText.getDocument();
		      //doc.insertString(doc.getLength(), s, null);
		      HTMLEditorKit kit = (HTMLEditorKit) usersText.getEditorKit();
		      
		      usersText.setText("");
		      
		      StringReader reader = new StringReader(s);
		      try {
		    	  kit.read(reader, doc, doc.getLength());
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		   } catch (BadLocationException e) {
		      e.printStackTrace();
		   }
	}
	private class SendListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!input.getText().equals("")) {
				String msg = ChatServerFunctions.parseIn(input.getText());
				if (msg != null) ChatServer.sendAll(Colors.PINK + "[Server]" + Colors.WHITE + ": " + msg); //when send button pressed, send message in text box
				input.setText(null); //clear box
			}
		}
	}

}
