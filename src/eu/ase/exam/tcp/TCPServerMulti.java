package eu.ase.exam.tcp;

import eu.ase.exam.IO.Utils;
import eu.ase.exam.db.Database;
import eu.ase.exam.Animal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

class EndOfFile implements Serializable{
	private static final long serialVersionUID = 1L;
	
}

class ThreadClient extends Thread{
	private final Socket clientSocket;
	private final List<Animal> list;
	public ThreadClient(Socket clientSocket, List<Animal> list) {
		super();
		this.clientSocket = clientSocket;
		this.list = list;
	}
	@Override
	public void run() {
		System.out.println("Client " + clientSocket.getInetAddress() + "/" + clientSocket.getPort() +
				" arrived in server port " + clientSocket.getLocalPort());
		BufferedReader in = null;
		PrintWriter out = null;
		ObjectOutputStream oos = null;
		String inputLine = null;
		
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(),true);
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			
			while((inputLine = in.readLine())!=null) {
				System.out.println("Request from client: " + inputLine);
				
				if(inputLine.compareTo("GETLIST")==0) {
					/*oos.writeObject(list);
					oos.flush();*/
					
					for(Animal a : list) {
						oos.writeObject(a);
						oos.flush();
					}
					oos.writeObject(new EndOfFile());
					oos.flush();
				}else if(inputLine.compareTo("GETJSON")==0) {
					/*oos.writeObject(list);
					oos.flush();*/
					
					for(Animal a : list) {
						oos.writeObject(a);
						oos.flush();
					}
					oos.writeObject(new EndOfFile());
					oos.flush();
				}else if(inputLine.compareTo("GETDB")==0) {
					Database.setConnection();
					Database.createTable();
					Database.insertData(list);
					String select = Database.selectData();
					Database.closeConnection();
					
					out.println(select);
					out.println("END");
				}
				else {
					String response = "EXITING SERVER...";
					out.println(response);
				}
			}
			in.close();
			out.close();
			oos.close();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
    }
	
	
}

public class TCPServerMulti {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		boolean listening = true;
		
		Animal a1 = new Animal("Dodo",3, 14);
		Animal a2 = new Animal("Max",4,9);
		Animal a3 = new Animal("CDaFA",5,84);
		Utils.addAnimal(a1);
		Utils.addAnimal(a2);
		Utils.addAnimal(a3);
		Utils.filterList("a");
		Utils.writeBinary("animals.dat");
		
		try {
			serverSocket = new ServerSocket(3333);
			System.out.println("Server listens in port " + serverSocket.getLocalPort());
			while(listening) {
				clientSocket = serverSocket.accept();
				ThreadClient tc = new ThreadClient(clientSocket, Utils.readBinary("animals.dat"));
				tc.start();
			}
			serverSocket.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
