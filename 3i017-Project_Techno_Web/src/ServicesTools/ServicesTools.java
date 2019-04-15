package ServicesTools;
import java.sql.SQLException;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class ServicesTools {
	
	public static JSONObject serviceRefused(String message, int id) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id_erreur ", id);
		json.put("message", message);
		json.put("Status", "Erreur");
		return json;
	}
	
	public static JSONObject serviceAccepted(JSONObject json) throws JSONException {
		json.put("Status", "Accepté");
		return json;
	}
	
	// Génère et renvoi une clé de connection 
	public static String createConnectionKey() {
		
		StringBuilder key = new StringBuilder();
		String alphabet = "0123456789abcdefghijiklmnopqrstuvwxyz";
		Random r = new Random();
		for(int i=0; i<64; i++) {
			key.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		
		return key.toString();
	}
}
