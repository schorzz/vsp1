import dto.BroadcastInfo;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class HelloProf {


    private static int port = 24000;

    private Integer blackboardPort=null;
    private InetAddress broadcastAdresse=null;

    private Console sysConsole = System.console();

    private static User user = null;
    private static BroadcastInfo bci = null;

    private QuestRepro questRepro;
    private String locationPath= "/map/";



    public static void main(String[] args){
        System.out.println("Hallo");



        if(args.length >0){
            //String[] split = a
            for(int i=0;i<args.length;i++){
                //System.out.println("Argument nr "+i+": "+args[i]);

                if(args[i].equals("-debug")){
                    Log.setDebug(true);
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

        bci = upf.fetch();

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
            }else if(input.startsWith("getTasks")){
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

            }else if(input.equals("quit") || input.equals("exit")){
                break;
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
                Log.logDebug("User:login() ->"+"Fehlercode: "+conn.getResponseCode()+"\nFehlertext: "+conn.getResponseMessage());


            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String obj="";
            String output="";

            while ((output = br.readLine()) != null){
                obj+=output;
            }
            Log.log(Log.LINE);
            Log.log("Objekt:\n"+obj);
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
    private void helpDialog(){
        Log.logDebug("HelloProf:helpDialog()--> betreten. Zeige Dialog..");

        Log.log("list of possible commands:");
        Log.log("\"createUser\" - you can create a new user");
        Log.log("\"login\" - you can login with an existing user");
        Log.log("\"help\" - shows this dialog");
        Log.log("\"getQuests\" - gets a questlist");
        Log.log("\"getTask\" - gets a questlist. add a ID as parameter");
        Log.log("\"getLocation\" - gets the location");
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