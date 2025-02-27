package eu.ase.exam.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

//Subject+2 points <=> Mark 9/10:
//Create public class eu.ase.exam.udp.UDPClientSocket which implements a proprietary communication protocol and implements AutoCloseable (override specific method)
//It has 2 fields: socket - DatagramSocket
//It contains the following constructor methods:
//a. public eu.ase.exam.udp.UDPClientSocket() throws SocketException - init socket WITHOUT bind port
//b. public String sendAndReceiveMsg(String msg, String ipAddr, int port) throws UnknownHostException 
//- send UDP packets and process them (without infinite loop) with the following rules:
//		- when the sent request contains W? , then the response UDP packet from server contains as pay-load "UDPS".
//		- when the sent request contains BYE , then the response UDP packet from server contains as pay-load "BYE ACK" 
//		- when the sent request contains any other pay-load , then the response UDP packet contains as pay-load "ACK".
public class UDPClientSocket implements AutoCloseable{
	private final DatagramSocket socket;
	
	public UDPClientSocket() throws SocketException {
		this.socket = new DatagramSocket();
	}
	
	public String sendAndReceiveMsg(String msg, String ipAddr, int port) throws UnknownHostException {
		byte[] bufSent = msg.getBytes();
		InetAddress address = InetAddress.getByName(ipAddr);
		DatagramPacket packetSent = new DatagramPacket(bufSent,bufSent.length,address,port);
		try {
			this.socket.send(packetSent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] bufRecv = new byte[256];
		DatagramPacket packetRecv = new DatagramPacket(bufRecv,bufRecv.length);
		try {
			this.socket.receive(packetRecv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String received = new String(packetRecv.getData());
		System.out.println("Received from server: " + received);
		return received;
	}
	
	@Override
	public void close() throws Exception {
		this.socket.close();
		
	}
	
	public static void main(String[] args) {
		try(UDPClientSocket client = new UDPClientSocket()){
			//String received = client.sendAndReceiveMsg("W?", "localhost", 60001);
			@SuppressWarnings("unused")
			String received = client.sendAndReceiveMsg("BYE", "localhost", 60001);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

}
