package chatclient;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

public class ChatClientGUI {

	private final String ver = ChatClient.ver;
	
	private JFrame frame;
	private JPanel panel;
	private JTextField input;
	private JEditorPane outputText;
	private JScrollPane output;
	private JButton btnSend;
	private String username, ip, port;
	
	private ChatClientSettingsGUI settingsGUI = new ChatClientSettingsGUI();
	private JPanel settingsPanel = settingsGUI.getPanel();
	//private ChatClient client;

	public ChatClientGUI() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setSize(ChatClient.WIDTH, ChatClient.HEIGHT);
		frame.setVisible(true);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(ChatClient.WIDTH, ChatClient.HEIGHT));
		
		input = new JTextField(30);
		//input.setBounds(10, 373, 591, 23);
		input.setFont(new Font("Arial", Font.PLAIN, 14));
		input.addActionListener(new SendListener());
		
		output = new JScrollPane();
		//output.setBounds(0, 0, 694, 367);
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
		output.setViewportView(outputText);
		output.setFocusable(false);
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
				
		btnSend = new JButton("Send");
		//btnSend.setBounds(611, 373, 73, 24);
		btnSend.addActionListener(new SendListener());
		panel.add(input);
		panel.add(btnSend);
		panel.add(output);
		//panel.setLayout(null);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(input, GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSend)
					.addContainerGap())
				.addComponent(output, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(output, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(input, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSend))
					.addGap(6))
		);
		panel.setLayout(gl_panel);
		panel.validate();
		panel.repaint();
	}
	public void displaySettings() {
		frame.setTitle("ChatClient v" + ver + " - Setup");
		/*Container pane = frame.getContentPane();
		pane.remove(panel);
		pane.add(settingsPanel);
		pane.revalidate();
		pane.repaint();*/
		//settingsPanel = settingsGUI.getPanel();
		frame.setContentPane(settingsPanel);
		//frame.getContentPane().revalidate();
		//frame.getContentPane().repaint();
		frame.pack();
		//frame.setVisible(true);
		settingsGUI.updateMessages();
	}
	public boolean checkName() {
		if (settingsGUI.getName().length() < 1) {
			settingsGUI.setErrorMessage("Username cannot be blank");
			return false;
		}
		return true;
	}
	public void displayMain() {
		settingsGUI.setErrorMessage("");
		//frame.pack();
		outputText.setText("");
		username = ChatClient.user;
		ip = ChatClient.host;
		port = Integer.toString(ChatClient.port);
		frame.setTitle("ChatClient v" + ver + " - " + username + "@" + ip + ":" + port);
		/*Container pane = frame.getContentPane();
		pane.remove(settingsPanel);
		pane.add(panel);
		pane.revalidate();
		pane.repaint();*/
		frame.setContentPane(panel);
		//frame.getContentPane().revalidate();
		//frame.getContentPane().repaint();
		frame.pack();
		//frame.setVisible(true);
	}
	public void pushToChat(String msg) {
		append(msg + "\n");
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
		      outputText.setCaretPosition(outputText.getDocument().getLength()); //3rd attempt to make it always always scroll
		   } catch (BadLocationException e) {
		      e.printStackTrace();
		   }
		}
	private class SendListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!input.getText().equals("")) {
				ChatClient.send(input.getText()); //when send button pressed, send message in text box
				input.setText(""); //clear box
			}
		}
	}

}
