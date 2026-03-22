package com.odin.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class OdinCodeScanner extends RuleBasedScanner {
    public OdinCodeScanner() {
        Color keywordColor = new Color(Display.getCurrent(), 127, 0, 85);
        Color typeColor = new Color(Display.getCurrent(), 0, 0, 192);
        Color stringColor = new Color(Display.getCurrent(), 42, 0, 255);
        Color commentColor = new Color(Display.getCurrent(), 63, 127, 95);

        IToken keyword = new Token(new TextAttribute(keywordColor, null, SWT.BOLD));
        IToken type = new Token(new TextAttribute(typeColor));
        IToken string = new Token(new TextAttribute(stringColor));
        IToken comment = new Token(new TextAttribute(commentColor, null, SWT.ITALIC));
        IToken other = new Token(new TextAttribute(null));

        IRule[] rules = new IRule[5];
        rules[0] = new SingleLineRule("\"", "\"", string, '\\');
        rules[1] = new SingleLineRule("//", "", comment, (char) 0, true);
        rules[2] = new MultiLineRule("/*", "*/", comment);
        
        WordRule wordRule = new WordRule(new IWordDetector() {
            public boolean isWordStart(char c) { return Character.isLetter(c) || c == '_'; }
            public boolean isWordPart(char c) { return Character.isLetterOrDigit(c) || c == '_'; }
        }, other);
        
        String[] keywords = {"package", "import", "proc", "struct", "enum", "union", "if", "else", 
            "for", "switch", "case", "return", "defer", "when", "where", "in", "not_in", "do", 
            "break", "continue", "fallthrough", "using", "cast", "transmute", "auto_cast", 
            "distinct", "context", "foreign", "dynamic", "map", "bit_set", "or_return", "or_else"};
        
        for (String kw : keywords) wordRule.addWord(kw, keyword);
        
        String[] types = {"int", "i8", "i16", "i32", "i64", "i128", "uint", "u8", "u16", "u32", 
            "u64", "u128", "f32", "f64", "bool", "string", "rune", "rawptr", "any", "typeid", 
            "byte", "complex64", "complex128", "quaternion128", "quaternion256"};
        
        for (String t : types) wordRule.addWord(t, type);
        
        rules[3] = wordRule;
        rules[4] = new WhitespaceRule(c -> Character.isWhitespace(c));
        
        setRules(rules);
    }
}
