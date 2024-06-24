package cn.itcast.freemarker.test;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试字符串模板
 */
public class FreeMarkerTest02
{
    private Configuration cfg;

    @Before
    public void init() {
        // 1、模板实例
        cfg = new Configuration();
    }

    /**
     * com.${p1}.${p1}.${p1}.User
     */
    @Test
    public void test() throws Exception {
        // 2、模板加载器
        cfg.setTemplateLoader(new StringTemplateLoader());
        // 3、模板
        final String templateStr = "欢迎您，${username}";
        final Template template = new Template("name1", templateStr, cfg);
        // 4、处理数据
        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("username", "张三");
        final StringWriter out = new StringWriter();
        template.process(dataModel, out);
        System.out.println(out);
    }
}
