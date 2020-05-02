package com.github.learn.java.net.serversocket;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class Client implements Closeable {

    private final String host;
    private final int port;
    private final Socket socket;

    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(host, port);
    }

    public String sendAndWait(String msg) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(msg);
        out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String resp = in.readLine();
        log.info("c <- s: {}", resp);
        return resp;
    }

    @Override public void close() throws IOException {
        socket.close();
    }
}
