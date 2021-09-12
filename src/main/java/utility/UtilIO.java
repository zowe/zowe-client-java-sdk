/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class will handle common sequences of I/O, issue messages, and throw errors as necessary.
 *
 * @version 1.0
 */
public class UtilIO {

    /**
     * File delimiter
     */
    public static final String FILE_DELIM = "/";

    /**
     * UTF8 identifier
     */
    public static final String UTF8 = "UTF8";

    /**
     * Windows OS identifier
     */
    public static String OS_WIN32 = "win32";

    /**
     * Mac OS identifier
     */
    public static String OS_MAC = "darwin";

    /**
     * Linux OS identifier
     */
    public static String OS_LINUX = "linux";

    /**
     * Return whether input file is a directory or file
     *
     * @param dirOrFile file path
     * @return true if file path is a directory, false otherwise
     */
    public static Boolean isDir(String dirOrFile) {
        Util.checkNullParameter(dirOrFile == null, "dirOrFile is null");
        Util.checkIllegalParameter(dirOrFile.isEmpty(), "dirOrFile not specified");
        Path path = Paths.get(dirOrFile);

        return Files.isDirectory(path);
    }

    /**
     * Take an extension and prefix with a '.' identifier
     *
     * @param extension extension to normalize
     * @return string '.bin' for input 'bin' for example
     */
    public static String normalizeExtension(String extension) {
        Util.checkNullParameter(extension == null, "dirOrFile is null");
        Util.checkIllegalParameter(extension.isEmpty(), "dirOrFile not specified");
        extension = extension.trim();
        if (extension.length() > 0 && !".".equals(extension.substring(0, 1))) {
            // Add a '.' character to the extension if omitted.
            // If someone specifies just "bin", make the extension ".bin"
            extension = "." + extension;
        }
        return extension;
    }

    /**
     * Wraps Files. Exists so that we don't have to import fs unnecessarily
     *
     * @param file file to validate existence against
     * @return true Ff file exists
     */
    public static Boolean existsSync(String file) {
        Util.checkNullParameter(file == null, "dirOrFile is null");
        Util.checkIllegalParameter(file.isEmpty(), "dirOrFile not specified");
        return Files.exists(Paths.get(file));
    }

    /**
     * Create a directory if it does not yet exist synchronously.
     *
     * @param dir directory to create
     * @throws IOException i/o error processing
     */
    public static void createDirSync(String dir) throws IOException {
        Util.checkNullParameter(dir == null, "dirOrFile is null");
        Util.checkIllegalParameter(dir.isEmpty(), "dirOrFile not specified");
        if (!existsSync(dir)) {
            Path dirs = Paths.get(System.getProperty("user.dir") + dir);
            Files.createDirectory(dirs);
        }
    }

    /**
     * Create all needed directories for an input directory in the form of:
     * first/second/third where first will contain director second and second
     * will contain directory third
     *
     * @param dir directory to create all subdirectories for
     * @throws IOException i/o error processing
     */
    public static void createDirsSync(String dir) throws IOException {
        Util.checkNullParameter(dir == null, "dirOrFile is null");
        Util.checkIllegalParameter(dir.isEmpty(), "dirOrFile not specified");
        // we're splitting on a specific separator character, so replace \ with / before splitting
        Path dirs = Paths.get(dir);
        Files.createDirectories(dirs);
    }

    /**
     * Create all necessary directories for a fully qualified file and its path,
     * for example, if filePath = oneDir/twoDir/threeDir/file.txt,
     * oneDir, twoDir, and threeDir will be created.
     *
     * @param filePath file path
     * @throws IOException i/o error processing
     */
    public static void createDirsSyncFromFilePath(String filePath) throws IOException {
        Util.checkNullParameter(filePath == null, "dirOrFile is null");
        Util.checkIllegalParameter(filePath.isEmpty(), "dirOrFile not specified");
        createDirsSync(filePath);
    }

    /**
     * Create a symbolic link to a directory. If the symbolic link already exists,
     * re-create it with the specified target directory.
     *
     * @param newSymLinkPath  path new symbolic link to be created
     * @param existingDirPath path the existing directory that we will link to
     * @throws Exception error processing
     */
    public static void createSymlinkToDir(String newSymLinkPath, String existingDirPath) throws Exception {
        try {
            if (!existsSync(newSymLinkPath)) {
                Files.createSymbolicLink(Paths.get(existingDirPath), Paths.get(newSymLinkPath));
                return;
            }

            if (Files.isSymbolicLink(Paths.get(newSymLinkPath))) {
                Files.delete(Paths.get(newSymLinkPath));
                Files.createSymbolicLink(Paths.get(existingDirPath), Paths.get(newSymLinkPath));
                return;
            }
        } catch (Exception exception) {
            throw new Exception("Failed to create symbolic link from '" + newSymLinkPath +
                    "' to '" + existingDirPath + "'\n" +
                    "Reason: " + exception.getCause() + "\n" +
                    "Full exception: " + exception);
        }

        throw new Exception(
                "The intended symlink '" + newSymLinkPath +
                        "' already exists and is not a symbolic link. So, we did not create a symlink from there to '" +
                        existingDirPath + "'.");
    }

    /**
     * Uses the fs-extra package to create a directory (and all subdirectories)
     *
     * @param dir directory (do not include a file name)
     * @throws IOException i/o error processing
     */
    public static void mkdirp(String dir) throws IOException {
        Util.checkNullParameter(dir == null, "dirOrFile is null");
        Util.checkIllegalParameter(dir.isEmpty(), "dirOrFile is not specified");
        Files.createDirectories(Paths.get(dir));
    }

    /**
     * Read file as a stream or specify encoding.
     *
     * @param file file to read
     * @return buffer the content of the file
     * @throws IOException i/o error processing
     */
    public static BufferedReader readFileSyncBinary(String file) throws IOException {
        Util.checkNullParameter(file == null, "dirOrFile is null");
        Util.checkIllegalParameter(file.isEmpty(), "dirOrFile not specified");

        return Files.newBufferedReader(Paths.get(file));
    }

    /**
     * Read file as a string with line normalization or specify encoding.
     *
     * @param file              file to read
     * @param normalizeNewLines true to toggle Remove Windows line endings (\r\n) in favor of \n
     * @return string with the content of the file
     */
    public static String readFileSyncAsString(String file, Boolean normalizeNewLines) {
        Util.checkNullParameter(file == null, "dirOrFile is null");
        Util.checkIllegalParameter(file.isEmpty(), "dirOrFile not specified");

        if (normalizeNewLines == null) {
            normalizeNewLines = false;
        }
        InputStreamReader isr = new InputStreamReader((InputStream) Paths.get(file), StandardCharsets.UTF_8);
        BufferedReader content = new BufferedReader(isr);
        if (normalizeNewLines) {
            return content.toString().replace("\r\n", "\n");
        }
        return content.toString();
    }

    /**
     * Create a Readable stream from a file
     *
     * @param file file from which to create a read stream
     * @return string with the content of the file
     * @throws IOException i/o error processing
     */
    public static String createReadStream(String file) throws IOException {
        Util.checkNullParameter(file == null, "dirOrFile is null");
        Util.checkIllegalParameter(file.isEmpty(), "dirOrFile not specified");
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader((InputStream) Paths.get(file)),
                1024);
        for (String line = r.readLine();
             line != null;
             line = r.readLine()) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * Create a Node.js Readable stream from a file
     *
     * @param file file from which to create a read stream
     * @return string with the content of the file
     * @throws FileNotFoundException file not found error
     */
    public static FileOutputStream createWriteStream(String file) throws FileNotFoundException {
        Util.checkNullParameter(file == null, "dirOrFile is null");
        Util.checkIllegalParameter(file.isEmpty(), "dirOrFile not specified");
        // Creates an OutputStream
        return new FileOutputStream(file);
    }

    /**
     * Process a string so that its line endings are operating system
     * appropriate before you save it to disk
     * (basically, if the user is on Windows, change  \n to \r\n)
     *
     * @param original original input
     * @return string with input with removed newlines
     */
    public static String processNewlines(String original) {
        Util.checkNullParameter(original == null, "dirOrFile is null");
        Util.checkIllegalParameter(original.isEmpty(), "dirOrFile not specified");
        String OS = System.getProperty("os.name").toLowerCase();
//       TODO
//            if (OS.indexOf("win") >= 0) {
//                return original.replace(/([^\r])\n/g, "$1\r\n");
//            }
        return original;
    }

    /**
     * Get default text editor for a given operating system
     *
     * @return string with text editor
     */
    public static String getDefaultTextEditor() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            return "notepad";
        } else if ((OS.contains("mac"))) {
            return "open -a TextEdit";
        } else {
            return "gedit";
        }
    }

}
