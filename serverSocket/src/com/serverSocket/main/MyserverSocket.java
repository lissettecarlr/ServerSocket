package com.serverSocket.main;

//2016/3/5  lissettecarlr
//服务器程序,连接服务器后会接收到welcome
//这是一个socket服务器程序的入口，如果从一个客服端得到消息，它将转发给其他客服端

public class MyserverSocket {

	public static void main(String[] args) {
		 new ServerListener().start(); //建立实时监听客服端
	}

}
