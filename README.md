##1.概述
###1.1最终效果
如果android设备连接了网络，则通过android的客服端程序能够使这几台设备间相互交流，相当于一个聊天室
###1.2数据流程
手机通过socket向服务器发送数据，服务器接收到后分别发送给除接收端外的其他接入对象。呃。。。就这么简单

*接下来单独讲下服务器和客服端的实现，具体代码主要参考至极客学院*
##2.服务器
###2.1结构
这个程序主要是扔到服务器上去转发接收到的数据。我仅仅单纯的写了一个socket方式的消息接收和转发。总计4个java文件。
MyserverSocket：主线程
ServerListener：子线程A，用于阻塞等待客服端的连接，并且对接		 入的客服端再开启一个线程B。
ChatSocket：子线程B的具体要做的事，接收数据，发送数据
ChatManager：多客服端子线程的管理，里面封装了3个方法，添加一个对象，删除一个对象，将信息广播。
###2.2流程
程序入口为MyserverSocket

```
package com.serverSocket.main;

//2016/3/5  lissettecarlr
//服务器程序,连接服务器后会接收到welcome
//这是一个socket服务器程序的入口，如果从一个客服端得到消息，它将转发给其他客服端

public class MyserverSocket {

	public static void main(String[] args) {
		 new ServerListener().start(); //建立实时监听客服端
	}

}
```
由于socket监听会阻塞线程，所以对其单独建立一个线程监听。
ServerListener

```
package com.serverSocket.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
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
			ServerSocket serverSocket = new ServerSocket(23456); //端口
			while(true){
				//每当有一个客服端连接就有一个socket
				Socket socket =  serverSocket.accept();//阻塞
//				JOptionPane.showMessageDialog(null, "有客服端连接到本地的23457端口");
				System.out.println("有客服端链接-------");
				ChatSocket cs = new ChatSocket(socket);//新线程
				cs.start();         
 	            ChatManager.getChatManager().add(cs);		
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
```
每当有一个新的客服端接入，就建立一个socket 对象，并且传入一个新的线程，再将他加入到一个对象表中。之后进入socket对象线程。

```
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
```
这次封装的发送方法
下面是run方法，用于将接收到的数据转发出去
![这里写图片描述](http://img.blog.csdn.net/20160305172859251)
至此流程就结束了
还有一个ChatManager是一个工具，他用来保存管理各个接入对象

```
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
```
以上是服务器的所有代码，至于如何放到服务器上去运行，看我之前的帖子吧。

##3.android端
*这里不会贴出所有代码，太长啦，就讲讲关键的部分吧。程序虽然有各种BUG，但是主要通信功能没问题，这里只是一个测试程序，之后会修改的*
###3.1布局
![这里写图片描述](http://img.blog.csdn.net/20160305175942031)
由3个button、2个EditText、2个textView的简单布局
###3.2事件
我仅仅写了一个activity。
protected void onCreate(Bundle savedInstanceState)中主要写三个按键的点击事件。
这是连接的点击事件（仅仅是点击事件里面的内容）：
```
public void connect() {
AsyncReadTask asyncReadTask = new AsyncReadTask();  asyncReadTask.execute(ip.getText().toString().trim());
    }


    private final class AsyncReadTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {  //由线程池开启新线程执行该函数中的内容
            try {
                socket = new Socket(params[0], 23456);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                publishProgress("与服务器连接成功");
                flag = true;
            } catch (UnknownHostException e1) {
                Toast.makeText(getApplicationContext(), "无法建立链接", Toast.LENGTH_SHORT).show();
                flag =false ;
            } catch (IOException e1) {
                Toast.makeText(getApplicationContext(), "无法建立链接", Toast.LENGTH_SHORT).show();
                flag =false;
            }
            try {
                String line;
                while ((line = reader.readLine())!= null) {
                    publishProgress(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {  //此处UI更新

        }

        @Override
        protected void onProgressUpdate(String... values) {    //数据由doInBackground返回过来
            if (values[0].equals("与服务器连接成功")) {
            }
            text.append("获得消息："+values[0]+"\n");
            super.onProgressUpdate(values);
        }
    }
```
之后是断开的点击事件：

```
       close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socket.close();
                    flag = false;
                    statue.setText("连接断开");
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "关闭失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
```
最后是发送按键：

```

    public void send() {
        try {
            text.append("发送消息："+editText.getText().toString()+"\n");
            writer.write(editText.getText().toString()+"\n");
            writer.flush();
            editText.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
###3.3流程
在点击连接后，会读取IP对其建立socket对象，这里是另起的一个线程进行的，建立完对象后，在建立输入输出流，并且等待接收数据。当点击发送按钮时触发send事件，不会影响接收线程。

##4.测试
###4.1第一步
首先将java程序运行至服务器。
我的系统是cenos于是就在文件目录下输入  java -jar  XX.jar，这是前台运行，后台运行则输入nohup  java -jar  XX.jar & 后 在输入exit退出。
（如果发现端口被占用，则首先输入netstat -tln  查看端口占用情况，之后lsof -i :8083  看是那个程序占用了端口，在使用kill -9 进程ID杀死他）
###4.2第二步
你可以使用两个android手机，笔者只有一个，于是就在开启一个虚拟机就好了。
首先输入你服务器的IP地址，点击连接（这里有一个BUG，第一次点击断开会闪退  - -||）
![这里写图片描述](http://img.blog.csdn.net/20160305180146297)
这时消息显示区域会受到消息：
![这里写图片描述](http://img.blog.csdn.net/20160305180326312)
另外一台设备重复一遍后，随便使用一台设备在发送区域输入文字，点击发送，你将会在另外一台设备看到该文字。我这里将自己发送的前标为"发送消息"，从服务器得到数据我标为了"获得数据"
##5.相关下载
[服务器jar包和apk包下载](http://download.csdn.net/detail/lissettecarlr/9453526)
eclipse服务器程序下载
android程序下载