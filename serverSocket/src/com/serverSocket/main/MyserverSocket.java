package com.serverSocket.main;

//ctrl + shitf + o  清除不必要的包
//通过使用客服端连接该服务器 控制台将显示客服端发来的数据
public class MyserverSocket {

	public static void main(String[] args) {
		 new ServerListener().start(); //建立实时监听客服端
	}

}
