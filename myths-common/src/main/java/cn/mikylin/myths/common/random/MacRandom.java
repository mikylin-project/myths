package cn.mikylin.myths.common.random;

import cn.mikylin.myths.common.Constants;
import cn.mikylin.myths.common.RandomUtils;

/**
 * random to create mac address.
 *
 * @author mikylin
 * @date 20191217
 */
public final class MacRandom {


    /**
     * Generate a random MAC address.
     * Every fields are random, range from 0 to 255
     *
     * @return MAC address string
     */
    public static String randomMac() {
        String[] mac = {
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff))
        };
        return String.join(Constants.System.SEPARATOR,mac);
    }

    /**
     * Generate a random MAC address with oui.
     * The OUI list is available at http://standards.ieee.org/regauth/oui/oui.txt.
     * The remaining 3 fields are random,  range from 0 to 255
     *
     * @param oui string[] like {52, 54, 00}
     * @return MAC address string
     */
    public static String randomMacWithOui(Integer[] oui) {
        String[] mac = {
                String.format("%02x", oui[0]),
                String.format("%02x", oui[1]),
                String.format("%02x", oui[2]),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff))
        };
        return String.join(Constants.System.SEPARATOR,mac);
    }

    /**
     * Generate a random MAC address for qemu/kvm
     * 52-54-00 used by qemu/kvm
     * The remaining 3 fields are random,  range from 0 to 255
     *
     * @return MAC address string
     */
    public static String randomMac4Qemu() {
        String[] mac4Qemu = {
                String.format("%02x", 0x52),
                String.format("%02x", 0x54),
                String.format("%02x", 0x00),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff))
        };
        return String.join(Constants.System.SEPARATOR,mac4Qemu);
    }

    /**
     * Generate a random MAC address for xen
     * 00-16-3E allocated to xensource
     * The remaining 3 fields are random,  range from 0 to 255
     *
     * @return MAC address string
     */
    public static String randomMac4Xen() {
        String[] mac4Xen = {
                String.format("%02x", 0x00),
                String.format("%02x", 0x16),
                String.format("%02x", 0x3e),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff))
        };
        return String.join(Constants.System.SEPARATOR,mac4Xen);
    }

    /**
     * Generate a random MAC address for Apple
     * 00-cd-fe allocated to Apple
     * The remaining 3 fields are random,  range from 0 to 255
     *
     * @return MAC address string
     */
    public static String randomMac4Apple() {
        String[] mac4Apple = {
                String.format("%02x", 0x00),
                String.format("%02x", 0xcd),
                String.format("%02x", 0xfe),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff))
        };
        return String.join(Constants.System.SEPARATOR,mac4Apple);
    }

    /**
     * Generate a random MAC address for Huawei
     * 00-cd-fe used by Huawei
     * The remaining 3 fields are random,  range from 0 to 255
     *
     * @return MAC address string
     */
    public static String randomMac4Huawei() {
        String[] mac4Huawei = {
                String.format("%02x", 0x48),
                String.format("%02x", 0xad),
                String.format("%02x", 0x08),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff)),
                String.format("%02x", RandomUtils.nextInt(0xff))
        };
        return String.join(Constants.System.SEPARATOR,mac4Huawei);
    }
}