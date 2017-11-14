import com.google.gson.Gson;
import dto.BroadcastInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPPackageFetcher {


    private int port;

    public UDPPackageFetcher(int port) {

        this.port=port;

    }

    public BroadcastInfo fetch() {

        Gson obj = new Gson();


        byte[] packetArray = new byte[1024];

        try {
            DatagramSocket socket = new DatagramSocket(24000);
            DatagramPacket packet = new DatagramPacket(packetArray, packetArray.length);
            // System.out.println("Receiving..");
            socket.receive(packet);
            String satz = new String(packet.getData(), 0, packet.getLength());



            BroadcastInfo info = obj.fromJson(satz, BroadcastInfo.class);
            info.setAdresse(packet.getAddress());

            return info;


            // System.out.println("Empfangen:\n"+satz);
            // System.out.println("got data!");

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    return null;
    }
}
