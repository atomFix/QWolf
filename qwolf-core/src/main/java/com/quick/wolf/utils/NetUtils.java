package com.quick.wolf.utils;

import com.google.common.base.Strings;
import com.quick.wolf.common.WolfConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/09:57
 */
@Slf4j
public class NetUtils {

    public static final String LOCALHOST = "127.0.0.1";
    public static final String ANYHOST = "0.0.0.0";

    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private static final AtomicReference<InetAddress> LOCAL_INET_ADDRESS = new AtomicReference<>();

    public static InetAddress getLocalAddress() {
        return getLocalAddress(null);
    }

    public static InetAddress getLocalAddress(Map<String, Integer> destHostPost) {
        if (LOCAL_INET_ADDRESS.get() != null) {
            return LOCAL_INET_ADDRESS.get();
        }
        InetAddress address = null;
        String ipPrefix = System.getenv(WolfConstants.ENV_WOLF_IP_PREFIX);
        if (!Strings.isNullOrEmpty(ipPrefix)) {
            address = getLocalAddressByNetworkInterface(ipPrefix);
            log.info("get local address by ip prefix: " + ipPrefix + ", address: " + address);
        }

        if (!isValidAddress(address)) {
            address = getLocalAddressByHostName();
        }

        if (!isValidAddress(address)) {
            address = getLocalAddressBySocket(destHostPost);
        }

        if (!isValidAddress(address)) {
            address = getLocalAddressByNetworkInterface(null);
        }

        if (isValidAddress(address)) {
            LOCAL_INET_ADDRESS.set(address);
        }

        return address;
    }

    private static InetAddress getLocalAddressBySocket(Map<String, Integer> destHostPost) {
        if (destHostPost == null || destHostPost.size() == 0) {
            return null;
        }

        for (Map.Entry<String, Integer> entry : destHostPost.entrySet()) {
            String host = entry.getKey();
            int port = entry.getValue();
            try (Socket socket = new Socket()){
                SocketAddress socketAddress = new InetSocketAddress(host, port);
                socket.connect(socketAddress, 1000);
                log.info("get local address from socket. remote host:" + host + ", port:" + port);
                return socket.getLocalAddress();
            } catch (IOException e) {
                log.warn("Failed to retrieving local address by connecting to dest host:port({}:{}) false, e={}", host, port, e.getMessage());
            }
        }

        return null;
    }

    private static InetAddress getLocalAddressByHostName() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (isValidAddress(localHost)) {
                return localHost;
            }
        } catch (UnknownHostException e) {
            log.warn("getLocalAddressByHostName has error + " + e.getMessage(), e);
        }
        return null;
    }

    private static InetAddress getLocalAddressByNetworkInterface(String ipPrefix) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (isValidAddress(address)) {
                        if (Strings.isNullOrEmpty(ipPrefix)) {
                            return address;
                        }
                        if (address.getHostAddress().startsWith(ipPrefix)) {
                            return address;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.warn("Failed to retrieving ip address, " + e.getMessage(), e);
        }

        return null;
    }

    public static boolean isValidLocalHost(String host) {
        return !isInvalidLocalHost(host);
    }

    public static boolean isInvalidLocalHost(String host) {
        return Strings.isNullOrEmpty(host) || host.equalsIgnoreCase("localhost") || host.equals("0.0.0.0") || LOCAL_IP_PATTERN.matcher(host).matches();
    }


    public static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();

        return name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches();
    }
}
