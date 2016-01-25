package chatclient;

import java.io.*;
import java.net.*;

import common.*;

public class ChatClient implements Runnable {
	
	public static final String REQ = "‡";
	public static final String ver = "1.5";
	private static PrintWriter msgOut;
	private static BufferedReader msgIn;
	private static ChatClientGUI g;
	public static String host, user;
	public static int port;
	private static int numCloseSpans = 0;
	public static final int HEIGHT = 402,
							WIDTH = 700;
		
	public static void main(String[] args) {
		new File("cfg/").mkdirs();
		File serverFile = new File("cfg/servers.json");
		if (!serverFile.exists()) {
			try {
				serverFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new Thread(new ChatClient()).start();
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				//new Thread(new ChatClient()).start();
				g = new ChatClientGUI();
				new Thread(g).start();
				displaySettings();
			}
		});*/
	}
	public void run() {
		g = new ChatClientGUI();
		displaySettings();
	}
	private static void displaySettings() {
		g.displaySettings();
	}
	public static void displayMain() {
		if (!g.checkName()) {
			return;
		}
		g.displayMain();
		p.nl("Starting ChatClient");
		p.nl("Connecting to " + host + " on port " + port);
		g.pushToChat("Logging in...");
		try {
			Socket sSocket = new Socket(host, port);
				
			Thread msgThread = new Thread(() -> {
				try {
					msgOut = new PrintWriter(sSocket.getOutputStream(), true);
					msgIn = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
					String msgFrom;
						
					msgOut.println(REQ+"/login/"+user); //send the username as the client's first message to the server
						
					while ((msgFrom = msgIn.readLine()) != null) { //read response from server
						if (msgFrom.equalsIgnoreCase("kick"+Colors.WHITE)) {
							p.ne("you were kicked");
							sSocket.close();
							g.pushToChat("Type /retry to go back to the setup window.");
							break;
						} else {
							g.pushToChat(parseColor(msgFrom) + closeSpans());
						}							
					}
				} catch (Exception e) {
					e.printStackTrace();
					g.pushToChat("Lost connection to server: " + e.getMessage());
					g.pushToChat("Type /retry to go back to the setup window.");
				}
			});
			msgThread.start();
		} catch (Exception e) {
			e.printStackTrace();
			g.pushToChat("Error: " + e.getMessage());
			g.pushToChat("Type /retry to go back to the setup window.");
		}
	}
	
	public static void send(String msg) {
		String msgTo = msg;
		//msgTo = kb.readString(); //read user input -- have this so that it sends when user presses a button or something
		if (msgTo != null) {
			if (msgTo.equalsIgnoreCase("/retry")) {
				displaySettings();
			} else {
				msgOut.println(msgTo); //send input to server
				if (msgTo.equalsIgnoreCase("/dc") || msgTo.equalsIgnoreCase("/logout") || msgTo.equalsIgnoreCase("/logoff")) {
					displaySettings();
				}
			}
		}
	}
	
	private static String parseColor(String msg) { //this method uses bad/malformed HTML BUT IT WORKS
		if (msg.contains(Colors.CS)) {
			msg = msg.replaceAll(Colors.LIGHTGREEN, "<span style=\"color: #8afb17\">")
			 .replace(Colors.LIGHTBLUE, "<span style=\"color: #00ffff\">")
			 .replace(Colors.RED, "<span style=\"color: #e55451\">")
			 .replace(Colors.PINK, "<span style=\"color: #ff55ff\">")
			 .replace(Colors.YELLOW, "<span style=\"color: #fff380\">")
			 .replace(Colors.WHITE, "<span style=\"color: #ffffff\">")
			 .replace(Colors.DARKBLUE, "<span style=\"color: #0000a0\">")
			 .replace(Colors.GREEN, "<span style=\"color: #348017\">")
			 .replace(Colors.TEAL, "<span style=\"color: #008080\">")
			 .replace(Colors.DARKRED, "<span style=\"color: #9f000f\">")
			 .replace(Colors.PURPLE, "<span style=\"color: #6c2dc7\">")
			 .replace(Colors.GOLD, "<span style=\"color: #d4a017\">")
			 .replace(Colors.GRAY, "<span style=\"color: #837e7c\">")
			 .replace(Colors.DARKGRAY, "<span style=\"color: #555555\">")
			 .replace(Colors.BLUE, "<span style=\"color: #1f45fc\">")
			 .replace(Colors.BLACK, "<span style=\"color: #000000\">")
			 .replace(Colors.BOLD, "<span style=\"font-weight: bold\">") //bold
			 .replace(Colors.ITALIC, "<span style=\"font-style: italic\">") //italics
			 .replace(Colors.REGULAR, "<span style=\"font-weight: normal; font-style: normal\">"); //normal text
			numCloseSpans = msg.split(Colors.CS).length - 1;
		} else {
			//nothing
		}
		return msg;
	}
	private static String closeSpans() { //I THINK THIS FIXES IT MAYBE KIND OF NOT REALLY
		String ret = "";
		for (int i = 0; i < numCloseSpans; i++) {
			ret += "</span>";
		}
		return ret;
	}

}
