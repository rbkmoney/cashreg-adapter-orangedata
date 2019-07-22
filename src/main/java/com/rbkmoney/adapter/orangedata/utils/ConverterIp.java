package com.rbkmoney.adapter.orangedata.utils;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv6.IPv6Address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: in libs
 */
public class ConverterIp {

    public static final String NAT_64_PREFIX = "2a04:4a00:5:10ff:4:1:";

    private static final String REGEXP_IP = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
    private static final String DEFAULT_NAT_64_PREFIX = "::ffff:";

    private String nat64prefix;

    public ConverterIp() {
        this.nat64prefix = NAT_64_PREFIX;
    }

    public ConverterIp(String nat64prefix) {
        this.nat64prefix = nat64prefix;
    }

    public String replaceIpv4ToIpv6(String url) {

        Pattern p = Pattern.compile(REGEXP_IP);
        Matcher m = p.matcher(url);

        if (m.find()) {
            String ip = m.group(1);

            IPAddress address;
            try {
                address = new IPAddressString(ip).toAddress();
            } catch (AddressStringException e) {
                throw new IllegalArgumentException(e);
            }

            IPv6Address ipv6Address = address.toIPv6();
            String canonicalAddress = ipv6Address.toCanonicalString();

            String suffix = canonicalAddress.replace(DEFAULT_NAT_64_PREFIX, this.nat64prefix);
            return url.replace(ip, "[" + suffix + "]");
        }

        return url;
    }

}
