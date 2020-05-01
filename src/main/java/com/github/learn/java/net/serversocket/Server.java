package com.github.learn.java.net.serversocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/26
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(args[0], Integer.parseInt(args[1])));
        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String str;
            while ((str = in.readLine()) != null) {
                out.println(str);
                if (str.equals("exit")) {
                    socket.close();
                }
            }
        }
    }

}
