package top.sharehome.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 静态文件生成器
 *
 * @author AntonyCheng
 */

public class StaticGenerator {
    public static void main(String[] args) {
        // 获取项目根路径
        String projectRootPath = System.getProperty("user.dir");
        System.out.println(projectRootPath);
        // 获取输入路径
        String inputPath = projectRootPath + File.separator + "code-generator-demo-projects" + File.separator + "acm-template";
        // 将输入路径下的文件写入项目根路径
        String outputPath = projectRootPath;
        // 复制
        copyFilesByHutool(inputPath, outputPath);
    }

    /**
     * 使用Hutool复制文件
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }

    /**
     * 使用递归复制文件
     *
     * @param inputPath  输入路径
     * @param outputPath 输出路径
     */
    public static void copyFilesByRecursion(String inputPath, String outputPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        try {
            recursion(inputFile, outputFile);
        } catch (Exception e) {
            System.out.println("文件复制失败");
            e.printStackTrace();
        }
    }

    /**
     * 递归方法
     *
     * @param inputFile  输入文件
     * @param outputFile 输出文件
     */
    private static void recursion(File inputFile, File outputFile) throws IOException {
        // 首先判断是文件还是目录
        if (inputFile.isDirectory()) {
            File destOutputFile = new File(outputFile, inputFile.getName());
            // 如果是目录，首先创建目标目录
            if (!destOutputFile.exists()) {
                destOutputFile.mkdirs();
            }
            // 获取目录下的所有内容
            File[] files = inputFile.listFiles();
            // 如果该目录下没有文件，那么就直接返回
            if (ArrayUtil.isEmpty(files)) {
                return;
            }
            // 如果有文件，那么遍历后递归
            for (File file : files) {
                // 递归拷贝到新创建的目标目录中
                recursion(file, destOutputFile);
            }
        } else {
            Path destPath = outputFile.toPath().resolve(inputFile.getName());
            Files.copy(inputFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
