package chatserver;

import java.io.*;
import java.net.*;

import common.*;

public class ChatServerThread extends Thread {
	
	//private static final String SPLITTER = "‡"; //double dagger character
	private static final String CS = "§"; //section sign character used for color codes
	
	private Socket socket = null;
	private ChatServer server = null;
	private int id = -1;
	private int alvl = 1;
	public String name = null;
	private PrintWriter msgOut;
	private BufferedReader msgIn;
	private boolean isMuted = false;
	
	public ChatServerThread(Socket socket, ChatServer server) {
		//super("ChatServerThread");
		this.socket = socket;
		this.server = server;
		this.id = server.getNextClientID();
		p.nl("Connection from: " + socket.toString());
	}
	public void run() {
		
		try {
			msgOut = new PrintWriter(socket.getOutputStream(), true);
			msgIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
				String input;
				String output;
				
				TextTransferProtocol ttp = new TextTransferProtocol(this); //set up ttp parsey thing
				
				if (name == null) {
					name = msgIn.readLine();
					if (ChatServer.users.contains(name)) {
		            	ChatServer.pushToChat(CS+"cUser " + name + " already exists, changing to " + name + "_" + ChatServer.users.size() + 1);
		            	name = name + "_" + ChatServer.users.size();
		            }
				}
				ChatServer.users.add(name);
				server.restoreClientRanks();
				ChatServer.updateUsers();
				
				ChatServer.sendAll(CS + "e" + name + " has joined the server");
				
				while ((input = msgIn.readLine()) != null) { //continue doing that until ends
					output = ttp.processIn(input);
					if (output != null) ChatServer.sendAll(CS + getRankColor() + name + CS + "f: " + output);
				}
				socket.close();
				
			} catch (Exception e) {
				//e.printStackTrace();
				leave();
				p.nl("Lost connection to user: " + name);
				ChatServer.pushToChat("[WARNING] Lost connection to user: " + name);
				//ChatServer.updateUsers();
				//server.removeClient(ChatServer.getClientID(name)); //figure out what to do if the client exits without doing /logout
			}
	}
	
	public void send(String msg) {
		msgOut.println(msg);
	}
	public void leave() {
		try {
			//int id = ChatServer.getClientID(name);
			ChatServer.sendAll(CS + "e" + name + " has left the server");
			ChatServer.sendOne(id, "kick");
			ChatServer.removeClient(id);
			ChatServer.updateUsers();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void mute() {
		if (isMuted()) {
			isMuted = false;
		} else {
			isMuted = true;
		}
	}
	public boolean startVote(String type, int starter, int target, String desc) {
		if (server.startVote(type, starter, target, desc)) {
			return true;
		}
		return false;
	}
	public boolean startVote(String type, int starter, String desc) {
		if (server.startVote(type, starter, desc)) {
			return true;
		}
		return false;
	}
	public void vote(int voter, int option) {
		server.vote(voter, option);
	}
	private int lastUserPMd = -1;
	@SuppressWarnings("static-access")
	public void runCmd(String[] cmd) {
		//String cmdName = cmd[0].toLowerCase().trim();
		String msg;
		int target;
		switch (cmd[0].toLowerCase().trim()) {
		case "votekick":
			server.sendOne(server.getClientID(name), CS + "4/vote and /votekick are unimplemented.");
			break;
			/*if (cmd.length != 2) {
				server.sendOne(server.getClientID(name), CS + "4votekick syntax: /votekick <user>");
				break;
			}
			target = server.getClientID(cmd[1]); //get id of user name
			String starter = name;
			if (target == -1) {
				server.sendOne(server.getClientID(name), CS + "4User '" + cmd[1] + "' is not online");;
			} else {
				startVote("kick", server.getClientID(starter), target, "kick");
				vote(server.getClientID(starter), 1); //voter also votes yes
			}
			break;*/
		case "vote":
			server.sendOne(server.getClientID(name), CS + "4/vote and /votekick are unimplemented.");
			break;
			/*if (cmd.length != 2) {
				server.sendOne(server.getClientID(name), CS + "4vote syntax: /vote <yes | no>");
				break;
			}
			int voter = server.getClientID(name);
			if (cmd[1].equalsIgnoreCase("yes")) {
				server.vote(voter, 1);
			} else if (cmd[1].equalsIgnoreCase("no")) {
				server.vote(voter, 0);
			} else if (cmd[1].equalsIgnoreCase("end")) {
				server.sendOne(server.getClientID(name), CS + "4Ending current vote and tallying results.");
			} else {
				server.sendOne(server.getClientID(name), CS + "4Please vote \"yes\" or \"no\"");
			}
			break;*/
		case "me":
		case "emote":
			if (cmd.length <= 1) {
				server.sendOne(server.getClientID(name), CS + "4me syntax: /me &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			server.sendAll(CS + "f> " + CS + "2" + name + CS + "f " + msg);
			break;
		case "msg":
		case "message":
			if (cmd.length < 3) {
				server.sendOne(server.getClientID(name), CS + "4msg syntax: /msg &lt;user&gt; &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 2; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = server.getClientID(cmd[1]);
			lastUserPMd = target;
			if (target == -1) {
				server.sendOne(server.getClientID(name), CS + "4User '" + cmd[1] + "' is not online");
			} else {
				//server.sendOne(server.getClientID(name), target, CS + "d" + msg);
				server.sendOne(target, CS + "d" + name + " -> you: " + msg);
				server.sendOne(server.getClientID(name), CS + "dme -> " + cmd[1] + ": " + msg);
				server.sendMods(CS + "d" + name + " -> " + cmd[1] + ": " + msg);
			}
			break;
		case "r":
		case "reply":
			if (cmd.length < 2) {
				server.sendOne(server.getClientID(name), CS + "4reply syntax: /r &lt;message&gt;");
				break;
			}
			if (lastUserPMd == -1) {
				server.sendOne(server.getClientID(name), CS + "4You have not messaged anyone yet");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = lastUserPMd;
			if (target == -1) {
				server.sendOne(server.getClientID(name), CS + "4User '" + server.getClientName(target) + "' is not online");;
			} else {
				//server.sendOne(server.getClientID(name), target, CS + "d" + msg);
				server.sendOne(target, CS + "d" + name + " -> you: " + msg);
				server.sendOne(server.getClientID(name), CS + "dme -> " + server.getClientName(target) + ": " + msg);
				server.sendMods(CS + "d" + name + " -> " + server.getClientName(target) + ": " + msg);
			}
			break;
		case "help":
		case "?":
			if (cmd.length != 1) {
				server.sendOne(server.getClientID(name), CS + "4help syntax: /help");
				break;
			}
			String cmds = "";
			if (getRank() > 0) cmds += "/me /logout /msg /r /who /help /ping ";
			if (getRank() > 1) cmds += "/kick /broadcast /mute ";
			if (getRank() > 2) cmds += "/stop /kickall /pex ";
			server.sendOne(server.getClientID(name), CS + "aAvailable commands: " + cmds);
			break;
		case "who":
		case "list":
			if (cmd.length != 1) {
				server.sendOne(server.getClientID(name), CS + "4user list syntax: /list");
				break;
			}
			server.sendOne(server.getClientID(name), CS + "aUsers online: " + server.getClientsOnline());
			break;
		case "logout":
		case "logoff":
		case "dc":
			leave();
			break;
		case "ping":
		case "pong":
			if (cmd.length != 1) {
				server.sendOne(server.getClientID(name), CS + "4user list syntax: /list");
				break;
			}
			server.sendOne(server.getClientID(name), "Pong!");
			break;
		case "stop": //admin
			if (getRank() < 3) {
				server.sendOne(server.getClientID(name), CS + "cYou do not have permission to use this command");
				break;
			}
			if (cmd.length != 1) {
				server.sendOne(server.getClientID(name), CS + "4server stop syntax: /stop");
				break;
			}
			server.sendOne(server.getClientID(name), CS + "cServer is shutting down...");
			System.exit(0);
			break;
		case "kick": //mod
			if (getRank() < 2) {
				server.sendOne(server.getClientID(name), CS + "cYou do not have permission to use this command");
				break;
			}
			if (cmd.length < 2) {
				server.sendOne(server.getClientID(name), CS + "4kick syntax: /kick &lt;user &lt;reason&gt;&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[1]) == -1) {
				server.sendOne(server.getClientID(name), CS + "4User '" + cmd[1] + "' is not online");;
			} else {
				if (cmd.length == 2) {
					ChatServer.kick(ChatServer.getClientID(name), ChatServer.getClientID(cmd[1]));
				} else if (cmd.length > 2) {
					msg = "";
					for (int i = 2; i < cmd.length; i++) {
						msg += cmd[i] + " ";
					}
					ChatServer.kick(ChatServer.getClientID(cmd[1]), msg);
				}
			}
			break;
		case "kickall": //admin
			if (getRank() < 3) {
				server.sendOne(server.getClientID(name), CS + "cYou do not have permission to use this command");
				break;
			}
			if (cmd.length != 1) {
				server.sendOne(server.getClientID(name), CS + "4kickall syntax: /kickall");
				break;
			}
			//ChatServer.kickAll();
			ChatServer.pushToChat(CS + "4kickall command is unimplemented");
			break;
		case "mute": //mod
			if (getRank() < 2) {
				server.sendOne(server.getClientID(name), CS + "cYou do not have permission to use this command");
				break;
			}
			if (cmd.length < 2) {
				server.sendOne(server.getClientID(name), CS + "4mute syntax: /mute &lt;user&gt;");
				break;
			}
			if (server.getClientID(cmd[1]) == -1) {
				server.sendOne(server.getClientID(name), CS + "4User '" + cmd[1] + "' is not online");
			}
			server.mute(server.getClientID(cmd[1]));
			server.sendOne(server.getClientID(cmd[1]), CS + "cYou have been " + ((server.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			server.sendOne(server.getClientID(name), CS + "c" + cmd[1] + " has been " + ((server.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			break;
		case "broadcast": //mod
		case "bc":
			if (getRank() < 2) {
				server.sendOne(server.getClientID(name), CS + "cYou do not have permission to use this command");
				break;
			}
			if (cmd.length < 2) {
				server.sendOne(server.getClientID(name), CS + "4broadcast syntax: /broadcast &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			ChatServer.sendAll(CS + "4[" + CS + "aBroadcast" + CS + "4]" + CS + "a" + CS + "l " + msg);
			break;
		case "pex": //admin
		case "rank":
			if (getRank() < 3) {
				server.sendOne(server.getClientID(name), CS + "cYou do not have permission to use this command");
				break;
			}
			if (cmd.length < 2 || cmd.length > 4) { // /pex promote user rank
				server.sendOne(server.getClientID(name), CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			if (cmd[2].equals(name)) {
				server.sendOne(server.getClientID(name), CS + "4You cannot promote/demote yourself");
				break;
			}
			if (server.getClientID(cmd[2]) == -1) {
				server.sendOne(server.getClientID(name), CS + "4User '" + cmd[2] + "' is not online");
			}
			switch(cmd[1].toLowerCase().trim()) {
			case "set":
				if (cmd.length != 4) {
					server.sendOne(server.getClientID(name), CS + "4permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				} else {
					switch (cmd[3].toLowerCase().trim()) {
						case "mod":
						case "moderator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 2);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to moderator");
							server.sendOne(server.getClientID(name), CS + "dChanged " + cmd[2] + " to moderator");
							break;
						case "admin":
						case "administrator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 3);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to administrator");
							server.sendOne(server.getClientID(name), CS + "dChanged " + cmd[2] + " to administrator");
							break;
						case "user":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 1);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to user");
							server.sendOne(server.getClientID(name), CS + "dChanged " + cmd[2] + " to user");
							break;
						default:
							server.sendOne(server.getClientID(name), CS + "4permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
							break;
					}
				}
				break;
			case "promote":
				if (cmd.length != 3) {
					server.sendOne(server.getClientID(name), CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.promoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been promoted");
					server.sendOne(server.getClientID(name), CS + "dPromoted " + cmd[2]);
				}
				break;
			case "demote":
				if (cmd.length != 3) {
					server.sendOne(server.getClientID(name), CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.demoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been demoted");
					server.sendOne(server.getClientID(name), CS + "dDemoted " + cmd[2]);
				}				
				break;
			default: 
				server.sendOne(server.getClientID(name), CS + "4permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			break;
		default:
			server.sendOne(server.getClientID(name), CS + "4Unknown command. Type /help for a list of commands.");
			break;
		}
	}
	public int getRank() {
		return alvl;
	}
	public void setRank(int alvl) {
		this.alvl = alvl;
	}
	public boolean isMuted() {
		return isMuted;
	}
	public int getID() {
		return id;
	}
	public String getRankColor() {
		int rank = getRank();
		switch(rank) {
		case 1: return "2";
		case 2: return "b";
		case 3: return "c";
		default: return "f";
		}
	}
}

