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
			ServerSocket serverSocket = new ServerSocket(23457); //端口
			while(true){
				//每当有一个客服端连接就有一个socket
				Socket socket =  serverSocket.accept();//阻塞
				JOptionPane.showMessageDialog(null, "有客服端连接到本地的23457端口");	

				 // 获得输入流
		        InputStream inputStream = socket.getInputStream();  
		          	        
		        //获得输出流
		        OutputStream os=socket.getOutputStream();  
	            PrintWriter pw=new PrintWriter(os);  
	            
	            //进入时输入显示welcome
	            String reply="welcome";  
	            pw.write(reply);  
	            pw.flush(); 
	            
	            //读取用户信息
	            byte buffer[] = new byte[1024 * 4];  
		        int temp = 0;  
		        // 没接收到一段数据将其显示在控制台，并且返回OK
		        while ((temp = inputStream.read(buffer)) != -1) {  
		            System.out.println(new String(buffer, 0, temp));
		            pw.write("OK!");  
		            pw.flush(); 
		        }   
	   
	            //5.关闭资源  
	            pw.close();  
	            os.close();  
//	            br.close();  
	            inputStream.close();  
	            socket.close();  
	            serverSocket.close();  
	               
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
