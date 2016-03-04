package com.serverSocket.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;


//该类用于等待客服端连接
public class ServerListener extends Thread {
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(23456); //端口
			while(true){
				//每当有一个客服端连接就有一个socket
				Socket socket =  serverSocket.accept();//阻塞
//				JOptionPane.showMessageDialog(null, "有客服端连接到本地的23457端口");	
				ChatSocket cs = new ChatSocket(socket);
				cs.start();         
 	               			
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
