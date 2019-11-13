package main.server;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    public void start(int port, String fileName) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        while (!socket.isClosed()) {
            ServerRun serverRun = new ServerRun(socket.accept(), fileName);
            Thread t = new Thread(serverRun);
            t.start();
        }
    }
}
