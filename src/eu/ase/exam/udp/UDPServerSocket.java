package eu.ase.exam.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

//Subject+2 points <=> Mark 9/10:
//Create public class eu.ase.exam.udp.UDPServerSocket which implements a proprietary communication protocol and implements AutoCloseable (override specific method)
//It has 2 fields: socket - DatagramSocket and bindPort - int
//It contains the following constructor methods:
//a. public eu.ase.exam.udp.UDPServerSocket() throws SocketException - init bindPort on 60001
//b. public int getBindPort() - returns the bindPord field
//c. public void processRequest() throws IOException - receive UDP packets and process them (without infinite loop) with the following rules:
//		- if the request contains W? , then the reply UDP packet contains as pay-load "UDPS".
//		- if the request contains BYE , then the reply UDP packet contains as pay-load "BYE ACK" and close the resources (e.g. socket)
//		- if the request contains any other pay-load , then the reply UDP packet contains as pay-load "ACK".
public class UDPServerSocket implements AutoCloseable{
	private final DatagramSocket socket;
	private final int bindPort;
	
	public UDPServerSocket() throws SocketException {
		this.bindPort = 60001;
		this.socket = new DatagramSocket(60001);
	}
	
	
	public int getBindPort() {
		return bindPort;
	}
	
	public void processRequest() throws IOException {
		byte[] byteRecv = new byte[256];
		DatagramPacket packetRecv = new DatagramPacket(byteRecv,byteRecv.length);
		this.socket.receive(packetRecv);
		
		System.out.println("Client " + packetRecv.getAddress() + ":" + packetRecv.getPort() + " sent to server: "
				+ new String(packetRecv.getData()));
		String received = new String(packetRecv.getData());
		String response = null;
		if(received.contains("W?")) {
			response = "UDPS";
		}else if(received.contains("BYE")) {
			response = "BYE ACK";
		}else {
			response = "ACK";
		}
		
		byte[] byteResp = response.getBytes();
		InetAddress address = packetRecv.getAddress();
		int port = packetRecv.getPort();
		
		DatagramPacket packetResp = new DatagramPacket(byteResp,byteResp.length,address,port);
		this.socket.send(packetResp);
		
		if(response == "BYE ACK") {
			try {
				this.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void close() throws Exception {
		this.socket.close();
	}
	
	
	public static void main(String[] args) {
		try(UDPServerSocket server = new UDPServerSocket()){
			while(true) {
				server.processRequest();
			}
		} catch (SocketException e) {
			//e.printStackTrace();
			System.out.println("Server is closed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

}
