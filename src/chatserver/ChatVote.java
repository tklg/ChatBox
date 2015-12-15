package chatserver;

public class ChatVote {
	
	private int votesYes = 0;
	private int votesNo = 0;
	private int totalVotes = 0;
	private boolean passed = false;
	private int requiredToPass = 0;

	public ChatVote(int starter, String type, String desc) {
		requiredToPass = 5;
	}
	public ChatVote(int starter, String type, int target) {
		requiredToPass = 5;
	}
	public void addVote(int option) {
		if (option == 1) {
			votesYes++;
		} else if (option == 0) {
			votesNo++;
		}
		totalVotes++;
	}
	public int totalVotes() {
		return totalVotes;
	}

}
