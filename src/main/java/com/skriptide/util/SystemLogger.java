package com.skriptide.util;

import javax.xml.bind.DatatypeConverter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by yannh on 30.10.2016.
 */
public class SystemLogger {


    public static void delaySender() throws IOException {


        new Thread(() -> {


            while (true) {

                if (IDESystemErr.erros.size() != 0) {
                    Socket socket = null;
                    try {
                        socket = new Socket("api.liz3.de", 8564);
                    } catch (IOException e) {

                    }
					System.out.println("Connection set");
                    OutputStream outputStream = null;
                    try {
                        outputStream = socket.getOutputStream();
                    } catch (IOException e) {
					System.out.println("Connection-Failed");

                    }

                    DataOutputStream out = new DataOutputStream(outputStream);


                    String all = "";
                    for (String str : IDESystemErr.erros) {

                        all = all + str + "\\";
                    }
                    IDESystemErr.erros.clear();
                    try {
                        out.writeBytes(encode("<error os=\"" + System.getProperty("os.name") + "\">" + all + "</error>"));
                    } catch (IOException e) {

                    }

                    try {
                        socket.close();
                    } catch (IOException e) {

                    }
                    try {
                        out.close();
                    } catch (IOException e) {

                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {

                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            }


        }).start();

    }


    private static String encode(String txt) {

        String finish = txt;

        for (int i = 0; i != 12; i++) {

            finish = DatatypeConverter.printBase64Binary(finish.getBytes());


        }
        return finish;
    }
}
