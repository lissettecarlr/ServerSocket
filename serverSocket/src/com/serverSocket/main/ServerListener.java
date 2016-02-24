package com.serverSocket.main;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

//该类用于等待客服端连接
public class ServerListener extends Thread {
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(23457); //端口
			while(true){
				//每当有一个客服端连接就有一个socket
				Socket socket =  serverSocket.accept();//阻塞
				JOptionPane.showMessageDialog(null, "有客服端连接到本地的23457端口");	

				 // 从Socket当中得到InputStream对象
		        InputStream inputStream = socket.getInputStream();  
		        byte buffer[] = new byte[1024 * 4];  
		        int temp = 0;  
		        // 从InputStream当中读取客户端所发送的数据  
		        while ((temp = inputStream.read(buffer)) != -1) {  
		            System.out.println(new String(buffer, 0, temp));  
		        }  
		        serverSocket.close();  
		        
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
