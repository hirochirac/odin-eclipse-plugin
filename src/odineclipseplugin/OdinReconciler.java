package odineclipseplugin;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.reconciler.Reconciler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;

public class OdinReconciler extends Reconciler {

    private OdinReconcilerStrategy fStrategy;

    public OdinReconciler() {
        fStrategy = new OdinReconcilerStrategy();
        this.setReconcilingStrategy(fStrategy, IDocument.DEFAULT_CONTENT_TYPE);
    }

    @Override
    public void install(ITextViewer textViewer) {
        super.install(textViewer);
        if (textViewer instanceof ProjectionViewer pViewer)
            fStrategy.setProjectionViewer(pViewer);
    }
}