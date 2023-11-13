import freemarker.core.Configurable;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FreeMarkerTest {
    public static void main(String[] args) throws IOException, TemplateException {
        // 创建配置对象
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        // 设置模板所在目录
        configuration.setDirectoryForTemplateLoading(new File("code-generator-basic/src/main/resources/templates"));
        // 设置模板的默认编码
        configuration.setDefaultEncoding("UTF-8");
        // 设置数字格式
        configuration.setNumberFormat("0.###");

        // 加载模板
        Template template = configuration.getTemplate("web.html.ftl");

        // 创建数据模型
        HashMap<String, Object> dataModel = new HashMap<>() {
            {
                put("currentYear", 2023);
                put("menuItems", new ArrayList<HashMap<String, String>>() {
                    {
                        add(new HashMap<>() {
                            {
                                put("url", "https://abc.cn");
                                put("label", "abc");
                            }
                        });
                        add(new HashMap<>() {
                            {
                                put("url", "https://def.cn");
                                put("label", "def");
                            }
                        });
                    }
                });
            }
        };

        // 生成文件路径和名称
        FileWriter out = new FileWriter("web.html");

        // 生成文件
        template.process(dataModel, out);

        // 关闭文件流对象
        out.close();
    }
}
