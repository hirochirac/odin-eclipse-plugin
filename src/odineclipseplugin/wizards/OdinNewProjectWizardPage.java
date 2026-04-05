package odineclipseplugin.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class OdinNewProjectWizardPage extends WizardPage {

    private Text projectNameText;

    public OdinNewProjectWizardPage() {
        super("odinProjectPage");
        setTitle("New Odin Project");
        setDescription("Create a new Odin project.");
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout(2, false));

        new Label(container, SWT.NULL).setText("Project name:");
        projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
        projectNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        projectNameText.addModifyListener(e -> validate());

        setControl(container);
        setPageComplete(false);
    }

    private void validate() {
        String name = getProjectName().trim();
        if (name.isEmpty()) {
            setErrorMessage("Project name must be specified");
            setPageComplete(false);
        } else {
            setErrorMessage(null);
            setPageComplete(true);
        }
    }

    public String getProjectName() {
        return projectNameText == null ? "" : projectNameText.getText();
    }
}
