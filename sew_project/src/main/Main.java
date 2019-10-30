package main;


import main.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Server server;
        int port = 8080;

        try {
            System.out.println("Starting server at port " + port);
            server = new Server();
            server.start(port);
            System.out.println("Server started at port  " + port);
        } catch (IOException e) {
            System.out.println("Unexpected error " + e.getMessage());
        }

    }
}

