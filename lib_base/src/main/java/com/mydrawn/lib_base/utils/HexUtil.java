package com.mydrawn.lib_base.utils;

import java.math.BigInteger;

/**
 * 进制工具类
 */
public class HexUtil {


    /**
     * 10进制转16进制
     *
     * @param numb
     * @return
     */
    public static String encodeHEX(Integer numb) {
        String hex = Integer.toHexString(numb);
        return hex;
    }

    /**
     * 将byte数组转换为int数值,降序 ，高位在前，低位在后
     * @param  byte数组 ，起始位
     * @return int
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));
        return value;
    }

    /**
     * 16进制转10进制转
     *
     * @param numb
     * @return
     */
    public static int decodeHEX(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }

    /**
     * 将int转为高字节在前，低字节在后的byte数组 不带符号
     */
    public static byte[] intToBytes(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * byte数组 转16进制字符串
     *
     * @param b
     * @return
     */
    public static String bytes2String16(byte[] b) {
        char[] _16 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
// 1           sb.append(_16[b[i]>>4&0xf])
//                    .append(_16[b[i]&0xf]).append(":");

// 2          sb.append(String.format("%02x", b[i])).append(":");

            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * byte数组 转10进制字符串(单个)
     *
     * @param b
     * @return
     */
    public static String bytes2IntArray(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(byte2IntStr(b[i])).append(":");
        }
        return sb.toString();
    }

    public static String byte2IntStr(Byte b) {
        return String.valueOf(b & 0xff);
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 二进制样式的字符串转byte数组
     *
     * @param binaryStr
     * @return
     */
    public static byte[] binaryStr2ByteArray(String binStr, String splitStr) {
        String[] temp = binStr.split(splitStr);
        byte[] b = new byte[temp.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;
    }

    /**
     * 二进制样式的字符串转int
     *
     * @param binaryStr
     * @return
     */
    public static int binaryStr2ByteArray(String binStr) {
        return Integer.valueOf(binStr, 2);
    }

    /**
     * byte数组  转为 8位二进制的字符串
     *
     * @param byteArr
     * @return
     */
    public static String byteArray2BinaryStr(byte[] byteArr, String splitStr) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < byteArr.length; i++) {
            result.append(byte2BinaryStr(byteArr[i]) + splitStr);
        }
        return result.toString().substring(0, result.length() - 1);
    }

    /**
     * byte 转8位二进制字符串
     *
     * @param mByte
     * @return
     */
    public static String byte2BinaryStr(Byte mByte) {
        String st = Long.toString(mByte & 0xff, 2);
        while (st.length() < 8) {
            st = "0" + st;
        }
        return st;
    }

}
