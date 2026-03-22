package com.odin.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;

public class OdinPresentationReconciler extends PresentationReconciler {
    public OdinPresentationReconciler() {
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new OdinCodeScanner());
        setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
    }
}
