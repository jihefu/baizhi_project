import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2018/4/28.
 */
public class MemcachedClientTest {

    public static void main(String[] args) throws IOException {
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(
                AddrUtil.getAddresses("192.168.43.132:11211 192.168.43.132:11311"));
        // 使用一致性哈希 分布式算法
        // builder.setSessionLocator(new KetamaMemcachedSessionLocator());
        MemcachedClient memcachedClient = builder.build();
        try {
            // 设置key value  第二个参数；有效时长 0 表示永久
            memcachedClient.set("aaa", 0, "Hello,xmemcached");

            // 通过key获取指定value
            String value = memcachedClient.get("aaa");
//            System.out.println("hello=" + value);
//
//            // 删除key value
//            memcachedClient.delete("hello");
//            value = memcachedClient.get("hello");
            System.out.println("hello=" + value);
        } catch (MemcachedException e) {
            System.err.println("MemcachedClient operation fail");
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.err.println("MemcachedClient operation timeout");
            e.printStackTrace();
        } catch (InterruptedException e) {
            // ignore
        }
        try {
            //close memcached client
            memcachedClient.shutdown();
        } catch (IOException e) {
            System.err.println("Shutdown MemcachedClient fail");
            e.printStackTrace();
        }
    }
}
