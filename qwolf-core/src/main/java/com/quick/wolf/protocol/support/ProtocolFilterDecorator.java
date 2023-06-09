package com.quick.wolf.protocol.support;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.quick.wolf.common.URLParamType;
import com.quick.wolf.common.WolfConstants;
import com.quick.wolf.core.extension.ActivationComparator;
import com.quick.wolf.core.extension.ExtensionLoader;
import com.quick.wolf.core.extension.SpiMeta;
import com.quick.wolf.exception.WolfErrorMsgConstant;
import com.quick.wolf.exception.WolfFrameworkException;
import com.quick.wolf.exception.WolfServiceException;
import com.quick.wolf.filter.Filter;
import com.quick.wolf.filter.InitializableFilter;
import com.quick.wolf.rpc.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/16:46
 */
@Slf4j
public class ProtocolFilterDecorator implements Protocol {

    private final Protocol protocol;

    public ProtocolFilterDecorator(Protocol protocol) {
        if (protocol == null) {
            throw new WolfFrameworkException("Protocol is null when construct ProtocolFilterDecorator");
        }
        this.protocol = protocol;
    }

    @Override
    public <T> Exporter<T> export(Provider<T> provider, URL url) {
        return protocol.export(decorateProviderWithFilter(provider, url), url);
    }

    private <T> Provider<T> decorateProviderWithFilter(Provider<T> provider, URL url) {
        List<Filter> filters = getFilter(url, WolfConstants.NODE_TYPE_SERVICE);

        if (filters == null || filters.isEmpty()) {
            return provider;
        }

        Provider<T> lastProvider = provider;

        for (Filter filter : filters) {
            final Filter f = filter;
            if (f instanceof InitializableFilter) {
                ((InitializableFilter) f).init();
            }
            final Provider<T> finalProvider = lastProvider;
            lastProvider = new Provider<>() {
                @Override
                public T getProvider() {
                    return provider.getProvider();
                }

                @Override
                public Method lookupMethod(String methodName, String methodDesc) {
                    return finalProvider.lookupMethod(methodName, methodDesc);
                }

                @Override
                public Class<T> getInterface() {
                    return finalProvider.getInterface();
                }

                @Override
                public Response call(Request request) {
                    return f.filter(finalProvider, request);
                }

                @Override
                public void init() {
                    finalProvider.init();
                }

                @Override
                public void destory() {
                    finalProvider.destory();
                }

                @Override
                public boolean isAvailable() {
                    return finalProvider.isAvailable();
                }

                @Override
                public String desc() {
                    return finalProvider.desc();
                }

                @Override
                public URL getUrl() {
                    return finalProvider.getUrl();
                }
            };
        }

        return lastProvider;
    }

    protected List<Filter> getFilter(URL url, String key) {
        List<Filter> filters = Lists.newArrayList();
        List<Filter> defaultFilters = ExtensionLoader.getExtensionLoader(Filter.class).getExtensions(key);
        if (defaultFilters != null && !defaultFilters.isEmpty()) {
            filters.addAll(defaultFilters);
        }
        String paramFilters = url.getParameter(URLParamType.filter.getName());
        if (!Strings.isNullOrEmpty(paramFilters)) {
            String[] filterNames = paramFilters.split(WolfConstants.COMMA_SEPARATOR);
            List<String> waitRemoveFilters = Lists.newArrayList();

            for (String filterName : filterNames) {
                if (filterName.startsWith(WolfConstants.DISABLE_FILTER_PREFIX)) {
                    waitRemoveFilters.add(filterName.substring(WolfConstants.DISABLE_FILTER_PREFIX.length()));
                } else {
                    Filter extension = ExtensionLoader.getExtensionLoader(Filter.class).getExtension(filterName, false);
                    if (extension == null) {
                        log.warn("user set a invalid filter! name is : {}", filterName);
                    } else {
                        filters.add(extension);
                    }
                }
            }

            if (!waitRemoveFilters.isEmpty()) {
                for (String waitRemoveFilter : waitRemoveFilters) {
                    filters.removeIf(filter -> waitRemoveFilter.equals(filter.getClass().getAnnotation(SpiMeta.class).name()));
                }
            }
        }

        filters.sort(new ActivationComparator<>());
        Collections.reverse(filters);
        return filters;
    }
}
