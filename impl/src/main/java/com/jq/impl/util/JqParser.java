package com.jq.impl.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Slf4j
public class JqParser {
    public static <E extends Enum<E>> E parseEnum(String input, Class<E> tClass) {
        try {
            Method method = Arrays.stream(tClass.getMethods()).filter(i -> i.getName().toLowerCase().contains(JqConstant.enumDescription.toLowerCase())).findFirst().get();

            for (E type : EnumSet.allOf(tClass)) {
                String description = (String) method.invoke(type);
                if (type.name().equalsIgnoreCase(input) || description.equals(input)) {
                    return type;
                }
            }

            return null;
        }
        catch (Exception ex){
            return null;
        }
    }
}
