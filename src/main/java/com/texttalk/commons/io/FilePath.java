package com.texttalk.commons.io;

import org.apache.commons.io.FilenameUtils;
import java.io.File;

/**
 * Created by Andrew on 22/06/2014.
 */
public class FilePath {

    public static String getParentPath(String path) {

        return new File(path).getParent();
    }

    /**
     * Get path folder. Level starts counting from 1 right position: getParentPath("/folder3/folder2/folder1/file.ext", 2) = "/folder3/folder2"
     * @param path
     * @param level
     * @return
     */
    public static String getParentPath(String path, int level) {

        while (level >= 1 && path != null) {
            path = new File(path).getParent();
            level--;
        }

        return path;
    }

    public static String joinPaths(String... paths) {

        String path = "";
        int counter = 0;

        for (String p : paths) {
            counter++;
            path += p;
            if (counter != paths.length) {
                path += File.separator;
            }
        }

        return FilenameUtils.normalize(path);
    }

    public static String getResourcePath(ClassLoader classLoader, String path) {
        return joinPaths(classLoader.getResource("").getPath(), path);
    }

    public static String getResourcePath(ClassLoader classLoader, String path, int level) {
        return joinPaths(getParentPath(classLoader.getResource("").getPath(), level), path);
    }
}
