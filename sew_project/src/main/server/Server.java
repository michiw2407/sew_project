package main.server;

import at.technikum.Interfaces.Request;
import at.technikum.Interfaces.Response;
import main.request.RequestClass;
import main.response.ResponseClass;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public void start(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        while (!socket.isClosed()) {
            ServerRun serverRun = new ServerRun(socket.accept());
            Thread t = new Thread(serverRun);
            t.start();
        }
    }

}
