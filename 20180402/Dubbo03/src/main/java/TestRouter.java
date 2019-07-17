import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/4/4.
 */
public class TestRouter {
    public static void main(String[] args) throws UnsupportedEncodingException {
       RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
       Registry registry = registryFactory.getRegistry(URL.valueOf("zookeeper://192.168.100.128:2181"));
       String rule=URL.encode("application = UserCenterConsumer => ");
       registry.unregister(URL.valueOf("condition://0.0.0.0/com.xxx.service.IUserService?category=routers&dynamic=false&rule=" +rule));

    }
}
