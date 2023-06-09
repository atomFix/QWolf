package com.quick.wolf.config.handler;

import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.exception.WolfFrameworkException;
import com.quick.wolf.protocol.support.ProtocolFilterDecorator;
import com.quick.wolf.register.Register;
import com.quick.wolf.register.RegisterFactory;
import com.quick.wolf.rpc.Protocol;
import com.quick.wolf.core.extension.ExtensionLoader;
import com.quick.wolf.core.extension.SpiMeta;
import com.quick.wolf.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/10:53
 */
@SpiMeta(name = WolfConstants.STR_DEFAULT_VALUE)
@Slf4j
public class SimpleConfigHandler implements ConfigHandler {

    @Override
    public <T> Exporter<T> export(Class<T> interfaceClass, T ref, List<URL> registers, URL serviceUrl) {
        String protocolName = serviceUrl.getParameter(URLParamType.protocol.getName(), URLParamType.protocol.getValue());
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolName);
        Provider<T> provider = getProvider(protocol, ref, serviceUrl, interfaceClass);

        ProtocolFilterDecorator filterDecorator = new ProtocolFilterDecorator(protocol);
        Exporter<T> export = filterDecorator.export(provider, serviceUrl);

        register(registers, serviceUrl);
        return export;
    }

    @Override
    public <T> void unexport(List<Exporter<T>> exporters, Collection<URL> registryUrls) {
        for (Exporter<T> exporter : exporters) {
            unRegister(registryUrls, exporter.getUrl());
            exporter.unexport();
        }
    }

    private void register(List<URL> registers, URL serviceUrl) {
        for (URL url : registers) {
            RegisterFactory registerFactory = ExtensionLoader.getExtensionLoader(RegisterFactory.class).getExtension(serviceUrl.getProtocol(), false);
            if (registerFactory == null) {
                throw new WolfFrameworkException("registerFactory has error! protocol value is : " + serviceUrl.getProtocol());
            }
            Register register = registerFactory.getRegister(url);
            register.register(serviceUrl);
        }
    }

    private <T> Provider<T> getProvider(Protocol protocol, T ref, URL serviceUrl, Class<T> refClazz) {
        if (protocol instanceof ProviderFactory) {
            return ((ProviderFactory) protocol).getProvider(ref, serviceUrl, refClazz);
        } else {
            return new DefaultProvider<>(ref, serviceUrl, refClazz);
        }
    }

    private void unRegister(Collection<URL> registers, URL serviceUrl) {
        for (URL url : registers) {
            try {
                RegisterFactory factory = ExtensionLoader.getExtensionLoader(RegisterFactory.class).getExtension(url.getProtocol());
                Register register = factory.getRegister(url);
                register.unregister(serviceUrl);
            } catch (Exception e) {
                log.warn("unRegister has error", e);
            }
        }
    }
}
