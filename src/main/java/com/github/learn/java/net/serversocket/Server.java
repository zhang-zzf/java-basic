package com.github.learn.java.net.serversocket;

import java.io.IOException;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/1
 */
public interface Server {

    /**
     * start the server
     */
    void start() throws IOException;

    /**
     * stop the server
     */
    void stop() throws IOException;
}
