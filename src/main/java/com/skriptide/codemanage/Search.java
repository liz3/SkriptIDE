package com.skriptide.codemanage;

import com.skriptide.gui.OpenProject;
import javafx.scene.control.Tab;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yannh on 31.12.2016.
 */
public class Search {

    public static void search(OpenProject openProject, String searched) {


        Tab tab = openProject.getTab();
        CodeArea code = openProject.getArea();


        String oldText = code.getText();

        code = new CodeArea();
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.appendText(oldText);
        code.clear();
        Pattern p = Pattern.compile(
                "(?<SEARCHED>" + searched + ")"


                , Pattern.CASE_INSENSITIVE);

        CodeArea finalCode = code;
        code.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())).subscribe(change -> {
            finalCode.setStyleSpans(0, computeHighlighting(finalCode.getText(), p));
        });
        code.replaceText(0, 0, oldText);
        tab.setContent(finalCode);
        openProject.setArea(finalCode);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {

            String styleClass =
                    matcher.group("SEARCHED") != null ? "marked" :


                            null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();


        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
