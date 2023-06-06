package com.quick.wolf.core.extension;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quick.wolf.exception.WolfException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Description: SPI Loader
 * @author: liukairong1
 * @date: 2023/06/06/09:48
 */
@Slf4j
public class ExtensionLoader<T> {

    public static final String MAPPING_CONFIG_PREFIX = "META-INF/services/";
    private static final Map<Class<?>, ExtensionLoader<?>> extensionLoaders = Maps.newConcurrentMap();
    private final Class<T> type;
    private final ClassLoader classLoader;
    private final Object LOCK = new Object();
    private Map<String, T> singletionInstances;
    private Map<String, Class<T>> extensionClass;
    private volatile boolean init;
    private volatile Scope scope = Scope.PROTOTYPE;


    private ExtensionLoader(Class<T> clazz) {
        this(clazz, Thread.currentThread().getContextClassLoader());
    }

    private ExtensionLoader(Class<T> clazz, ClassLoader classLoader) {
        this.singletionInstances = Maps.newConcurrentMap();
        this.classLoader = classLoader;
        this.type = clazz;
        this.init = false;
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(clazz);

        if (loader == null) {
            synchronized (ExtensionLoader.class) {
                if (extensionLoaders.get(clazz) == null) {
                    loader = new ExtensionLoader<>(clazz);
                    extensionLoaders.put(clazz, loader);
                }
            }
        }
        return loader;
    }

    private static void failError(String msg, URL url, int lineNumber) {
        throw new WolfException(msg + " : " + url.getPath() + " : " + lineNumber);
    }

    private static void failLog(String msg) {
        log.error(msg);
    }

    private static void failThrows(String msg) {
        throw new WolfException(msg);
    }

    public T getExtension(String name) {
        return getExtension(name, true);
    }

    public T getExtension(String name, boolean throwException) {
        checkInit();

        if (Strings.isNullOrEmpty(name)) {
            if (throwException) {
                failThrows("get extension fail. extension name is empty");
            }
            return null;
        }

        if (scope.equals(Scope.SINGLETON)) {
            return getSingletonInstance(name, throwException);
        }

        return getPrototypeInstance(name, throwException);
    }

    public List<T> getExtensions(String key) {
        checkInit();

        if (extensionClass.size() == 0) {
            return Collections.emptyList();
        }
        List<T> instances = Lists.newArrayList();

        for (Map.Entry<String, Class<T>> entry : extensionClass.entrySet()) {
            Activation activation = entry.getValue().getAnnotation(Activation.class);
            if (Strings.isNullOrEmpty(key)) {
                instances.add(getExtension(entry.getKey()));
            } else if (activation != null && activation.key() != null) {
                for (String k : activation.key()) {
                    if (key.equals(k)) {
                        instances.add(getExtension(entry.getKey()));
                        break;
                    }
                }
            }
        }
        instances.sort(new ActivationComparator<>());
        return instances;
    }

    private T getPrototypeInstance(String name, boolean throwException) {
        Class<T> clazz = extensionClass.get(name);
        if (clazz == null) {
            if (throwException) {
                failThrows("Prototype instance not found class type, name is " + name);
            }
            return null;
        }
        T instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            failThrows("Prototype instance has error " + name + ", " + e.getMessage());
        }
        return instance;
    }


    private void checkInit() {
        if (init) {
            return;
        }
        loadExtensionClasses();
    }

    private void loadSingleInstances() {
        if (extensionClass == null || extensionClass.isEmpty()) {
            failLog("class " + type.getSimpleName() + " not found any instance!");
            return;
        }
        Spi spi = type.getAnnotation(Spi.class);
        if (spi == null) {
            return;
        }
        Scope scope = spi.scope();
        if (!scope.equals(Scope.SINGLETON)) {
            return;
        }

        for (Map.Entry<String, Class<T>> entry : extensionClass.entrySet()) {
            getSingletonInstance(entry.getKey(), true);
        }
        this.scope = Scope.SINGLETON;
    }

    private T getSingletonInstance(String name, boolean throwException) {
        T instance = singletionInstances.get(name);

        if (instance != null) {
            return instance;
        }

        Class<T> clazz = extensionClass.get(name);
        if (clazz == null) {
            if (throwException) {
                failThrows("Failed to initialize singleton object！" + name);
            }
            return null;
        }

        synchronized (singletionInstances) {
            instance = singletionInstances.get(name);
            if (instance != null) {
                return instance;
            }
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
                singletionInstances.put(name, instance);
            } catch (Exception e) {
                if (throwException) {
                    failThrows("Failed to initialize singleton object！" + name);
                }
                return null;
            }
        }
        return instance;
    }

    private void loadExtensionClasses() {
        if (init) {
            return;
        }
        synchronized (LOCK) {
            if (!init) {
                extensionClass = loadExtensionClassesWithPrefix();
                // use pre init single instance
                loadSingleInstances();
                init = true;
            }
        }
    }

    /**
     * spi load class
     */
    private Map<String, Class<T>> loadExtensionClassesWithPrefix() {
        String fullName = MAPPING_CONFIG_PREFIX + type.getName();
        Set<String> classNames = Sets.newHashSet();

        try {
            Enumeration<URL> urls;
            if (classLoader == null) {
                urls = ClassLoader.getSystemResources(fullName);
            } else {
                urls = classLoader.getResources(fullName);
            }
            if (urls == null || !urls.hasMoreElements()) {
                return Maps.newConcurrentMap();
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                parseUrl(url, classNames);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loadClass(classNames);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Class<T>> loadClass(Set<String> classNames) {
        Map<String, Class<T>> result = Maps.newConcurrentMap();

        for (String className : classNames) {
            Class<T> clazz;
            try {
                if (classLoader == null) {
                    clazz = (Class<T>) Class.forName(className);
                } else {
                    clazz = (Class<T>) Class.forName(className, true, classLoader);
                }

                checkExtension(clazz);

                String spiName = getSpiName(clazz);

                if (result.containsKey(spiName)) {
                    failThrows("Error spiName already exist: " + spiName);
                } else {
                    result.put(spiName, clazz);
                }
            } catch (ClassNotFoundException e) {
                failLog(e.getMessage());
            }
        }
        return result;
    }

    private String getSpiName(Class<T> clazz) {
        SpiMeta annotation = clazz.getAnnotation(SpiMeta.class);
        return annotation != null && !Strings.isNullOrEmpty(annotation.name()) ? annotation.name() : clazz.getSimpleName();
    }

    private void checkExtension(Class<T> clazz) {
        checkClassPublic(clazz);
        checkConstructorPublic(clazz);
        checkClassInherit(clazz);
    }

    private void checkClassInherit(Class<T> clazz) {
        if (!type.isAssignableFrom(clazz)) {
            failThrows("Error is not instanceof " + clazz.getName());
        }
    }

    private void checkConstructorPublic(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 0) {
            failThrows("Error has no public no-args constructor : " + clazz.getName());
        }

        for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterCount() == 0) {
                return;
            }
        }

        failThrows("Error has no public no-args constructor : " + clazz.getName());
    }

    private void checkClassPublic(Class<T> clazz) {
        if (!Modifier.isPublic(clazz.getModifiers())) {
            failThrows("Error is not a public class :" + clazz.getName());
        }
    }

    private void parseUrl(URL url, Set<String> classNames) {
        try (InputStream inputStream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line = null;
            int indexNumber = 0;
            while ((line = reader.readLine()) != null) {
                indexNumber++;
                parseLine(url, line, indexNumber, classNames);
            }
        } catch (IOException e) {
            failLog(e.getMessage());
        }
    }

    /**
     * parse services meta-inf file
     */
    private void parseLine(URL url, String line, int indexNumber, Set<String> classNames) {
        int ci = line.indexOf("#");

        if (ci >= 0) {
            line = line.substring(0, ci);
        }
        line = line.trim();
        if (line.length() == 0) {
            return;
        }

        if (line.indexOf(' ') >= 0 || line.indexOf('\t') >= 0) {
            failError("read line char has error", url, indexNumber);
        }

        int cp = line.codePointAt(0);
        if (!Character.isJavaIdentifierStart(cp)) {
            failError("read line char has error", url, indexNumber);
        }

        for (int index = Character.charCount(cp); index < line.length(); index += Character.charCount(cp)) {
            cp = line.codePointAt(index);
            if (!Character.isJavaIdentifierStart(cp) && cp != '.') {
                failError("read line char has error", url, indexNumber);
            }
        }
        classNames.add(line);
    }


}
