package odineclipseplugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.jface.text.source.projection.ProjectionViewer;

public class OdinReconcilerStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {

    private IDocument document;
    private ProjectionViewer projectionViewer;

    @Override
    public void setDocument(IDocument document) {
        this.document = document;
    }

    public void setProjectionViewer(ProjectionViewer projectionViewer) {
        this.projectionViewer = projectionViewer;
    }

    @Override
    public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {}

    @Override
    public void reconcile(IRegion partition) {}

    @Override
    public void initialReconcile() {}

    @Override
    public void setProgressMonitor(IProgressMonitor monitor) {}
}
