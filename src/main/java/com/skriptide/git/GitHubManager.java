package com.skriptide.git;

import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * Created by Matthew E on 1/26/2017.
 */
public class GitHubManager {

    void test() {
        try {
            GitHub gitHub = GitHub.connectUsingPassword("", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
