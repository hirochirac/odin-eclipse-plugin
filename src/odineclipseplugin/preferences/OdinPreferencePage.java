package odineclipseplugin.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import odineclipseplugin.Activator;

public class OdinPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String PREF_ODIN_PATH = "odin.path";
    public static final String PREF_OLS_PATH  = "ols.path";

    public OdinPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Odin toolchain configuration");
    }

    @Override
    protected void createFieldEditors() {
        addField(new FileFieldEditor(PREF_ODIN_PATH, "Odin compiler path:", getFieldEditorParent()));
        addField(new FileFieldEditor(PREF_OLS_PATH,  "OLS (language server) path:", getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {}
}
