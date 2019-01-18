package com.exceljava.jinx.examples;

import com.exceljava.jinx.ExcelMenu;
import javax.swing.JOptionPane;

/**
 * Example menu functions that can be called from Excel via Jinx.
 *
 * See the examples spreadsheet included in the Jinx download.
 */
public class MenuFunctions {
    /**
     * Menu item that shows a message box when called.
     */
    @ExcelMenu("Menu Example")
    public static void menuExample() {
        JOptionPane.showMessageDialog(null,
                "Example menu item selected!",
                "Menu Example",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
