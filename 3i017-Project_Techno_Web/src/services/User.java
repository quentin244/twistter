package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import ServicesTools.DBTools;
import ServicesTools.ServicesTools;

public class User {
	
	// Création d'un compte utilisateur
	public static JSONObject createUser(String login, String password, String nom, String prenom, String mail) throws SQLException, JSONException {
		if(login==null || password==null) { return ServicesTools.serviceRefused("Missing Capital Arguments", -1);}
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {return ServicesTools.serviceRefused("DB Instance Problem : "+e.getMessage(), 1000);
		} catch (IllegalAccessException e) {return ServicesTools.serviceRefused("DB Acces Problem : "+e.getMessage(), 1000);
		} catch (ClassNotFoundException e) {return ServicesTools.serviceRefused("DB No Class Problem : "+e.getMessage(), 1000);}
		JSONObject newUser = new JSONObject();
		
		try {
			if(DBTools.checkUser(login)) {return ServicesTools.serviceRefused("User Already Exists", -1);}
			if(!DBTools.checkSecurity(password)) {return ServicesTools.serviceRefused("Invalid Password", -1);}
			
			ServicesTools.serviceAccepted(newUser);
			newUser.put("Login", login);
			newUser.put("Password", password);
			newUser.put("message", "Compte créé avec succès");
			DBTools.insertUser(login, password, nom, prenom, mail);
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		return newUser;
	}
	
	// Connexion à un compte existant
	public static JSONObject login(String login, String password, boolean root) throws JSONException {
		if(login==null || password==null) { return ServicesTools.serviceRefused("Missing Arguments", -1);}
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {return ServicesTools.serviceRefused("DB Instance Problem : "+e.getMessage(), 1000);
		} catch (IllegalAccessException e) {return ServicesTools.serviceRefused("DB Acces Problem : "+e.getMessage(), 1000);
		} catch (ClassNotFoundException e) {return ServicesTools.serviceRefused("DB No Class Problem : "+e.getMessage(), 1000);}
		JSONObject co = new JSONObject();
		
		try {
			if(!DBTools.checkUser(login)) { return ServicesTools.serviceRefused("Unknown User", -1);}
			if(!DBTools.checkPassword(login, password)) { return ServicesTools.serviceRefused("Wrong Password", -1);}
			
			int id_user = DBTools.lookForId_User(login);
			String key = ServicesTools.createConnectionKey();
			while(DBTools.checkKey(key)) {key = ServicesTools.createConnectionKey();}
			
			DBTools.insertKey(key, id_user, root);
			ServicesTools.serviceAccepted(co);
			co.put("Key", key);
			co.put("message", "Bienvenue, vous êtes connecté(e)");
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		return co;
	}
	
	// Deconnexion d'un compte
	public static JSONObject logout(String key, String login) throws JSONException, SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {return ServicesTools.serviceRefused("DB Instance Problem : "+e.getMessage(), 1000);
		} catch (IllegalAccessException e) {return ServicesTools.serviceRefused("DB Acces Problem : "+e.getMessage(), 1000);
		} catch (ClassNotFoundException e) {return ServicesTools.serviceRefused("DB No Class Problem : "+e.getMessage(), 1000);}
		JSONObject deco = new JSONObject();
		
		try {
			if(!DBTools.checkKey(key)) {return ServicesTools.serviceRefused("Database Problem : Key doesn't exists", 1000);}
			if(!DBTools.checkConnection(key, login)) {return ServicesTools.serviceRefused("Database Problem : This key and this login are not associated", 1000);}
			
			ServicesTools.serviceAccepted(deco);
			deco.put("message", "A la prochaine");
			DBTools.removeKey(key);
			
		}catch(JSONException j) { return ServicesTools.serviceRefused("JSON Problem : "+j.getMessage(), 100);
		}catch(SQLException s) { return ServicesTools.serviceRefused("DataBase Problem : "+s.getMessage(), 1000);}
		
		
		return deco;
	}
}
