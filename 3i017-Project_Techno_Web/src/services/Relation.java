package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import ServicesTools.DBTools;
import ServicesTools.ServicesTools;

public class Relation {
	
	//Ajout d'un autre profil à la liste d'amis
	public static JSONObject addFriend(String key, int id_friend) throws JSONException {
		Integer id_F = (Integer) id_friend;
		if(key==null || id_F==null) { return ServicesTools.serviceRefused("Missing Arguments", -1);}
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {return ServicesTools.serviceRefused("DB Instance Problem : "+e.getMessage(), 1000);
		} catch (IllegalAccessException e) {return ServicesTools.serviceRefused("DB Acces Problem : "+e.getMessage(), 1000);
		} catch (ClassNotFoundException e) {return ServicesTools.serviceRefused("DB No Class Problem : "+e.getMessage(), 1000);}
		JSONObject newFriend = new JSONObject();
		
		try {
			if(!DBTools.checkKey(key)) {return ServicesTools.serviceRefused("Connection required", -1);}
			if(!DBTools.checkId(id_friend)) { return ServicesTools.serviceRefused("No Such User", -1);}
			if(DBTools.lookForId_Session(key)==id_friend) {return ServicesTools.serviceRefused("You can't befriend yourself", -1);}
			
			String new_key = ServicesTools.createConnectionKey();
			while(DBTools.checkKey(new_key)) {new_key = ServicesTools.createConnectionKey();}
			DBTools.updateConnection(key,new_key);
			
			ServicesTools.serviceAccepted(newFriend);
			newFriend.put("NewFriend", id_friend);
			String friend = DBTools.lookForLogin_User(id_friend);
			newFriend.put("message","Vous avez un nouvel ami en la personne de "+friend);
			newFriend.put("Key",new_key);
			
			int id_self = DBTools.lookForId_Session(new_key);
			DBTools.insertFriend(id_self, id_friend);
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		return newFriend;
	}
	
	
	
	//Suppression d'un profil de la liste d'amis
	public static JSONObject removeFriend(String key, int id_friend) throws JSONException {
		Integer id_F = (Integer) id_friend;
		if(key==null || id_F==null) { return ServicesTools.serviceRefused("Missing Arguments", -1);}
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {return ServicesTools.serviceRefused("DB Instance Problem : "+e.getMessage(), 1000);
		} catch (IllegalAccessException e) {return ServicesTools.serviceRefused("DB Acces Problem : "+e.getMessage(), 1000);
		} catch (ClassNotFoundException e) {return ServicesTools.serviceRefused("DB No Class Problem : "+e.getMessage(), 1000);}
		JSONObject oldFriend = new JSONObject();
		
		try {
			if(!DBTools.checkKey(key)) {return ServicesTools.serviceRefused("Connection required", -1);}
			if(!DBTools.checkId(id_friend)) { return ServicesTools.serviceRefused("No Such User", -1);}
			String login_friend = DBTools.lookForLogin_User(id_friend);
			int id_self = DBTools.lookForId_Session(key);
			if(!DBTools.checkFriend(login_friend,id_self)) { return ServicesTools.serviceRefused("No Such Friend", -1);}
			
			String new_key = ServicesTools.createConnectionKey();
			while(DBTools.checkKey(new_key)) {new_key = ServicesTools.createConnectionKey();}
			DBTools.updateConnection(key,new_key);
			
			ServicesTools.serviceAccepted(oldFriend);
			oldFriend.put("OldFriend", id_friend);
			String friend = DBTools.lookForLogin_User(id_friend);
			oldFriend.put("message",friend+" n'est plus votre ami");
			oldFriend.put("Key",new_key);
			
			id_self = DBTools.lookForId_Session(new_key);
			DBTools.removeFriend(id_self, id_friend);
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		return oldFriend;
	}
	
	
	
	//Recherche du profil d'un ami
	public static JSONObject searchFriend(String key, String friend) throws JSONException {
		if(key==null || friend==null) { return ServicesTools.serviceRefused("Missing Arguments", -1);}
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {return ServicesTools.serviceRefused("DB Instance Problem : "+e.getMessage(), 1000);
		} catch (IllegalAccessException e) {return ServicesTools.serviceRefused("DB Acces Problem : "+e.getMessage(), 1000);
		} catch (ClassNotFoundException e) {return ServicesTools.serviceRefused("DB No Class Problem : "+e.getMessage(), 1000);}
		JSONObject searchedFriend = new JSONObject();
		
		try {
			if(!DBTools.checkKey(key)) {return ServicesTools.serviceRefused("Connection required", -1);}
			if(!DBTools.checkUser(friend)) {return ServicesTools.serviceRefused("No Such User", -1);}
			int self = DBTools.lookForId_Session(key);
			if(!DBTools.checkFriend(friend, self)) {return ServicesTools.serviceRefused(friend+" ne fait pas partie vos amis", -1);}
			
			String new_key = ServicesTools.createConnectionKey();
			while(DBTools.checkKey(new_key)) {new_key = ServicesTools.createConnectionKey();}
			DBTools.updateConnection(key,new_key);
			
			ServicesTools.serviceAccepted(searchedFriend);
			int id_friend = DBTools.lookForId_User(friend);
			searchedFriend.put("SearchedFriend", id_friend);
			searchedFriend.put("message","Vous avez trouvé votre ami "+friend);
			searchedFriend.put("Key",new_key);
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		return searchedFriend;
	}
}
