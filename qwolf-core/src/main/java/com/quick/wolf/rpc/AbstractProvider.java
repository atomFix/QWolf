package com.quick.wolf.rpc;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quick.wolf.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/11:35
 */
@Slf4j
public abstract class AbstractProvider<T> implements Provider<T> {

    protected Class<T> clazz;
    protected URL url;

    protected boolean alive = false;
    protected boolean close = false;

    protected Map<String, Method> methods = Maps.newHashMap();

    public AbstractProvider(URL url, Class<T> clazz) {
        this.clazz = clazz;
        this.url = url;
        
        initMethods(clazz);
    }

    private void initMethods(Class<T> clazz) {
        Method[] methods = clazz.getMethods();

        List<String> rmList = Lists.newArrayList();
        for (Method method : methods) {
            String methodDesc = ReflectUtils.getMethodDesc(method);
            this.methods.put(methodDesc, method);
            if (this.methods.get(method.getName()) == null) {
                this.methods.put(method.getName(), method);
            } else {
                rmList.add(method.getName());
            }
        }

        if (!rmList.isEmpty()) {
            for (String methodName : rmList) {
                this.methods.remove(methodName);
            }
        }
    }

    @Override
    public Class<T> getInterface() {
        return clazz;
    }

    @Override
    public Response call(Request request) {
        log.info("call request : {}", request);
        Response response = invoke(request);
        log.info("call response : {}", response);
        return response;
    }

    public abstract Response invoke(Request request);


    @Override
    public Method lookupMethod(String methodName, String methodDesc) {
        Method result = null;
        String fullMethodName = ReflectUtils.getMethodDesc(methodName, methodDesc);
        result = methods.get(fullMethodName);
        if (result == null && Strings.isNullOrEmpty(methodDesc)) {
            result = methods.get(methodName.substring(0, 1).toLowerCase() + methodName.substring(1));
        }
        return result;
    }

    @Override
    public void init() {
        this.alive = true;
    }

    @Override
    public void destroy() {
        this.alive = false;
        this.close = true;
    }

    @Override
    public boolean isAvailable() {
        return alive;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public String desc() {
        return null;
    }
}
