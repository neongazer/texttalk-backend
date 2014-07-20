package com.texttalk.common.player;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Andrew on 19/07/2014.
 */
public class MP3 {

    private String filename = null;
    private Player player = null;
    private File file = null;

    public MP3(String filename) {
        this.filename = filename;
    }

    // constructor that takes the file object
    public MP3(File file) {
        this.file = file;
    }

    public void close() {
        if (player != null) player.close();
    }

    public void play() {
        try {
            FileInputStream fis;
            if (file != null && file.exists()) {
                fis = new FileInputStream(file);
            } else {
                fis = new FileInputStream(filename);
            }
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        } catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        try {
            player.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

