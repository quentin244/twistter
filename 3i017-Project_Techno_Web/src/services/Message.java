package services;

import java.sql.SQLException;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import ServicesTools.DBTools;
import ServicesTools.ServicesTools;

public class Message {
	
	// Ajout d'un message sur le profil
	public static JSONObject addMessage(String key, String texte) throws JSONException {
		if(key==null || texte==null) { return ServicesTools.serviceRefused("Missing Arguments", -1);}
		
		JSONObject message = new JSONObject();
		try {
			if(!DBTools.checkKey(key)) {return ServicesTools.serviceRefused("Connection Required", 1000);}
			if(texte=="") {return ServicesTools.serviceRefused("No message sent", -1);}
			
			String new_key = ServicesTools.createConnectionKey();
			while(DBTools.checkKey(new_key)) {new_key = ServicesTools.createConnectionKey();}
			DBTools.updateConnection(key,new_key);
			
			message.put("Key", new_key);
			DBTools.insertMessage(new_key, texte);		
			ServicesTools.serviceAccepted(message);
			message.put("Message", texte);
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		return message;
	}
	
	// Recherches des messages :
	//	- Si 2 arguments nuls -> retourne tous les messages
	//	- Si clé non nulle et login nul -> retourne tous les messages associés à la clé de connexion
	// 	- Si login et clé non nuls -> retourne tous les messages de l'ami (si c'en est un)
	public static JSONObject listeMessage(String key, String login) throws JSONException {
		JSONObject liste = new JSONObject();
		
		try {
			int id = DBTools.lookForId_Session(key);
			String new_key = ServicesTools.createConnectionKey();
			while(DBTools.checkKey(new_key)) {new_key = ServicesTools.createConnectionKey();}
			DBTools.updateConnection(key,new_key);
			liste.put("Key", new_key);
			Document rep = new Document();
			
			if(key==null && login==null) {rep = DBTools.lookForAllMessages();}
			else if(login==null) {rep = DBTools.lookForSelfMessages(key);}
			else {
				if(!DBTools.checkFriend(login, id)) {return ServicesTools.serviceRefused("No Such Friend", -1);}
				else {rep = DBTools.lookForFriendMessages(login);}
			}
			
			liste.put("Key", new_key);
			ServicesTools.serviceAccepted(liste);
			liste.put("Liste", rep);
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		return liste;
	}

}
