package com.skriptide.gui;


import com.skriptide.Main;
import com.skriptide.codemanage.highlighting.ControlMain;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.fxmisc.richtext.CodeArea;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by yannh on 16.02.2017.
 */
public class ExternFile {

    private File file;
    private CodeArea area;
    private Tab tab;
    private TabPane tabPane;
    private ControlMain highlight;
    public ExternFile(String path, TabPane tabPane) {

        file = new File(path);
        this.tabPane = tabPane;
        highlight = new ControlMain();
        tab = new Tab("Extern: " + file.getName());
        area = new CodeArea();
        tab.setContent(area);
        area.setEditable(false);
        area.appendText(getCurentCode());
        highlight.controlCode(area, tab, this);
        tabPane.getTabs().add(tab);

        tab.setOnCloseRequest(event -> Main.sceneManager.getExternFiles().remove(ExternFile.this));
    }

    public File getFile() {
        return file;
    }

    public Tab getTab() {
        return tab;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public CodeArea getArea() {
        return area;
    }

    public void setArea(CodeArea area) {
        this.area = area;
    }

    private String getCurentCode() {



        String code = "";
        try {
            InputStream stream = new FileInputStream(this.file);

            InputStreamReader input = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(input);

            try {

                String line;

                do {
                    line = reader.readLine();
                    if (line != null) {

                        code = code + line + System.getProperty("line.separator");
                    }
                }
                while (line != null);

                if(Main.debugMode) {
                    System.out.println("Open code: " + file.getAbsolutePath());
                }
                reader.close();
                input.close();
                stream.close();
                return code;
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
