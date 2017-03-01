package com.skriptide.util;

import com.skriptide.Main;
import com.skriptide.gui.controller.IdeGuiController;
import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yannh on 16.02.2017.
 */
public class ClientSocket {

    private ServerSocket serverSocket;
    private Thread t;
    public ClientSocket() {

        try {
            serverSocket = new ServerSocket(25655);
        } catch (IOException e) {
            e.printStackTrace();
        }
        t = new Thread(() -> {


            while (true) {

                try {
                    Socket cl = serverSocket.accept();


                    BufferedReader reader = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cl.getOutputStream()));
                    String line = "";

                    while ((line = reader.readLine()) != null) {

                        if(line.contains("c-start")) {

                            writer.write("started");
                            writer.newLine();
                            writer.flush();
                            //TODO request focus
                            reader.close();
                            writer.close();
                            cl.close();
                            Platform.runLater(() -> IdeGuiController.controller.getStateLabel().getScene().getWindow().requestFocus());
                            break;
                        } else if(line.startsWith("-file")) {

                            writer.write("Opening");
                            String finalLine = line;
                            Platform.runLater(() -> IdeGuiController.controller.openFile(finalLine.split(" ")[1]));
                            writer.newLine();
                            writer.flush();
                            reader.close();
                            writer.close();
                            cl.close();
                            break;
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        });
        t.setName("IDE Socket");
    }
    public void start() {

        t.start();
    }
    public void stop() {

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        t.interrupt();
        t = null;

    }
}
