package chatclient;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
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
	private boolean doneSettings = false;
	
	private ChatClientSettingsGUI settingsGUI = new ChatClientSettingsGUI();
	private JPanel settingsPanel = settingsGUI.getPanel();
	private ChatClient client;

	public ChatClientGUI(ChatClient client) {
		this.client = client;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(ChatClient.WIDTH, ChatClient.HEIGHT);
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(ChatClient.WIDTH, ChatClient.HEIGHT));
		
		input = new JTextField(30);
		input.setBounds(10, 373, 591, 23);
		input.setFont(new Font("Arial", Font.PLAIN, 14));
		input.addActionListener(new SendListener());
		
		output = new JScrollPane();
		output.setBounds(0, 0, 694, 367);
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
		//outputText.setFont(new Font("Arial", Font.PLAIN, 14));
		output.setViewportView(outputText);
		output.setFocusable(false);
		output.setViewportView(outputText);
		outputText.setEditable(false);
		outputText.setContentType("text/html");
		outputText.setBackground(new Color(20, 20, 20));
		output.setAutoscrolls(true);
		DefaultCaret caret = (DefaultCaret) outputText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		//outputText.setForeground(Color.WHITE);
		HTMLEditorKit kit = new HTMLEditorKit();
		outputText.setEditorKit(kit);
		Font font = new Font("Segoe UI", Font.PLAIN, 14);
	    String stylesheet = "body { font-family: " + font.getFamily() + "; " +
	            "font-size: " + font.getSize() + "pt; color: white;}";
		((HTMLDocument) outputText.getDocument()).getStyleSheet().addRule(stylesheet);
		//outputText.setLineWrap(true);
		//outputText.setWrapStyleWord(true);
				
		btnSend = new JButton("Send");
		btnSend.setBounds(611, 373, 73, 24);
		//btnSend.addActionListener(new SendListener());

		panel.setLayout(null);
		panel.add(input);
		panel.add(btnSend);
		panel.add(output);
		//frame.getContentPane().setLayout(groupLayout);
		
	}
	
	public void displaySettings() {
		/*framePre.pack();
		frame.setVisible(false);
		framePre.setVisible(true);*/
		frame.setTitle("ChatClient v" + ver + " - Setup");
		frame.setContentPane(settingsPanel);
		frame.pack();
		frame.setVisible(true);
	}
	public void displayMain() {
		//frame.pack();
		username = ChatClient.user;
		ip = ChatClient.host;
		port = Integer.toString(ChatClient.port);
		frame.setTitle("ChatClient v" + ver + " - " + username + "@" + ip + ":" + port);
		/*framePre.setVisible(false);
		frame.setVisible(true);*/
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		doneSettings = false;
	}
	public boolean settingsAreDone() {
		return settingsGUI.isDone();
	}
	public void setDone(boolean done) {
		settingsGUI.setDone(done);
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
				client.send(input.getText()); //when send button pressed, send message in text box
				input.setText(""); //clear box
			}
		}
	}

}
