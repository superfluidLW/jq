package com.chendu.jq.excel;

import com.exceljava.jinx.ExcelAddIn;
import com.exceljava.jinx.ExcelFunction;

public class TestFunc1 {
    private final ExcelAddIn xl;

    /**
     * A non-default constructor is not required, but if there is one that
     * takes an ExcelAddIn then it will be used when instantiating the class
     * (which is only done if there are non-static methods).
     *
     * @param xl ExcelAddIn object for calling back into Excel and Jinx
     */
    public TestFunc1(ExcelAddIn xl) {
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
            value = "getJinxVersion",
            isVolatile = true
    )
    public static String getJinxVersion() {
        Package jinx = Package.getPackage("com.exceljava.jinx");
        return jinx.getImplementationVersion();
    }

}
