package top.dooper.utils;

import java.io.File;

public class DirectoryUtils {
    public static String getDirectoryHierarchy(String directoryPath) {
        StringBuilder result = new StringBuilder();
        File rootDirectory = new File(directoryPath);

        if (rootDirectory.exists() && rootDirectory.isDirectory()) {
            result.append(printDirectoryHierarchy(rootDirectory, 0));
        } else {
            result.append("目录不存在或不是一个有效的目录");
        }

        return result.toString();
    }

    private static String printDirectoryHierarchy(File directory, int level) {
        StringBuilder hierarchy = new StringBuilder();

        if (directory.isDirectory()) {
            // 拼接当前目录
            hierarchy.append(getIndentation(level)).append(directory.getName()).append(" (目录)\n");

            File[] subFiles = directory.listFiles();
            if (subFiles != null) {
                for (File file : subFiles) {
                    if (file.isDirectory() || file.isFile()) {
                        // 递归调用，拼接子目录和文件
                        hierarchy.append(getIndentation(level + 1)).append(file.getName());
                        if (file.isDirectory()) {
                            hierarchy.append(" (目录)");
                        } else {
                            hierarchy.append(" (文件)");
                        }
                        hierarchy.append("\n");
                    }
                    if (file.isDirectory()) {
                        // 递归调用，拼接子目录和文件
                        hierarchy.append(printDirectoryHierarchy(file, level + 1));
                    }
                }
            }
        }

        return hierarchy.toString();
    }

    private static String getIndentation(int level) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentation.append("    "); // 4个空格作为缩进
        }
        return indentation.toString();
    }
    static int flag = 0;
    public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            flag = 0;
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
}
