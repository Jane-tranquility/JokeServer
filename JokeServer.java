import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Arrays;


/*
 The class JokeServer contains the main function,
 it creates a new thread for making connection with clientadmins
 and opens a server socket and make it wait to connet to clients.
 After connecting to a client, use Worker class to do the task.
 */
public class JokeServer{
	public static void main(String[] args) throws IOException{
		int q_len=6;
		int port=4545;      //the port number for connecting with clients are at port 4545
		Socket sock;
		

		Admin admin=new Admin();    //admin is a new object which is runnable
		new Thread(admin).start();  //builds a new thread using a runnable object to control the mode of the server

		ServerSocket servsock=new ServerSocket(port, q_len); //create the server waiting for the clients at port 4545.
		System.out.println("\nJokeServer: localhose, port 4545.\n");
		while(true){
			sock=servsock.accept();           //block and wait to connet to client
			new Worker(sock).start();          //use Worker class to do the actual work
		}
	}
}

/*
  The Worker class which does the actual task for clients.
  Deals with clients in a multithreaded way - can talk to multiple clients.
*/
class Worker extends Thread{
	Socket sock;
	Worker(Socket s){sock=s;}

	public void run(){
		PrintStream out=null;
		BufferedReader in=null;
		String fromClient;
		String userName;
		String[] fromClientSep;
		String statusFrom;
		String statusFromNew;
		String[] status;

		try{
			in=new BufferedReader(new InputStreamReader(sock.getInputStream()));  //in is a buffered read to read the character-based text coming into the socket
			out=new PrintStream(sock.getOutputStream()); 
			fromClient=in.readLine();
			fromClientSep=fromClient.split(",");
			userName=fromClientSep[0].substring(1);
			statusFrom=in.readLine();
			if (statusFrom.length()<=2){status=new String[0];}
			else{
				statusFromNew=statusFrom.substring(1,statusFrom.length()-1);
				status=statusFromNew.split(", ");
				System.out.println(statusFromNew);
			}

		

			if (Admin.modeControl==true){

				if (Arrays.asList(status).contains("JA")==false){
					out.println("JA "+ userName+" "+ Contents.jokes.get("JA"));
				}else if ((Arrays.asList(status).contains("JA")==true)&&(Arrays.asList(status).contains("JB")==false)){
					out.println("JB "+ userName+ " "+Contents.jokes.get("JB"));
				}else if ((Arrays.asList(status).contains("JA")==true)&&(Arrays.asList(status).contains("JB")==true)&&(Arrays.asList(status).contains("JC")==false)){
					out.println("JC "+ userName+ " "+Contents.jokes.get("JC"));
				}else if ((Arrays.asList(status).contains("JA")==true)&&(Arrays.asList(status).contains("JB")==true)&&(Arrays.asList(status).contains("JC")==true)&&(Arrays.asList(status).contains("JD")==false)){
					out.println("JD "+ userName+ " "+Contents.jokes.get("JD"));
				}else {
					out.println("JD "+ userName+ " "+Contents.jokes.get("JD"));//out.println("JD "+ userName+ " "+Contents.jokes.get("JD"));
				}
				
			}else{
				if (Arrays.asList(status).contains("PA")==false){
					out.println("PA "+ userName+" "+ Contents.proverbs.get("PA"));
				}else if ((Arrays.asList(status).contains("PA")==true)&&(Arrays.asList(status).contains("PB")==false)){
					out.println("PB "+ userName+ " "+Contents.proverbs.get("PB"));
				}else if ((Arrays.asList(status).contains("PA")==true)&&(Arrays.asList(status).contains("PB")==true)&&(Arrays.asList(status).contains("PC")==false)){
					out.println("PC "+ userName+ " "+Contents.proverbs.get("PC"));
				}else if ((Arrays.asList(status).contains("PA")==true)&&(Arrays.asList(status).contains("PB")==true)&&(Arrays.asList(status).contains("PC")==true)&&(Arrays.asList(status).contains("PD")==false)){
					out.println("PD "+ userName+ " "+Contents.proverbs.get("PD"));
				}else {
					out.println("PD "+ userName+ " "+Contents.proverbs.get("PD"));//out.println("JD "+ userName+ " "+Contents.jokes.get("JD"));
				}
				
			}
			

		}catch(IOException e){
			System.out.println(e);
		}
	} 
}

/*
  Admin class implements Runnable Interface 
  and waits to be connected with admin clients
*/
class Admin implements Runnable{
	public static boolean modeControl=true;     //set the default value of mode to be true

	public void run(){
		int q_len=6;
		int port=5050;
		Socket sock;

		try{
			ServerSocket servsockadmin=new ServerSocket(port, q_len);  
			while(true){
				sock=servsockadmin.accept();       //block and wait for admin clients' connection
				new AdminWorker(sock).start();     //control the mode of the server
			}
		}catch(IOException ioe){
			System.out.print(ioe);
		}
		
	}

}

class AdminWorker extends Thread{
	Socket sock;
	AdminWorker(Socket s){sock=s;}

	public void run(){
		PrintStream out;

		try{
			out=new PrintStream(sock.getOutputStream());
			if (Admin.modeControl==true){
				Admin.modeControl=false;
				System.out.println("The joke Server is in PROVERB mode now.");
				out.println("The joke Server is in PROVERB mode now.");
			}else {
				Admin.modeControl=true;
				System.out.println("The joke Server is in JOKE mode now.");
				out.println("The joke Server is in JOKE mode now.");
			}
			sock.close();
		}catch(IOException e){
			System.out.println("sock closing error");
		}
	}
}
class Contents{
	public static final HashMap<String, String> jokes=new HashMap<String, String>();
	public static final HashMap<String, String> proverbs=new HashMap<String, String>();

	static{
		jokes.put("JA", "I wanted to grow my own food but I couldnt get bacon seeds anywhere.");
		jokes.put("JB","How do you tell that a crab is drunk? It walks forwards.");
		jokes.put("JC","Why do cows wear bells? Their horns don’t work.");
		jokes.put("JD","What goes up and down but never moves? - The stairs!");
		proverbs.put("PA","You can't wake a person who is pretending to be asleep.");
		proverbs.put("PB","The talkative bird can not make a nest.");
		proverbs.put("PC","Rumors are carried by haters, spread by fools, and accepted by idiots.");
		proverbs.put("PD","It is not work that kills, but worry.");
	}
}

