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
import java.util.List;
import java.nio.Buffer;

/**
 * This class will handle common sequences of node I/O and issue messages /
 * throw errors as neccessary.
 * @export
 * @class IO
 */
public class UtilIO {

        /**
         * File delimiter
         * @static
         * @type {string}
         * @memberof IO
         */
        public static String FILE_DELIM = "/";

        /**
         * UTF8 identifier
         * @static
         * @memberof IO
         */
        public static String UTF8 = "utf8";

        /**
         * Windows OS identifier
         * @static
         * @memberof IO
         */
        public static String OS_WIN32 = "win32";

        /**
         * Mac OS identifier
         * @static
         * @memberof IO
         */
        public static String OS_MAC = "darwin";

        /**
         * Linux OS identifier
         * @static
         * @memberof IO
         */
        public static String OS_LINUX = "linux";

        /**
         * Return whether input file is a directory or file
         * @static
         * @param {string} dirOrFile - file path
         * @returns {boolean} - true if file path is a directory, false otherwise
         * @memberof IO
         */
        public static Boolean isDir(String dirOrFile) {
            Util.checkNullParameter(dirOrFile == null,"dirOrFile is null");
            Util.checkStateParameter(dirOrFile == "","dirOrFile is empty");
            Path path = Paths.get(dirOrFile);

            Boolean stat = Files.isDirectory(path);
            return stat;
        }

        /**
         * Take an extension and prefix with a '.' identifier
         * @static
         * @param {string} extension - extension to normalize
         * @returns {string} - '.bin' for input 'bin' for example
         * @memberof IO
         */
        public static String normalizeExtension(String extension) {
        Util.checkNullParameter(extension == null,"dirOrFile is null");
        Util.checkStateParameter(extension.isBlank() || extension.isEmpty() ,"dirOrFile is empty");
        extension = extension.trim();
            if (extension != null && extension.length() > 0 && extension.substring(0,1) != ".") {
                // add a '.' character to the extension if omitted
                // (if someone specifies just "bin", make the extension ".bin" )
                extension = "." + extension;
            }
            return extension;
        }

        /**
         * Wraps Files.exists so that we dont have to import fs unnecessarily
         * @static
         * @param  {string} file - file to validate existence against
         * @returns true if file exists
         * @memberof IO
         */
        public static Boolean existsSync(String file ) {
            Util.checkNullParameter(file == null,"dirOrFile is null");
            Util.checkStateParameter(file.isBlank() || file.isEmpty() ,"dirOrFile is empty");
            return Files.exists(Paths.get(file));
        }

        /**
         * Create a directory if it does not yet exist synchronously.
         * @static
         * @param  {string} dir - directory to create
         * @return {undefined}
         * @memberof IO
         */
        public static void createDirSync(String dir) throws IOException {
            Util.checkNullParameter(dir == null,"dirOrFile is null");
            Util.checkStateParameter(dir.isBlank() || dir.isEmpty() ,"dirOrFile is empty");
            if (!existsSync(dir)) {
                Path dirs = Paths.get( System.getProperty("user.dir") + dir );
                Files.createDirectory(dirs);
            }
        }

        /**
         * Create all needed directories for an input directory in the form of:
         * first/second/third where first will contain director second and second
         * will contain directory third
         * @static
         * @param {string} dir - directory to create all sub directories for
         * @memberof IO
         */
        public static void createDirsSync(String dir) throws IOException {
            Util.checkNullParameter(dir == null,"dirOrFile is null");
            Util.checkStateParameter(dir.isBlank() || dir.isEmpty() ,"dirOrFile is empty");
            // we're splitting on a specific separator character, so replace \ with /
            // before splitting
            Path dirs = Paths.get(dir);
            Files.createDirectories(dirs);
        }

        /**
         * Create all necessary directories for a fully qualified file and its path,
         * for example, if filePath = oneDir/twoDir/threeDir/file.txt,
         * oneDir, twoDir, and threeDir will be created.
         * @static
         * @param  {string} filePath [description]
         * @return {[type]}          [description]
         * @memberof IO
         */
        public static void createDirsSyncFromFilePath(String filePath) throws IOException {
            Util.checkNullParameter(filePath == null,"dirOrFile is null");
            Util.checkStateParameter(filePath.isBlank() || filePath.isEmpty() ,"dirOrFile is empty");
            createDirsSync(filePath);
        }

        /**
         * Create a symbolic link to a directory. If the symbolic link already exists,
         * re-create it with the specified target directory.
         *
         * @param {string} newSymLinkPath - the path new symbolic link to be created
         * @param {string} existingDirPath - the path the existing directory that we will link to
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
            };

            throw new Exception(
                    "The intended symlink '" + newSymLinkPath +
                    "' already exists and is not a symbolic link. So, we did not create a symlink from there to '" +
                    existingDirPath + "'.");
        }

        /**
         * Uses the fs-extra package to create a directory (and all subdirectories)
         * @static
         * @param {string} dir - the directory (do not include a file name)
         * @memberof IO
         */
        public static void mkdirp(String dir) throws IOException {
            Util.checkNullParameter(dir == null,"dirOrFile is null");
            Util.checkStateParameter(dir.isBlank() || dir.isEmpty() ,"dirOrFile is empty");
            Files.createDirectories(Paths.get(dir));
        }

        /**
         * Wraps Files.readAllLines so that we dont have to import fs unnecessarily
         * or specify encoding.
         * @static
         * @param  {string} file - file to read
         * @param normalizeNewLines - remove Windows line endings (\r\n)  in favor of \n
         * @param binary - should the file be read in binary mode? If so, normalizeNewLines is ignored. If false,
         *                 the file will be read in UTF-8 encoding
         * @return Buffer - the content of the file
         * @memberof IO
         */
        public static BufferedReader readFileSync(String file, Boolean normalizeNewLines , Boolean binary ) throws IOException {
            Util.checkNullParameter(file == null,"dirOrFile is null");
            Util.checkStateParameter(file.isBlank() || file.isEmpty() ,"dirOrFile is empty");

            if (normalizeNewLines == null) {
                normalizeNewLines = false;
            }
            if (binary == null) {
                binary = false;
            }

            if (binary) {
                return Files.newBufferedReader(Paths.get(file));
            }

        }
    public static String readFileSync(String file, Boolean normalizeNewLines , Boolean binary ) throws IOException {
        Util.checkNullParameter(file == null,"dirOrFile is null");
        Util.checkStateParameter(file.isBlank() || file.isEmpty() ,"dirOrFile is empty");

        if (normalizeNewLines == null) {
            normalizeNewLines = false;
        }
        if (binary == null) {
            binary = false;
        }

        if (!binary) {
            InputStreamReader isr = new InputStreamReader((InputStream) Paths.get(file), StandardCharsets.UTF_8);
            BufferedReader content = new BufferedReader(isr);
            if (normalizeNewLines) {
                return content.toString().replace("\r\n", "\n");
            }
            return content.toString();
        }
    }

        /**
         * Create a Readable stream from a file
         * @param file - the file from which to create a read stream
         * @return String - the content of the file
         * @memberof IO
         */
        public static String createReadStream(String file) throws IOException {
        Util.checkNullParameter(file == null,"dirOrFile is null");
        Util.checkStateParameter(file.isBlank() || file.isEmpty() ,"dirOrFile is empty");
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
         * @param file - the file from which to create a read stream
         * @return Buffer - the content of the file
         * @memberof IO
         */
        public static FileOutputStream createWriteStream(String file) {
            Util.checkNullParameter(file == null,"dirOrFile is null");
            Util.checkStateParameter(file.isBlank() || file.isEmpty() ,"dirOrFile is empty");
            // Creates an OutputStream
            FileOutputStream output = new FileOutputStream(String file);
            return output;
        }

        /**
         * Process a string so that its line endings are operating system
         * appropriate before you save it to disk
         * (basically, if the user is on Windows, change  \n to \r\n)
         * @static
         * @param {string} original - original input
         * @returns {string} - input with removed newlines
         * @memberof IO
         */
        public static String  processNewlines(String original) {
            Util.checkNullParameter(original == null,"dirOrFile is null");
            Util.checkStateParameter(original.isBlank() || original.isEmpty() ,"dirOrFile is empty");
            String OS = System.getProperty("os.name").toLowerCase();
//       TODO
//            if (OS.indexOf("win") >= 0) {
//                return original.replace(/([^\r])\n/g, "$1\r\n");
//            }
            return original;
        }

        /**
         * Get default text editor for a given operating system
         * @static
         * @returns {string} - text editor launch string
         * @memberof IO
         */
        public static String getDefaultTextEditor() {
        String OS = System.getProperty("os.name").toLowerCase();
            if (OS.indexOf("win") >= 0) {
                return "notepad";
            } else if ((OS.indexOf("mac") >= 0)) {
                return "open -a TextEdit";
            } else {
                return "gedit";
            }
        }
//
//        /**
//         * Create a file
//         * @static
//         * @param  {string} file - file to create
//         * @memberof IO
//         */
//        public static createFileSync(file: string) {
//            ImperativeExpect.toBeDefinedAndNonBlank(file, "file");
//            fs.closeSync(fs.openSync(file, "w"));
//        }
//
//        /**
//         * Create a file asynchronously
//         * @static
//         * @param  {string} file    - file to create
//         * @param  {string} content - content to write in the file
//         * @return {[type]}         [description]
//         * @memberof IO
//         */
//        public static writeFileAsync(file: string, content: string) {
//            ImperativeExpect.toBeDefinedAndNonBlank(file, "file");
//            ImperativeExpect.toNotBeNullOrUndefined(content, "Content to write to the file must not be null or undefined");
//            return new Promise<void>((resolve, reject: ImperativeReject) => {
//                try {
//                    fs.writeFile(file, content, IO.UTF8, (err) => {
//                    if (!isNullOrUndefined(err)) {
//                        throw new ImperativeError({msg: err.message});
//                    }
//                    resolve();
//                });
//                } catch (error) {
//                    throw new ImperativeError({msg: error.message});
//                }
//            });
//        }
//
//        /**
//         * Write a file
//         * @static
//         * @param  {string} file - file to create
//         * @param  {string} content    - content to write
//         * @return {undefined}
//         * @memberof IO
//         */
//        public static writeFile(file: string, content: Buffer) {
//            ImperativeExpect.toBeDefinedAndNonBlank(file, "file");
//            ImperativeExpect.toNotBeNullOrUndefined(content, "Content to write to the file must not be null or undefined");
//            IO.createFileSync(file);
//            fs.writeFileSync(file, content);
//        }
//
//        /**
//         * Write an object to a file and set consistent formatting on the serialized
//         * JSON object.
//         * @static
//         * @param  {string} configFile - file to create
//         * @param  {Object} object     - object to serialize
//         * @return {undefined}
//         * @memberof IO
//         */
//        public static writeObject(configFile: string, object: object) {
//            ImperativeExpect.toBeDefinedAndNonBlank(configFile, "configFile");
//            ImperativeExpect.toNotBeNullOrUndefined(object, "content");
//            fs.closeSync(fs.openSync(configFile, "w"));
//            fs.appendFileSync(configFile, JSON.stringify(object, null, 2));
//        }
//
//        /**
//         * Delete a file
//         * @static
//         * @param {string} file: The file to delete
//         * @memberof IO
//         */
//        public static deleteFile(file: string) {
//            ImperativeExpect.toBeDefinedAndNonBlank(file, "file");
//            if (fs.existsSync(file)) {
//                fs.unlinkSync(file);
//            }
//        }
//
//        /**
//         * Delete a directory
//         * @static
//         * @param {string} dir: The directory to delete
//         * @memberof IO
//         */
//        public static deleteDir(dir: string) {
//            ImperativeExpect.toBeDefinedAndNonBlank(dir, "dir");
//            fs.rmdirSync(dir);
//        }
//
//        /**
//         * Recursively delete all files and subdirectories of the specified directory.
//         * Ensure that we do not follow a symlink. Just delete the link.
//         *
//         * @params {string} pathToTreeToDelete - Path to top directory of the tree
//         *      to delete.
//         */
//        public static deleteDirTree(pathToTreeToDelete: string) {
//            try {
//                // if pathToTreeToDelete is a symlink, just delete the link file
//                if (fs.existsSync(pathToTreeToDelete)) {
//                const fileStats = fs.lstatSync(pathToTreeToDelete);
//                    if (fileStats.isSymbolicLink() || fileStats.isFile()) {
//                        fs.unlinkSync(pathToTreeToDelete);
//                        return;
//                    }
//
//                    // read all of the children of this directory
//                    fs.readdirSync(pathToTreeToDelete).forEach((nextChild, index) => {
//                        // recursively delete the child
//                        IO.deleteDirTree(pathToTreeToDelete + path.sep + nextChild);
//                    });
//
//                    // delete our starting directory
//                    fs.rmdirSync(pathToTreeToDelete);
//                }
//            } catch (exception) {
//                throw new ImperativeError({
//                        msg: "Failed to delete the directory tree '" + pathToTreeToDelete +
//                        "'\nReason: " + exception.message + "\n" +
//                        "Full exception: " + exception
//                }
//            );
//            }
//        }
//
//        /**
//         * Delete a symbolic link.
//         *
//         * @param {string} symLinkPath - the path to a symbolic link to be deleted
//         */
//        public static deleteSymLink(symLinkPath: string) {
//            try {
//                if (!fs.existsSync(symLinkPath)) {
//                    return;
//                }
//
//                // Get the file status to determine if it is a symlink.
//            const fileStats = fs.lstatSync(symLinkPath);
//                if (fileStats.isSymbolicLink()) {
//                    fs.unlinkSync(symLinkPath);
//                    return;
//                }
//            } catch (ioExcept) {
//                throw new ImperativeError({
//                        msg: "Failed to delete the symbolic link '" + symLinkPath +
//                        "'\nReason: " + ioExcept.message + "\n" +
//                        "Full exception: " + ioExcept
//                }
//            );
//            }
//
//            throw new ImperativeError({
//                    msg: "The specified symlink '" + symLinkPath +
//                    "' already exists and is not a symbolic link. So, we did not delete it."
//            }
//        );
//        }
//    }
}
