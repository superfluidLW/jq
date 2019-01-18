package com.exceljava.jinx.examples;

import com.exceljava.jinx.ExcelAddIn;
import com.exceljava.jinx.ExcelArgument;
import com.exceljava.jinx.ExcelArguments;
import com.exceljava.jinx.ExcelFunction;


/**
 * Example worksheet functions (UDFs) that can be called
 * from Excel via Jinx.
 *
 * See the examples spreadsheet included in the Jinx download.
 */
public class WorksheetFunctions {
    private final ExcelAddIn xl;

    /**
     * A non-default constructor is not required, but if there is one that
     * takes an ExcelAddIn then it will be used when instantiating the class
     * (which is only done if there are non-static methods).
     *
     * @param xl ExcelAddIn object for calling back into Excel and Jinx
     */
    public WorksheetFunctions(ExcelAddIn xl) {
        this.xl = xl;
    }

    /**
     * Worksheet function that returns the Jinx version.
     *
     * This is a volatile function so will be recalculated each time
     * the workbook is opened or refreshed.
     *
     * @return Jinx version
     */
    @ExcelFunction(
            value = "jinx.version",
            isVolatile = true
    )
    public static String getJinxVersion() {
        Package jinx = Package.getPackage("com.exceljava.jinx");
        return jinx.getImplementationVersion();
    }

    /**
     * Worksheet function that returns the Java runtime version.
     *
     * This is a volatile function so will be recalculated each time
     * the workbook is opened or refreshed.
     *
     * @return Java version
     */
    @ExcelFunction(
            value = "java.version",
            isVolatile = true
    )
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Worksheet function that returns the Jinx config file.
     *
     * This is a volatile function so will be recalculated each time
     * the workbook is opened or refreshed.
     *
     * @return Jinx config file
     */
    @ExcelFunction(
            value = "jinx.config",
            isVolatile = true
    )
    public String getJinxConfigPath() {
        return xl.getConfigPath();
    }

    /**
     * Worksheet function that returns the Jinx config file.
     *
     * This is a volatile function so will be recalculated each time
     * the workbook is opened or refreshed.
     *
     * @return Jinx log file
     */
    @ExcelFunction(
            value = "jinx.log",
            isVolatile = true
    )
    public String getJinxLogPath() {
        return xl.getLogPath();
    }

    /**
     * Return (x * y) + z
     *
     * This is a simple function only intended to show how arguments can
     * be passed to a worksheet function and named so they appear in the
     * function wizard.
     */
    @ExcelFunction(
            value = "jinx.multiplyAndAdd",
            description = "Simple function that returns (x * y) + z"
    )
    @ExcelArguments({
            @ExcelArgument("x"),
            @ExcelArgument("y"),
            @ExcelArgument("z")
    })
    public static double multiplyAndAdd(int x, double y, double z) {
        return (x * y) + z;
    }

    /**
     * Join a list of strings with a delimiter.
     *
     * This demonstrates using a variable number of arguments in an Excel function.
     */
    @ExcelFunction(
            value = "jinx.stringJoin",
            description = "Join multiple strings with a separator"
    )
    @ExcelArguments({
            @ExcelArgument("sep"),
            @ExcelArgument("string")
    })
    public static String stringJoin(String sep, String ... strings) {
        // Note: Could use String.join in Java 8, but these examples are for Java >= 6
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<strings.length; ++i) {
            if (i >= 1) {
                builder.append(sep);
            }
            builder.append(strings[i]);
        }
        return builder.toString();
    }

    /**
     * Transposes a 2d array of numbers.
     * When the transposed array is returned to Excel, the output range is automatically
     * resized as 'autoResize' is set to true.
     */
    @ExcelFunction(
            value = "jinx.transpose",
            description = "Transposes a 2d matrix of numbers",
            autoResize = true
    )
    @ExcelArguments({
            @ExcelArgument("array")
    })
    public static double[][] transpose(double[][] array) {
        int m = array.length;
        int n = array[0].length;

        double[][] transposed = new double[n][m];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                transposed[x][y] = array[y][x];
            }
        }

        return transposed;
    }

    /**
     * As well as simple types, Jinx can handle returning complex
     * objects to the Excel to pass to other functions.
     *
     * Objects that can't be converted to Excel types are cached and a
     * handle to the object is returned to Excel. The cache is managed
     * and when no longer needed the objects are removed from the cache
     * to be garbage collected.
     */
    public class CachedObjectExample {
        private final String name;

        public CachedObjectExample(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    /**
     * Create a new CachedObjectExample instance to demonstrate returning
     * an object to Excel.
     */
    @ExcelFunction("jinx.createCachedObject")
    public CachedObjectExample createCachedObject(String name) {
        return new CachedObjectExample(name);
    }

    /**
     * Takes a CachedObjectExample instance to demonstrate passing an
     * object returned to the Excel sheet.
     */
    @ExcelFunction("jinx.cachedObjectName")
    public String getCachedObjectName(CachedObjectExample obj) {
        return obj.getName();
    }
}
