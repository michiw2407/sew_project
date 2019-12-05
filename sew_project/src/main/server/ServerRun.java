package main.server;

import interfaces.Plugin;
import main.pluginManager.PluginManagerClass;
import main.request.RequestClass;
import main.response.ResponseClass;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;

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
//                response.setContent(Files.readAllBytes(Paths.get(fileName)));

                PluginManagerClass plugManager = new PluginManagerClass();
                System.out.println("PlugManager init");
                Plugin plugin = plugManager.getPlugin();
                System.out.println(plugin);
                System.out.println(request);
                System.out.println("gets Plugin");
                response = (ResponseClass) plugin.handle(request);
                System.out.println("sets response");
            } else {
                response.setStatusCode(400);
                response.setContent("Bad Request");
                socket.close();
            }

            response.send(socket.getOutputStream());
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
