import java.io.*;
import java.net.*;


public class JokeClientAdmin{
	public static void main (String[] args){
		String serverName;
		if (args.length<1){
			serverName="localhost";
		}else{
			serverName=args[0];
		}

		System.out.println("JokeClientAdmin: using server: "+serverName+", port 5050");

		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		try{
			String name;
			do{
				name=in.readLine();
				if (name.indexOf("quit")<0){
					getControlInfo(serverName, name);
				}
			}while(name.indexOf("quit")<0);
			System.out.println("Cancelled by user request.");
		}catch(IOException e){
			e.printStackTrace(); 
		}
	}


	public static void getControlInfo(String serverName, String name){
		Socket sock;
		PrintStream toServer;
		BufferedReader fromServer;
		String textFromServer;

		try{
			sock=new Socket(serverName, 5050);
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