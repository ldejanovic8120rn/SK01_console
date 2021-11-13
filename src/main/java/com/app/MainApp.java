package com.app;

import com.StorageManager;
import com.storage.Create;
import com.storage.Delete;
import com.storage.Operations;
import com.storage.Storage;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        // com.localimpl.Local && com.driveimpl.GoogleDrive
        try {
            Class.forName("com.localimpl.Local");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Storage storage = StorageManager.getStorage();
        Create create = StorageManager.getCreate();
        Delete delete = StorageManager.getDelete();
        Operations operations = StorageManager.getOperations();

        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            command = scanner.nextLine();
            if (command.equals("exit")) {
                return;
            }

            if (command.equals("help")) {
                help();
                continue;
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
}
