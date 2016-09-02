package com.skriptide.codemanage;

import com.skriptide.util.Project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Liz3ga on 27.08.2016.
 */
public class CodeWriter {


    private Project pr;
    private String inputCode;

    public CodeWriter(String code, Project pr) {
            this.inputCode = code;
        this.pr = pr;



    }

    public void write() {

        File projFile = new File(pr.getSkriptPath());

        FileWriter fw = null;
        try {
            fw = new FileWriter(projFile.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {

            bw.write(inputCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
