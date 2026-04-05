package odineclipseplugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class OdinPresentationReconciler extends PresentationReconciler {

    private static final String[] KEYWORDS = {
        "package", "import", "proc", "struct", "enum", "union", "bit_field",
        "if", "else", "when", "for", "switch", "case", "return", "break",
        "continue", "fallthrough", "defer", "in", "not_in", "do", "where",
        "foreign", "using", "cast", "transmute", "auto_cast", "distinct",
        "dynamic", "map", "bit_set", "matrix", "nil", "true", "false",
        "or_else", "or_return", "or_break", "or_continue"
    };

    public OdinPresentationReconciler() {
        Display display = Display.getCurrent();
        IToken keyword  = new Token(new TextAttribute(new Color(display, new RGB(127, 0, 85)), null, SWT.BOLD));
        IToken comment  = new Token(new TextAttribute(new Color(display, new RGB(63, 127, 95))));
        IToken string   = new Token(new TextAttribute(new Color(display, new RGB(42, 0, 255))));
        IToken number   = new Token(new TextAttribute(new Color(display, new RGB(125, 125, 125))));

        WordRule wordRule = new WordRule(new IWordDetector() {
            public boolean isWordStart(char c) { return Character.isLetter(c) || c == '_'; }
            public boolean isWordPart(char c)  { return Character.isLetterOrDigit(c) || c == '_'; }
        });
        for (String kw : KEYWORDS) wordRule.addWord(kw, keyword);

        RuleBasedScanner scanner = new RuleBasedScanner();
        scanner.setRules(new IRule[] {
            new EndOfLineRule("//", comment),
            new MultiLineRule("/*", "*/", comment),
            new SingleLineRule("\"", "\"", string, '\\'),
            new SingleLineRule("'", "'", string, '\\'),
            new SingleLineRule("`", "`", string),
            new NumberRule(number),
            wordRule
        });

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
        this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
    }
}