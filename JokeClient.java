import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class JokeClient {
	public static void main(String[] args){
		String serverName;
		String userName;
		ArrayList<String> status=new ArrayList<String>();
		ArrayList<String> cycleJoke=new ArrayList<String>();
		ArrayList<String> cycleProverb=new ArrayList<String>();
		ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();

		if (args.length<1){
			serverName="localhost";
		}else{serverName=args[0];}
			
		System.out.println("Jokeclient: using server: "+serverName+", port 4545");
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please enter your user name: ");

		try{
			userName=in.readLine();
			String name;
			do {
				name=in.readLine();
				if (name.indexOf("quit")<0){
					result=getInfo(serverName, name, userName,status,cycleJoke,cycleProverb);
					status=result.get(0);
					cycleJoke=result.get(1);
					cycleProverb=result.get(2);
				}
			}while(name.indexOf("quit")<0);
			System.out.println("Cancelled by user request.");
		}catch(IOException e){
			e.printStackTrace(); 
		}
		
	}

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
			toServer.println(passInfo);
			toServer.println(status);
			toServer.println(cycleJoke);
			toServer.println(cycleProverb);
			toServer.flush();
			textFromServer=fromServer.readLine();
			if (textFromServer!=null){
                System.out.println(textFromServer);      //if the content is not empty, print out the message on the console
		    }
		    newItem=textFromServer.split(" ")[0];
		    status.add(newItem);
		    cycleFrom=fromServer.readLine();
		    if (cycleFrom.length()<=2){cycleIn=new String[0];}
			else{
				cycleIn=cycleFrom.substring(1,cycleFrom.length()-1).split(", ");
			}
			for (String item: cycleIn){
				cycleArray.add(item);
			}
			mode=fromServer.readLine();
			if (mode.equals("true")){
				result.add(status);
				result.add(cycleArray);
				result.add(cycleProverb);
			}else{
				result.add(status);
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