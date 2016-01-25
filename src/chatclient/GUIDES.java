package chatclient;

import java.awt.Dimension;

import javax.swing.*;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

import java.awt.Color;
import java.awt.Font;

public class GUIDES {

private static JFrame frame;
private static JPanel panel;

private static JEditorPane outputText;
private static JTextField input;
private static JScrollPane output;

private static JButton btnSend;

public static void main(String[] args) {
	frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(true);
	frame.setSize(700, 432);
	
	panel = new JPanel();
	panel.setPreferredSize(new Dimension(700, 500));
	
	input = new JTextField(30);
	input.setFont(new Font("Arial", Font.PLAIN, 14));
	
	output = new JScrollPane();
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
	outputText.setFont(new Font("Arial", Font.PLAIN, 14));
	output.setViewportView(outputText);
	output.setFocusable(false);
	DefaultCaret caret = (DefaultCaret) outputText.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
	btnSend = new JButton("Send");
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
	frame.getContentPane().setLayout(groupLayout);

	frame.pack();
	frame.setVisible(true);
}
}
