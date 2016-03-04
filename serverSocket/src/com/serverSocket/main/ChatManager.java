package com.serverSocket.main;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Vector;

//该类用于多个socket之间通信管理
public class ChatManager {
	
	private ChatManager(){}
	private static final ChatManager cm = new ChatManager();
	public static ChatManager getChatManager() {
		return cm;
	}
	
	Vector<ChatSocket> vector = new Vector<ChatSocket>();
	
	//为集合添加一个新的socket对象
	public void add(ChatSocket cs) {
		vector.add(cs);
	}
	
	public void remove(ChatSocket cs) {
		vector.remove(cs);
	}
	
	//首先将自己传递进来，在传入一个发送的信息
	public void publish(ChatSocket cs,String out) {
		for (int i = 0; i < vector.size(); i++) {
			ChatSocket csChatSocket = vector.get(i); //获取循环中第I个对象
			if (!cs.equals(csChatSocket)) { //如果不是当前
				csChatSocket.print(out);
			}
		}
	}
}
