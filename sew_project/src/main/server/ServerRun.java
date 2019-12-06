package main.server;

import interfaces.Plugin;
import main.request.RequestClass;
import main.response.ResponseClass;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

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
            ResponseClass response = null;

            if (request.isValid()) {
                System.out.println("PlugManager init");
                Plugin plugin = Server.plugManager.getPlugin();
                System.out.println(plugin);
                System.out.println(request);
                System.out.println("gets Plugin");
                response = plugin.handle(request);
                System.out.println("sets response");

                if (response == null) {
                    response = new ResponseClass();
                    response.setStatusCode(404);
                } else response.setStatusCode(200);

                response.send(socket.getOutputStream());
                socket.close();
            }
        } catch (IOException | ParserConfigurationException | SAXException | SQLException e) {
            e.printStackTrace();
        }
    }
}
