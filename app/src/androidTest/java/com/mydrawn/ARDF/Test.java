package com.mydrawn.ARDF;

import java.util.ArrayList;
import java.util.List;

public class Test {
    String[] domainLis = {
            "https://xdc1.szwego.com/",
            "https://xdc2.szwego.com/",
            "https://xdc3.szwego.com/",
            "https://xdc4.szwego.com/"
    };
    String str_a = "-0123456789ABCDE";
    String str_b = "FGHIJKLMNOPQRSTU";
    String str_c = "VWXYZ_abcdefghij";
    String str_d = "klmnopqrstuvwxyz";

    public String getDomainAddress(String key) {
        String domainAddress = "";
        if (key.length() != 1) {
            return domainAddress;
        }
        if ("-0123456789ABCDE".contains(key)) {
            domainAddress = "https://xdc1.szwego.com/";
        }
        if ("FGHIJKLMNOPQRSTU".contains(key)) {
            domainAddress = "https://xdc2.szwego.com/";
        }
        if ("VWXYZ_abcdefghij".contains(key)) {
            domainAddress = "https://xdc3.szwego.com/";
        }
        if ("klmnopqrstuvwxyz".contains(key)) {
            domainAddress = "https://xdc4.szwego.com/";
        }
        //打 log输出 domainAddress的值
        return domainAddress;
    }
}
