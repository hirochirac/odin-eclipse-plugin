package odineclipseplugin.lsp;

import java.util.List;

import org.eclipse.lsp4e.server.ProcessStreamConnectionProvider;

import odineclipseplugin.Activator;
import odineclipseplugin.preferences.OdinPreferencePage;

public class OdinLanguageServerStreamProvider extends ProcessStreamConnectionProvider {

    public OdinLanguageServerStreamProvider() {
        String olsPath = Activator.getDefault().getPreferenceStore().getString(OdinPreferencePage.PREF_OLS_PATH);
        if (olsPath != null && !olsPath.isBlank()) {
            setCommands(List.of(olsPath));
            setWorkingDirectory(System.getProperty("user.home"));
        }
    }

    @Override
    public String toString() {
        return "Odin Language Server (OLS)";
    }
}
