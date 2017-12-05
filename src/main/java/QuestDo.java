import dto.BroadcastInfo;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

public class QuestDo {
    private String pfad ="/blackboard/quests";
    private BroadcastInfo bci = null;


    public QuestDo(BroadcastInfo bci) {
        this.bci = bci;
    }

    public void doQuest(){
        Resty r = new Resty();

        String boardAdresse = bci.getURL();

        try {
            // Questdaten sammeln
            //-------------------------------------------------------------------------
            String chosenQuest = r.text(quests + "/" + questAusgabe).toString();
            System.out.println(chosenQuest);//questdaten ausgeben
            JSONObject chosenQuestJson = new JSONObject(chosenQuest);

            //Quest-Attribute sammeln
            String questName = chosenQuestJson.getJSONObject("object").getString("name").toString();
            String questDelivery = boardAdresse + chosenQuestJson.getJSONObject("object").getJSONObject("_links").getString("deliveries").toString();
            String questTask = boardAdresse + chosenQuestJson.getJSONObject("object").getString("tasks").toString();
            questTask = questTask.replace("[", "");
            questTask = questTask.replace("]", "");
            questTask = questTask.replace("\"", "");
            questTask = questTask.replace("\"", "");
            //Task-Attribute der Quest ermitteln
            String questTaskAusgabe = r.text(questTask).toString();
            JSONObject questTaskAusgabeJson = new JSONObject(questTaskAusgabe);
            String questLocation = boardAdresse + questTaskAusgabeJson.getJSONObject("object").getString("location").toString();
            String questResource = questTaskAusgabeJson.getJSONObject("object").getString("resource").toString();
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
}
