package com.quick.wolf.transport;

import com.google.common.collect.Maps;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.core.extension.ExtensionLoader;
import com.quick.wolf.exception.WolfFrameworkException;
import com.quick.wolf.exception.WolfServiceException;
import com.quick.wolf.rpc.DefaultResponse;
import com.quick.wolf.rpc.Provider;
import com.quick.wolf.rpc.Request;
import com.quick.wolf.rpc.URL;
import com.quick.wolf.utils.WolfFrameworkUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/12:03
 */
public class ProviderMessageRouter implements MessageHandler {

    protected Map<String, Provider<?>> providers = Maps.newHashMap();

    protected AtomicInteger exportMethodCounter = new AtomicInteger(0);

    protected ProviderProtectedStrategy strategy;

    public ProviderMessageRouter() {
        this(null);
    }

    public ProviderMessageRouter(URL url) {
        String strategyName = url == null ? URLParamType.providerProtectedStrategy.getName() : url.getParameter(URLParamType.proxyRegistryUrlString.getName(), URLParamType.providerProtectedStrategy.getValue());
        strategy = ExtensionLoader.getExtensionLoader(ProviderProtectedStrategy.class).getExtension(strategyName);
        strategy.setMethodCounter(exportMethodCounter);
    }

    @Override
    public Object handler(Channel channel, Object message) {
        if (channel == null || message == null) {
            throw new WolfFrameworkException("RequestRouter handler(channel, message) params is null");
        }

        if (!(message instanceof Request)) {
            throw new WolfFrameworkException("RequestRouter message type not support: " + message.getClass());
        }

        Request request = (Request) message;
        String serviceKey = WolfFrameworkUtil.getServiceKey(request);
        Provider<?> provider = providers.get(serviceKey);

        if (provider == null) {
            provider = providers.get(request.getInterfaceName());
        }
        if (provider == null) {
            String errInfo = this.getClass().getSimpleName() + " handler Error: provider not exist serviceKey=" + serviceKey + " ";
            WolfServiceException exception = new WolfServiceException(errInfo);
            return WolfFrameworkUtil.buildErrorResponse(request, exception);
        }

        Method method = provider.lookupMethod(request.getMethodName(), request.getParamtersDesc());

    }
}
