package com.chendu.jq.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Slf4j
public class JqParser {
    private static List<DateTimeFormatter> sdfs = Arrays.asList(DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    public static LocalDate jqDateFromStr(String dateStr){
        for (DateTimeFormatter sdf:sdfs
             ) {
            try {
                return LocalDate.parse(dateStr, sdf);
            }
            catch (Exception ex){

            }
        }

        log.warn("Convert string {} to LocalDate failed.", dateStr);
        return null;
    }

    public static <E extends Enum<E>> E enumFromStr(String input, Class<E> tClass) {
        E result = enumFromName(input, tClass);
        if(result == null){
            result = enumFromNameCn(input, tClass);
        }
        return result;
    }

    private static <E extends Enum<E>> E enumFromName(String input, Class<E> tClass) {
        if (input == null) {
            return null;
        }
        for (E type : EnumSet.allOf(tClass)) {
            if (type.name().equalsIgnoreCase(input)) {
                return type;
            }
        }

        return null;
    }

    private static <E extends Enum<E>> E enumFromNameCn(String input, Class<E> tClass) {
        if (input == null) {
            return null;
        }

        try {
            Method method = Arrays.stream(tClass.getMethods()).filter(i -> i.getName().toLowerCase().contains(JqConstant.ENUM_NAME_CN.toLowerCase())).findFirst().get();

            for (E type : EnumSet.allOf(tClass)) {
                String nameCn = (String)method.invoke(type);
                if (nameCn.equalsIgnoreCase(input)){
                    return type;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }
}
