package com.exceljava.jinx.examples;

import com.exceljava.jinx.ExcelAction;
import com.exceljava.jinx.IUnknown;

import javax.swing.*;

public class RibbonFunctions {

    @ExcelAction("jinx.examples.checkboxInitialState")
    public static boolean checkboxInitialState(IUnknown control) {
        return true;
    }

    @ExcelAction("jinx.examples.checkboxOnAction")
    public static void checkboxOnAction(IUnknown control, boolean isChecked) {
        JOptionPane.showMessageDialog(null,
                "Checkbox " + (isChecked ? "checked" : "un-checked"),
                "Checkbox Example",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @ExcelAction("jinx.examples.dropDownOnAction")
    public static void dropDownOnAction(IUnknown control, String id, int idx) {
        JOptionPane.showMessageDialog(null,
                "Item '" + id + "' (" + idx + ") selected",
                "Dropdown Example",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @ExcelAction("jinx.examples.comboBoxItemCount")
    public static int comboBoxItemCount(IUnknown control) {
        return 10;
    }

    @ExcelAction("jinx.examples.comboBoxItem")
    public static String comboBoxItemCount(IUnknown control, int idx) {
        return "Item " + idx;
    }

    @ExcelAction("jinx.examples.comboBoxOnChange")
    public static void comboBoxOnChange(IUnknown control, String item) {
        JOptionPane.showMessageDialog(null,
                "Item '" + item + "' selected",
                "Combobox Example",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
