import java.util.ArrayList;

public class Person {

	private String name;
	private String predecessor;
	private boolean explored;
	private ArrayList<String> FriendsList = new ArrayList<String>();
	
	//constructor for person class
	public Person(String n){
		name = n;
	}
	
	//toString method for person class returns person's name
	public String toString() {
		return this.name;
	}
	
	//sets a person's explored variable to A
	public void setExplored(boolean A){
		explored = A;
	}
	
	//sets a person's predecessor variable to A
	public void setPredecessor(String A){
		predecessor = A;
	}
	
	//adds a name to a person's friendlist
	public void setNewFriend(String A){
		FriendsList.add(A);
	}
	
	//gets a person's name
	public String getName() {
		return name;
	}
	
	//gets a person's explored variable
	public boolean getExplored() {
		return explored;
	}
	
	//gets a person's friendslist
	public ArrayList<String> getFriendsList(){
		return FriendsList;
	}
	
	//gets a person's predecessor variable
	public String getPredecessor(){
		return predecessor;
	}
}
