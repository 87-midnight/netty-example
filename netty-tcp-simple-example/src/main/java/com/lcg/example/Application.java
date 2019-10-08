package com.lcg.example;

import com.lcg.example.client.TcpClient;
import com.lcg.example.server.TcpServer;

public class Application {

    public static void main(String...args)throws Exception{
        TcpServer server = new TcpServer(5687);
        server.startServer();
        new Thread(()->{
            try {
                while (true){
                    Thread.sleep(2000);
                    server.sendToAll("你好，客户端");
                }
            }catch (Exception var){
                var.printStackTrace();
            }

        }).start();
        TcpClient client1 = new TcpClient(5687,"127.0.0.1");
        TcpClient client2 = new TcpClient(5687,"127.0.0.1");
        client1.startClient();
        client2.startClient();
        new Thread(()->{
            try {
                while (true){
                    Thread.sleep(3000);
                    client1.sendMsg("你好，服户端");
                    client2.sendMsg("你好，服户端");
                }
            }catch (Exception var){
                var.printStackTrace();
            }

        }).start();
    }

}
