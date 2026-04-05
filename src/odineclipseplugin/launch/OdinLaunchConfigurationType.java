package odineclipseplugin.launch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.RuntimeProcess;

import odineclipseplugin.Activator;
import odineclipseplugin.preferences.OdinPreferencePage;

public class OdinLaunchConfigurationType implements ILaunchConfigurationDelegate {

    public static final String TYPE_ID      = "odineclipseplugin.launch.OdinLaunchConfigurationType";
    public static final String ATTR_PROJECT = "odin.launch.project";
    public static final String ATTR_ARGS    = "odin.launch.args";

    @Override
    public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor monitor)
            throws CoreException {

        String odinPath    = Activator.getDefault().getPreferenceStore().getString(OdinPreferencePage.PREF_ODIN_PATH);
        String projectPath = config.getAttribute(ATTR_PROJECT, "");
        String extraArgs   = config.getAttribute(ATTR_ARGS, "");

        if (odinPath.isBlank())
            throw new CoreException(Status.error("Odin compiler path is not set. Configure it in Preferences > Odin."));
        if (projectPath.isBlank())
            throw new CoreException(Status.error("Project path is not set in the run configuration."));

        File projectDir = new File(projectPath);
        String exeName  = projectDir.getName() + (isWindows() ? ".exe" : "");
        File   exeFile  = new File(projectDir, exeName);

        // Step 1 — compile
        monitor.beginTask("Compiling Odin project...", 2);
        List<String> buildCmd = new ArrayList<>();
        buildCmd.add(odinPath);
        buildCmd.add("build");
        buildCmd.add(projectPath);
        buildCmd.add("-out:" + exeFile.getAbsolutePath());
        if (!extraArgs.isBlank())
            for (String arg : extraArgs.split("\\s+")) buildCmd.add(arg);

        try {
            Process buildProcess = new ProcessBuilder(buildCmd)
                    .directory(projectDir)
                    .redirectErrorStream(true)
                    .start();
            // Show build output in console
            new RuntimeProcess(launch, buildProcess, config.getName() + " [build]", null);
            int exitCode = buildProcess.waitFor();
            monitor.worked(1);
            if (exitCode != 0)
                throw new CoreException(Status.error("Odin build failed (exit code " + exitCode + "). Check the console for errors."));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        } catch (CoreException e) {
            throw e;
        } catch (Exception e) {
            throw new CoreException(Status.error("Failed to run Odin compiler: " + e.getMessage(), e));
        }

        // Step 2 — run the produced exe
        monitor.setTaskName("Running " + exeName + "...");
        try {
            Process runProcess = new ProcessBuilder(exeFile.getAbsolutePath())
                    .directory(projectDir)
                    .redirectErrorStream(true)
                    .start();
            new RuntimeProcess(launch, runProcess, config.getName(), null);
            monitor.worked(1);
        } catch (Exception e) {
            throw new CoreException(Status.error("Failed to run executable: " + e.getMessage(), e));
        } finally {
            monitor.done();
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }
}
