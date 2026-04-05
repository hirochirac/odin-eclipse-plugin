package odineclipseplugin;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class OdinContentAssistProcessor implements IContentAssistProcessor {

    private static final String[] PROPOSALS = {
        "package", "import", "proc", "struct", "enum", "union", "bit_field",
        "if", "else", "when", "for", "switch", "case", "return", "break",
        "continue", "fallthrough", "defer", "in", "not_in", "do", "where",
        "foreign", "using", "cast", "transmute", "auto_cast", "distinct",
        "dynamic", "map", "bit_set", "matrix", "nil", "true", "false",
        "or_else", "or_return", "or_break", "or_continue",
        "int", "i8", "i16", "i32", "i64", "i128",
        "uint", "u8", "u16", "u32", "u64", "u128",
        "f16", "f32", "f64", "f128", "bool", "byte", "rune", "string",
        "cstring", "rawptr", "typeid", "any", "uintptr"
    };

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        String text = viewer.getDocument().get();
        int start = offset;
        while (start > 0 && (Character.isLetterOrDigit(text.charAt(start - 1)) || text.charAt(start - 1) == '_'))
            start--;
        final int prefixStart = start;
        String prefix = text.substring(prefixStart, offset).toLowerCase();

        return java.util.Arrays.stream(PROPOSALS)
            .filter(p -> p.startsWith(prefix))
            .map(p -> new CompletionProposal(p, prefixStart, offset - prefixStart, p.length()))
            .toArray(ICompletionProposal[]::new);
    }

    @Override
    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) { return null; }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() { return new char[]{}; }

    @Override
    public char[] getContextInformationAutoActivationCharacters() { return null; }

    @Override
    public String getErrorMessage() { return null; }

    @Override
    public IContextInformationValidator getContextInformationValidator() { return null; }
}