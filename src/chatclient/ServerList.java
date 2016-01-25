package chatclient;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.io.IOUtils;

import common.p;

import org.json.JSONArray;
import org.json.JSONObject;

public class ServerList {
	
	private ArrayList<ServerListItem> servers = new ArrayList<ServerListItem>();
	private JSONArray serverList;
	private String filepath = "cfg/servers.json";
	
	public ServerList() {
		getServersFromJson();
	}
	public void getServersFromJson() {
		servers.clear();
		try {
            File f = new File(filepath);
            if (f.exists()) {
                InputStream is = new FileInputStream(filepath);
                String jsonTxt = IOUtils.toString(is);
                if (jsonTxt.equals("")) {
                	return;
                } else {
                	serverList = new JSONArray(jsonTxt);
                }
            } else {
                p.nl("Could not find file: " + filepath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		for (int i = 0; i < serverList.length(); i++) {
			JSONObject obj = serverList.getJSONObject(i);
			servers.add(new ServerListItem(obj.getString("name"), obj.getString("ip")));
		}
	}
	private void putServersToJson() {
		JSONArray arr = new JSONArray();
		for (ServerListItem s : servers) {
			JSONObject obj = new JSONObject(s);
			arr.put(obj);
		}
		String res = arr.toString();
		PrintWriter writer;
		try {
			writer = new PrintWriter(filepath, "UTF-8");
			writer.print(res);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public void updateServers() {
		getServersFromJson();
		for (ServerListItem s : servers) {
			s.updatePanel();
		}
	}
	public ArrayList<ServerListItem> getServers() {
		//updateServers();
		return servers;
	}
	public JScrollPane getServerPanels() {
		updateServers();
		JScrollPane panel = new JScrollPane();
		if (servers.isEmpty()) return panel;
		JPanel viewport = new JPanel();
		viewport.setLayout(new BoxLayout(viewport, BoxLayout.Y_AXIS));
		panel.setBounds(0, 23, 704, 304);
		panel.setViewportBorder(null);
		for (ServerListItem s : servers) {
			viewport.add(s.panel());
		}
		panel.setViewportView(viewport);
		return panel;
	}
	public Component getViewportView() {
		JPanel viewport = new JPanel();
		if (servers.isEmpty()) return viewport;
		viewport.setLayout(new BoxLayout(viewport, BoxLayout.Y_AXIS));
		for (ServerListItem s : servers) {
			viewport.add(s.panel());
		}
		return viewport;
	}
	public String toString() {
		updateServers();
		String str = "";
		for (ServerListItem s : servers) {
			str += s.toString()+"\n";
		}
		return str;
	}
	public void addServer(String name, String ip) {
		for (ServerListItem s : servers) {
			if (s.getIp().equals(ip)) return;
		}
		servers.add(new ServerListItem(name, ip));
		putServersToJson();
	}
	public void removeServer(ServerListItem si) {
		servers.remove(si);
		putServersToJson();
	}
	public void editServer(ServerListItem si, String name, String ip) {
		servers.set(servers.indexOf(si), new ServerListItem(name, ip));
		putServersToJson();
	}
}
