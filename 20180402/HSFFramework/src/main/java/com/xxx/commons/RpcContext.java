package com.xxx.commons;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8.
 */
public class RpcContext {
    public static final String REMOTE_HOST="REMOTE_HOST";
    private Map<Object,Object> attchments=new HashMap<Object, Object>();
    private RpcContext(){
        try {
            attchments.put(REMOTE_HOST, InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final  ThreadLocal<RpcContext> RPC_CONTEXT=new ThreadLocal<RpcContext>();
    public static RpcContext getContext() {
        RpcContext rpcContext = RPC_CONTEXT.get();
        if(rpcContext==null){
            rpcContext=new RpcContext();
            RPC_CONTEXT.set(rpcContext);
        }
        return rpcContext;
    }
    public void setAttchment(Object key,Object value){
        attchments.put(key,value);
    }
    public Object getAttchment(Object key){
       return  attchments.get(key);
    }
    public Map<Object,Object> getAllAttchments(){
        return attchments;
    }
}
