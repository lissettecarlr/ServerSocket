package com.serverSocket.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

//该类用于对每个socke对象进行操作
public class ChatSocket extends Thread {
	Socket socket;
	
	public ChatSocket(Socket s){
		this.socket = s;
	}

	//输出
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
        print("wellcome"+"\n");
        try {
        	
        	BufferedReader br = new BufferedReader(
        			new InputStreamReader(
        					socket.getInputStream(),"UTF-8"));
        	String line =null ; //用来保存接收数据
        	while((line = br.readLine())!=null) //读取一行，也就是说发送时候句末需要加换行符
        	{
        		ChatManager.getChatManager().publish(this, line+"\n");//发给其他客服端
        		System.err.println(line);
        	}
        	br.close();
        	ChatManager.getChatManager().remove(this);
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}  
          	        

	}
	
}
