package com.github.learn.java.net.serversocket;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultServer implements Server {

    final String host;
    final int port;
    ServerSocket serverSocket;
    private volatile boolean exit = false;

    public DefaultServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override public void start() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(host, port));
        new Thread(() -> doInListenThread(serverSocket), "ServerSocket-listen").start();
    }

    protected void doInListenThread(ServerSocket serverSocket) {
        try {
            while (!isStopped()) {
                Socket socket = serverSocket.accept();
                this.handleSocket(socket);
            }
            log.info("server : close ServerSocket");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleSocket(final Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String str;
            while ((str = in.readLine()) != null) {
                log.info("s <-- c({}): {}", socket.getRemoteSocketAddress(), str);
                if (str.equals("close")) {
                    log.info("s <-> c({}): close socket to the client", socket.getRemoteSocketAddress());
                    socket.close();
                    break;
                }
                out.println(str);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void stop() throws IOException {
        exit = true;
        this.serverSocket.close();
    }

    protected boolean isStopped() {
        return exit;
    }
}
