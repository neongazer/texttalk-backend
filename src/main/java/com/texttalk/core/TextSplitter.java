/**
 * Copyright 2014 www.text-talk.com
 * User: andrewboss
 * Date: 04/06/2014
 * Time: 22:16
 */

package com.texttalk.core;

import java.util.ArrayList;
import java.util.List;

public class TextSplitter {

    private static final String WORD_SEPARATOR = " ";

    public static List<String> splitText(String text, int maxLength) {

        // clear multiple spaces and split into words by space
        String[] words = text.trim().replaceAll("\\s+", " ").split(WORD_SEPARATOR);
        List<String> splittedText = new ArrayList<String>();
        StringBuilder line = new StringBuilder();
        int lineLen = 0;
        int wordCount = 0;


        for (String word : words) {
            wordCount++;
            if (lineLen + (WORD_SEPARATOR + word).length() > maxLength) {
                if (wordCount > 0) {
                    splittedText.add(line.toString());
                    line = new StringBuilder();
                }
                lineLen = 0;
            }

            word += WORD_SEPARATOR;
            line.append(word);
            lineLen += word.length();
        }

        if(line.length() > 0) splittedText.add(line.toString());

        return splittedText;
    }
}
