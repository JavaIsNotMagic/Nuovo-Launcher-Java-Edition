package com.nl.launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;


import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.regex.PatternSyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.apache.commons.io.*;

@SuppressWarnings("all")
public class initFiles {
    public static void config_data(String file_path) throws IOException {
        File temp_file = new File(file_path);
        if (temp_file.exists()) {
            // System.out.println("File " + file_path + " exists");
        } else {
            FileWriter tfw = new FileWriter(temp_file);
            tfw.write("\n");
            tfw.flush();
            tfw.close();
        }

    }

    public static void download(URL url, String file_path) throws IOException {
        File temp = new File(file_path);
        if (temp.exists()) {
            // System.out.println("File " + file_path + " exists");
        } else {
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file_path));
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
        }
    }

    public static void createDirectories(List paths) {
        final String current_dir = System.getProperty("user.dir");
        File lib_root = new File(current_dir + "/.nuovo/minecraft/libs");
        Iterator paths_it = paths.iterator();

        while (paths_it.hasNext()) {
            String path = paths_it.next().toString();
            File lib_path = new File(lib_root + "/" + path);
            File lib_dir = new File(lib_path.getParent());
            lib_dir.mkdirs();
        }

    }
    public static void createObjectDriectories(String hash) {
        final String current_dir = System.getProperty("user.dir");
        String first_two = hash.substring(0, 2);
        File object_dir = new File(current_dir + "/.nuovo/minecraft/assets/objects/" + first_two + "/" + hash);
        //print("Making directory %s", object_dir.toString());
        object_dir.mkdirs();

    }
    
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width-1) + ".";
        } else {
            return s;
        }

    }
    
    static List ver_links = new ArrayList<String>();
    public static List getLinks(String version) throws IOException {
        String url = "https://mcversions.net/download/" + version;
        //print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        
        for (Element link : links) {
            //print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));

            ver_links.add(link.attr("abs:href"));
        }
        return ver_links;
    }
    static final List objects = new ArrayList<String>();
    public static List decodeObjects(String path) throws IOException, NuovoGenericError {
        Pattern p = Pattern.compile("^?([a-f0-9]){2,41}");
        String search_string = FileUtils.readFileToString(new File(path));
        //System.out.println(search_string);
        Matcher m = p.matcher(search_string);
        while(m.find()) {
            if(m.group().toString().length() > 10) {
                objects.add(m.group().toString());
            } else {
                ; //do nothing
            }
        }
        return objects;
    }
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    public static void RecursivePrint(File[] arr,int index,int level, String file) throws IOException {
         try{
            // terminate condition 
            if(index == arr.length) {
                ;
            } 
            // for files 
            String os_name = System.getProperty("os.name").toLowerCase();
            String path_sep = "";
            switch(os_name) {
                case "linux":
                    path_sep = ":";
                    break;
                case "windows":
                    path_sep = ";";
                    break;
            }
            
            if(arr[index].isFile()) { 
                //System.out.println(arr[index].getAbsolutePath());
                String path = new String(arr[index].getAbsolutePath() + path_sep);
                FileWriter tfw = new FileWriter(new File(file), true);
                tfw.append(path);
                tfw.flush();
                tfw.close();

            // for sub-directories 
            } else if(arr[index].isDirectory()) { 
                //System.out.println("[" + arr[index].getAbsolutePath() + "]"); 
                // recursion for sub-directories 
                RecursivePrint(arr[index].listFiles(), 0, level + 1, file); 
            } 
            
            // recursion for main directory 
             RecursivePrint(arr,++index, level, file);
         } catch(ArrayIndexOutOfBoundsException ae) {
             ;
         }
    } 
}