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


public class JokeClientAdmin{
	public static void main (String[] args){
		String serverName;
		if (args.length<1){                     //if didn't give arguments for the ip address of server, use localhost
			serverName="localhost";
		}else{
			serverName=args[0];                  //otherwise use the argument
		}

		System.out.println("JokeClientAdmin: using server: "+serverName+", port 5050");

		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		try{
			String name;
			do{
				name=in.readLine();
				if (name.indexOf("quit")<0){
					getControlInfo(serverName, name);              //if requsted, do as the getControlInfo function tells to
				}
			}while(name.indexOf("quit")<0);
			System.out.println("Cancelled by user request.");
		}catch(IOException e){
			e.printStackTrace(); 
		}
	}

	/*
	getControlInfo function connects with the server
	and switch the server mode
	*/
	public static void getControlInfo(String serverName, String name){
		Socket sock;
		PrintStream toServer;
		BufferedReader fromServer;
		String textFromServer;

		try{
			sock=new Socket(serverName, 5050);       //connects with the server
			toServer=new PrintStream(sock.getOutputStream());
			fromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer.println(name);
			toServer.flush();
			textFromServer=fromServer.readLine();     
			if (textFromServer!=null){
                System.out.println(textFromServer);      //if the content is not empty, print out the message on the console
		    }

			sock.close();
		}catch(IOException e){
			System.out.print("Connerction error!");
			e.printStackTrace();
		}

	}
}