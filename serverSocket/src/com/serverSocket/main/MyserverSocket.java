package com.serverSocket.main;

//ctrl + shitf + o  清除不必要的包
//服务器程序,连接服务器后会接收到welcome，在没对他发送一组数据后，将返回OK，并且在控制台显示
public class MyserverSocket {

	public static void main(String[] args) {
		 new ServerListener().start(); //建立实时监听客服端
	}

}
