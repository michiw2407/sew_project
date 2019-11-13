package main.server;

import main.request.RequestClass;
import main.response.ResponseClass;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerRun implements Runnable {

    private Socket socket;
    private String fileName;

    ServerRun(Socket socket, String fileName) {
        this.socket = socket;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            RequestClass request = new RequestClass(socket.getInputStream());
            ResponseClass response = new ResponseClass();

            if (request.isValid()) {
                response.setStatusCode(200);
                response.setContent(new String(Files.readAllBytes(Paths.get(fileName))));

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
