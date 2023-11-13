package top.sharehome.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 动态模板配置
 */
@Data
@Accessors(chain = true)
public class TemplateModel {

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 是否生成循环
     */
    private boolean loop;

    /**
     * 输出提示信息
     */
    private String outputMsg;

}