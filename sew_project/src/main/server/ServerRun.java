package main.server;

import interfaces.Plugin;
import main.request.RequestClass;
import main.response.ResponseClass;

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
            Plugin plugin = null;

            if (request.isValid()) {
                    System.out.println("PlugManager init");
                    plugin = Server.plugManager.getPlugin(request);

                    if(plugin!=null) {
                        System.out.println("gets Plugin");
                        response = plugin.handle(request);
                        System.out.println("sets response");
                    } else {
                        System.out.println("URL: " + request.getUrl().getRawUrl());
                        fileName+=request.getUrl().getRawUrl();
                        response.setContent(Files.readAllBytes(Paths.get(fileName)));
                    }


                if (response == null) {
                    response = new ResponseClass();
                    response.setStatusCode(404);
                } else response.setStatusCode(200);

                response.send(socket.getOutputStream());


                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
