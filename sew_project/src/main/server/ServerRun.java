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

            if (!request.isValid()) {
                socket.close();
            }

            ResponseClass response = new ResponseClass();
            response.setStatusCode(200);
            response.setContent("Test");
            response.send(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
