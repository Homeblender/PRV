package ru.kolch.prv_4;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClientHandler implements Runnable {
    protected static final List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader buffReader;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            this.buffReader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            String message = buffReader.readLine();

            clientHandlers.add(this);

            log.info(clientHandlers.toString());
            broadcastMessage(message);
        } catch (IOException e) {
            closeAll(socket, buffReader, out);
        }
    }

    @Override
    public void run() {

        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = buffReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeAll(socket, buffReader, out);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.out.println(messageToSend);
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        log.info(clientHandlers.toString());
    }

    public void closeAll(Socket socket, BufferedReader buffReader,
                         PrintWriter out) {
        removeClientHandler();
        try {
            if (buffReader != null) {
                buffReader.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}