package ServicesTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class DBTools {
	
	// Recherche de l'existence d'un login dans la Database
	public static boolean checkUser(String login) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT * FROM user WHERE user_login='"+login+"';";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean userExists = false;
		while(rs.next()) {userExists = true;}
		return userExists;
	}
	
	// Test d'existence d'un utilisateur selon son numéro d'identification (clé primaire)
	public static boolean checkId(int id) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT * FROM user WHERE user_id="+id+";";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean userExists = false;
		while(rs.next()) {userExists = true;}
		return userExists;
	}
	
	// Test de correspodance entre le mot de passe proposé et celui enregistré dans la DataBase comme associé au login proposé
	public static boolean checkPassword(String login, String password) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT * FROM user WHERE user_login='"+login+"' and user_password='"+password+"';";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean correctPassword = false;
		while(rs.next()) {correctPassword = true;}
		return correctPassword;
	}
		
	// Test d'acceptation du format du mot de passe proposé
	public static boolean checkSecurity(String password) {
		boolean acceptedPassword = false;
		int taille = password.length();
		if(taille<=32 && taille>=8) { acceptedPassword = true;}
		return acceptedPassword;
	}
	
	// Recherche de la clé de connection proposée dans la table session
	public static boolean checkKey(String key) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT * FROM session WHERE session_key='"+key+"';";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean keyExists = false;
		while(rs.next()) {keyExists = true;}
		return keyExists;
		
	}
		
	// Test de correspondance entre le login proposé et celui associé à la clé de connection proposée
	public static boolean checkConnection(String key, String login) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT s.session_key FROM session s, user u WHERE s.session_key='"+key+"' and u.user_login='"+login+"' and s.user_id=u.user_id;";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean coExists = false;
		while(rs.next()) {coExists = true;}
		return coExists;
	}
	
	// Recherche un ami dans la table follow
	public static boolean checkFriend(String login_friend, int id_self) throws SQLException {
		int id_friend = lookForId_User(login_friend);
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT id_user2 FROM follow WHERE id_user1="+id_self+" and id_user2="+id_friend+";";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		boolean friendExists = false;
		while(rs.next()) {friendExists = true;}
		return friendExists;
	}
	
	
	
	// Recherche du numéro d'identification (clé primaire) associé au login proposé
	public static int lookForId_User(String login) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT user_id FROM user WHERE user_login='"+login+"';";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		return rs.getInt(1);
	}
	
	// Recherche du login associé au numéro d'identification proposé
	public static String lookForLogin_User(int id) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT user_login FROM user WHERE user_id="+id+";";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		return rs.getString(1);
	}
	
	// Recherche du numéro d'identification associé à la clé de conneion proposée (=numéro du compte utilisateur appelant)
	public static int lookForId_Session(String key) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT user_id FROM session WHERE session_key='"+key+"';";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		int id = rs.getInt(1);
		return id;
	}
	
	// Recherche de la clé de connexion associé au numéro d'identification proposé
	public static String lookForKey_Session(int id) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "SELECT session_key FROM session WHERE user_id="+id+";";
		Statement st = (Statement) co.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		String cle = rs.getString(1);
		return cle;
	}
	
	// Recherche de tous les messages écrits par le login proposé sous condition de connexion
	public static Document lookForAllMessages() throws SQLException {
		MongoCollection<Document> message_collection = Database.getMongoBD();
		Document rep = new Document();
		Document tri = new Document();
//		tri.append("date", -1);
		FindIterable<Document> fi = message_collection.find();
		MongoCursor<Document> cu = fi.iterator();
		Integer cpt = new Integer(1);
		while(cu.hasNext()) {
			rep.append("message_"+cpt.toString(),cu.next());
			cpt++;
		}
		return rep;
	}
	
	public static Document lookForFriendMessages(String login) throws SQLException {
		MongoCollection<Document> message_collection = Database.getMongoBD();
		Document rep = new Document();
		int id = lookForId_User(login);
		Document query = new Document();
		query.append("user_id", id);
		Document tri = new Document();
		tri.append("date", -1);
		FindIterable<Document> fi = message_collection.find(query).sort(tri);
		MongoCursor<Document> cu = fi.iterator();
		Integer cpt = new Integer(1);
		while(cu.hasNext()) {
			rep.append("message_"+cpt.toString(),cu.next());
			cpt++;
		}
		return rep;
	}
	
	// Recherche de tous les messages écrits par le login proposé sous condition de connexion
		public static Document lookForSelfMessages(String key) throws SQLException {
			MongoCollection<Document> message_collection = Database.getMongoBD();
			Document rep = new Document();
			int id = lookForId_Session(key);
			Document query = new Document();
			query.append("user_id", id);
			Document tri = new Document();
			tri.append("date", -1);
			FindIterable<Document> fi = message_collection.find(query).sort(tri);
			MongoCursor<Document> cu = fi.iterator();
			Integer cpt = new Integer(1);
			while(cu.hasNext()) {
				rep.append("message_"+cpt.toString(),cu.next());
				cpt++;
			}
			return rep;
		}
	
	// Insertion d'un nouveau couple (login, mot de passe) dans la table useer
	public static void insertUser(String login, String password, String nom, String prenom, String mail) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "INSERT INTO user(user_login, user_password, user_nom, user_prenom, user_mail) VALUES('"+login+"', '"+password+"', '"+nom+"', '"+prenom+"', '"+mail+"');";
		Statement st = (Statement) co.createStatement();
		int rs = st.executeUpdate(query);
	}
	
	// Insersion de la clé de connection dans la table session
	public static void insertKey(String key, int id, boolean root) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		System.out.println("Avant insertion");
		String query = "INSERT INTO session(session_key, user_id, session_root) VALUES('"+key+"', "+id+", "+root+");";
		Statement st = (Statement) co.createStatement();
		int rs = st.executeUpdate(query);
		System.out.println("Avant insertion");
	}
	
	// Insertion d'un nouvel ami dans la table follow
	public static void insertFriend(int user_1, int user_2) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "INSERT INTO follow(id_user1, id_user2) VALUES("+user_1+", "+user_2+");";
		Statement st = (Statement) co.createStatement();
		int rs = st.executeUpdate(query);
	}
	
	// Insertion d'un nouveau message dans la MongoBD
	public static void insertMessage(String key, String texte) throws SQLException {
		MongoCollection<Document> message_collection = Database.getMongoBD();
		Document query = new Document();
		GregorianCalendar cal = new GregorianCalendar();
		Date now = cal.getTime();
		int id = lookForId_Session(key);
		query.append("user_id", id);
		query.append("date", now);
		query.append("texte", texte);
		message_collection.insertOne(query);
	}
	
	// Mise à jour de la clé de connexion d'une session en cas d'activité de l'utilisateur
	public static void updateConnection(String old_key, String new_key) throws SQLException{
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "UPDATE session set session_key='"+new_key+"' where session_key='"+old_key+"';";
		Statement st = (Statement) co.createStatement();
		int rs = st.executeUpdate(query);
	}
	
	
	// Suppression de la clé de connection proposée de la table seesion
	public static void removeKey(String key) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "DELETE FROM session WHERE session_key='"+key+"';";
		Statement st = (Statement) co.createStatement();
		int rs = st.executeUpdate(query);
	}
	
	// Suppression d'un ami de la talbe follow
	public static void removeFriend(int user_1, int user_2) throws SQLException {
		Connection co = (Connection) Database.getMySQLConnection();
		String query = "DELETE FROM follow WHERE id_user1="+user_1+" and id_user2="+user_2+";";
		Statement st = (Statement) co.createStatement();
		int rs = st.executeUpdate(query);
	}
	
}
