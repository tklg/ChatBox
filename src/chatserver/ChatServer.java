package chatserver;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import common.*;

public class ChatServer implements Runnable {
	
	private static ArrayList<ChatServerThread> client = new ArrayList<ChatServerThread>();
	private static ArrayList<String> admins = new ArrayList<String>();
	private static ArrayList<String> mods = new ArrayList<String>();
	public static ArrayList<String> users = new ArrayList<String>();
	private static int numClients = 0;
	public static int port = 25565;
	private static String MOTD = "";
	private static String LOGINMESSAGE = "";
	public static final String REQ = "‡";
	private static ChatServerGUI g;
	public static final String ver = "1.5";
	public static final int HEIGHT = 402,
			WIDTH = 700;
	
	public static void main(String[] args) {
		new File("cfg/").mkdirs();
		File cfg = new File("cfg/servercfg.json");
		File staffFile = new File("cfg/staff.json");
		if (!cfg.exists()) {
			try {
				cfg.createNewFile();
				PrintWriter writer;
				try {
					writer = new PrintWriter(cfg, "UTF-8");
					writer.println("[{");
					writer.println("\"MOTD\": \"\"");
					writer.println("\"welcomemessage\": \"\",");
					writer.println("\"port\": 25565");
					writer.println("}]");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!staffFile.exists()) {
			try {
				staffFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		new File("cfg/server.cfg");
		new Thread(new ChatServer()).start();
		loadConfigFile();
		getStaffFromJson();
	}
	public ChatServer() {
		try {
			ServerSocket tSocket = new ServerSocket(port); //default to 25565 - will throw an exception if port is taken
			tSocket.close();
		} catch (Exception e) {
			port = findFreePort();
			p.ne("Port 25565 unavailable, using port " + port);
		}
	}
	
	public void run() {
		g = new ChatServerGUI(this);
		g.displayMain();
		p.nl("Starting ChatServer on port " + port);
		g.pushToChat(Colors.YELLOW + "[INFO]" + Colors.WHITE + " Starting ChatServer on port " + port);
		p.nl("This computer's IP Addresses are:");
		g.pushToChat(Colors.YELLOW + "[INFO]"+ Colors.WHITE +" This computer's IP Addresses are:");
		try {
			InetAddress inet = InetAddress.getLocalHost();
			InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
			if (ips != null ) {
			    for (int i = 0; i < ips.length; i++) {
			    	System.out.println(ips[i]);
			    	g.pushToChat(Colors.YELLOW + "[INFO]"+ Colors.WHITE +" &gt;" + ips[i]);
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
		updateUsers();
		try (ServerSocket sSocket = new ServerSocket(port)) {
			p.nl("Waiting for incoming connections");
			g.pushToChat(Colors.YELLOW + "[INFO]"+ Colors.WHITE +" Waiting for incoming connections");
			//Socket socket = sSocket.accept();
			/*BufferedReader msgIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	String in = msgIn.readLine();
        	System.out.println(in);*/
        	//if (in != null) {
	        	while (listening) {
		            //client.add(new ChatServerThread(sSocket.accept(), this));
			        client.add(null);
			        //System.out.println(getNextClientID());
			        client.set(getNextClientID(), new ChatServerThread(sSocket.accept(), this, getNextClientID()));
			        client.get(client.size() - 1).start();
			        numClients++;
			        //updateUsers();
		        }
        	/*} else {
        		g.pushToChat("pinged");
        	}*/
	    } catch (Exception e) {
            p.ne("Could not listen on port " + port);
            g.pushToChat(Colors.RED + "[ERROR]"+Colors.WHITE+" Could not bind to port! Is another server already running on that port?");
            e.printStackTrace();
            //System.exit(-1);
        }
	}
	private static void loadConfigFile() {
		try {
            File f = new File("cfg/servercfg.json");
            if (f.exists()) {
                InputStream is = new FileInputStream("cfg/servercfg.json");
                String jsonTxt = IOUtils.toString(is);
                if (jsonTxt.equals("")) {
                	return;
                } else {
                	JSONArray cfg = new JSONArray(jsonTxt);
                	JSONObject o = cfg.getJSONObject(0);
                	setMotd(o.getString("MOTD"));
                	LOGINMESSAGE = o.getString("welcomemessage");
                	port = o.getInt("port");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public static String getMotd() {
		return MOTD;
	}
	public static String getLoginMessage() {
		return LOGINMESSAGE;
	}
	private static void setMotd(String m) {
		MOTD = m;
	}
	private static void getStaffFromJson() {
		mods.clear();
		admins.clear();
		JSONArray staffList = new JSONArray();
		try {
            File f = new File("cfg/staff.json");
            if (f.exists()) {
                InputStream is = new FileInputStream("cfg/staff.json");
                String jsonTxt = IOUtils.toString(is);
                if (jsonTxt.equals("")) {
                	return;
                } else {
                	staffList = new JSONArray(jsonTxt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		for (int i = 0; i < staffList.length(); i++) { //should be 2
			JSONArray arr = staffList.getJSONArray(i);
			for (int j = 0; j < arr.length(); j++) {
				if (i == 0)
					mods.add(arr.getString(j));
				else if (i == 1)
					admins.add(arr.getString(j));
			}
		}
	}
	private static void putStaffToJson() {
		JSONArray arr = new JSONArray();
		arr.put(new JSONArray(mods));
		arr.put(new JSONArray(admins));
		String res = arr.toString();
		PrintWriter writer;
		try {
			writer = new PrintWriter("cfg/staff.json", "UTF-8");
			writer.print(res);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * do these things whenever someone joins or leaves
	 */
	public static void updateUsers() {
		String users = "";
		int num = 0;
		for (ChatServerThread u : client) {
			if (u != null && !u.isPing()) num++;
		}
		int i = 0;
		users += Colors.LIGHTGREEN + num + Colors.WHITE+" users online<br>";
		for (ChatServerThread u : client) {
			if (u != null && !u.isPing()) {
				if (i < numClients - 1) users += getRankColor(u.name) + u.name + "<br>";
				else users += getRankColor(u.name) + u.name;
				i++;
			}
		}
		users = ChatServerFunctions.parseColor(users);
		g.setUsersList(users);
	}
	public static void addUser(String name) {
		users.add(name);
	}
	public static void restoreClientRank(String name) {
		for (String user : admins) {
			if (name.equals(user))
				client.get(getClientID(name)).setRank(3);
        }
        for (String user : mods) {
        	if (name.equals(user))
				client.get(getClientID(name)).setRank(2);
        }
	}
	public int getNextClientID() {
		return numClients;
	}
	// this method is broken because the client ids do not actually match up with the index in the clients list
	public static void removeClient(int id) {
		System.out.println("removing client: " + id);
		users.remove(users.indexOf(getClientName(id)));
		client.set(id, null);
		//numClients--; //maybe rebuild the clients list and reassign all threads new ids
		updateUsers();
	}
	public static ChatServerThread getClient(String name) {
		for (int i = 0; i < client.size(); i++) {
			if (client.get(i) != null) {
				if (!client.get(i).isPing() && client.get(i).name.equalsIgnoreCase(name)) return client.get(i);
			}
		}
		return null;
	}
	public static int getClientID(String name) {
		for (int i = 0; i < client.size(); i++) {
			if (client.get(i) != null) {
				if (!client.get(i).isPing() && client.get(i).name.equalsIgnoreCase(name)) return i;
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
			if (client.get(id).isPing()) {
				return "SERV_PING";
			}
		}
		return "User not found";
	}
	public static String getClientsOnline() {
		String ret = "";
		int i = 0;
		for (ChatServerThread u : client) {
			if (u != null && !u.isPing()) {
				if (i < numClients - 1) ret += getRankColor(u.name) + u.name + Colors.LIGHTGREEN +", ";
				else ret += getRankColor(u.name) + u.name;
			}
			i++;
		}
		return ret;
	}
	public static String getRankColor(String name) {
		return client.get(getClientID(name)).getRankColor();
	}
	public static void sendAll(String msg) {
		if (msg != null) {
			for (ChatServerThread user : client) {
				if (user != null) user.send(msg + Colors.WHITE);
			}
			p.nl(msg);
			g.pushToChat(ChatServerFunctions.parseColor(msg + Colors.WHITE) + ChatServerFunctions.closeSpans());
		}
	}
	public static void sendAdmins(String msg) {
		if (msg != null) {
			for (ChatServerThread user : client) {
				if (user != null) {
					if (user.getRank() >= 3) user.send(msg + Colors.WHITE);
				}
			}
		}
	}
	public static void sendMods(String msg) {
		if (msg != null) {
			for (ChatServerThread user : client) {
				if (user != null) {
					if (user.getRank() >= 2) user.send(msg + Colors.WHITE);
				}
			}
		}
	}
	public static void sendOne(int starter, int target, String msg) { //used for pms, is currently unused
		if (msg != null && starter > -1 && target > -1) {
			client.get(target).send(msg + Colors.WHITE);
			sendMods(client.get(starter).name + " -> " + client.get(target).name + ": " + msg);
			p.nl(client.get(starter).name + " -> " + client.get(target).name + ": " + msg);
			g.pushToChat(ChatServerFunctions.parseColor(msg + Colors.WHITE) + ChatServerFunctions.closeSpans());
		}
	}
	public static void sendOne(int target, String msg) { //used for server -> user (command responses)
		if (msg != null && target > -1) {
			client.get(target).send(msg + Colors.WHITE);
			p.nl("Server -> " + client.get(target).name + ": " + msg);
			g.pushToChat("Server -> " + ChatServerFunctions.parseColor(client.get(target).name + ": " + msg + Colors.WHITE) + ChatServerFunctions.closeSpans());
		}
	}
	
	public static void kick(int starter, int target) {
		if (!(client.get(target).getRank() > 2)) { //make sure mods cant kick admins
			sendOne(starter, target, Colors.DARKRED + "Kicked from server");
			sendOne(starter, target, "kick");
			sendAll(Colors.RED + client.get(starter).name + " has kicked " + client.get(target).name + " from the server");
			//g.pushToChat(ChatServerFunctions.parseColor(CS + "c" + client.get(starter).name + " has kicked " + client.get(target).name + " from the server") + ChatServerFunctions.closeSpans());
			client.get(target).leave();
		} else {
			sendOne(starter, Colors.DARKRED + client.get(target).name + " cannot be kicked.");
		}
	}
	public static void kick(int target) {
		sendOne(target, Colors.DARKRED + "Kicked from server");
		sendOne(target, "kick");
		sendAll(Colors.RED + client.get(target).name + " has been kicked from the server");
		//g.pushToChat(ChatServerFunctions.parseColor(CS + "c" + client.get(target).name + " has been kicked from the server") + ChatServerFunctions.closeSpans());
		client.get(target).leave();
	}
	public static void kick(int target, String reason) {
		if (!(client.get(target).getRank() > 2)) { //make sure mods cant kick admins
			sendOne(target, Colors.DARKRED + "Kicked " + reason);
			sendOne(target, "kick");
			sendAll(Colors.RED + client.get(target).name + " has been kicked " + reason);
			//g.pushToChat(ChatServerFunctions.parseColor(CS + "c" + client.get(target).name + " has been kicked " + reason) + ChatServerFunctions.closeSpans());
			client.get(target).leave();
		} else {
			g.pushToChat(Colors.DARKRED + client.get(target).name + " cannot be kicked.");
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
	private static boolean voting = false;
	private static ChatVote vote;
	public static boolean startVote(String type, int starter, int target, String desc) {
		if (!voting) {
			vote = new ChatVote(starter, type, target);
			sendAll(Colors.RED + client.get(starter).name + " has voted to " + desc + client.get(target).name);
			p.nl(client.get(starter).name + " has voted to " + desc + client.get(target).name);
			voting = true;
			return true;
		}
		return false;
	}
	public static boolean startVote(String type, int starter, String desc) {
		if (!voting) {
			vote = new ChatVote(starter, type, desc);
			sendAll(Colors.RED + client.get(starter).name + " has voted to " + desc);
			p.nl(client.get(starter).name + " has voted to " + desc);
			voting = true;
			return true;
		}
		return false;
	}
	public static void vote(int voter, int option) {
		sendAll(Colors.RED + client.get(voter).name + " has voted " + ((option == 0) ? "no" : "yes"));
		p.nl(client.get(voter).name + " has voted " + ((option == 0) ? "no" : "yes"));
		vote.addVote(option);
		if (vote.totalVotes() == client.size()) {
			endVote();
		}
	}
	private static void endVote() {
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
		putStaffToJson();
	}
	public static void removeFromStaff(int id) {
		admins.remove(getClientName(id));
		mods.remove(getClientName(id));
		putStaffToJson();
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
