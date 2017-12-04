package teemp;

import java.io.IOException;

public class QuestLog {

	public static void main(String[] args) throws IOException {
		FindBoard board = new FindBoard();
		Join_AdventureGuild guild = new Join_AdventureGuild();
		Questing questing = new Questing();
		board.find();
		System.out.println("--------------------------------------");
		guild.register();
		guild.login("http://blackboard:5000/login");
		//System.out.println("--------------------------------------");
		//Es muss erst durch chooseQuest() eine Quest ausgewaehlt werden
		
		questing.chooseQuest();
		String locationQuest_1 = questing.getQuestLocation();
		String resourceQuest_1 = questing.getQuestResource();
		String hostQuest_1 = questing.findQuestHost(locationQuest_1);
		String questDeliveryDestination_1 = questing.getQuestDelivery();
		questing.visitQuestLocation(hostQuest_1, resourceQuest_1, questDeliveryDestination_1);
		System.out.println("--------------------------------------");
	}

}
