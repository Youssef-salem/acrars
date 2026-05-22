package rars.venus.registers;

import rars.venus.VenusUI;

import javax.swing.*;
import java.awt.Color;

/*
Copyright (c) 2003-2006,  Pete Sanderson and Kenneth Vollmar

Developed by Pete Sanderson (psanderson@otterbein.edu)
and Kenneth Vollmar (kenvollmar@missouristate.edu)

Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the 
"Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, and to 
permit persons to whom the Software is furnished to do so, subject 
to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
 */

/**
 * Contains tabbed areas in the UI to display register contents
 *
 * @author Sanderson
 * @version August 2005
 **/

public class RegistersPane extends JTabbedPane {
    private RegistersWindow regsTab;
    private FloatingPointWindow fpTab;
    private ControlAndStatusWindow csrTab;

    private VenusUI mainUI;

    /**
     * Constructor for the RegistersPane class.
     **/

    public RegistersPane(VenusUI appFrame, RegistersWindow regs, FloatingPointWindow cop1,
                         ControlAndStatusWindow cop0) {
        super();
        this.mainUI = appFrame;

        regsTab = regs;
        fpTab = cop1;
        csrTab = cop0;
        regsTab.setVisible(true);
        fpTab.setVisible(true);
        csrTab.setVisible(true);

        this.addTab("Registers", regsTab);
        this.addTab("Floating Point", fpTab);
        this.addTab("Control and Status", csrTab);
        this.setForeground(Color.black);

        this.setToolTipTextAt(0, "CPU registers");
        this.setToolTipTextAt(1, "Floating point unit registers");
        this.setToolTipTextAt(2, "Control and Status registers");

        // --- Accessibility ---------------------------------------------------
        // Give every tab and the underlying register panels a screen-reader
        // friendly name and description. The JTables they contain are already
        // exposed cell-by-cell through Swing's AccessibleJTable.
        getAccessibleContext().setAccessibleName("Register tabs");
        getAccessibleContext().setAccessibleDescription(
                "Use the left and right arrow keys to switch between the integer, "
                + "floating-point and control-and-status register tables.");
        setAccessibleTab(0, "Integer registers", "Table of the 32 RISC-V integer registers.");
        setAccessibleTab(1, "Floating-point registers", "Table of the 32 RISC-V floating-point registers.");
        setAccessibleTab(2, "Control and status registers", "Table of RISC-V control and status registers (CSRs).");
        regsTab.getAccessibleContext().setAccessibleName("Integer register table");
        fpTab.getAccessibleContext().setAccessibleName("Floating-point register table");
        csrTab.getAccessibleContext().setAccessibleName("Control and status register table");
        // Allow Control+PageDown / Control+PageUp (the standard JTabbedPane
        // shortcut) and provide a mnemonic-style traversal hint to AT.
        setFocusable(true);
        // ---------------------------------------------------------------------
    }

    private void setAccessibleTab(int index, String name, String description) {
        java.awt.Component tab = getTabComponentAt(index);
        if (tab instanceof javax.accessibility.Accessible) {
            ((javax.accessibility.Accessible) tab).getAccessibleContext().setAccessibleName(name);
            ((javax.accessibility.Accessible) tab).getAccessibleContext().setAccessibleDescription(description);
        }
        // Also annotate the page content's accessible context if available.
        java.awt.Component page = getComponentAt(index);
        if (page instanceof javax.accessibility.Accessible) {
            javax.accessibility.AccessibleContext ctx =
                    ((javax.accessibility.Accessible) page).getAccessibleContext();
            if (ctx.getAccessibleName() == null) {
                ctx.setAccessibleName(name);
            }
            if (ctx.getAccessibleDescription() == null) {
                ctx.setAccessibleDescription(description);
            }
        }
    }

    /**
     * Return component containing integer register set.
     *
     * @return integer register window
     */
    public RegistersWindow getRegistersWindow() {
        return regsTab;
    }

    /**
     * Return component containing floating point register set.
     *
     * @return floating point register window
     */
    public FloatingPointWindow getFloatingPointWindow() {
        return fpTab;
    }

    /**
     * Return component containing Control and Status register set.
     *
     * @return exceptions register window
     */
    public ControlAndStatusWindow getControlAndStatusWindow() {
        return csrTab;
    }
}
