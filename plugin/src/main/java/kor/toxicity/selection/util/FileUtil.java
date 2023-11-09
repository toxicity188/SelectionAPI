package kor.toxicity.selection.util;

import kor.toxicity.selection.data.FileName;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.function.Consumer;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileUtil {
    private FileUtil() {
        throw new SecurityException();
    }

    public static void loadFolder(Plugin plugin, String dir, Consumer<File> consumer) {
        var dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        var folder = new File(dataFolder, dir);
        if (!folder.exists()) folder.mkdir();
        var listFiles = folder.listFiles();
        if (listFiles != null) for (File listFile : listFiles) {
            consumer.accept(listFile);
        }
    }
    public static FileName getFileName(File file) {
        var name = file.getName().split("\\.");
        return new FileName(name[0], name.length > 1 ? name[1] : "");
    }
}
