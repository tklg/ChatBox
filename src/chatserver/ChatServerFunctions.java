package chatserver;

import common.p;

public class ChatServerFunctions {
	
	private static final String CS = "§";
	
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
		if (msg.contains(CS)) {
			msg = msg.replaceAll(CS+"a", "<span style=\"color: #8afb17\">")
			 .replace(CS+"b", "<span style=\"color: #00ffff\">")
			 .replace(CS+"c", "<span style=\"color: #e55451\">")
			 .replace(CS+"d", "<span style=\"color: #ff55ff\">")
			 .replace(CS+"e", "<span style=\"color: #fff380\">")
			 .replace(CS+"f", "<span style=\"color: #ffffff\">")
			 .replace(CS+"1", "<span style=\"color: #0000a0\">")
			 .replace(CS+"2", "<span style=\"color: #348017\">")
			 .replace(CS+"3", "<span style=\"color: #008080\">")
			 .replace(CS+"4", "<span style=\"color: #9f000f\">")
			 .replace(CS+"5", "<span style=\"color: #6c2dc7\">")
			 .replace(CS+"6", "<span style=\"color: #d4a017\">")
			 .replace(CS+"7", "<span style=\"color: #837e7c\">")
			 .replace(CS+"8", "<span style=\"color: #555555\">")
			 .replace(CS+"9", "<span style=\"color: #1f45fc\">")
			 .replace(CS+"0", "<span style=\"color: #000000\">")
			 .replace(CS+"l", "<span style=\"font-weight: bold\">") //bold
			 .replace(CS+"o", "<span style=\"font-style: italic\">") //italics
			 .replace(CS+"r", "<span style=\"font-weight: normal; font-style: normal\">"); //normal text
			numCloseSpans = msg.split(CS).length - 1;
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
				ChatServer.pushToChat(CS + "4me syntax: /me &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			ChatServer.sendAll(CS + "f> " + CS + "d[Server]" + CS + "f " + msg);
			break;
		case "msg":
		case "message":
			if (cmd.length < 3) {
				ChatServer.pushToChat(CS + "4msg syntax: /msg &lt;user&gt; &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 2; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = ChatServer.getClientID(cmd[1]);
			lastUserPMd = target;
			if (target == -1) {
				ChatServer.pushToChat(CS + "4User '" + cmd[1] + "' is not online");
			} else {
				ChatServer.sendOne(target, CS + "d[Server] -> you: " + msg);
				ChatServer.pushToChat(CS + "dme -> " + cmd[1] + ": " + msg);
			}
			break;
		case "r":
		case "reply":
			if (cmd.length < 2) {
				ChatServer.pushToChat(CS + "4reply syntax: /r &lt;message&gt;");
				break;
			}
			if (lastUserPMd == -1) {
				ChatServer.pushToChat(CS + "4You have not messaged anyone yet");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = lastUserPMd;
			if (target == -1) {
				ChatServer.pushToChat(CS + "4User '" + ChatServer.getClientName(target) + "' is not online");;
			} else {
				ChatServer.sendOne(target, CS + "d[Server] -> you: " + msg);
				ChatServer.pushToChat(CS + "dme -> " + ChatServer.getClientName(target) + ": " + msg);
			}
			break;
		case "help":
		case "?":
			if (cmd.length != 1) {
				ChatServer.pushToChat(CS + "4help syntax: /help");
				break;
			}
			ChatServer.pushToChat(CS + "aAvailable commands: /me /broadcast /bc /emote /msg /message /mute /r /reply /who /list /stop /kick /kickall /pex /help /?");
			break;
		case "who":
		case "list":
			if (cmd.length != 1) {
				ChatServer.pushToChat(CS + "4user list syntax: /list");
				break;
			}
			ChatServer.pushToChat(CS + "aUsers online: " + ChatServer.getClientsOnline());
			break;
		case "stop":
			if (cmd.length != 1) {
				ChatServer.pushToChat(CS + "4server stop syntax: /stop");
				break;
			}
			ChatServer.sendAll(CS + "cServer is shutting down...");
			System.exit(0);
			break;
		case "kick":
			if (cmd.length < 2) {
				ChatServer.pushToChat(CS + "4kick syntax: /kick &lt;user &lt;reason&gt;&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[1]) == -1) {
				ChatServer.pushToChat(CS + "4User '" + cmd[1] + "' is not online");
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
				ChatServer.pushToChat(CS + "4kickall syntax: /kickall");
				break;
			}
			//ChatServer.kickAll();
			ChatServer.pushToChat(CS + "4kickall command is unimplemented");
			break;
		case "mute": //mod
			if (cmd.length < 2) {
				ChatServer.pushToChat(CS + "4mute syntax: /mute &lt;user&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[1]) == -1) {
				ChatServer.pushToChat(CS + "4User '" + cmd[1] + "' is not online");
				break;
			}
			ChatServer.mute(ChatServer.getClientID(cmd[1]));
			ChatServer.sendOne(ChatServer.getClientID(cmd[1]), CS + "cYou have been " + ((ChatServer.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			ChatServer.pushToChat(CS + "c" + cmd[1] + " has been " + ((ChatServer.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			break;
		case "broadcast":
		case "bc":
			if (cmd.length < 2) {
				ChatServer.pushToChat(CS + "4broadcast syntax: /broadcast &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			ChatServer.sendAll(CS + "4[" + CS + "aBroadcast" + CS + "4]" + CS + "a" + CS + "l " + msg);
			break;
		case "pex":
		case "rank":
			if (cmd.length < 2 || cmd.length > 4) { // /pex promote user rank
				ChatServer.pushToChat(CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[2]) == -1) {
				ChatServer.pushToChat(CS + "4User '" + cmd[2] + "' is not online");
				break;
			}
			switch(cmd[1].toLowerCase().trim()) {
			case "set":
				if (cmd.length != 4) {
					ChatServer.pushToChat(CS + "4permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				} else {
					switch (cmd[3].toLowerCase().trim()) {
						case "mod":
						case "moderator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 2);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to moderator");
							ChatServer.pushToChat(CS + "dChanged " + cmd[2] + " to moderator");
							break;
						case "admin":
						case "administrator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 3);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to administrator");
							ChatServer.pushToChat(CS + "dChanged " + cmd[2] + " to administrator");
							break;
						case "user":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 1);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to user");
							ChatServer.pushToChat(CS + "dChanged " + cmd[2] + " to user");
							break;
						default:
							ChatServer.pushToChat(CS + "4permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
							break;
					}
				}
				break;
			case "promote":
				if (cmd.length != 3) {
					ChatServer.pushToChat(CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.promoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been promoted");
					ChatServer.pushToChat(CS + "dPromoted " + cmd[2]);
				}
				break;
			case "demote":
				if (cmd.length != 3) {
					ChatServer.pushToChat(CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.demoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been demoted");
					ChatServer.pushToChat(CS + "dDemoted " + cmd[2]);
				}				
				break;
			default: 
				ChatServer.pushToChat(CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			break;
		default:
			ChatServer.pushToChat(CS + "4Unknown command. Type /help for a list of commands.");
			break;
		}
	}

}
