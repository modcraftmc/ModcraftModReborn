package fr.modcraftmc.modcraftmod.client;

import java.io.File;

public class FilesManager {

    public static char FP = File.separatorChar;

    public static boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");
    public static String BASE_PATH = windows ? System.getenv("appdata") : System.getenv("HOME");
    public static File DEFAULT_PATH = new File(BASE_PATH + FP + ".modcraftmc" + FP);
    public static File LAUNCHER_PATH = new File(DEFAULT_PATH, "launcher");
    public static File LOGS_PATH = new File(LAUNCHER_PATH, "logs");
    public static File OPTIONS_PATH = new File(LAUNCHER_PATH, "modcraftlauncher.json");
    public static File INSTANCES_PATH = new File(DEFAULT_PATH, "instances");
    public static File JAVA_PATH = new File(DEFAULT_PATH, "java");

    static {
        try {
            if (!DEFAULT_PATH.exists()) {
                DEFAULT_PATH.mkdirs();
            }
            if (!LAUNCHER_PATH.exists()) {
                LAUNCHER_PATH.mkdirs();
            }
            if (!OPTIONS_PATH.exists()) {
                OPTIONS_PATH.createNewFile();
            }
            if (!INSTANCES_PATH.exists()) {
                INSTANCES_PATH.mkdirs();
            }
            if (!JAVA_PATH.exists()) {
                JAVA_PATH.mkdirs();
            }

            if (!LOGS_PATH.exists()) {
                LOGS_PATH.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getDefaultPath() {
        return DEFAULT_PATH;
    }

    public File getOptionsPath() {
        return OPTIONS_PATH;
    }

    public File getInstancesPath() {
        return INSTANCES_PATH;
    }

    public File getJavaPath() {
        return JAVA_PATH;
    }

    public void setDefaultPath(File defaultPath) {
        DEFAULT_PATH = defaultPath;
    }

    public void setOptionsPath(File optionsPath) {
        OPTIONS_PATH = optionsPath;
    }

    public void setInstancesPath(File instancesPath) {
        INSTANCES_PATH = instancesPath;
    }

    public static void setJavaPath(File javaPath) {
        JAVA_PATH = javaPath;
    }

    public File getLauncherPath() {
        return LAUNCHER_PATH;
    }

    public void setLauncherPath(File launcherPath) {
        LAUNCHER_PATH = launcherPath;
    }
}
