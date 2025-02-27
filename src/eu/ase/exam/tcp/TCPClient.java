package eu.ase.exam.tcp;

import eu.ase.exam.Animal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPClient {

	public static void main(String[] args) {
		Socket clientSocket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		ObjectInputStream ois = null;
		
		try {
			clientSocket = new Socket("localhost",3333);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(),true);
			ois = new ObjectInputStream(clientSocket.getInputStream());
			
			String request = "GETLIST";
			out.println(request);
			
			if(request.compareTo("GETLIST")==0) {
				//List<eu.ase.exam.Animal> listTCP = (List<eu.ase.exam.Animal>) ois.readObject();
				
				List<Animal> listTCP = new ArrayList<>();
				Object obj = null;
				while((obj = ois.readObject())!=null) {
					if(obj instanceof EndOfFile) {
						break;
					}
					
					Animal a = (Animal) obj;
					listTCP.add(a);
				}
				
				for(Animal a : listTCP) {
					System.out.println("Object received from server: " + a);
				}
			}else if(request.compareTo("GETJSON")==0) {
				//List<eu.ase.exam.Animal> listTCP = (List<eu.ase.exam.Animal>) ois.readObject();
				
				List<Animal> listTCP = new ArrayList<>();
				Object obj = null;
				while((obj = ois.readObject())!=null) {
					if(obj instanceof EndOfFile) {
						break;
					}
					
					Animal a = (Animal) obj;
					listTCP.add(a);
				}
				
				for(Animal a : listTCP) {
					System.out.println("JSON Object received from server: "+ a.toJSON());
				}
			}else if(request.compareTo("GETDB")==0) {
				String response = null;
				while((response = in.readLine())!=null) {
					if(response.compareTo("END")==0) {
						break;
					}
					System.out.println(response);
				}
			}
			else {
				String response = in.readLine();
				System.out.println("Message received from server: " + response);
			}
			
			in.close();
			out.close();
			ois.close();
			clientSocket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

}
