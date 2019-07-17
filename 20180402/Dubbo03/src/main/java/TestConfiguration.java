import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/4/4.
 */
public class TestConfiguration {
    public static void main(String[] args) throws UnsupportedEncodingException {
        //RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
       // Registry registry = registryFactory.getRegistry(URL.valueOf("zookeeper://192.168.100.128:2181"));
       // registry.unregister(URL.valueOf("override://192.168.8.5:20880/com.xxx.service.IUserService?category=configurators&dynamic=false&disabled=true"));
       String url= URLEncoder.encode("override://192.168.8.5:20880/com.xxx.service.IUserService?category=configurators&dynamic=false&enabled=true&weight=200","utf-8");
        System.out.println(url);
    }
}
