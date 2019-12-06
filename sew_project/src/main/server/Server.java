package main.server;

import main.pluginManager.PluginManagerClass;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    static PluginManagerClass plugManager = new PluginManagerClass();

    public void start(int port, String fileName) throws IOException {
        ServerSocket socket = new ServerSocket(port);

        while (!socket.isClosed()) {
            ServerRun serverRun = new ServerRun(socket.accept(), fileName);
            Thread t = new Thread(serverRun);
            t.start();
        }
    }
}
