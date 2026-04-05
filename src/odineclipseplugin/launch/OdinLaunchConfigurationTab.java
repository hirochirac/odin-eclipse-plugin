package odineclipseplugin.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.jface.viewers.LabelProvider;

public class OdinLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

    private Text projectText;
    private Text argsText;

    @Override
    public void createControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout(3, false));
        setControl(comp);

        new Label(comp, SWT.NONE).setText("Project / Directory:");
        projectText = new Text(comp, SWT.BORDER);
        projectText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        projectText.addModifyListener(e -> updateLaunchConfigurationDialog());

        Button browseBtn = new Button(comp, SWT.PUSH);
        browseBtn.setText("Browse...");
        browseBtn.addListener(SWT.Selection, e -> browseProject());

        new Label(comp, SWT.NONE).setText("Extra arguments:");
        argsText = new Text(comp, SWT.BORDER);
        GridData argsData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        argsData.horizontalSpan = 2;
        argsText.setLayoutData(argsData);
        argsText.addModifyListener(e -> updateLaunchConfigurationDialog());
    }

    private void browseProject() {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new LabelProvider() {
            @Override public String getText(Object element) { return ((IProject) element).getName(); }
        });
        dialog.setTitle("Project Selection");
        dialog.setMessage("Select a project:");
        dialog.setElements(projects);
        if (dialog.open() == org.eclipse.jface.window.Window.OK) {
            IProject project = (IProject) dialog.getFirstResult();
            projectText.setText(project.getLocation().toOSString());
        }
    }

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy config) {
        config.setAttribute(OdinLaunchConfigurationType.ATTR_PROJECT, "");
        config.setAttribute(OdinLaunchConfigurationType.ATTR_ARGS, "");
    }

    @Override
    public void initializeFrom(ILaunchConfiguration config) {
        try {
            projectText.setText(config.getAttribute(OdinLaunchConfigurationType.ATTR_PROJECT, ""));
            argsText.setText(config.getAttribute(OdinLaunchConfigurationType.ATTR_ARGS, ""));
        } catch (CoreException e) {
            setErrorMessage(e.getMessage());
        }
    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy config) {
        config.setAttribute(OdinLaunchConfigurationType.ATTR_PROJECT, projectText.getText().trim());
        config.setAttribute(OdinLaunchConfigurationType.ATTR_ARGS, argsText.getText().trim());
    }

    @Override
    public boolean isValid(ILaunchConfiguration config) {
        setErrorMessage(null);
        if (projectText.getText().trim().isEmpty()) {
            setErrorMessage("Project path must be specified.");
            return false;
        }
        return true;
    }

    @Override
    public String getName() { return "Main"; }
}
