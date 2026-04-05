package odineclipseplugin;

import org.eclipse.lsp4e.LanguageServersRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import odineclipseplugin.preferences.OdinPreferencePage;

public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "odin-eclipse-plugin";
    private static final String LS_ID = "odineclipseplugin.lsp.OdinLanguageServer";
    private static Activator instance;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        instance = this;
        getPreferenceStore().addPropertyChangeListener(event -> {
            if (OdinPreferencePage.PREF_OLS_PATH.equals(event.getProperty())) {
                LanguageServersRegistry.getInstance().getDefinition(LS_ID);
            }
        });
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return instance;
    }
}
