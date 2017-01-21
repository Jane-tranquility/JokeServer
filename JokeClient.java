import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class JokeClient {
	public static void main(String[] args){
		String serverName;
		String userName;

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
					getInfo(serverName, name, userName);
				}
			}while(name.indexOf("quit")<0);
			System.out.println("Cancelled by user request.");
		}catch(IOException e){
			e.printStackTrace(); 
		}
		
	}

	static void getInfo(String serverName, String name, String userName){
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		ArrayList<String> passInfo=new ArrayList<String>();
		passInfo.add(userName);
		passInfo.add(name);

		try{
			sock=new Socket(serverName, 4545);
			fromServer=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			toServer=new PrintStream(sock.getOutputStream());
			toServer.println(passInfo);
			toServer.flush();
			textFromServer=fromServer.readLine();
			if (textFromServer!=null){
                System.out.println(textFromServer);      //if the content is not empty, print out the message on the console
		    }
		    sock.close();
		}catch(IOException e){
			System.out.println("Socket Error!");            //IOException, prints an error message
			e.printStackTrace();
		}
	}
}