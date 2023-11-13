package ru.kolch.prv_4;

import lombok.extern.slf4j.Slf4j;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class Client {
    public static final DefaultListModel<String> messageListModel = new DefaultListModel<>();
    private static Socket socket;

    static {
        try {
            socket = new Socket("localhost", 1239);
        } catch (IOException e) {
            log.error("failed to create socket on port {}", 1239, e);
        }
    }

    private BufferedReader bufferedReader;
    private PrintWriter out;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        final JFrame frame = new JFrame();

        frame.setSize(880, 350);

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.CENTER);

        JPanel leftContentPanel = new JPanel();
        leftContentPanel.setLayout(new BoxLayout(leftContentPanel,
                BoxLayout.Y_AXIS));

        JPanel loginPanel = new JPanel();
        final JTextField loginField = new JTextField(30);
        final JButton loginButton = new JButton("Login");
        loginPanel.add(loginField);
        loginPanel.add(loginButton);

        leftContentPanel.add(loginPanel);

        JPanel messagePanel = new JPanel();
        final JTextField messageField = new JTextField(30);
        final JButton sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        messagePanel.add(messageField);
        messagePanel.add(sendButton);

        leftContentPanel.add(messagePanel);

        JList<String> messageList = new JList<>(messageListModel);
        JScrollPane messageScrollPane = new JScrollPane(messageList);
        messageScrollPane.setPreferredSize(new Dimension(200, 230));

        leftContentPanel.add(messageScrollPane);

        leftPanel.add(leftContentPanel);

        JPanel drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setPreferredSize(new Dimension(400, 300));
        rightPanel.add(drawingPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setVisible(true);

        final Thread get = new Thread(() -> {
            String message;
            while (socket.isConnected()) {
                try {
                    message = bufferedReader.readLine();
                    messageListModel.addElement(message);
                } catch (IOException e) {
                    closeAll(socket, bufferedReader, out);
                }
            }
        });

        loginButton.addActionListener(actionEvent -> {
            Thread connectThread = new Thread(() -> {
                try {

                    if (socket.isConnected()) {
                        loginButton.setEnabled(false);
                        loginField.setEnabled(false);
                        sendButton.setEnabled(true);
                        out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket
                                        .getOutputStream())), true);
                        bufferedReader = new BufferedReader(
                                new InputStreamReader(socket
                                        .getInputStream()));
                        get.start();
                        out.println(" - " + loginField.getText()
                                + " connected");
                    }
                } catch (IOException e) {
                    log.error("Error", e);
                }
            });

            connectThread.start();
        });

        sendButton.addActionListener(e -> {
            if (!messageField.getText().isEmpty()) {
                Thread sendThread = new Thread(() -> {
                    if (out != null) {
                        out.println(loginField.getText() + ": "
                                + messageField.getText());
                        messageField.setText("");
                    }
                });

                sendThread.start();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (out != null) {
                    out.println(" - " + loginField.getText()
                            + " disconnected");
                }
                closeAll(socket, bufferedReader, out);
            }
        });
    }

    public void closeAll(Socket socket, BufferedReader buffReader,
                         PrintWriter out) {
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