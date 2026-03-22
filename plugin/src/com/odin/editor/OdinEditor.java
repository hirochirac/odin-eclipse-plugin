package com.odin.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.lsp4e.LSPEclipseUtils;
import org.eclipse.ui.editors.text.TextEditor;

public class OdinEditor extends TextEditor {
    public OdinEditor() {
        super();
        setSourceViewerConfiguration(new OdinSourceViewerConfiguration());
    }
    
    @Override
    protected void initializeEditor() {
        super.initializeEditor();
    }
}
