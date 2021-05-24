package com.nl.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import org.apache.commons.io.*;

@SuppressWarnings("all")
public class LauncherMain {
    public static void Launch(String version, String game_direcotry, String assets_root, String client, String path)
            throws IOException, InterruptedException {
        String os_name = System.getProperty("os.name").toLowerCase();
        String natives_path = System.getProperty("user.dir") + "/.nuovo/minecraft/natives/" + os_name;
        String classpath = FileUtils.readFileToString(new File(path)) + client;
        classpath = classpath.replace("\n", "").replace("\r", "");
        String uuid = (String) UUID.randomUUID().toString();
        String empty = "{}";
        String root = "java -Djava.library.path=" + natives_path + " -cp " + classpath + " net.minecraft.client.main.Main" + " --username test" + " --version " + version + " --gameDir " + game_direcotry + " --assetsDir " + assets_root + " --assetIndex " + version + " --uuid " + uuid + " --accessToken " + uuid + " --userProperties " + empty + " --userType legacy";

        Process process = Runtime.getRuntime().exec(root);
        //StreamGobbler sg = new StreamGobbler(process.getInputStream(), System.out::println);
        //Executors.newSingleThreadExecutor().submit(sg);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
    public static void LaunchWithTokens(String username, String at, String ct, String version, String game_direcotry, String assets_root, String client, String path)
            throws IOException, InterruptedException {
        String os_name = System.getProperty("os.name").toLowerCase();
        String natives_path = System.getProperty("user.dir") + "/.nuovo/minecraft/natives/" + os_name;
        File classpath = new File(path).getAbsoluteFile();
        String cp = FileUtils.readFileToString(classpath);
        cp = cp.replace("\n", "").replace("\r", "");
        String empty = "{}";

        //Issue #0001: java.io.IOException: error=7, Argument list too long.
        //Fix: Write the command to a file, then execute
        String command = "java -Djava.library.path=" + natives_path + " -cp " + cp + " net.minecraft.client.main.Main" + " --username " + username + " --version " + version + " --gameDir " + game_direcotry + " --assetsDir " + assets_root + " --assetIndex " + version + " --uuid " + ct + " --accessToken " + at + " --userProperties " + empty + " --userType legacy";
        File command_file = new File(System.getProperty("user.dir") + "/bin/launch");
        //Set the execute permissions, and allow anyone to execute
        command_file.setExecutable(true, false);
        //Write the command to disk
        PrintWriter pw1 = new PrintWriter(command_file);
        pw1.write(command);
        pw1.flush();
        pw1.close();

        //Execute
        System.out.println("Command to execute: " + command);
        Process process = Runtime.getRuntime().exec(System.getProperty("user.dir") + "/bin/launch");
        StreamGobbler sg = new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(sg);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}
