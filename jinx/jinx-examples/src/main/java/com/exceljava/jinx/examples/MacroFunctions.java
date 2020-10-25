package com.exceljava.jinx.examples;

import com.exceljava.jinx.*;
import javax.swing.JOptionPane;

/**
 * Example macro functions that can be called from Excel via Jinx.
 *
 * See the examples spreadsheet included in the Jinx download.
 */
public class MacroFunctions {
    /**
     * Macro that shows a message box when called.
     */
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
