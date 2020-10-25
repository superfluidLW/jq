package com.exceljava.jinx.examples;

import com.exceljava.jinx.ExcelArgumentConverter;
import com.exceljava.jinx.ExcelFunction;
import com.exceljava.jinx.ExcelReturnConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * When passing values from Excel to Java, Jinx uses registered
 * converters to convert between the Excel types and Java types.
 *
 * Additional converters can be registered using the @ExcelArgumentConverter
 * and @ExcelReturnConverter annotations.
 *
 * The @ExcelArgumentConverter annotation is used for converters that
 * convert from basic types Excel understands (eg numbers and strings etc)
 * to more complex Java types. The @ExcelReturnConverter annotation is
 * for converters to convert from these more complex Java types back
 * to types Excel understands.
 *
 * A common use-case for these is enums where you want to display
 * a human readable string in Excel, while ensuring that only
 * valid enums are used.
 *
 * Using converters avoids duplicating boilerplate code in your
 * worksheet functions.
 */
public class TypeConverters {

    /**
     * Trivial currency class to illustrate how Jinx can
     * convert from basic types to Java classes automatically.
     */
    public static class Currency {
        private final String name;

        Currency(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    /**
     * Amount and currency pair for illustrating argument
     * conversion in Jinx.
     */
    public static class DenominatedValue {
        private final double amount;
        private final Currency currency;

        DenominatedValue(double amount, Currency currency) {
            this.amount = amount;
            this.currency = currency;
        }

        public Currency getCurrency() {
            return currency;
        }

        public double getAmount() {
            return amount;
        }
    }

    /**
     * This conversion function is registered with Jinx using
     * the @ExcelArgumentConverter annotation.
     *
     * This enables worksheet functions to accept 'Currency'
     * as an argument type, and Jinx will handle the conversion
     * from a string from Excel to a Currency instance automatically
     * using this conversion function.
     *
     * @param name Name of the currency object to create.
     * @return Currency object.
     */
    @ExcelArgumentConverter
    public static Currency stringToCurrency(String name) {
        return new Currency(name);
    }

    /**
     * So that Currency objects can be returned to Excel in
     * a readable way (rather than as an object handle) we
     * use the @ExcelReturnConverter annotation to register
     * a return type converter.
     *
     * Whenever a UDF returns a Currency, this converter will
     * be used to convert that Currency to a String to return
     * to Excel.
     *
     * @param currency Currency to convert to a String.
     * @return String representation of the Currency.
     */
    @ExcelReturnConverter
    public static String currencyToString(Currency currency) {
        return currency.toString();
    }

    /**
     * This worksheet function takes a Currency instance, which is
     * a custom type and not a standard Excel type.
     *
     * Jinx will automatically convert a string argument passed to the
     * UDF into a Currency using the registered argument converter.
     *
     * @param amount Denominated value amount.
     * @param currency Denominated value amount.
     * @return DenominatedValue instance.
     */
    @ExcelFunction("jinx.makeDenominatedValue")
    public static DenominatedValue makeDenominatedValue(double amount, Currency currency) {
        return new DenominatedValue(amount, currency);
    }

    /**
     * This worksheet function takes a DenominatedValue created using
     * 'makeDenominatedValue' and returns its currency.
     *
     * Because of the registered Currency return type converter,
     * that Currency will be converted to a String before being
     * returned to Excel.
     *
     * @param dv DenominatedValue to get the Currency of.
     * @return Currency of the DenominatedValue.
     */
    @ExcelFunction("jinx.denominatedValueGetCurrency")
    public static Currency denominatedValueGetCurrency(DenominatedValue dv) {
        return dv.getCurrency();
    }

    /**
     * There's no argument or return type conversion needed for this
     * function, it's just here for completeness.
     *
     * The DenominatedValue is passed in from an Excel object handle
     * returned from 'makeDenominatedValue'.
     *
     * @param dv DenominatedValue to get the amount of.
     * @return Amount of the DenominatedValue.
     */
    @ExcelFunction("jinx.denominatedValueGetAmount")
    public static double denominatedValueGetAmount(DenominatedValue dv) {
        return dv.getAmount();
    }


    /**
     * Interface to illustrate how @ExcelArgumentConverter
     * converters can also be used as factories.
     *
     * If an @ExcelArgumentConverter has 'baseTypes' set it also
     * takes an additional 'Class' argument to tell it what
     * type to return, which will be a sub-type of one of the
     * defined base classes.
     */
    public interface TimeSpan {
        String getName();
    }

    /**
     * Tenor implements TimeSpan but does nothing.
     */
    public static class Tenor implements TimeSpan {
        private final String name;

        public Tenor(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return "Tenor[" + name + "]";
        }
    }

    /**
     * Period implements TimeSpan but does nothing.
     */
    public static class Period implements TimeSpan {
        private final String name;

        public Period(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return "Period[" + name + "]";
        }
    }

    /**
     * This ExcelArgumentConverter is generic and it will be used to
     * convert any type matching the bounded type parameter.
     *
     * This means that we don't need a converter for each class and
     * is especially useful if using a class factory pattern.
     */
    @ExcelArgumentConverter
    public static <T extends TimeSpan> T stringToTimeSpan(String name, Class<T> cls) {
        if (cls.isAssignableFrom(Tenor.class)) {
            return (T)new Tenor(name);
        }
        else if (cls.isAssignableFrom(Period.class)) {
            return (T)new Period(name);
        }

        throw new RuntimeException("Cannot construct TimeSpan class '" + cls.getCanonicalName() + "'.");
    }

    /**
     * Gets the String representation of a Period.
     *
     * The Period is automatically constructed using the registered
     * 'stringToTimeSpan' converter when called from Excel.
     *
     * @param period Period to get the string representation of.
     * @return String representation of the period.
     */
    @ExcelFunction("jinx.periodToString")
    public static String periodToString(Period period) {
        return period.toString();
    }

    /**
     * Gets the String representation of a Tenor.
     *
     * The Tenor is automatically constructed using the registered
     * 'stringToTimeSpan' converter when called from Excel.
     *
     * @param tenor Period to get the string representation of.
     * @return String representation of the period.
     */
    @ExcelFunction("jinx.tenorToString")
    public static String tenorToString(Tenor tenor) {
        return tenor.toString();
    }


    /**
     * Generic functions can also be used as converters, for example,
     * converting an array to a list.
     *
     * This example converts input arrays of numbers, replacing anu
     * null values with 0.
     *
     * @param array Input array to convert to a list.
     * @param <T> Array component type, extends 'Number'.
     * @return List of values in the input array.
     */
    @ExcelArgumentConverter
    public static <T extends Number> List<T> numberArrayToList(T[] array) {
        List<T> result = new ArrayList<T>(array.length);

        for (int i=0; i<array.length; ++i) {
            T value = array[i];
            if (null == value) {
                value = (T)(Object) 0;
            }
            result.add(value);
        }

        return result;
    }

    /**
     * 2d arrays can also be used with argument converters.
     *
     * @param array Input array to convert to a list.
     * @param <T> Array component type, extends 'Number'.
     * @return List of values in the input array.
     */
    @ExcelArgumentConverter
    public static <T extends Number> List<List<T>> numberArrayToList2d(T[][] array) {
        List<List<T>> result = new ArrayList<List<T>>(array.length);

        for (int i=0; i<array.length; ++i) {
            List<T> row = numberArrayToList(array[i]);
            result.add(row);
        }

        return result;
    }

    /**
     * Return the sum of a list of numbers.
     *
     * This uses the 'numberArrayToList' converter to automatically
     * convert the Excel array of doubles to a list and replace
     * any missing (null) values with 0.
     *
     * @param input List of doubles to compute the sum of.
     * @return double Sum of the inputs.
     */
    @ExcelFunction("jinx.sumList")
    public static double sumListOfDoubles(List<Double> input) {
        double result = 0;

        for (Double d: input) {
            result += d;
        }

        return result;
    }


    /**
     * Return converters can also use generic types.
     *
     * This return converter will convert a List of Numbers into an array.
     *
     * @param list Input list to convert to an array.
     * @param <T> Array component type.
     * @return Array of values in the input list.
     */
    @ExcelReturnConverter
    public static <T extends Number> Number[] numberListToArray(List<T> list) {
        Number[] result = new Number[list.size()];
        return list.toArray(result);
    }

    /**
     * Return the sums of the rows of a list of lists of numbers.
     *
     * This uses the 'numberArrayToList2d' converter to automatically
     * convert the Excel array of doubles to a list of lists and replaces
     * any missing (null) values with 0.
     *
     * The Return converter 'numberListToArray' converter is used
     * to convert the retued List to an array for Excel.
     *
     * @param input List of lists of doubles to compute the sum of.
     * @return List<Double> List of the row sums of the input.
     */
    @ExcelFunction(
            value = "jinx.sumRows",
            autoResize = true
    )
    public static List<Double> sumListOfDoubles2d(List<List<Double>> input) {
        List<Double> result = new ArrayList<Double>(input.size());

        for (List<Double> row: input) {
            double rowSum = sumListOfDoubles(row);
            result.add(rowSum);
        }

        return result;
    }
}

