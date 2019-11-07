package main.server;

import main.request.RequestClass;
import main.response.ResponseClass;

import java.io.IOException;
import java.net.Socket;

public class ServerRun implements Runnable {

    private Socket socket;

    ServerRun(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            RequestClass request = new RequestClass(socket.getInputStream());
            ResponseClass response = new ResponseClass();

            if (request.isValid()) {
                response.setStatusCode(200);
                response.setContent("HelloWorld");

            } else {
                response.setStatusCode(400);
                response.setContent("Bad Request");
                socket.close();
            }

            response.send(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
