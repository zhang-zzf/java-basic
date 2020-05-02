package com.github.learn.java.net.serversocket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.BDDAssertions.then;

public class DefaultServerTest {
    final String host = "127.0.0.1";
    final int port = 9999;
    final String msg = "Hello, World.";
    final String close = "close";

    Server server;
    Client client;

    @BeforeEach
    void beforeEachMethod() throws IOException {
    }

    @AfterEach
    void afterEachMethod() throws IOException {
    }

    @Test
    void testSendAndReceive() throws IOException {
        server = new DefaultServer(host, port);
        server.start();
        client = new Client(host, port);

        then(client.sendAndWait(msg)).isEqualTo(msg);
        // 触发 服务端关闭 socket
        then(client.sendAndWait(close)).isNull();
        server.stop();
    }

    @Test
    void testTwoClient() throws IOException {
        server = new DefaultServer(host, port);
        server.start();
        // first client
        client = new Client(host, port);
        then(client.sendAndWait(msg)).isEqualTo(msg);
        // the first client must be closed before server can serve another client
        client.close();

        // second client
        Client client2 = new Client(host, port);
        client2.sendAndWait(msg);
        // client 关闭 socket
        client2.close();

        server.stop();
    }

}