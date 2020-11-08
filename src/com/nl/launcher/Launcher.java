package com.nl.launcher;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@SuppressWarnings("all")
public class Launcher {
    static final String current_dir = System.getProperty("user.dir");

    static final File root = new File(current_dir + "/.nuovo");
    static final File game_dir = new File(root + "/minecraft");
    static final File assets_dir = new File(game_dir + "/assets");
    static final File index_dir = new File(assets_dir + "/indexes");
    static final File object_dir = new File(assets_dir + "/objects");
    static final File versions_dir = new File(game_dir + "/versions");
    static final File lib_dir = new File(game_dir + "/libs");
    static final File natives_dir = new File(game_dir + "/natives");

    static final String version_manifest = new String(current_dir + "/.nuovo/minecraft/version_manifest.json");
    static final String object_path = new String(current_dir + "/.nuovo/minecraft/object.json");
    static final String classpath = new String(current_dir + "/.nuovo/minecraft/classpath.txt");
    static final String resource_base = "http://resources.download.minecraft.net/";

    public static void create_folders() {
    	root.mkdir();
        game_dir.mkdir();
        assets_dir.mkdir();
        index_dir.mkdir();
        object_dir.mkdir();
        versions_dir.mkdir();
        lib_dir.mkdir();
        natives_dir.mkdir();
    }
    public static List setup() throws IOException, MalformedURLException, ParseException {

        final URL version_manifest_url = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");

        System.out.println("Create asset directories");
        root.mkdir();
        game_dir.mkdir();
        assets_dir.mkdir();
        index_dir.mkdir();
        object_dir.mkdir();
        versions_dir.mkdir();
        lib_dir.mkdir();
        natives_dir.mkdir();
        System.out.println("Create files needed for launcher");
        initFiles.config_data(classpath);
        System.out.println("Download and parse the version manifest");
        initFiles.download(version_manifest_url, version_manifest);

        // Get the version id and url
        Object obj = new JSONParser().parse(new FileReader(version_manifest));
        JSONObject jo = (JSONObject) obj;
        JSONArray ja = (JSONArray) jo.get("versions");
        Iterator it = ja.iterator();

        List<String> versions_supported = new ArrayList<String>();
        while (it.hasNext()) {
            String[] manifest_comma = it.next().toString().split("[,{}]+");
            List versions = new ArrayList<String>();
            versions.add(manifest_comma[5]);
            Iterator url = versions.iterator();

            while (url.hasNext()) {
                String vurl = url.next().toString();
                String replacement = "/";
                String[] ver_url_split = vurl.replace('\\', replacement.charAt(0)).split("\"url\":\"");
                String version_url = ver_url_split[1].replaceAll(".$", "");
                String[] id = vurl.split("[json\\/]");
                String version = id[13].replaceAll(".$", "");
                //System.out.println("Version: " + version + " URL: " + version_url.replaceAll("https:////", "https://").replaceAll("//", "/").replaceAll("https:/", "https://"));

                // Create a list of all the versions
                versions_supported.add(version);

                // Download all the indexes
                initFiles.download(new URL(version_url.replaceAll("https:////", "https://").replaceAll("//", "/").replaceAll("https:/", "https://")), index_dir + "/" + version + ".json");
            }

        }

        System.out.println("Setup completed!");
        return versions_supported;

    }

    public static void download_libs(String version_data) throws FileNotFoundException, IOException, ParseException,
            NuovoGenericError {
        //Globals
        List version_paths = new ArrayList<String>();
        String version = version_data;
        
        //firsts things first, get the client version
        List links = initFiles.getLinks(version);
        Iterator links_it = links.iterator();
        while(links_it.hasNext()) {
            if(links_it.next().toString().contains("jar")) {
                URL client_url = new URL(links_it.next().toString());
                //System.out.println("Version URL: " + client_url);
                initFiles.download(client_url, versions_dir + "/client-" + version + ".jar");
                System.out.println("Done!");
            }
        }

        
        Object version_obj = new JSONParser().parse(new FileReader(index_dir + "/" + version + ".json"));
        JSONObject version_jo = (JSONObject) version_obj;
        JSONObject obj_1 = (JSONObject) version_jo.get("assetIndex");
        String object_url = (String) obj_1.get("url").toString();

        //get the libraries
        JSONArray libArray = (JSONArray) version_jo.get("libraries");
        Iterator lib_iterator = libArray.iterator();
        while(lib_iterator.hasNext()) {
            //System.out.println(lib_iterator.next().toString());
            JSONObject libObject = (JSONObject) lib_iterator.next();
            JSONObject root = (JSONObject) libObject.get("downloads");
            JSONObject artifact = (JSONObject) root.get("artifact");
            try {
                String path = artifact.get("path").toString();
                String url = artifact.get("url").toString();
                version_paths.add(path);
                //Make all the directories needed
                initFiles.createDirectories(version_paths, version);
                //Download the libraries
                String full_path = new File(lib_dir + "/" + version + "/" + path).toString();
                initFiles.download(new URL(url), full_path);
                //Download the objects definition
                initFiles.download(new URL(object_url), object_path);
            } catch(NullPointerException ne) {
                ;
            }
            // download the natives
            String natives_os = System.getProperty("os.name").toLowerCase();
            String natives_os_arch = System.getProperty("os.arch");
            for(Iterator iterator = root.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                String thing = root.get(key).toString();
                if(thing.contains(natives_os)) {
                    String things[] = thing.split("url");
                    for(String temp : things) {
                        String thing_1 = temp.replaceAll("[\",]", "");
                        String things_1[] = thing_1.split("}");
                        for(String x: things_1) {
                            if(x.endsWith("natives-" + natives_os + ".jar")) {
                                String _native_url = x.replace("\\", "/");
                                String native_url = _native_url.replaceAll(":https:////", "https://").replaceAll("//", "/").replaceAll("https:/", "https://");
                                String native_path = new URL(native_url).getFile();
                                String native_dir_path = new File(native_path).getParent();
                                //make the directory
                                File temp_file = new File(natives_dir + native_dir_path);
                                if(temp_file.mkdirs()) {
                                    ;
                                } else {
                                    if(temp_file.exists()) {
                                        ;
                                    } else {
                                        throw new NuovoGenericError("Cannot creative natives path: " + native_dir_path);
                                    }
                                }
                                //download and extract the native
                                initFiles.download(new URL(native_url), natives_dir + native_path);
                                initFiles.unzip(natives_dir + native_path, natives_dir + "/" + natives_os);
                            }
                        }
                    }
                }
                if(thing.contains(natives_os)) {
                    String things[] = thing.split("url");
                    for(String temp : things) {
                        String thing_1 = temp.replaceAll("[\",]", "");
                        String things_1[] = thing_1.split("}");
                        for(String x: things_1) {
                            if(x.endsWith("natives-" + natives_os + "-" + natives_os_arch + ".jar")) {
                                String _native_url = x.replace("\\", "/");
                                String native_url = _native_url.replaceAll(":https:////", "https://").replaceAll("//", "/").replaceAll("https:/", "https://");
                                String native_path = new URL(native_url).getFile();
                                String native_dir_path = new File(native_path).getParent();
                                //make the directory
                                File temp_file = new File(natives_dir + native_dir_path);
                                if(temp_file.mkdirs()) {
                                    ;
                                } else {
                                    if(temp_file.exists()) {
                                        ;
                                    } else {
                                        throw new NuovoGenericError("Cannot creative natives path: " + natives_dir + native_dir_path);
                                    }
                                }
                                //download and extract the native
                                initFiles.download(new URL(native_url), natives_dir + native_path);
                                initFiles.unzip(natives_dir + native_path, natives_dir + "/" + natives_os);
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    public static void main(String[] args) throws IOException, ParseException, NuovoGenericError, InterruptedException, ArrayIndexOutOfBoundsException {
        System.out.println("DEBUG");
        System.out.println("----------");
        System.out.println("Selected Version: " + args[0]);
        System.out.println("Version Type: " + args[1]);
        System.out.println("----------");
        
    	//Main
        System.out.println("Basic setup");
        setup();
        System.out.println("Download game and respective version libraries");
        download_libs(args[0]);
        System.out.println("Prepare game for launch");
        List objects = initFiles.decodeObjects(object_path);
        Iterator objects_iterator = objects.iterator();
        System.out.println("Downloading resources...");
        while(objects_iterator.hasNext()) {
            String hash = objects_iterator.next().toString();
            initFiles.createObjectDriectories(hash);
            String resource_path = object_dir + "/" + hash.substring(0,2) + "/" + hash + "/" + hash;
            String resource_url = resource_base + hash.substring(0,2) + "/" + hash;
            initFiles.download(new URL(resource_url), resource_path);
        }
        System.out.println("Finished downloading resources!");
        //Now launch minecraft
        File[] search_dir = lib_dir.listFiles();
        initFiles.RecursivePrint(search_dir, 0, 0, classpath);
        File[] client_dir = versions_dir.listFiles();
        initFiles.RecursivePrint(client_dir, 0, 0, classpath);
        String ver = new String("1.7.10");
        System.out.println("Starting Minecraft " + ver);
        LauncherMain.Launch(ver, game_dir.toString(), assets_dir.toString(), versions_dir.toString() + "/" + "client-" + ver + ".jar", classpath);  
    };
    public static void launcher(String args[]) throws IOException, InterruptedException, NuovoGenericError, ParseException {
    	 System.out.println("DEBUG");
         System.out.println("----------");
         System.out.println("Selected Version: " + args[0]);
         System.out.println("Version Type: " + args[1]);
         System.out.println("----------");
         
     	//Main
         System.out.println("Basic setup");
         setup();
         System.out.println("Download game and respective version libraries");
         download_libs(args[0]);
         System.out.println("Prepare game for launch");
         List objects = initFiles.decodeObjects(object_path);
         Iterator objects_iterator = objects.iterator();
         System.out.println("Downloading resources...");
         while(objects_iterator.hasNext()) {
             String hash = objects_iterator.next().toString();
             initFiles.createObjectDriectories(hash);
             String resource_path = object_dir + "/" + hash.substring(0,2) + "/" + hash + "/" + hash;
             String resource_url = resource_base + hash.substring(0,2) + "/" + hash;
             initFiles.download(new URL(resource_url), resource_path);
         }
         System.out.println("Finished downloading resources!");
         //Now launch minecraft
         File[] search_dir = lib_dir.listFiles();
         initFiles.RecursivePrint(search_dir, 0, 0, classpath);
         
         FilenameFilter filter = (dir,name) -> name.contains(args[0]);
         File[] hello = versions_dir.listFiles(filter);
         initFiles.RecursivePrint(hello, 0, 0, classpath);
         
         String ver = new String(args[0]);
         System.out.println("Starting Minecraft " + ver);
         LauncherMain.LaunchWithTokens(args[4], args[2], args[3], ver, game_dir.toString(), assets_dir.toString(), versions_dir.toString() + "/" + "client-" + ver + ".jar", classpath); 
    }
}