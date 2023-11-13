package top.sharehome.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import top.sharehome.model.TemplateModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 代码生成工具类
 *
 * @author AntonyCheng
 */
public class GenerateUtils {
    public static void main(String[] args) {
        String projectRootPath = System.getProperty("user.dir");
        TemplateModel acmData = new TemplateModel()
                .setAuthorName("AntonyCheng")
                .setLoop(false)
                .setOutputMsg("总和为：");
        // 获取输入路径
        String inputPath = projectRootPath + File.separator + "code-generator-demo" + File.separator + "acm-template";
        // 将输入路径下的文件写入项目根路径
        String outputPath = projectRootPath + File.separator + "code-generator-demo" + File.separator + "acm-generate";
        doGenerate(acmData, inputPath, outputPath);
    }

    public static void doGenerate(Object templateModel, String inputPath, String outputPath) {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        if (outputFile.getParent().contains(inputPath) || StrUtil.equals(inputPath, outputPath)) {
            throw new RuntimeException("非法操作");
        }
        try {
            recursion(templateModel, inputFile, outputFile);
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
    private static void recursion(Object templateModel, File inputFile, File outputFile) throws IOException {
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
                recursion(templateModel, file, destOutputFile);
            }
        } else {
            if (StrUtil.equals(FileUtil.getSuffix(inputFile), "ftl")) {
                templateGenerate(templateModel, inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
            } else {
                Path destPath = outputFile.toPath().resolve(inputFile.getName());
                Files.copy(inputFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * 模板文件生成
     *
     * @param templateModel           模板模型
     * @param templateFilePreciseName 模板所在的精确路径
     * @param outputPath              目标文件输出路径
     */
    private static void templateGenerate(Object templateModel, String templateFilePreciseName, String outputPath) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        File templateFile = new File(templateFilePreciseName);
        String templateParentPath = templateFile.getParent();
        String templateFileName = templateFile.getName();
        FileWriter out = null;
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateParentPath));
            configuration.setDefaultEncoding("UTF-8");
            configuration.setNumberFormat("0.######");
            Template template = configuration.getTemplate(templateFileName);
            File outputFile = new File(outputPath);
            if (StrUtil.isEmpty(FileUtil.getSuffix(outputFile))) {
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }
                out = new FileWriter(outputPath + File.separator + FileUtil.getPrefix(templateFileName));
            } else {
                File outputParentFile = outputFile.getParentFile();
                if (!outputParentFile.exists()) {
                    outputParentFile.mkdirs();
                }
                out = new FileWriter(outputPath);
            }
            template.process(templateModel, out);
            out.close();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
