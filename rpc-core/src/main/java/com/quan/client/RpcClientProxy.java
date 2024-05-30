package com.quan.client;

import com.quan.entity.RpcRequest;
import com.quan.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RPC 客户端动态代理
 * @author Quan
 */
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;

    /**
     * 构造函数
     * @param host
     * @param port
     */
    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 获取代理对象
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 代理对象调用方法时的操作
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        return ((RpcResponse) rpcClient.sendRequest(rpcRequest, host, port)).getData();
    }
}