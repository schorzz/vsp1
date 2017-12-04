package teemp;

//https://beders.github.io/Resty/apidocs/index.html JAVADOC EXTREME
import static us.monoid.web.Resty.content;

import java.io.IOException;
import java.util.Base64;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

public class Join_AdventureGuild {
	// requests
	private String identity = "http://blackboard:5000/whoami";
	private String register = "http://blackboard:5000/users";

	public void register() throws IOException {
		JSONObject user = new JSONObject();
		try {
			user.put("name", "Harold the Knight");
			user.put("password", "securePassword");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new Resty().json(register, content(user));
	}

	public String login(String path) throws IOException {
		String login_token = "not found";
		String username = "Harold the Knight";
		String pw = "securePassword";
		String encoded = Base64.getEncoder().encodeToString((username + ":" + pw).getBytes());
		//"Authorization: Basic RHJfQXJyb2dhbnRvOndka3NuYndpZGJuZg=="
		Resty r = new Resty();
		r.withHeader("Authorization", "Basic " + encoded);
		String login_ausgabe = r.text(path).toString();
		System.out.println(login_ausgabe);
		
		JSONObject login_ausgabe_json;
		try {
			login_ausgabe_json = new JSONObject(login_ausgabe);
			login_token = login_ausgabe_json.getString("token").toString();
			//System.out.println(login_token);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		
		
		whoAmI(r);
		return login_token;
	}

	private void whoAmI(Resty r) throws IOException {
		String whoAmI = r.text(identity).toString();
		System.out.println(whoAmI);
	}
}
