package com.app;

import com.StorageManager;
import com.storage.Create;
import com.storage.Delete;
import com.storage.Operations;
import com.storage.Storage;
import com.utils.FileMetadata;
import com.utils.Privilege;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainApp {

    public static void main(String[] args) {

        // com.localimpl.Local && com.driveimpl.GoogleDrive
        try {
            Class.forName("com.driveimpl.GoogleDrive");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Storage storage = StorageManager.getStorage();
        Create create = StorageManager.getCreate();
        Delete delete = StorageManager.getDelete();
        Operations operations = StorageManager.getOperations();

        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("If you need some instructions, please type command help");
        System.out.println("If you want to quit application, please type command exit");

        while (true) {
            command = scanner.nextLine();
            if (command.equals("exit")) {
                return;
            }

            if (command.equals("help")) {
                help();
                continue;
            }

            String[] commArray = command.split(" ");
            try {

                //STORAGE
                if (commArray[0].equals("createstorage") && commArray.length == 5) {
                    storage.createStorage(commArray[1], commArray[2], commArray[3], commArray[4]);
                    continue;
                }

                if (commArray[0].equals("config") && commArray.length >= 3 && commArray.length <= 7) {
                    String maxSize = null;
                    String maxNOF = null;
                    List<String> unsupportedExtension = null;
                    for (int i = 1; i < commArray.length; i += 2) {
                        if (commArray[i].equals("-maxsize")) {
                            maxSize = commArray[i+1];
                        }

                        if (commArray[i].equals("-maxnof")) {
                            maxNOF = commArray[i+1];
                        }

                        if (commArray[i].equals("-une")) {
                            unsupportedExtension = new ArrayList<>(Arrays.asList(commArray[i + 1].split(",")));
                        }
                    }

                    storage.configure(maxSize, maxNOF, unsupportedExtension);
                    continue;
                }

                if (commArray[0].equals("adduser") && commArray.length == 4) {
                    storage.addUser(commArray[1], commArray[2], Privilege.valueOf(commArray[3]));
                    continue;
                }

                if (commArray[0].equals("login") && commArray.length == 4) {
                    storage.logToStorage(commArray[1], commArray[2], commArray[3]);
                    continue;
                }

                if (commArray[0].equals("logout")) {
                    storage.logOut();
                    continue;
                }

                //CREATE
                if (commArray[0].equals("createdir") && commArray.length == 2) {
                    create.createDirectory(commArray[1]);
                    continue;
                }

                if (commArray[0].equals("createdirs") && commArray.length == 2) {
                    create.createDirectories(Arrays.asList(commArray[1].split(",")));
                    continue;
                }

                if (commArray[0].equals("createfile") && commArray.length == 2) {
                    create.createFile(commArray[1]);
                    continue;
                }

                if (commArray[0].equals("createfiles") && commArray.length == 2) {
                    create.createFiles(Arrays.asList(commArray[1].split(",")));
                    continue;
                }

                //DELETE
                if (commArray[0].equals("deletedir") && commArray.length == 2) {
                    delete.removeDirectory(commArray[1]);
                    continue;
                }

                if (commArray[0].equals("deletedirs") && commArray.length == 2) {
                    delete.removeDirectories(Arrays.asList(commArray[1].split(",")));
                    continue;
                }

                if (commArray[0].equals("deletefile") && commArray.length == 2) {
                    delete.removeFile(commArray[1]);
                    continue;
                }

                if (commArray[0].equals("deletefiles") && commArray.length == 2) {
                    delete.removeFiles(Arrays.asList(commArray[1].split(",")));
                    continue;
                }

                if (commArray[0].equals("clearstorage")) {
                    delete.removeAll();
                    continue;
                }

                //OPERATIONS
                if (commArray[0].equals("getfiles") && commArray.length == 2) {
                    String path = commArray[1].equals("/root") ? "" : commArray[1];
                    printFiles(operations.getAllFiles(path));
                    continue;
                }

                if (commArray[0].equals("getdirs") && commArray.length == 2) {
                    String path = commArray[1].equals("/root") ? "" : commArray[1];
                    printFiles(operations.getAllDirectories(path));
                    continue;
                }

                if (commArray[0].equals("getall") && commArray.length == 2) {
                    String path = commArray[1].equals("/root") ? "" : commArray[1];
                    printFiles(operations.getAllFilesRecursive(path));
                    continue;
                }

                if (commArray[0].equals("download") && commArray.length == 2) {
                    operations.download(commArray[1]);
                    continue;
                }

                if (commArray[0].equals("upload") && commArray.length == 3) {
                    String toPath = commArray[2].equals("/root") ? "" : commArray[2];
                    operations.uploadFile(commArray[1], toPath);
                    continue;
                }

                if (commArray[0].equals("move") && commArray.length == 3) {
                    String toPath = commArray[2].equals("/root") ? "" : commArray[2];
                    operations.moveFile(commArray[1], toPath);
                    continue;
                }

                if (commArray[0].equals("getfileswe") && commArray.length == 3) {
                    String path = commArray[1].equals("/root") ? "" : commArray[1];
                    printFiles(operations.getAllFilesWithExtention(operations.getAllFiles(path), commArray[2]));
                    continue;
                }

                if (commArray[0].equals("sort") && commArray.length == 3) {
                    String path = commArray[1].equals("/root") ? "" : commArray[1];
                    printFiles(operations.getSortedBy(operations.getAllFiles(path), commArray[2]));
                    continue;
                }

                if (commArray[0].equals("between") && commArray.length == 4) {
                    String path = commArray[1].equals("/root") ? "" : commArray[1];
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date startDate = sdf.parse(commArray[2]);
                    Date endDate = sdf.parse(commArray[3]);
                    printFiles(operations.getInBetweenDates(operations.getAllFiles(path), startDate, endDate));
                    continue;
                }

                System.out.println("Incorrect command, for more information use command help");

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private static void help() {
        System.out.println("INFO:");
        System.out.println("    Path within storage must be relative");
        System.out.println("    Path outside of storage must be absolute");
        System.out.println("    When creating and logging to storage path must be absolute");
        System.out.println("    Root path must be entered as - /root");
        System.out.println("    Dates format: dd/MM/yyyy");
        System.out.println("STORAGE:");
        System.out.println("    Create Storage - createstorage path storage_name admin_name admin_psw");
        System.out.println("    Configure - config -maxsize nn -maxnof nn -une e1,e2...");
        System.out.println("    Add User - adduser u_name u_psw u_priv -> choose one of [ADMIN,RDCD,RD,RO]");
        System.out.println("    Log To Storage - login path name password");
        System.out.println("    Log Out - logout");
        System.out.println("CREATE:");
        System.out.println("    Create Directory - createdir dir_name");
        System.out.println("    Create Directories - createdirs dir1,dir2...");
        System.out.println("    Create File - createfile file_name");
        System.out.println("    Create Files - createfiles file1,file2...");
        System.out.println("DELETE:");
        System.out.println("    Delete Directory - deletedir dir_name");
        System.out.println("    Delete Directories - deletedirs dir1,dir2,...");
        System.out.println("    Delete File - deletefile file_name");
        System.out.println("    Delete Files - deletefiles file1,file2...");
        System.out.println("    Delete All - clearstorage");
        System.out.println("OPERATIONS:");
        System.out.println("    Get All Files - getfiles path");
        System.out.println("    Get All Directories - getdirs path");
        System.out.println("    Get All Files Recursive - getall path");
        System.out.println("    Download - download path");
        System.out.println("    Upload File - upload from_path to_path");
        System.out.println("    Move File - move from_path to_path");
        System.out.println("    Get All Files With Extention - getfileswe path e");
        System.out.println("    Get Sort By - sort path criteria -> sortByName, sortByDate, sortByModification and combinations ex. sortByName-sortByDate");
        System.out.println("    Get Files In Between Dates - between path start end");
    }

    private static void printFiles(List<FileMetadata> metadataList) {
        for (FileMetadata metadata: metadataList) {
            System.out.println(metadata);
        }
    }

}
