package com.chendu.jq.core.util;

public class JqLog {
    public static void info(String base, Object ...objs){
        System.out.println("Info:\t" + formalize(base, objs));
    }

    public static void warn(String base, Object ...objs){
        System.out.println("Warning:\t" + formalize(base, objs));
    }

    public static void error(String base, Object ...objs){
        System.out.println("Error:\t" + formalize(base, objs));
    }

    private static String formalize(String base, Object ...objs){
        String result = base;
        for(Object o:objs)
            result = result.replaceFirst("\\{}", o.toString());
        return result;
    }
}
