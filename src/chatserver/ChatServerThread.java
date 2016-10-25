package chatserver;

import java.io.*;
import java.net.*;

import common.*;

public class ChatServerThread extends Thread {
	
	private Socket socket = null;
	//private ChatServer server = null;
	private int clientID = -1;
	private int alvl = 1;
	public String name = null;
	private PrintWriter msgOut;
	private BufferedReader msgIn;
	private boolean isMuted = false;
	private boolean isPing = false;
	
	public ChatServerThread(Socket socket, ChatServer ChatServer, int id) {
		//super("ChatServerThread");
		this.socket = socket;
		//this.server = ChatServer;
		this.clientID = id;
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
				String first = msgIn.readLine();
				
				if (first.startsWith(ChatServer.REQ)) {
					if (first.indexOf("/message") > -1) {
						isPing = true;
						msgOut.println(ChatServer.getMotd());
						//ChatServer.pushToChat("ping");
					} else if (first.indexOf("/login") > -1){
						//ChatServer.pushToChat("login");
						if (name == null) {
							name = first.substring(8, first.length());
							//ChatServer.pushToChat("setting name to " + name);
							//msgOut.println("setting name to " + name);
							if (ChatServer.users.contains(name)) {
				            	ChatServer.pushToChat(Colors.RED+"User " + name + " already exists, changing to " + name + "_" + ChatServer.users.size() + 1);
				            	name = name + "_" + ChatServer.users.size();
				            }
							ChatServer.addUser(name);
							ChatServer.restoreClientRank(name);
							ChatServer.updateUsers();
						}
						
						ChatServer.sendAll(Colors.YELLOW + name + " has joined the server");
						ChatServer.sendOne(clientID, ChatServer.getLoginMessage());
						while ((input = msgIn.readLine()) != null) { //continue doing that until ends]
							//ChatServer.pushToChat(input);
							output = ttp.processIn(input);
							if (output != null) ChatServer.sendAll(getRankColor() + name + Colors.WHITE + ": " + output);
						}
					}
				} else {
					msgOut.println("400: PLEASE START ANY CONNECTION WITH " + ChatServer.REQ+"/fn");
				}
				socket.close();
				//ChatServer.pushToChat("closed socket");
				
			} catch (Exception e) {
				e.printStackTrace(msgOut);
				if (!isPing) {
					leave();
					p.nl("Lost connection to user: " + name);
					ChatServer.pushToChat(Colors.GOLD+"[WARNING]"+Colors.WHITE+" Lost connection to user: " + name);
				} else {
					p.nl("got pinged by: " + socket);
					ChatServer.pushToChat(Colors.YELLOW+"[INFO]"+Colors.WHITE+" Pinged by: " + socket);
				}
				//ChatServer.updateUsers();
				//ChatServer.removeClient(ChatServer.getClientID(name)); //figure out what to do if the client exits without doing /logout
			}
	}
	public void send(String msg) {
		msgOut.println(msg);
	}
	public void leave() {
		try {
			//int id = ChatServer.getClientID(name);
			//ChatServer.sendAll(CS + "e" + name + " has left the ChatServer");
			ChatServer.sendOne(clientID, "kick");
			ChatServer.removeClient(clientID);
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
		if (ChatServer.startVote(type, starter, target, desc)) {
			return true;
		}
		return false;
	}
	public boolean startVote(String type, int starter, String desc) {
		if (ChatServer.startVote(type, starter, desc)) {
			return true;
		}
		return false;
	}
	public void vote(int voter, int option) {
		ChatServer.vote(voter, option);
	}
	private int lastUserPMd = -1;
	//@SuppressWarnings("static-access")
	public void runCmd(String[] cmd) {
		//String cmdName = cmd[0].toLowerCase().trim();
		String msg;
		int target;
		switch (cmd[0].toLowerCase().trim()) {
		case "votekick":
			ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "/vote and /votekick are unimplemented.");
			break;
			/*if (cmd.length != 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "votekick syntax: /votekick <user>");
				break;
			}
			target = ChatServer.getClientID(cmd[1]); //get id of user name
			String starter = name;
			if (target == -1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "User '" + cmd[1] + "' is not online");;
			} else {
				startVote("kick", ChatServer.getClientID(starter), target, "kick");
				vote(ChatServer.getClientID(starter), 1); //voter also votes yes
			}
			break;*/
		case "vote":
			ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "/vote and /votekick are unimplemented.");
			break;
			/*if (cmd.length != 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "vote syntax: /vote <yes | no>");
				break;
			}
			int voter = ChatServer.getClientID(name);
			if (cmd[1].equalsIgnoreCase("yes")) {
				ChatServer.vote(voter, 1);
			} else if (cmd[1].equalsIgnoreCase("no")) {
				ChatServer.vote(voter, 0);
			} else if (cmd[1].equalsIgnoreCase("end")) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "Ending current vote and tallying results.");
			} else {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "Please vote \"yes\" or \"no\"");
			}
			break;*/
		case "me":
		case "emote":
			if (cmd.length <= 1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "me syntax: /me &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			ChatServer.sendAll(Colors.WHITE + "> " + Colors.GREEN + name + Colors.WHITE + " " + msg);
			break;
		case "msg":
		case "message":
			if (cmd.length < 3) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "msg syntax: /msg &lt;user&gt; &lt;message&gt;");
				break;
			}
			msg = "";
			for (int i = 2; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = ChatServer.getClientID(cmd[1]);
			lastUserPMd = target;
			if (target == -1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "User '" + cmd[1] + "' is not online");
			} else {
				//ChatServer.sendOne(ChatServer.getClientID(name), target, Colors.PINK + "" + msg);
				ChatServer.sendOne(target, Colors.PINK + "" + name + " -> you: " + msg);
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.PINK + "me -> " + cmd[1] + ": " + msg);
				ChatServer.sendMods(Colors.PINK + "" + name + " -> " + cmd[1] + ": " + msg);
			}
			break;
		case "r":
		case "reply":
			if (cmd.length < 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "reply syntax: /r &lt;message&gt;");
				break;
			}
			if (lastUserPMd == -1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "You have not messaged anyone yet");
				break;
			}
			msg = "";
			for (int i = 1; i < cmd.length; i++) {
				msg += cmd[i] + " ";
			}
			target = lastUserPMd;
			if (target == -1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "User '" + ChatServer.getClientName(target) + "' is not online");;
			} else {
				//ChatServer.sendOne(ChatServer.getClientID(name), target, Colors.PINK + "" + msg);
				ChatServer.sendOne(target, Colors.PINK + "" + name + " -> you: " + msg);
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.PINK + "me -> " + ChatServer.getClientName(target) + ": " + msg);
				ChatServer.sendMods(Colors.PINK + "" + name + " -> " + ChatServer.getClientName(target) + ": " + msg);
			}
			break;
		case "help":
		case "?":
			if (cmd.length != 1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "help syntax: /help");
				break;
			}
			String cmds = "";
			if (getRank() > 0) cmds += "/me /logout /msg /r /who /help /ping /motd";
			if (getRank() > 1) cmds += "/kick /broadcast /mute ";
			if (getRank() > 2) cmds += "/stop /kickall /pex ";
			ChatServer.sendOne(ChatServer.getClientID(name), Colors.LIGHTGREEN + "Available commands: " + cmds);
			break;
		case "who":
		case "list":
			if (cmd.length != 1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "user list syntax: /list");
				break;
			}
			ChatServer.sendOne(ChatServer.getClientID(name), Colors.LIGHTGREEN + "Users online: " + ChatServer.getClientsOnline());
			break;
		case "logout":
		case "logoff":
		case "dc":
			leave();
			break;
		case "ping":
		case "pong":
			if (cmd.length == 1) {
				ChatServer.sendOne(ChatServer.getClientID(name), "Pong!");
			} else {
				String str = "";
				for (int i = 1; i < cmd.length; i++) {
					str += cmd[i] + " ";
				}
				ChatServer.sendOne(ChatServer.getClientID(name), str);
			}
			break;
		case "stop": //admin
			if (getRank() < 3) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to use this command");
				break;
			}
			if (cmd.length != 1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "ChatServer stop syntax: /stop");
				break;
			}
			ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "Server is shutting down...");
			System.exit(0);
			break;
		case "kick": //mod
			if (getRank() < 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to use this command");
				break;
			}
			if (cmd.length < 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "kick syntax: /kick &lt;user &lt;reason&gt;&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[1]) == -1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "User '" + cmd[1] + "' is not online");;
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
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to use this command");
				break;
			}
			if (cmd.length != 1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "kickall syntax: /kickall");
				break;
			}
			//ChatServer.kickAll();
			ChatServer.pushToChat(Colors.DARKRED + "kickall command is unimplemented");
			break;
		case "mute": //mod
			if (getRank() < 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to use this command");
				break;
			}
			if (cmd.length < 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "mute syntax: /mute &lt;user&gt;");
				break;
			}
			if (ChatServer.getClientID(cmd[1]) == -1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "User '" + cmd[1] + "' is not online");
			}
			ChatServer.mute(ChatServer.getClientID(cmd[1]));
			ChatServer.sendOne(ChatServer.getClientID(cmd[1]), Colors.RED + "You have been " + ((ChatServer.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "" + cmd[1] + " has been " + ((ChatServer.getClient(cmd[1]).isMuted()) ? "muted" : "unmuted"));
			break;
		case "broadcast": //mod
		case "bc":
			if (getRank() < 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to use this command");
				break;
			}
			if (cmd.length < 2) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "broadcast syntax: /broadcast &lt;message&gt;");
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
				ChatServer.sendOne(ChatServer.getClientID(name), ChatServer.getLoginMessage());
			else if (cmd.length > 1) {
				if (getRank() < 2) {
					ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to change this");
					break;
				}
				String motd = "";
				for (int i = 1; i < cmd.length; i++) {
					if (i != 1) motd += " ";
					motd += cmd[i];
				}
				ChatServer.setMotd(motd);
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.LIGHTGREEN + "motd changed to: " + Colors.REGULAR + motd);
				ChatServer.writeConfig();
			} else
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "motd syntax: /motd or /motd &lt;new motd&gt;");
			break;
		case "loginmessage":
			if (cmd.length == 1)
				ChatServer.sendOne(ChatServer.getClientID(name), ChatServer.getLoginMessage());
			else if (cmd.length > 1) {
				if (getRank() < 2) {
					ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to change this");
					break;
				}
				String motd = "";
				for (int i = 1; i < cmd.length; i++) {
					if (i != 1) motd += " ";
					motd += cmd[i];
				}
				ChatServer.setLoginMessage(motd);
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.LIGHTGREEN + "loginmessage changed to: " + Colors.REGULAR + motd);
				ChatServer.writeConfig();
			} else
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "loginmessage syntax: /loginmessage or /loginmessage &lt;new motd&gt;");
			break;
		case "pex": //admin
		case "rank":
			if (getRank() < 3) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.RED + "You do not have permission to use this command");
				break;
			}
			if (cmd.length < 2 || cmd.length > 4) { // /pex promote user rank
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			if (cmd[2].equals(name)) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "You cannot promote/demote yourself");
				break;
			}
			if (ChatServer.getClientID(cmd[2]) == -1) {
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "User '" + cmd[2] + "' is not online");
			}
			switch(cmd[1].toLowerCase().trim()) {
			case "set":
				if (cmd.length != 4) {
					ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				} else {
					switch (cmd[3].toLowerCase().trim()) {
						case "mod":
						case "moderator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 2);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to moderator");
							ChatServer.sendOne(ChatServer.getClientID(name), Colors.PINK + "Changed " + cmd[2] + " to moderator");
							break;
						case "admin":
						case "administrator":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 3);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to administrator");
							ChatServer.sendOne(ChatServer.getClientID(name), Colors.PINK + "Changed " + cmd[2] + " to administrator");
							break;
						case "user":
							ChatServer.promoteClient(ChatServer.getClientID(cmd[2]), 1);
							ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been moved to user");
							ChatServer.sendOne(ChatServer.getClientID(name), Colors.PINK + "Changed " + cmd[2] + " to user");
							break;
						default:
							ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "permissions syntax: /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
							break;
					}
				}
				break;
			case "promote":
				if (cmd.length != 3) {
					ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.promoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been promoted");
					ChatServer.sendOne(ChatServer.getClientID(name), Colors.PINK + "Promoted " + cmd[2]);
				}
				break;
			case "demote":
				if (cmd.length != 3) {
					ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt;");
				} else {
					ChatServer.demoteClient(ChatServer.getClientID(cmd[2]));
					ChatServer.sendOne(ChatServer.getClientID(cmd[2]), "You have been demoted");
					ChatServer.sendOne(ChatServer.getClientID(name), Colors.PINK + "Demoted " + cmd[2]);
				}				
				break;
			default: 
				ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "permissions syntax: /pex &lt;promote | demote&gt; &lt;user&gt; or /pex set &lt;user &lt;user | mod | admin&gt;&gt;");
				break;
			}
			break;
		default:
			ChatServer.sendOne(ChatServer.getClientID(name), Colors.DARKRED + "Unknown command. Type /help for a list of commands.");
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
	public boolean isPing() {
		return isPing;
	}
	public int getID() {
		return clientID;
	}
	public String getRankColor() {
		int rank = getRank();
		switch(rank) {
		case 1: return Colors.GREEN;
		case 2: return Colors.LIGHTBLUE;
		case 3: return Colors.RED;
		default: return Colors.WHITE;
		}
	}
}

