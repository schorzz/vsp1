package teemp;

import static us.monoid.web.Resty.content;

import java.io.IOException;
import java.util.Scanner;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

public class Questing {
	//blackboardadresse
	private String boardAdresse = "http://blackboard:5000";
	// requests
	private String quests = boardAdresse+"/blackboard/quests";
	 //Questattribute inizialisieren
	private String questName = "not found..";
	private String questDelivery = "not found..";
	private String questTask = "not found..";
	private String questLocation = "not found..";
	private String questResource = "not found..";


	public void chooseQuest() throws IOException {
		Resty r = new Resty();
		String questList = r.text(quests).toString();
		System.out.println(questList);
		System.out.println("An welche Aufgabe wollt ihr euch wagen Abenteurer?");
		System.out.println("Questnummer:");

		// Userinput lese
		String questAusgabe = "";
		Scanner scanner = new Scanner(System.in);
		questAusgabe = scanner.nextLine();
		scanner.close();

		try {
			// Questdaten sammeln
			//-------------------------------------------------------------------------
			String chosenQuest = r.text(quests + "/" + questAusgabe).toString();
			System.out.println(chosenQuest);//questdaten ausgeben
			JSONObject chosenQuestJson = new JSONObject(chosenQuest);

			//Quest-Attribute sammeln
			questName = chosenQuestJson.getJSONObject("object").getString("name").toString();
			questDelivery = boardAdresse + chosenQuestJson.getJSONObject("object").getJSONObject("_links").getString("deliveries").toString();
			questTask = boardAdresse + chosenQuestJson.getJSONObject("object").getString("tasks").toString();
			questTask = questTask.replace("[", "");
			questTask = questTask.replace("]", "");
			questTask = questTask.replace("\"", "");
			questTask = questTask.replace("\"", "");
			//Task-Attribute der Quest ermitteln
			String questTaskAusgabe = r.text(questTask).toString();
			JSONObject questTaskAusgabeJson = new JSONObject(questTaskAusgabe);
			questLocation = boardAdresse + questTaskAusgabeJson.getJSONObject("object").getString("location").toString();
			questResource = questTaskAusgabeJson.getJSONObject("object").getString("resource").toString();
			//Ausgabe
			System.out.println("'"+questName+"'"+" ist also die Quest eurer Wahl...");
			System.out.println("Ihren Kontakt nach vollbrachter Arbeit finden Sie hier:");
			System.out.println(questDelivery);
			System.out.println(questTask);
			System.out.println("Den Weg zur Quest findet ihr hier: "+questLocation);
			System.out.println("Die Ressource der Quest lautet: "+questResource);
			//-------------------------------------------------------------------------
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		

	}

	public String findQuestHost(String loc) throws IOException {
		// FindBoard board = new FindBoard();
		// String boardIp = board.getIp();
		// String loc = boardIp+":5000/map/"+name;
		Resty rest = new Resty();
		try {
			String map = rest.text(loc).toString();
			System.out.println(map);
			JSONObject mapAsJson = new JSONObject(map);
			String host = "http://" + mapAsJson.getJSONObject("object").getString("host").toString();
			// System.out.println("Du findest den Ort \""+name+"\" hier: "+host);
			System.out.println(host);
			return host;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Quest-Host konnte nicht gefunden werden";

	}

	public String getQuestName() {
		return questName;
	}

	public String getQuestDelivery() {
		return questDelivery;
	}

	public String getQuestLocation() {
		return questLocation;
	}

	public String getQuestResource() {
		return questResource;
	}

	public TextResource visitQuestLocation(String host, String resource, String questDeliveryDestination) {
		// Authorization:Token
		String questSolved_token = "Quest is not solved yet!";
		TextResource quest_reward = null; // = "You dont deserve a reward.. Go, solve the quest!";
		Join_AdventureGuild guild = new Join_AdventureGuild();
		try {
			String hostToVisit = host + resource;
			System.out.println(hostToVisit);

			String token = guild.login("http://blackboard:5000/login");

			Resty rest = new Resty();
			rest.withHeader("Authorization", "Token " + token);
			String visitHost_ausgabe = rest.text(hostToVisit).toString();
			System.out.println(visitHost_ausgabe);
			//muss noch Costumized werden, nur fuer quest 1 zZ
			String questDialog = rest.text(hostToVisit, content("Yes! LET ME IN!")).toString();
			System.out.println(questDialog);

			JSONObject questSolved_json;
			try {
				questSolved_json = new JSONObject(questDialog);
				questSolved_token = questSolved_json.getString("token").toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONObject delivery = null;
			try {
				delivery = new JSONObject().put("tokens",
						new JSONObject().put("/blackboard/tasks/2", questSolved_token));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			quest_reward = rest.text(questDeliveryDestination, content(delivery.toString()));
			// post to http://blackboard:5000/blackboard/quests/1/deliveries ->
			// {\"tokens\":{\"<task_uri>\":\"<token>\"}}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(quest_reward);
		return quest_reward;
	}

}
