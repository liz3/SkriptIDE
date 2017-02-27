package com.skriptide;

import com.skriptide.codemanage.AutoSaver;
import com.skriptide.codemanage.error.ErrorHandler;
import com.skriptide.gui.SceneManager;
import com.skriptide.include.Server;
import com.skriptide.util.ClientSocket;
import com.skriptide.util.IDESettings;

import java.io.*;
import java.net.Socket;

/**
 * Created by yannh on 27.01.2017.
 */
public class Main {

    public static IDESettings settings;
    public static AutoSaver saver;
    public static SceneManager sceneManager;
    public static Manager manager;
    public static boolean debugMode;
    public static Server runningServer;
    public static ClientSocket listener;
    public static String version;
    public static ErrorHandler errorHandler;

    public static void main(String[] args) {
        version = "0.5.5";
        String path = null;
        if(args.length != 0 && !args[0].startsWith("-")) {
           path = args[0];
        }


        try {
            Socket socket = new Socket("127.0.0.1", 25655);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            if(path == null) {
                writer.write("c-start");
                writer.newLine();
                writer.flush();
                return;
            } else {
                writer.write("-file " + path);
                writer.newLine();
                writer.flush();
                return;
            }
        } catch (IOException e) {

        }
        System.out.println("Pre gui working....");
        sceneManager = new SceneManager();
        manager = new Manager(sceneManager);
        if(path != null) {
            sceneManager.setExternFile(path);
        }
        System.out.println("Starting gui!");
        sceneManager.start();



    }
}
