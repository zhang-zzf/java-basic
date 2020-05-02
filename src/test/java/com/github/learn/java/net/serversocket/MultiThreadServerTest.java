package com.github.learn.java.net.serversocket;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MultiThreadServerTest {

    final String host = "127.0.0.1";
    final int port = 9999;
    final String msg = "Hello, World.";
    final String close = "close";

    @Test void testTwoClient() throws IOException {
        Server server = new MultiThreadServer(host, port, 2);
        server.start();

        Client c1 = new Client(host, port);
        c1.sendAndWait(msg);

        Client c2 = new Client(host, port);
        c2.sendAndWait(msg);

        c1.close();
        c2.close();
        server.stop();
    }

}