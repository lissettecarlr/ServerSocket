##1.使用eclipse编写一个简单的服务器程序

####MyserverSocket.java
```
package com.serverSocket.main;
//服务器程序,连接服务器后会接收到welcome，在没对他发送一组数据后，将返回OK，并且在控制台显示
public class MyserverSocket {

	public static void main(String[] args) {
		 new ServerListener().start(); //建立实时监听客服端
	}

}

```
####ServerListener.java

```
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
//				JOptionPane.showMessageDialog(null, "有客服端连接到本地的23457端口");	

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

```
*这里由于accept方法将阻塞线程，我于是新建立了一个线程来监听端口*
[工程下载](http://download.csdn.net/detail/lissettecarlr/9442300)

##2.将程序导出为jar包
点击eclipse 左上角 File - > Export，选择java栏里面的jar 
![这里写图片描述](http://img.blog.csdn.net/20160224191946465)

next，选择你需要打包的工程，选择放置位置后再next
![这里写图片描述](http://img.blog.csdn.net/20160224192137075)

最后选择程序的入口
![这里写图片描述](http://img.blog.csdn.net/20160224192213138)


##3.将包上传到服务器
这里可以直接在服务器里面使用代码上传，也可以使用winscp软件上传
我这里使用了后者，因为方面嘛
####建立连接
![这里写图片描述](http://img.blog.csdn.net/20160224192623468)

####拖拽放入文件
![这里写图片描述](http://img.blog.csdn.net/20160224192714843)

##4.运行
笔者使用的是cenos系统，首先测试是否安装java，如果没有可以输入

```
yum -y list java*  //查看java包
yum -y install java-1.7.0-openjdk* //安装java包
```
之后可以使用运行代码

```
java -jar xxxx.jar 
```


