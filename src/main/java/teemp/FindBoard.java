package teemp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class FindBoard {
	private byte buffer[] = new byte[1024];
	private String ip ="";
	public void find() {
		try {
			DatagramSocket socket = new DatagramSocket(24000);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			// System.out.println("Waiting on " + socket.getLocalSocketAddress());
			System.out.println("Frage Marktschreier(" + socket.getLocalSocketAddress()+")...");
			System.out.println("Wo finde ich das Blackboard?");
			socket.receive(packet);
			String boardAdresse = new String(packet.getData());
			System.out.print("Marktschreier: Das Blackboard findet ihr hier ");
			System.out.println(boardAdresse);
			System.out.println("falls ihr dennoch Probleme haben solltet es zu finden, ");
			System.out.println("hier ist ein genauer Stadtplan: "+packet.getAddress().toString());
			ip = packet.getAddress().toString()+":5000";
			socket.close();

		} catch (Exception e) {
			System.out.println("Blackboard nicht gefunden..."); 
		}
	}
	
	public String getIp() {
		return ip;
	}
}
