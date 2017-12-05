import dto.BroadcastInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import us.monoid.json.JSONException;
import us.monoid.web.Resty;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static us.monoid.web.Resty.content;

public class HelloProf {


    private static int port = 24000;

    private Integer blackboardPort=null;
    private InetAddress broadcastAdresse=null;

    private Console sysConsole = System.console();

    private static User user = null;
    private static BroadcastInfo bci = null;

    private QuestRepro questRepro;
    private String locationPath= "/map/";

    private Quest todoQuest=null;
    private Integer taskNr=null;
    private Task todoTask=null;
    private static boolean udpListen=true;



    public static void main(String[] args){
        System.out.println("Hallo");



        if(args.length >0){
            //String[] split = a
            for(int i=0;i<args.length;i++){
                //System.out.println("Argument nr "+i+": "+args[i]);

                if(args[i].equals("-debug")){
                    Log.setDebug(true);
                }else if(args[i].equals("-noudp")){
                    udpListen=false;
                }
                /*else if(args[i].equals("-debug")){

                }*/

            }
        }
        //HelloProf game = new HelloProf();
/*
        byte[] packetArray = new byte[1024];

            try {
                DatagramSocket socket = new DatagramSocket(24000);
                DatagramPacket packet = new DatagramPacket(packetArray,packetArray.length);
                System.out.println("Receiving..");
                socket.receive(packet);
                String satz = new String(packet.getData(),0,packet.getLength());
                System.out.println("Empfangen:\n"+satz);
                System.out.println("got data!");

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
        UDPPackageFetcher upf = new UDPPackageFetcher(24000);



        if(!udpListen) {
//            bci = upf.fetch();
            try {
                bci=new BroadcastInfo();
                bci.setAdresse(InetAddress.getByName("172.19.0.7"));
                bci.setBlackboard_port(5000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }else{
            bci = upf.fetch();
        }


        //System.out.println("url von broadcastinfo: \""+info.getURL()+"\"");
        Log.logDebug("url von broadcastinfo: \""+bci.getURL()+"\"");

/*
        if(info == null){
            System.out.println("kein Broadcast erhalten. Stoppe hier");
        }else {
            //System.out.println(info);
            //ab hier bradcast mit dem neuen port erhalten
            System.out.println("Hi, Blackboard wurde gefunden.");
            //boolean userRegistriert = game.registerUser("Schorzz","schorzz123");

        }
        */

        Console c = System.console();

        String input = "";

        HelloProf game = new HelloProf();

        Log.log("-help for commands");

        while(!input.equals("quit") || !input.equals("exit")){


           /* Log.log("\n\nWas wuerdest du gern tun?");
            Log.log("a)\tneuen User erstellen");
            Log.log("b)\t mit Daten einloggen");
            if(user!=null && user.isLoggedIn()){

            }


            Log.log("quit) beenden\n");

            */
            input = c.readLine(">");


            if(input.startsWith("createUser")){
                game.createUser();
            }else if(input.startsWith("login")){
                game.loginUser();
            }else if(input.startsWith("help") || input.startsWith("-help")){
                game.helpDialog();
            }else if(input.startsWith("getQuests")){
                game.getQuests();
            }else if(input.startsWith("getTasks") || input.startsWith("getTask")){
                //die ID die dann folgt extrahieren und dann die
                String[]tmp = input.split(" ");

                if(tmp.length>1){
                    //game.getLocation(tmp[1]);
                    game.getTaskFromQuest(new Integer(tmp[1]));
                }

            }else if(input.startsWith("getLocation")){

                String[]tmp = input.split(" ");

                if(tmp.length>1){
                    game.getLocation(tmp[1]);
                }

            }else if(input.startsWith("doQuest")){
                String[]tmp = input.split(" ");

                if(tmp.length>1){
                    //game.getLocation(tmp[1]);
                    Integer integer =new Integer(tmp[1]);

                    if(integer!=null) {

                        game.doQuest(integer);
                    }else {
                        Log.log("please insert a valid Integer");
                    }
                }
            }
            else if(input.equals("quit") || input.equals("exit")){
                return;
//                break;
            }




        }
        game.loginUser();

        //User neuerUser = new User(bci);
        //boolean neuerUserErfolgreich = neuerUser.createUser("test_user98","test_user98");
        //boolean loginUserErfolgreich = neuerUser.login("test_user98","test_user98");

/*

        if(!loginUserErfolgreich){
            System.out.println("nicht erfolgreich eingeloggt!");



        }else {
            System.out.println("erfolgreich eingeloggt");


        }
        */

    }

    /**
     * holt sich die Location - also das komplette Object vom Blackboard
     * @param location
     */

    public void getLocation(String location){
        try {




            URL url = new URL("" + bci.getURL() + locationPath+location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");



            if (conn.getResponseCode() != 200) {
                //throw new RuntimeException("Failed : HTTP error code : "
                //      + conn.getResponseCode());
                //System.out.println("Fehlercode: "+conn.getResponseCode()+"\nFehlertext: "+conn.getResponseMessage());
                Log.logDebug("HelloProf:getLocation() ->"+"Fehlercode: "+conn.getResponseCode()+"\nFehlertext: "+conn.getResponseMessage());


            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String obj="";
            String output="";

            while ((output = br.readLine()) != null){
                obj+=output;
            }
            Log.log(Log.LINE);
            Log.logDebug("HelloProf:getLocation() -> parsen der JSONObjekte");

            JSONObject jasonObj = new JSONObject(obj);

            Log.log("jasonObj.get(\"object\"): "+jasonObj.get("object"));
            Log.log("");
            JSONObject jasonObject = new JSONObject(jasonObj.get("object"));

            Log.log("jsonObj: "+jasonObj);
            Log.log("jsonobject: "+jasonObject.toString());

            //String host = jasonObject.get("host").toString();
            //String name = jasonObject.get("name").toString();

            String host = jasonObj.get("host").toString();
            String name = jasonObj.get("name").toString();

            Log.log("Info from \""+location+"\":");
            Log.log("host: "+host);
            Log.log("name: "+name);

            //Log.log("Objekt:\n"+obj);





            //json parsen und dann
            // host
            // name
            //rausparsen

            Log.log(Log.LINE);

            conn.disconnect();
        } catch (ProtocolException e) {
            //e.printStackTrace();
            Log.log("Diesen Ort gibt es wohl nicht");
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            Log.log("Diesen Ort gibt es wohl nicht");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * erstellt einen user wenn es ihn noch nicht gibt
     */
    public void createUser(){
        //sammelt Datenum einen neuen User anzulegen
        Console c = System.console();
        Log.logDebug(Log.LINE);
        Log.logDebug("HelloProf:createUser()--> methode betreten");
        boolean erneut=true;

        String input = "";
        String username=null;
        String passwd=null;

        while (erneut) {
            erneut=false;
            Log.logLine();
            Log.log("einen neuen User anlegen.");
            username = c.readLine("Gib einen Namen ein: ");
            passwd = c.readLine("Gib ein Passwort ein: ");

            user=new User(bci);
            boolean erfolg = user.createUser(username,passwd);

            if(!erfolg){
                input=c.readLine("erstellen nicht erfolgreich! (user schon vorhanden?!) Neuer Versuch? y/n?");
                if(input.equals("y") || input.equals("Y")|| input.equals("yes")){
                    erneut=true;
                }
            }

        }

    }

    /**
     * hier muss der user seine Daten eingeben um sich einzuloggen
     */
    public void loginUser(){
        //loggt einen user ein
        Console c = System.console();

        Log.logDebug(Log.LINE);
        Log.logDebug("HelloProf:loginuser()--> methode betreten");

        boolean erneut=true;

        String input = "";
        String username=null;
        String passwd=null;

        while (erneut) {
            erneut=false;
            Log.logLine();
            Log.log("loggt einen User ein");
            username = c.readLine("Gib einen Namen ein: ");
            passwd = c.readLine("Gib ein Passwort ein: ");

            user=new User(bci);
            boolean erfolg = user.login(username,passwd);

            if(!erfolg){
                input=c.readLine("einloggen nicht erfolgreich! (Daten korrekt?) Neuer Versuch? y/n?");
                if(input.equals("y") || input.equals("Y")|| input.equals("yes")){
                    erneut=true;
                }
            }else {
                Log.log("Login erfolgreich!");
            }

        }

    }

    /**
     * Der HilfeDialog der die kommandos anzeigt
     */
    private void helpDialog(){
        Log.logDebug("HelloProf:helpDialog()--> betreten. Zeige Dialog..");

        Log.log("list of possible commands:");
        Log.log("\"createUser\" - you can create a new user");
        Log.log("\"login\" - you can login with an existing user");
        Log.log("\"help\" - shows this dialog");
        Log.log("\"getQuests\" - gets a questlist");
        Log.log("\"getTask <id>\" - gets a tasklist for a quest. Add a ID as parameter");
        Log.log("\"getLocation <id>\" - gets the location of a Task. <id> is the TaskId");
        Log.log("\"doQuest <id>\" - Does the quest. <id> is the QuestId");
        Log.log("\"quit\" - exits this program");

    }
    public void getQuests(){
        Log.logDebug("HalloProf:getQuests --> enter");

        Log.log(Log.LINE);
        Log.log("Hole alle quests:");

        questRepro= new QuestRepro(bci);
        questRepro.getQuests();



    }

    public List<Task> getTaskFromQuest(Integer id){

        Log.logDebug("HelloProf:getTaskFromQuest()-->entering");
        Log.log(Log.LINE);

        if(questRepro==null){
            getQuests();
        }

        /*
        Quest quest = questRepro.getQuest(id);

        if(quest != null){

            List<Task> tasks = quest.getTaskliste();




        }
            return new ArrayList<>();

    */

        return questRepro.getTasks(id);
    }

    public void doQuest(Integer id){
        Log.logDebug("QuestRepro:doQuest-->parsen des Jsonobjektes");

        //eine Quest Auswählen.


        if(questRepro==null){
            questRepro=new QuestRepro(bci);
            questRepro.getQuests();
        }
        Log.logDebug("HelloProf:doQuest()-->questRepro questlistSize: "+questRepro.questlistSize());
        todoQuest=questRepro.getQuest(id);

        if(todoQuest==null){
            Log.log(""+id+" is no valid ID or no Quest with this id is not aviable..");
            return;
        }

        if(taskNr==null){
            taskNr=0;
        }


        todoTask=todoQuest.getTaskliste().get(taskNr);

        if(todoTask==null){
            Log.logDebug("HelloProf:doQuest --> Quest hat keine Tasks !IMPOSSIBLE?!");
            Log.log("fatal internal error! Quest got no Tasks");
            return;
        }

        //hier jetzt zu "location" gehen und den host holen.
        for(Task task : todoQuest.getTaskliste()){
            Log.log("Task: "+todoTask);

            Resty rest = new Resty();
            try {

                //location ist z.b. MAP
                String anUri=bci.getURL()+task.getLocation();
                Log.logDebug("HelloProf:doQuest()--> abfrage der Location: \""+anUri+"\" um den Host");
                String map = rest.text(anUri).toString();
                //System.out.println(map);
                us.monoid.json.JSONObject mapAsJson = new us.monoid.json.JSONObject(map);
                String host = "http://" + mapAsJson.getJSONObject("object").getString("host").toString();
                // System.out.println("Du findest den Ort \""+name+"\" hier: "+host);
                Log.logDebug("HelloProf:doQuest()-->Task Host: "+host);
//                System.out.println(host);
//                return host;
                task.setHost(host);

                if(user.isLoggedIn()){
                    //dann mit host+ressource(/wounded) zu Host und Quest machen..
                    Log.logDebug("HelloProf:doQuest(): wenn user eingeloggt dann ");
//                    String hostToVisit = "http://"+task.getHost()+task.getRessource();
                    String hostToVisit = task.getHost()+task.getRessource();
                    Resty resty = new Resty();
                    resty.withHeader("Authorization",user.getAuthorisationToken());

                    String taskText=resty.text(hostToVisit,content("")).toString();

                    us.monoid.json.JSONObject questSolved_json;
                    try {
                        Log.logDebug("HelloProf:doQuest(): parse token aus antwort");
                        questSolved_json = new us.monoid.json.JSONObject(taskText);
                        task.setToken(questSolved_json.getString("token").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else{
                    Log.log("doQuest(): User ist nicht eingeloggt");
                }



            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }





            Log.logDebug("HelloProf:doQuest()--> token ist: "+task.getToken());
        }




        String tokenJSON = "{\"tokens\":";

        boolean komma=false;

        for(Task e : todoQuest.getTaskliste()){
            if(komma){tokenJSON=tokenJSON+",";}else{komma=true;}
            tokenJSON=tokenJSON+"{\"/blackboard/tasks/"+e.getId()+"\":\""+e.getToken()+"\"}";
        }

            tokenJSON=tokenJSON+"}";

        Log.logDebug("\nHelloProf:doQuest()--> tokenJSON:\n"+tokenJSON+"\n");


        Resty resty = new Resty();

        resty.withHeader("Authorization", user.getAuthorisationToken());
        try {
            Log.logDebug("HelloProf:doQuest()--> liefere Token nach \""+bci.getURL()+"/blackboard/quests/"+todoQuest.getId()+"/deliveries\" aus.");
           Log.log("\n");
            Log.log(resty.text(bci.getURL()+"/blackboard/quests/"+todoQuest.getId()+"/deliveries",content(tokenJSON)).toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Hier jetzt die tokenliste erstellen und dann abgeben
        //task-uri: /blackboard/tasks/{id}
        //ab hier alle tokens abgeben









    }

/*
    public boolean login(){
        String auswahl=sysConsole.readLine("Willst du dich einloggen(1) oder einen neuen User anlegen(2): ");

        boolean erfolg=false;

        if (auswahl.equals("1")){
            erfolg=einloggen();
        }else if (auswahl.equals("2")){
            erfolg=neuerUser();
        }



        return false;
    }
    private boolean neuerUser(){
        System.out.println("\n--------------------neuer User--------------------");
        String name = sysConsole.readLine("Gib bitte einen Namen ein: ");
        String passwd = sysConsole.readLine("Gib bitte ein Passwort ein: ");

    try{

        //es wid versucht eine verbindung herzustellen und ein user anzulegen
        String urlString="http:/"+broadcastAdresse+":"+blackboardPort+"/users";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        String json = "{\"name\":\""+name+"\",\"password\":\""+passwd+"\"}";

        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes());
        os.flush();

    } catch (ProtocolException e) {
        e.printStackTrace();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }


        return false;
    }

*/
}
