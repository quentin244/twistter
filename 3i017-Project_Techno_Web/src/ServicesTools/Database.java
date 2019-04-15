package ServicesTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.bson.Document;

import com.mongodb.client.*;

public class Database {
	private DataSource dataSource;
	public static Database database;
	
	public Database(String jndiname) throws SQLException{
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
		}catch(NamingException n) {
			throw new SQLException(jndiname+" is missing in JNDI! : "+n.getMessage());
		}
	}
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public static Connection getMySQLConnection() throws SQLException {
		if(DBStatic.pooling==false) {
			return(DriverManager.getConnection("jdbc:mysql://"+DBStatic.host+"/"+DBStatic.db, DBStatic.user, DBStatic.password));
		}
		else {
			if(database==null) {
				database = new Database("jdbc/db");
			}
			return(database.getConnection());
		}
	}
	
	public static MongoCollection<Document> getMongoBD() {
		MongoClient mongo = (MongoClient) MongoClients.create("mongodb://"+DBStatic.host+":27017");
		MongoDatabase mDB = mongo.getDatabase(DBStatic.mongo_bd);
		MongoCollection<Document> message_collection = mDB.getCollection("message");
		return message_collection;
	}
}
