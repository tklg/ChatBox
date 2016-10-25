package chatserver;

import common.Colors;

public class ChatServerFunctions {
	
	static String parseIn(String in) {
		String out;
		if (in.startsWith("/")) {
    		String[] cmd = in.substring(1, in.length()).trim().split(" ");
    		runCmd(cmd);
    		ChatServer.pushToChat("[Server]: " + in);
    	} else {
    		out = in.trim();
    		return out;
    	}
		return null;
	}
	
	static int numCloseSpans = 0;
	static String parseColor(String msg) { //this method uses bad/malformed HTML BUT IT WORKS I THINK
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
	static String closeSpans() { //I THINK THIS FIXES IT MAYBE
		String ret = "";
		for (int i = 0; i < numCloseSpans; i++) {
			ret += "</span>";
		}
		return ret;
	}
	public static void runCmd(String[] cmd) {
		String msg;
		int target;
		int lastUserPMd = -1;
		switch (cmd[0].toLowerCase().trim()) {
		case "me":
		case "emote":
			if (cmd.length <= 1) {
				ChatServer.pushToChat(Colors.DARKRED + "me syntax: /me &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			ChatServer.sendAll(Colors.WHITE + "> " + Colors.PINK + "[Server]" + Colors.WHITE + " " + msg);
			break;
		case "msg":
		case "message":
			if (cmd.length < 3) {
				ChatServer.pushToChat(Colors.DARKRED + "msg syntax: /msg &lt;user&gt; &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 2; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = ChatServer.getClientID(cmd[1]);
			lastUserPMd = target;
			if (target == -1) {
				ChatServer.pushToChat(Colors.DARKRED + "User '" + cmd[1] + "' is not online");
			} else {
				ChatServer.sendOne(target, Colors.PINK + "[Server] -> you: " + msg);
				ChatServer.pushToChat(Colors.PINK + "me -> " + cmd[1] + ": " + msg);
			}
			break;
		case "r":
		case "reply":
			if (cmd.length < 2) {
				ChatServer.pushToChat(Colors.DARKRED + "reply syntax: /r &lt;message&gt;");
				break;
			}
			if (lastUserPMd == -1) {
				ChatServer.pushToChat(Colors.DARKRED + "You have not messaged anyone yet");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = lastUserPMd;
			if (target == -1) {
				ChatServer.pushToChat(Colors.DARKRED + "User '" + ChatServer.getClientName(target) + "' is not online");;
			} else {
				ChatServer.sendOne(target, Colors.PINK + "[Server] -> you: " + msg);
				ChatServer.pushToChat(Colors.PINK + "me -> " + ChatServer.getClientName(target) + ": " + msg);
			}
			break;
		case "help":
		case "?":
			if (cmd.length != 1) {
				ChatServer.pushToChat(Colors.DARKRED + "help syntax: /help");
				break;
			}
			ChatServer.pushToChat(Colors.LIGHTGREEN + "Available commands: /me /broadcast /bc /emote /msg /message /motd /mute /r /reply /who /list /stop /kick /kickall /pex /help /?");
			break;
		case "who":
		case "list":
			if (cmd.length != 1) {
				ChatServer.pushToChat(Colors.DARKRED + "user list syntax: /list");
				break;
			}
			ChatServer.pushToChat(Colors.LIGHTGREEN + "Users online: " + ChatServer.getClientsOnline());
			break;
		case "stop":
			if (cmd.length != 1) {
				ChatServer.pushToChat(Colors.DARKRED + "server stop syntax: /stop");
				break;
			}
			ChatServer.sendAll(Colors.RED + "Server is shutting down...");
			System.exit(0);
			break;
		case "kick":
			if (cmd.length < 2) {
				ChatServer.pushToChat(Colors.DARKRED + "kick syntax: /kick &lt;user &lt;reason&gt;&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[1]) == -1) {
				ChatServer.pushToChat(Colors.DARKRED + "User '" + cmd[1] + "' is not online");
			} else {
				if (cmd.length == 2) {
					ChatServer.kick(ChatServer.getClientID(cmd[1]));
				} else if (cmd.length > 2) {
					msg = "";
					for (int i = 2; i < cmd.length; i++) {
						msg += cmd[i] + " ";
					}
					ChatServer.kick(ChatServer.getClientID(cmd[1]), msg);
				}
			}
			break;
		case "kickall":
			if (cmd.length != 1) {
				ChatServer.pushToChat(Colors.DARKRED + "kickall syntax: /kickall");
				break;
			}
			//ChatServer.kickAll();
			ChatServer.pushToChat(Colors.DARKRED + "kickall command is unimplemented");
			break;
		case "mute": //mod
			if (cmd.length < 2) {
				ChatServer.pushToChat(Colors.DARKRED + "mute syntax: /mute &lt;user&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[1]) == -1) {
				ChatServer.pushToChat(Colors.DARKRED + "User '" + cmd[1] + "' is not online");
				break;
			}
			ChatServer.mute(ChatServer.getClientID(cmd[1]));
			ChatServer.sendOne(ChatServer.getClientID(cmd[1]), Colors.RED + "You have been " + ((ChatServer.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			ChatServer.pushToChat(Colors.RED + "" + cmd[1] + " has been " + ((ChatServer.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			break;
		case "broadcast":
		case "bc":
			if (cmd.length < 2) {
				ChatServer.pushToChat(Colors.DARKRED + "broadcast syntax: /broadcast &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			ChatServer.sendAll(Colors.DARKRED + "[" + Colors.LIGHTGREEN + "Broadcast" + Colors.DARKRED + "]" + Colors.LIGHTGREEN + Colors.BOLD + " " + msg);
			break;
		case "motd":
			if (cmd.length == 1)
				ChatServer.pushToChat(ChatServer.getLoginMessage());
			else if (cmd.length > 1) {
				String motd = "";
				for (int i = 1; i < cmd.length; i++) {
					if (i != 1) motd += " ";
					motd += cmd[i];
				}
				ChatServer.setMotd(motd);
				ChatServer.pushToChat(Colors.LIGHTGREEN + "motd changed to: " + Colors.REGULAR + motd);
				ChatServer.writeConfig();
			} else
				ChatServer.pushToChat(Colors.DARKRED + "motd syntax: /motd or /motd &lt;new motd&gt;");
			break;
		case "loginmessage":
			if (cmd.length == 1)
				ChatServer.pushToChat(ChatServer.getLoginMessage());
			else if (cmd.length > 1) {
				String motd = "";
				for (int i = 1; i < cmd.length; i++) {
					if (i != 1) motd += " ";
					motd += cmd[i];
				}
				ChatServer.setLoginMessage(motd);
				ChatServer.pushToChat(Colors.LIGHTGREEN + "loginmessage changed to: " + Colors.REGULAR + motd);
				ChatServer.writeConfig();
			} else
				ChatServer.pushToChat(Colors.DARKRED + "loginmessage syntax: /loginmessage or /loginmessage &lt;new motd&gt;");
			break;
		case "pex":
		case "rank":
			if (cmd.length < 2 || cmd.length > 4) { // /pex promote user rank
				ChatServer.pushToChat(Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[2]) == -1) {
				ChatServer.pushToChat(Colors.DARKRED + "User '" + cmd[2] + "' is not online");
				break;
			}
			switch(cmd[1].toLowerCase().trim()) {
			case "set":
				if (cmd.length != 4) {
					ChatServer.pushToChat(Colors.DARKRED + "permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				} else {
					switch (cmd[3].toLowerCase().trim()) {
						case "mod":
						case "moderator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 2);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to moderator");
							ChatServer.pushToChat(Colors.PINK + "Changed " + cmd[2] + " to moderator");
							break;
						case "admin":
						case "administrator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 3);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to administrator");
							ChatServer.pushToChat(Colors.PINK + "Changed " + cmd[2] + " to administrator");
							break;
						case "user":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 1);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to user");
							ChatServer.pushToChat(Colors.PINK + "Changed " + cmd[2] + " to user");
							break;
						default:
							ChatServer.pushToChat(Colors.DARKRED + "permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
							break;
					}
				}
				break;
			case "promote":
				if (cmd.length != 3) {
					ChatServer.pushToChat(Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.promoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been promoted");
					ChatServer.pushToChat(Colors.PINK + "Promoted " + cmd[2]);
				}
				break;
			case "demote":
				if (cmd.length != 3) {
					ChatServer.pushToChat(Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.demoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been demoted");
					ChatServer.pushToChat(Colors.PINK + "Demoted " + cmd[2]);
				}				
				break;
			default: 
				ChatServer.pushToChat(Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			break;
		default:
			ChatServer.pushToChat(Colors.DARKRED + "Unknown command. Type /help for a list of commands.");
			break;
		}
	}

}
