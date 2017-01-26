/*--------------------------------------------------------

1. Jing Li / Jan 22, 2017

2. Java version used: Version 8 Update 60 (build 1.8.0_60-b27) 

3. Precise command-line compilation examples / instructions:
> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java

4. Precise examples / instructions to run this program:
In separate shell windows:
> java JokeServer
> java JokeClient
> java JokeClientAdmin

5. List of files needed for running the program.
 a. JokeServer.java
 b. JokeClient.java
 c. JokeClientAdmin.java

5. Notes: I saved all the status of clients on the clients side

----------------------------------------------------------*/

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JokeClient {
	public static void main(String[] args){
		String serverName;
		String userName;
		ArrayList<String> status=new ArrayList<String>();                          //status keeps track of which jokes or proverbs the client has already seen
		ArrayList<String> cycleJoke=new ArrayList<String>();                       //keeps track of one cycle of jokes, records which jokes haven't been told in the cycle
		ArrayList<String> cycleProverb=new ArrayList<String>();                    //keeps track of one cycle of proverbs, records which proverbs haven't been told in the cycle
		ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();    //result is the value retruned from getInfo function, which updates the status and cycle information for the specific client

		if (args.length<1){
			serverName="localhost";            //if didn't give arguments for the ip address of server, use localhost
		}else{serverName=args[0];}             //otherwise use the argument
			
		System.out.println("Jokeclient: using server: "+serverName+", port 4545");
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter your user name: ");                      //ask for the user name

		try{
			userName=in.readLine();                     //get the user name from user 
			String name;
			do {
				name=in.readLine();
				if (name.indexOf("quit")<0){     //if hasn't quitted
					result=getInfo(serverName, name, userName,status,cycleJoke,cycleProverb);      //use getInfo function to update the status of this client after connecting with the server 
					status=result.get(0);                //the first value in the ArrayList represents the status which jokes and proverbs this client has already seen
					cycleJoke=result.get(1);             //the second value in the ArrayList represents that the jokes haven't been told in this cycle for this client
					cycleProverb=result.get(2);          //the third value in the ArrayList represents that the proverbs haven't been told in this cycle for this client
				}                                        //two of these three values would get updated
			}while(name.indexOf("quit")<0);
			System.out.println("Cancelled by user request.");
		}catch(IOException e){
			e.printStackTrace(); 
		}
		
	}

	/*
	the function getInfo takes in some information like server name, user name, the status, the joke cycle and proverb cycle
	connecting with the server, get a joke or probverb depending on the server mode, and
	update the status of the client after the connection
	*/
	static ArrayList<ArrayList<String>> getInfo(String serverName, String name, String userName, ArrayList<String> status, ArrayList<String> cycleJoke, ArrayList<String> cycleProverb){
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		String cycleFrom;
		ArrayList<String> passInfo=new ArrayList<String>();
		passInfo.add(userName);
		passInfo.add(name);
		String newItem;
		String[] cycleIn;
		ArrayList<String> cycleArray= new ArrayList<String>();
		ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
		String mode;

		try{
			sock=new Socket(serverName, 4545);
			fromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer=new PrintStream(sock.getOutputStream());
			toServer.println(passInfo);                     //send the user name to the server
			toServer.println(status);                       //send the jokes and proverbs that the client has already seen to the server
			toServer.println(cycleJoke);                    //send the joke cycle to the server
			toServer.println(cycleProverb);                 //send the proverb cycle to the server
			toServer.flush();
			textFromServer=fromServer.readLine();
			if (textFromServer!=null){
                System.out.println(textFromServer);      //if the content is not empty, print out the message on the console
		    }
		    newItem=textFromServer.split(" ")[0];        //get which joke or proverb the client sees this time
		    status.add(newItem);                         //add the joke/proverb the client sees this time to the status
		    cycleFrom=fromServer.readLine();
		    if (cycleFrom.length()<=2){cycleIn=new String[0];}
			else{
				cycleIn=cycleFrom.substring(1,cycleFrom.length()-1).split(", ");
			}
			for (String item: cycleIn){
				cycleArray.add(item);                    //get the joke cycle or proverb cycle depends on the server mode
			}
			mode=fromServer.readLine();                  //get the server mode
			if (mode.equals("true")){
				result.add(status);                      //if the server mode is on joke, then update status and joke cycle
				result.add(cycleArray);
				result.add(cycleProverb);
			}else{
				result.add(status);                      //if the server mode is on proverb, update the status and proverb cycle
				result.add(cycleJoke);
				result.add(cycleArray);
			}
		    sock.close();
		}catch(IOException e){
			System.out.println("Socket Error!");            //IOException, prints an error message
			e.printStackTrace();
		}
		return result;              
	}

	
}