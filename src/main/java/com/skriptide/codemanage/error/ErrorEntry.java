package com.skriptide.codemanage.error;

import com.skriptide.gui.OpenFile;

/**
 * Created by yannh on 25.02.2017.
 */
public class ErrorEntry {

    private int line;
    private ErrorType type;
    private OpenFile file;
    private String text;

    public ErrorEntry(int line, ErrorType type, OpenFile file, String text) {
        this.line = line;
        this.type = type;
        this.file = file;
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public ErrorType getType() {
        return type;
    }

    public OpenFile getFile() {
        return file;
    }

    public String getText() {
        return text;
    }
}
