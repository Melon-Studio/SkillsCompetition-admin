package top.dooper.utils;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.util.List;

public class UnPackageUtil {
    /**
     * zip文件解压
     *
     * @param destPath 解压文件路径
     * @param zipFile  压缩文件
     * @param targetFolder 解压文件夹名称
     */
    public static int unPackZip(File zipFile, String destPath, String targetFolder) {
        String password = null;
        try {
            ZipFile zip = new ZipFile(zipFile);
            zip.setFileNameCharset("GBK");
            if (zip.isEncrypted() && (password == null || password.isEmpty())) {
                return -1;
            } else if (password != null && !password.isEmpty()) {
                zip.setPassword(password);
            }

            File targetDir = new File(destPath, targetFolder);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            zip.extractAll(targetDir.getAbsolutePath());

            // 检查目标文件夹中是否存在 index.html 文件
            File[] files = targetDir.listFiles();
            boolean indexHtmlExists = false;
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equalsIgnoreCase("index.html")) {
                        indexHtmlExists = true;
                        break;
                    }
                }
            }

            if (!indexHtmlExists) {
                if (targetDir.exists()) {
                    deleteDirectory(targetDir);
                }
                return -3;
            }

            if (zipFile.exists()) {
                zipFile.delete();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return -2;
        }
        return 0;
    }

    private static void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        dir.delete();
    }
}
