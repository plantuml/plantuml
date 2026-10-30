package org.openpdf.text;

public enum StandardFonts {
    // Courier
    COURIER(Font.COURIER, Font.NORMAL),
    COURIER_ITALIC(Font.COURIER, Font.ITALIC),
    COURIER_BOLD(Font.COURIER, Font.BOLD),
    COURIER_BOLDITALIC(Font.COURIER, Font.BOLDITALIC),
    // Helvetica
    HELVETICA(Font.HELVETICA, Font.NORMAL),
    HELVETICA_ITALIC(Font.HELVETICA, Font.ITALIC),
    HELVETICA_BOLD(Font.HELVETICA, Font.BOLD),
    HELVETICA_BOLDITALIC(Font.HELVETICA, Font.BOLDITALIC),

    // Times
    TIMES(Font.TIMES_ROMAN, Font.NORMAL),
    TIMES_ITALIC(Font.TIMES_ROMAN, Font.ITALIC),
    TIMES_BOLD(Font.TIMES_ROMAN, Font.BOLD),
    TIMES_BOLDITALIC(Font.TIMES_ROMAN, Font.BOLDITALIC),
    // Others
    SYMBOL(Font.SYMBOL, -1),
    ZAPFDINGBATS(Font.ZAPFDINGBATS, -1),
    ;

    private final int family;
    private final int style;

    StandardFonts(int family, int style) {
        this.family = family;
        this.style = style;
    }

    public Font create() {
        return create(Font.DEFAULTSIZE);
    }

    public Font create(int size) {
        final Font font;
        if (style == -1) {
            font = new Font(family, size);
        } else {
            font = new Font(family, size, style);
        }
        return font;
    }

}
