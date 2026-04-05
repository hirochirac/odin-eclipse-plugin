package odineclipseplugin;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.core.filebuffers.IDocumentSetupParticipantExtension;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;

public class ValidatorDocumentSetupParticipant implements IDocumentSetupParticipant, IDocumentSetupParticipantExtension {

    @Override
    public void setup(IDocument document) {}

    @Override
    public void setup(IDocument document, IPath location, LocationKind locationKind) {}
}
