package com.github.learn.java.net.serversocket;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/2
 */
@Slf4j
public class MultiThreadServer extends DefaultServer {

    final int CPU_NUMBER = Runtime.getRuntime().availableProcessors();
    private final String IO_THREAD_PREFIX = "ServerSocket-io-";
    private final AtomicInteger THREAD_COUNT = new AtomicInteger();
    private final ExecutorService executorService;

    public MultiThreadServer(String host, int port, int threads) {
        super(host, port);
        int threadsToUse = CPU_NUMBER;
        if (threads > 0) {
            threadsToUse = threads;
        }
        executorService = new ThreadPoolExecutor(threadsToUse,
            threadsToUse * 2, 60, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(2),
            r -> new Thread(r, IO_THREAD_PREFIX + THREAD_COUNT.getAndIncrement()),
            new ThreadPoolExecutor.AbortPolicy());
    }

    @Override protected void doInListenThread(ServerSocket serverSocket) {
        try {
            while (!this.isStopped()) {
                final Socket socket = serverSocket.accept();
                executorService.submit(() -> this.handleSocket(socket));
            }
            log.info("server : close ServerSocket");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
