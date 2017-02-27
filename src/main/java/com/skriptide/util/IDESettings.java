package com.skriptide.util;

import com.skriptide.Main;
import com.skriptide.config.Config;

/**
 * Created by yannh on 15.02.2017.
 */
public class IDESettings {

    private Config config;

    //Code
    private boolean codeManagement;
    private boolean highlight;
    private boolean autoComplete;
    private boolean complexeAutoComplete;

    //Server
    private boolean minifyOutput;
    private boolean markMessage;
    private boolean startAfterCreate;
    private boolean clearAfterStop;
    private boolean saveOutput;

    //AutoSave
    private long delay;
    private long delayMultiPlier;

    private int theme;

    private boolean errorSys;
    private String errorApi;
    private String errorSkript;





    public void loadIn() {

        this.config = Main.manager.getSettingsConf();
        if(!config.contains("settings.use-code-management")) {


            config.set("settings.use-code-management", "true");
            config.set("settings.use-highlighting", "true");
            config.set("settings.use-autocomplete", "true");
            config.set("settings.minify-output", "true");
            config.set("settings.mark-message", "true");
            config.set("settings.post-start", "false");
            config.set("settings.post-clear", "true");
            config.set("settings.save-output", "false");
            config.set("settings.complex-hlt", "false");
            config.set("settings.auto-save-delay", "15");
            config.set("settings.auto-save-delay-interval", "60000");
            config.set("settings.theme", "0");
            config.set("settings.errsys.use", "true");
            config.set("settings.errsys.api", "");
            config.set("settings.errsys.skript", "");

            config.save();
        }

        codeManagement = Boolean.valueOf(config.getString("settings.use-code-management"));
        highlight = Boolean.valueOf(config.getString("settings.use-highlighting"));
        autoComplete = Boolean.valueOf(config.getString("settings.use-autocomplete"));
        minifyOutput = Boolean.valueOf(config.getString("settings.minify-output"));
        markMessage = Boolean.valueOf(config.getString("settings.mark-message"));
        startAfterCreate = Boolean.valueOf(config.getString("settings.post-start"));
        clearAfterStop = Boolean.valueOf(config.getString("settings.post-clear"));
        saveOutput = Boolean.valueOf(config.getString("settings.save-output"));
        complexeAutoComplete = Boolean.valueOf(config.getString("settings.complex-hlt"));
        delay = Long.valueOf(config.getString("settings.auto-save-delay"));
        delayMultiPlier = Long.valueOf(config.getString("settings.auto-save-delay-interval"));
        theme = Integer.valueOf(config.getString("settings.theme"));
        errorSys = Boolean.valueOf(config.getString("settings.errsys.use"));
        errorApi = config.getString("settings.errsys.api");
        errorSkript = config.getString("settings.errsys.skript");


    }
    public void saveSettings() {

        config.set("settings.use-code-management", String.valueOf(codeManagement));
        config.set("settings.use-highlighting", String.valueOf(highlight));
        config.set("settings.use-autocomplete", String.valueOf(autoComplete));
        config.set("settings.minify-output", String.valueOf(minifyOutput));
        config.set("settings.mark-message", String.valueOf(markMessage));
        config.set("settings.post-start", String.valueOf(startAfterCreate));
        config.set("settings.post-clear", String.valueOf(clearAfterStop));
        config.set("settings.save-output", String.valueOf(saveOutput));
        config.set("settings.auto-save-delay", String.valueOf(delay));
        config.set("settings.auto-save-delay-interval", String.valueOf(delayMultiPlier));
        config.set("settings.complex-hlt", String.valueOf(complexeAutoComplete));
        config.set("settings.theme", String.valueOf(theme));
        config.set("settings.errsys.use", String.valueOf(errorSys));
        config.set("settings.errsys.api", errorApi);
        config.set("settings.errsys.skript", errorSkript);
        config.save();
    }

    public boolean isCodeManagement() {
        return codeManagement;
    }

    public void setCodeManagement(boolean codeManagement) {
        this.codeManagement = codeManagement;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    public boolean isMinifyOutput() {
        return minifyOutput;
    }

    public void setMinifyOutput(boolean minifyOutput) {
        this.minifyOutput = minifyOutput;
    }

    public boolean isMarkMessage() {
        return markMessage;
    }

    public void setMarkMessage(boolean markMessage) {
        this.markMessage = markMessage;
    }

    public boolean isStartAfterCreate() {
        return startAfterCreate;
    }

    public void setStartAfterCreate(boolean startAfterCreate) {
        this.startAfterCreate = startAfterCreate;
    }

    public boolean isClearAfterStop() {
        return clearAfterStop;
    }

    public void setClearAfterStop(boolean clearAfterStop) {
        this.clearAfterStop = clearAfterStop;
    }

    public boolean isSaveOutput() {
        return saveOutput;
    }

    public void setSaveOutput(boolean saveOutput) {
        this.saveOutput = saveOutput;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelayMultiPlier() {
        return delayMultiPlier;
    }

    public void setDelayMultiPlier(long delayMultiPlier) {
        this.delayMultiPlier = delayMultiPlier;
    }

    public boolean isComplexeAutoComplete() {
        return complexeAutoComplete;
    }

    public void setComplexeAutoComplete(boolean complexeAutoComplete) {
        this.complexeAutoComplete = complexeAutoComplete;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public boolean isErrorSys() {
        return errorSys;
    }

    public String getErrorApi() {
        return errorApi;
    }

    public String getErrorSkript() {
        return errorSkript;
    }

    public void setErrorSkript(String errorSkript) {
        this.errorSkript = errorSkript;
    }

    public void setErrorSys(boolean errorSys) {
        this.errorSys = errorSys;
    }

    public void setErrorApi(String errorApi) {
        this.errorApi = errorApi;
    }
}
