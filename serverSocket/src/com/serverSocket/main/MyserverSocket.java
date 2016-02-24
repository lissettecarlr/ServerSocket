package com.serverSocket.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;


public class MyserverSocket {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(2345); //端口
			Socket socket =  serverSocket.accept();//阻塞
			JOptionPane.showMessageDialog(null, "有客服端连接到本地的12345端口");
			//如果被赋值了表示建立了连接
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

}
