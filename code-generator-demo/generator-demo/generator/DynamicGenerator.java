package top.sharehome.generator;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import top.sharehome.model.AcmTemplateConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 动态文件生成
 */
public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        String projectRootPath = System.getProperty("user.dir");
        AcmTemplateConfig acmData = new AcmTemplateConfig()
                .setAuthorName("AntonyCheng")
                .setLoop(false)
                .setOutputMsg("总和为：");

        doGenerate(acmData,
                projectRootPath + "/code-generator-demo/acm-template/src/top/sharehome/acm/MainTemplate.java.ftl",
                projectRootPath + "/code-generator-demo/acm-generate/src/top/sharehome/acm");
    }

    /**
     * 通过模板动态生成
     *
     * @param acmTemplateConfig 模板模型
     * @param templateFilePath  模板所在全路径
     * @param outputPath        目标文件输出路径
     */
    public static void doGenerate(AcmTemplateConfig acmTemplateConfig, String templateFilePath, String outputPath) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        File templateFile = new File(templateFilePath);
        String templateFileName = templateFile.getName();
        String templateParentPath = templateFile.getParent();
        FileWriter out = null;
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateParentPath));
            configuration.setDefaultEncoding("UTF-8");
            configuration.setNumberFormat("0.######");
            Template template = configuration.getTemplate(templateFileName);
            out = new FileWriter(outputPath + "/" + FileUtil.getPrefix(templateFileName));
            template.process(acmTemplateConfig, out);
            out.close();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}