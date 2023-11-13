package top.sharehome.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import top.sharehome.model.TemplateModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 动态文件生成
 */
public class DynamicGenerator {
    public static void main(String[] args) {
        String projectRootPath = System.getProperty("user.dir");
        TemplateModel acmData = new TemplateModel()
                .setAuthorName("AntonyCheng")
                .setLoop(true)
                .setOutputMsg("总和为：");
        doGenerate(acmData,
                projectRootPath + "/code-generator-demo/acm-template/src/top/sharehome/acm/MainTemplate.java.ftl",
                projectRootPath + "/code-generator-demo/acm-generate/src/top/sharehome/acm/MainTemplate.java");
    }

    /**
     * 通过模板动态生成
     *
     * @param templateConfig       模板模型
     * @param templateFilePreciseName 模板所在的精确路径
     * @param outputPath              目标文件输出路径
     */
    public static void doGenerate(Object templateConfig, String templateFilePreciseName, String outputPath) {
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
                out = new FileWriter(outputPath + "/" + FileUtil.getPrefix(templateFileName));
            } else {
                File outputParentFile = outputFile.getParentFile();
                if (!outputParentFile.exists()) {
                    outputParentFile.mkdirs();
                }
                out = new FileWriter(outputPath);
            }
            template.process(templateConfig, out);
            out.close();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}