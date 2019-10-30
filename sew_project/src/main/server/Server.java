package main.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /*
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String input;

    public Server() {
    }

    public Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.socket = serverSocket.accept();
    }

    public void start() throws IOException {
        setIn(socket.getInputStream());
        setOut(socket.getOutputStream());

        setInput(readMessageFirstLine());
        writeMessage();

        stop();
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }

    public String readMessageFirstLine() throws IOException {
        if (in != null) {

            System.out.println(in.readLine());

            final String raw = in.readLine();

            if (raw == null) {
                throw new IllegalArgumentException("Input must not be null!");
            }

            return raw.split(" ")[1]; // i.e. GET /index.html?x=1&y=2 HTTP1.1
        }

        throw new IllegalArgumentException("BufferedReader must not be null!");
    }

    public void writeMessage() throws IOException {
        if (out != null) {
            if (input.equals("index.html")) {
                out.println("hello client");
            } else {
                out.println("unrecognised greeting");
            }

            out.flush();
        } else {
            throw new IllegalArgumentException("PrintWriter must not be null!");
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = new PrintWriter(new OutputStreamWriter(out));
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    } */


    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket client = wait(serverSocket);
        String msg = readMsg(client);
        System.out.println(msg);
        writeMsg(client, msg);
    }

    Socket wait(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept(); // blockiert, bis sich ein Client angemeldet hat
        return socket;
    }

    String readMsg(java.net.Socket socket) throws IOException {
        BufferedReader bufferedReader =
                new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
        char[] buffer = new char[200];
        int amountSigns = bufferedReader.read(buffer, 0, 200);
        String msg = new String(buffer, 0, amountSigns);
        return msg;
    }

    void writeMsg(Socket socket, String msg) throws IOException {
        PrintWriter printWriter =
                new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()));
        printWriter.print(msg);
        printWriter.flush();
    }
}
