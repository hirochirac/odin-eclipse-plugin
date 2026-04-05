package odineclipseplugin.wizards;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import odineclipseplugin.builder.SampleOdinPluginNature;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class OdinNewProjectWizard extends Wizard implements INewWizard {

    private OdinNewProjectWizardPage page;

    public OdinNewProjectWizard() {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("New Odin Project");
    }

    @Override
    public void addPages() {
        page = new OdinNewProjectWizardPage();
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        String projectName = page.getProjectName().trim();
        try {
            getContainer().run(true, false, monitor -> {
                try {
                    monitor.beginTask("Creating Odin project", 3);

                    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                    IProjectDescription desc = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
                    project.create(desc, monitor);
                    monitor.worked(1);
                    project.open(monitor);
                    monitor.worked(1);

                    // Apply Odin nature
                    IProjectDescription openedDesc = project.getDescription();
                    openedDesc.setNatureIds(new String[]{ SampleOdinPluginNature.NATURE_ID });
                    project.setDescription(openedDesc, monitor);

                    IFile mainFile = project.getFile(IPath.fromOSString("main.odin"));
                    String content = "package main\n\nimport \"core:fmt\"\n\nmain :: proc() {\n\tfmt.println(\"Hello, Odin!\")\n}\n";
                    mainFile.create(new ByteArrayInputStream(content.getBytes()), true, monitor);
                    monitor.worked(1);

                    getShell().getDisplay().asyncExec(() -> {
                        IWorkbenchPage wbPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        try {
                            IDE.openEditor(wbPage, mainFile, true);
                        } catch (PartInitException e) {
                            // ignore
                        }
                    });
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            });
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            MessageDialog.openError(getShell(), "Error", e.getTargetException().getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {}
}
