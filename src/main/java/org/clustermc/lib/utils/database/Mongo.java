package org.clustermc.lib.utils.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Mongo {
	private MongoClient client;
	private final Map<String, MongoDatabase> databases = new HashMap<>();
	
	private int port;
	private String host;
	private String user;
	private String password;
	
	public Mongo(FileConfiguration file) {
		port = file.getInt("port");
		host = file.getString("host");
		user = file.getString("user");
		password = file.getString("password");
	}
	
	public void open() {
		String path = "mongodb://" + user + ":" + password + "@" + host + ":" + port;
		client = new MongoClient(new MongoClientURI(path));
	}

    public MongoClient getClient() {return this.client;}

    public MongoDatabase getDatabase(String name) {
		name = name.toLowerCase();
		if(!databases.containsKey(name)){
			return databases.put(name, client.getDatabase(name));
		}
		return databases.get(name);
	}
}