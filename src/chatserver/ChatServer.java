package chatserver;

import java.io.*;
import java.net.*;
import java.util.*;

import common.*;

public class ChatServer implements Runnable {
	
	/*TODO:
	 * Make it so that it saves 2 text files with admin/mod usernames
	 */
	
	private static ArrayList<ChatServerThread> client = new ArrayList<ChatServerThread>();
	private static ArrayList<String> admins = new ArrayList<String>();
	private static ArrayList<String> mods = new ArrayList<String>();
	public static ArrayList<String> users = new ArrayList<String>();
	private static int numClients = 0;
	public static int port = 0;
	private static final String CS = "§";
	private static ChatServerGUI g;
	public static final String ver = "1.4";
	public static final int HEIGHT = 402,
			WIDTH = 700;
	
	public static void main(String[] args) {
		new Thread(new ChatServer()).start();
	}
	public ChatServer() {
		try {
			ServerSocket tSocket = new ServerSocket(25565); //default to 25565 - will throw an exception if port is taken
			tSocket.close();
			port = 25565;
		} catch (Exception e) {
			port = findFreePort();
			p.ne("Port 25565 unavailable, using port " + port);
		}
	}
	
	public void run() {
		g = new ChatServerGUI(this);
		g.displayMain();
		p.nl("Starting ChatServer on port " + port);
		g.pushToChat("[INFO] Starting ChatServer on port " + port);
		p.nl();
		p.nl("This computer's IP Addresses are:");
		g.pushToChat("[INFO] This computer's IP Addresses are:");
		try {
			InetAddress inet = InetAddress.getLocalHost();
			InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
			if (ips != null ) {
			    for (int i = 0; i < ips.length; i++) {
			    	System.out.println(ips[i]);
			    	g.pushToChat("[INFO] &gt;" + ips[i]);
			    }
			}
			p.nl();
			p.nl("On LAN, join the address starting with 192.168.*.*");
			p.nl("If it doesn't exist, use the first IP in the list.");
			//g.pushToChat("[INFO] On LAN, join the address starting with 192.168.*.*");
			//g.pushToChat("[INFO] If it doesn't exist, use the first IP in the list.");
			p.nl();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		boolean listening = true;
		try (ServerSocket sSocket = new ServerSocket(port)) {
			p.nl("Waiting for incoming connections");
			g.pushToChat("[INFO] Waiting for incoming connections");
            while (listening) {
	            client.add(new ChatServerThread(sSocket.accept(), this));
	            client.get(client.size() - 1).start();
	            numClients++;
	            //updateUsers();
	        }
	    } catch (Exception e) {
            p.ne("Could not listen on port " + port);
            g.pushToChat("[ERROR] Could not bind to port! Is another server already running on that port?");
            e.printStackTrace();
            //System.exit(-1);
        }
	}
	/**
	 * do these things whenever someone joins or leaves
	 */
	public static void updateUsers() {
		String users = "";
		int i = 0;
		for (ChatServerThread u : client) {
			if (u != null) {
				if (i < numClients - 1) users += CS + getRankColor(u.name) + u.name + "<br>";
				else users += CS + getRankColor(u.name) + u.name;
				i++;
			}
		}
		users = ChatServerFunctions.parseColor(users);
		g.setUsersList(users);
	}
	public void restoreClientRanks() {
		for (String user : admins) {
        	if (client.get(client.size() - 1).name.equals(user)) client.get(client.size() - 1).setRank(3);
        }
        for (String user : mods) {
        	if (client.get(client.size() - 1).name.equals(user)) client.get(client.size() - 1).setRank(2);
        }
	}
	public int getNextClientID() {
		return numClients;
	}
	// this method is broken because the client ids do not actually match up with the index in the clients list
	public static void removeClient(int id) {
		users.remove(users.indexOf(getClientName(id)));
		client.set(id, null);
		numClients--;
		updateUsers();
	}
	public static ChatServerThread getClient(String name) {
		for (int i = 0; i < client.size(); i++) {
			if (client.get(i) != null) {
				if (client.get(i).name.equalsIgnoreCase(name)) return client.get(i);
			}
		}
		return null;
	}
	public static int getClientID(String name) {
		for (int i = 0; i < client.size(); i++) {
			if (client.get(i) != null) {
				if (client.get(i).name.equalsIgnoreCase(name)) return i;
			}
		}
		return -1;
	}
	public static String getClientName(int id) {
		try {
			if (client.get(id) != null) {
				return client.get(id).name;
			}
		} catch (Exception e) {
		}
		return "User not found";
	}
	public static String getClientsOnline() {
		String ret = "";
		int i = 0;
		for (ChatServerThread u : client) {
			if (u != null) {
				if (i < numClients - 1) ret += CS + getRankColor(u.name) + u.name + CS + "a, ";
				else ret += CS + getRankColor(u.name) + u.name;
				i++;
			}
		}
		return ret;
	}
	public static String getRankColor(String name) {
		int rank = client.get(getClientID(name)).getRank();
		switch(rank) {
		case 1: return "2";
		case 2: return "b";
		case 3: return "c";
		default: return "f";
		}
	}
	public static void sendAll(String msg) {
		if (msg != null) {
			for (ChatServerThread user : client) {
				if (user != null) user.send(msg + CS + "f");
			}
			p.nl(msg);
			g.pushToChat(ChatServerFunctions.parseColor(msg + CS + "f") + ChatServerFunctions.closeSpans());
		}
	}
	public static void sendAdmins(String msg) {
		if (msg != null) {
			for (ChatServerThread user : client) {
				if (user != null) {
					if (user.getRank() >= 3) user.send(msg + CS + "f");
				}
			}
		}
	}
	public static void sendMods(String msg) {
		if (msg != null) {
			for (ChatServerThread user : client) {
				if (user != null) {
					if (user.getRank() >= 2) user.send(msg + CS + "f");
				}
			}
		}
	}
	public static void sendOne(int starter, int target, String msg) { //used for pms, is currently unused
		if (msg != null && starter > -1 && target > -1) {
			client.get(target).send(msg + CS + "f");
			sendMods(client.get(starter).name + " -> " + client.get(target).name + ": " + msg);
			p.nl(client.get(starter).name + " -> " + client.get(target).name + ": " + msg);
			g.pushToChat(ChatServerFunctions.parseColor(msg + CS + "f") + ChatServerFunctions.closeSpans());
		}
	}
	public static void sendOne(int target, String msg) { //used for server -> user (command responses)
		if (msg != null && target > -1) {
			client.get(target).send(msg + CS + "f");
			p.nl("Server -> " + client.get(target).name + ": " + msg);
			g.pushToChat("Server -> " + ChatServerFunctions.parseColor(client.get(target).name + ": " + msg + CS + "f") + ChatServerFunctions.closeSpans());
		}
	}
	
	public static void kick(int starter, int target) {
		if (!(client.get(target).getRank() > 2)) { //make sure mods cant kick admins
			sendOne(starter, target, CS + "4Kicked from server");
			sendOne(starter, target, "kick");
			sendAll(CS + "c" + client.get(starter).name + " has kicked " + client.get(target).name + " from the server");
			//g.pushToChat(ChatServerFunctions.parseColor(CS + "c" + client.get(starter).name + " has kicked " + client.get(target).name + " from the server") + ChatServerFunctions.closeSpans());
			client.get(target).leave();
		} else {
			sendOne(starter, CS + "4" + client.get(target).name + " cannot be kicked.");
		}
	}
	public static void kick(int target) {
		sendOne(target, CS + "4Kicked from server");
		sendOne(target, "kick");
		sendAll(CS + "c" + client.get(target).name + " has been kicked from the server");
		//g.pushToChat(ChatServerFunctions.parseColor(CS + "c" + client.get(target).name + " has been kicked from the server") + ChatServerFunctions.closeSpans());
		client.get(target).leave();
	}
	public static void kick(int target, String reason) {
		if (!(client.get(target).getRank() > 2)) { //make sure mods cant kick admins
			sendOne(target, CS + "4Kicked " + reason);
			sendOne(target, "kick");
			sendAll(CS + "c" + client.get(target).name + " has been kicked " + reason);
			//g.pushToChat(ChatServerFunctions.parseColor(CS + "c" + client.get(target).name + " has been kicked " + reason) + ChatServerFunctions.closeSpans());
			client.get(target).leave();
		} else {
			g.pushToChat(CS+"4" + client.get(target).name + " cannot be kicked.");
		}
	}
	public static void kickAll() {
		for (int i = 0; i < client.size(); i++) {
			client.get(i).leave();
			sendOne(i, "kick");
		}
	}
	public static void mute(int target) {
		client.get(target).mute();
	}
	private boolean voting = false;
	private ChatVote vote;
	public boolean startVote(String type, int starter, int target, String desc) {
		if (!voting) {
			vote = new ChatVote(starter, type, target);
			sendAll(CS + "c" + client.get(starter).name + " has voted to " + desc + client.get(target).name);
			p.nl(client.get(starter).name + " has voted to " + desc + client.get(target).name);
			voting = true;
			return true;
		}
		return false;
	}
	public boolean startVote(String type, int starter, String desc) {
		if (!voting) {
			vote = new ChatVote(starter, type, desc);
			sendAll(CS + "c" + client.get(starter).name + " has voted to " + desc);
			p.nl(client.get(starter).name + " has voted to " + desc);
			voting = true;
			return true;
		}
		return false;
	}
	public void vote(int voter, int option) {
		sendAll(CS + "c" + client.get(voter).name + " has voted " + ((option == 0) ? "no" : "yes"));
		p.nl(client.get(voter).name + " has voted " + ((option == 0) ? "no" : "yes"));
		vote.addVote(option);
		if (vote.totalVotes() == client.size()) {
			endVote();
		}
	}
	private void endVote() {
		voting = false;
	}
	public static void pushToChat(String msg) {
		g.pushToChat(ChatServerFunctions.parseColor(msg) + ChatServerFunctions.closeSpans());
	}
	public static void promoteClient(int id) {
		if (client.get(id).getRank() < 3) client.get(id).setRank(client.get(id).getRank() + 1);
		if (client.get(id).getRank() == 3) changeStaffList("add", "admin", id);
		if (client.get(id).getRank() == 2) changeStaffList("add", "mod", id);
		updateUsers();
	}
	public static void promoteClient(int id, int alvl) {
		client.get(id).setRank(alvl);
		if (client.get(id).getRank() == 3) changeStaffList("add", "admin", id);
		if (client.get(id).getRank() == 2) changeStaffList("add", "mod", id);
		if (client.get(id).getRank() == 1) {
			removeFromStaff(id);
		}
		updateUsers();
	}
	public static void demoteClient(int id) {
		if (client.get(id).getRank() == 3) changeStaffList("remove", "admin", id);
		if (client.get(id).getRank() == 2) changeStaffList("remove", "mod", id);
		if (client.get(id).getRank() > 1) client.get(id).setRank(client.get(id).getRank() - 1);
		if (client.get(id).getRank() == 1) {
			removeFromStaff(id);
		}
		updateUsers();
	}
	public static void changeStaffList(String action, String rank, int id) {
		String name = getClientName(id);
		if (action.equalsIgnoreCase("add")) {
			if (rank.equalsIgnoreCase("admin")) {
				admins.add(name);
				mods.remove(name);
			} else {
				mods.add(name);
				admins.remove(name);
			}
		} else {
			if (rank.equalsIgnoreCase("admin")) {
				admins.remove(name);
			} else {
				mods.remove(name);
			}
		}
		updateUsers();
	}
	public static void removeFromStaff(int id) {
		admins.remove(getClientName(id));
		mods.remove(getClientName(id));
		updateUsers();
	}
	private static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			int port = socket.getLocalPort();
			try {
				socket.close();
			} catch (IOException e) {
			}
			return port;
		} catch (IOException e) { 
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		throw new IllegalStateException("Could not find a free TCP/IP port");
	}
	
}
