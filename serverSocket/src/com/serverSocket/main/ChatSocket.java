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

	public void print(String out)
	{
        //获得输出流
        OutputStream os;
		try {
			os = socket.getOutputStream();
	        PrintWriter pw=new PrintWriter(os);      
            pw.write(out);  
            pw.flush(); 
            
		} catch (IOException e) {
			ChatManager.getChatManager().remove(this);; 
			e.printStackTrace();
		}  

	}
	
	@Override
	public void run() {
        print("wellcome");
        try {
        	
        	BufferedReader br = new BufferedReader(
        			new InputStreamReader(
        					socket.getInputStream(),"UTF-8"));
        	String line =null ; //用来保存接收数据
        	while((line = br.readLine())!=null) //读取一行，也就是说发送时候句末需要加换行符
        	{
        		ChatManager.getChatManager().publish(this, line);//发给其他客服端
        	}
        	br.close();
        	ChatManager.getChatManager().remove(this);
//        	while(true){
        	// 获得输入流
//			InputStream inputStream = socket.getInputStream();
            //读取用户信息
//            byte buffer[] = new byte[1024 * 4];  
//	        int temp = 0;  
	        // 没接收到一段数据将其显示在控制台，并且返回OK
//	        while ((temp = inputStream.read(buffer)) != -1) {  
//	            System.out.println(new String(buffer, 0, temp));
//	            pw.write("你发送了："+new String(buffer, 0, temp));  
//	            pw.flush(); 
	        	
//	        }   	   

//            inputStream.close();  
//            socket.close();  
//        	}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}  
          	        

	}
	
}
