import java.io.*;
import java.util.*;

public class Driver {
	static ArrayList<Person> peopleArray = new ArrayList<Person>();
	
	//reads	the file line by line, assigning the first name as a new person
	//and the next name(s) as this new person's friends. Finally, adds
	//each person (object) to peopleArray.
	public static void readFile(String file) throws IOException {
		
		Scanner filereader = new Scanner(new File(file));
		
		while(filereader.hasNextLine()){
			
			String line = filereader.nextLine();
			String[] names = line.split("\t");
			Person person = new Person(names[0]); //assigning name of person
			for(int i=1; i<names.length; i++){
				person.setNewFriend(names[i]); //adding subsequent names to this person's friendlist
			}
			peopleArray.add(person);
		}
		System.out.println("File Reading Complete.");
		filereader.close();
	}
	
	//given the name of a person, returns their person object
	public static Person getPerson(String name){
		for(int i=0; i<peopleArray.size(); i++){
			if(peopleArray.get(i).getName().equals(name)){
				return peopleArray.get(i);
			}
		}
		return null;
	}
	
	//prompts the user to input a string and returns this string
	public static String promptInput(){
		Scanner scan = new Scanner(System.in);
		return scan.nextLine();
	}
	
	//Given two person objects A and B, finds out how to get from A to B via
	//the object's friendlist. Explores all the friends of A, then for each 
	//friend of person A, explores his/her unexplored friends, and so on, 
	//or until person B is found
	public static void search(Person A, Person B){
		boolean found = false;
		ArrayList<Person> ExploreList = new ArrayList<Person>();
		for(int i=0; i<peopleArray.size(); i++) //resetting all people's explore and predecessors
		{
			peopleArray.get(i).setExplored(false);
			peopleArray.get(i).setPredecessor(null);
		}
		ExploreList.add(A); //we want to start with person A
		while(!found && !ExploreList.isEmpty()) {
			
			Person X = ExploreList.remove(0); 
			if(!X.getName().equals(B.getName())) { //if person X isnt person B,
				//set X as explored and add each person who is friends with X 
				//to explorelist (if they have not yet been explored)
				
				X.setExplored(true);
				
				for(int j=0; j<X.getFriendsList().size(); j++) {
					
					Person Y = getPerson(X.getFriendsList().get(j));
					if(Y.getExplored() == false)
					{
						ExploreList.add(Y);
						Y.setExplored(true); //modifying explored to true so that this person doesnt get explored later on
						Y.setPredecessor(X.getName()); //setting predecessor to the person who caused Y to get added to the explorelist
					}
				}
			}
			else{
				found = true;
			}
			
		}
	}
	
	//traces the lineage of friendship of Person B (through predecessor variable)
	//returns the list of the name of every person involved in this lineage.
	public static ArrayList<String> chainFriends(Person B){ 
		String current = B.getName();
		ArrayList<String> ChainList = new ArrayList<String>();
		
		while(current != null)
		{	
			ChainList.add(current);
			current = getPerson(current).getPredecessor();
		}
		return ChainList;
	}
	
	//provided the chain list result from the chainFriends method, return the degrees of
	//separation. This is needed as one of the people may not have any friends and must
	//therefore be assigned a degree of sep. equivalent to the number of people +1. 
	public static int DegSeparation(ArrayList<String> chainList){
		if(chainList.size() == 1){
			return peopleArray.size() +1;
		}
		else{
			return chainList.size() -1;
		}
	}

	//given an arr of person objects, finds the average degree of separation between each person.
	public static double getAverage(ArrayList<Person> arr){
		double total = 0;
		
		for(int i=0; i<arr.size()-1; i++) {
			for(int j=i+1; j<arr.size(); j++){
				search(arr.get(i), arr.get(j));
				int separation = DegSeparation(chainFriends(arr.get(j))); 
				total += separation; //add the deg of sep for this pair to the total
			}
		}
		return (2*total)/(arr.size()*(arr.size()-1)); //here we must multiply total by two as we have
		// 												only computed the sum for unique pairs.
		//												e.g. we not only need the Mary-Michael degree of sep.
		//												but also should include the Michael-Mary
	}
	
	public static void main(String[] args) throws IOException {
		String outfile = "output.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
		String repeat = "";
		
		System.out.println("** Six Degrees of Separation **");
		System.out.println("Reading file from command line");
		readFile(args[0]); //cmd line
		do{	
			System.out.println("Enter the name of the first person: ");
			String name1 = promptInput(); //get name1
			while(getPerson(name1)==null){
				System.out.println("Error: " +name1+ " is not in the list\nEnter the name of the first person:");
				//get name1 again
				name1 = promptInput();
			}
				
			System.out.println("Enter the name of the second person: ");
			String name2 = promptInput(); //get name2
			while(getPerson(name2)==null){
				System.out.println("Error: " +name2+ " is not in the list\nEnter the name of the second person:");
				//get name2 again
				name2 = promptInput();
			}
			
			search(getPerson(name1),getPerson(name2)); //perform search on this pair (name1, name2)
			
			if(chainFriends(getPerson(name2)).size() == 1){ //if chainlist for person with name2 has a size of 1
				//then we know he/she is not friends with anybody, hence is not connected
				System.out.println("The two people are not connected.");
			}
			else{ 
				System.out.println("Degrees of Separation: " + (DegSeparation(chainFriends(getPerson(name2))))); //print the degrees of separation
				String output = "";
				for(int i=chainFriends(getPerson(name2)).size(); i>0; i--){
					output += chainFriends(getPerson(name2)).get(i-1) + " ";
				}
				System.out.println("Relation: ");
				System.out.println(output); //flip order of list and print out the relations with a space between each one
				out.write(output+"\n"); //write the above to the output file too
			}
			System.out.println("Want to try another query? (y/n)");
			repeat = promptInput();
		}while(repeat.equals("y"));
		//we are done establishing the connection between pairs, now calculate average degree of sep.
		System.out.println("Computing Average Degree of Separation...");
		System.out.println(getAverage(peopleArray));
		out.write("Average Degree of Separation: " + getAverage(peopleArray));
		out.close(); //save and close output file
	}

}
