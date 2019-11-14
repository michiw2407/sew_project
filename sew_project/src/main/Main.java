package main;
import main.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Server server;
        String inputFile = System.getProperty("user.dir") + "/src/resources/files/image.png";
        int port = 8080;

        try {
            System.out.println("Starting server at port " + port);
            server = new Server();
            server.start(port, inputFile);
            System.out.println("Server started at port  " + port);
        } catch (IOException e) {
            System.out.println("Unexpected error " + e.getMessage());
        }

    }
}

