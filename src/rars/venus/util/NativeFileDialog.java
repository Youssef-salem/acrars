package rars.venus.util;

import rars.Globals;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;

/**
 * Thin wrapper around {@link java.awt.FileDialog} so the rest of the code can
 * pop up the native OS file picker (NSOpenPanel on macOS, the standard Windows
 * common dialog on Windows). Compared with {@link javax.swing.JFileChooser} the
 * native dialog is dramatically more accessible to VoiceOver and other screen
 * readers, and it matches what users expect from other desktop apps.
 *
 * <p>The native Save dialog already handles its own overwrite confirmation, so
 * callers do not need to ask again.
 */
public final class NativeFileDialog {

    private NativeFileDialog() {
    }

    /**
     * Show a native "Open" file picker.
     *
     * @param parent     the owning frame (must not be null on macOS for the
     *                   dialog to be modal to the right window)
     * @param title      dialog title shown to the user
     * @param initialDir directory to open the picker in; may be null
     * @param filter     optional filename filter; may be null to accept all
     * @return the selected file or null if the user cancelled
     */
    public static File open(Frame parent, String title, String initialDir, FilenameFilter filter) {
        FileDialog dlg = new FileDialog(parent, title, FileDialog.LOAD);
        if (initialDir != null) {
            dlg.setDirectory(initialDir);
        }
        if (filter != null) {
            dlg.setFilenameFilter(filter);
        }
        dlg.setVisible(true);
        return selectedFile(dlg);
    }

    /**
     * Show a native "Save" file picker. The native dialog handles overwrite
     * confirmation on its own.
     *
     * @param parent          the owning frame
     * @param title           dialog title shown to the user
     * @param initialDir      directory to open the picker in; may be null
     * @param initialFileName suggested file name; may be null
     * @return the selected file or null if the user cancelled
     */
    public static File save(Frame parent, String title, String initialDir, String initialFileName) {
        FileDialog dlg = new FileDialog(parent, title, FileDialog.SAVE);
        if (initialDir != null) {
            dlg.setDirectory(initialDir);
        }
        if (initialFileName != null) {
            dlg.setFile(initialFileName);
        }
        dlg.setVisible(true);
        return selectedFile(dlg);
    }

    /**
     * Convenience filename filter that accepts any of RARS's assembler file
     * extensions (.s, .asm, ...).
     */
    public static FilenameFilter assemblerFilter() {
        return new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lower = name.toLowerCase(Locale.ROOT);
                for (String ext : Globals.fileExtensions) {
                    String dotted = ext.startsWith(".") ? ext.toLowerCase(Locale.ROOT)
                            : "." + ext.toLowerCase(Locale.ROOT);
                    if (lower.endsWith(dotted)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private static File selectedFile(FileDialog dlg) {
        String name = dlg.getFile();
        if (name == null) {
            return null;
        }
        String dir = dlg.getDirectory();
        return new File(dir == null ? "" : dir, name);
    }
}
