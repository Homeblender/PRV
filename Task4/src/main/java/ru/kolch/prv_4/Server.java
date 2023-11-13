package ru.kolch.prv_4;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @SneakyThrows
    public void serverStart() {
        log.info("Server is running");
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);

            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1239);
        Server server = new Server(serverSocket);
        server.serverStart();
    }
}