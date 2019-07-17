import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/27.
 */
public class FreemarkerTest {

    public static void main(String[] args) throws IOException, TemplateException {
        // 创建Map集合封装动态数据
        Map<String,Object> datas = new HashMap<String,Object>();
        datas.put("name","zs");

        datas.put("ball", Arrays.asList("basketball","football"));

        HashMap<String, Object> citys = new HashMap<String, Object>();
        citys.put("bj","北京");
        citys.put("tj","天津");

        datas.put("citys",citys);

        datas.put("user",new User(1,"ls"));

        datas.put("date",new Date());

        datas.put("exists",null);

        datas.put("boolean",false);

        // 生成文本输出
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDefaultEncoding("utf-8");
        configuration.setDirectoryForTemplateLoading(new File("E:\\workspace\\20180402\\freemarker_demo\\template"));

        // 获取指定的模板对象
        Template template = configuration.getTemplate("index.html");

        template.process(datas,new FileWriter("E:\\index.html"));
    }
}
