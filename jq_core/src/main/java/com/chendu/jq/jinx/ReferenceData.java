package com.chendu.jq.jinx;

import com.chendu.jq.excel.JqProp;
import com.exceljava.jinx.ExcelAddIn;
import com.exceljava.jinx.ExcelFunction;
import com.exceljava.jinx.ExcelMacro;

import javax.swing.*;

public class ReferenceData {
    private final ExcelAddIn xl;

    /**
     * A non-default constructor is not required, but if there is one that
     * takes an ExcelAddIn then it will be used when instantiating the class
     * (which is only done if there are non-static methods).
     *
     * @param xl ExcelAddIn object for calling back into Excel and Jinx
     */
    public ReferenceData(ExcelAddIn xl) {
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

    @ExcelFunction(
            value = "getJqProps",
            isVolatile = true
    )
    public static String[][] getJqProps() {
        return JqProp.GetAllProps();
    }

    @ExcelMacro(
            value = "jinx.exampleMacro",
            shortcut = "Ctrl+Alt+J"
    )
    public static void macroExample() {
        JOptionPane.showMessageDialog(null,
                "Macro!",
                "Macro called",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
