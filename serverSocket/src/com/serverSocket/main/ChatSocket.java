package com.serverSocket.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;


public class ChatSocket extends Thread {
	Socket socket;
	
	public ChatSocket(Socket s){
		this.socket = s;
	}
	@Override
	public void run() {

        try {
        	while(true){
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
            pw.close();  
            os.close();  
            inputStream.close();  
            socket.close();  
        	}
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
          	        

	}
	
}
