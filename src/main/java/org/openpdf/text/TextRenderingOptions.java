package org.openpdf.text;


/**
 * Text rendering options, including the default language of the document and a flag to enable font glyph substitution
 * (if FOP is available).
 *
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @see Document#setTextRenderingOptions(TextRenderingOptions)
 * @since 3.1.15
 */
public class TextRenderingOptions {

    public static final String DOCUMENT_LANGUAGE_DEFAULT = "dflt";

    /**
     * The default language of the document. Can be set to values like "en_US". This language is used in
     * FopGlyphProcessor to determine which glyphs are to be substituted. The default "dflt" means that all
     * glyphs which can be replaced will be substituted.
     */
    private String documentLanguage;

    private boolean glyphSubstitutionEnabled;

    /**
     * Creates a text rendering options instance with the default options: glyph substitution enabled and "dflt" as
     * document language.
     */
    public TextRenderingOptions() {
        this(DOCUMENT_LANGUAGE_DEFAULT, true);
    }

    /**
     * Creates a text rendering options instance.
     *
     * @param documentLanguage         the wanted language
     * @param glyphSubstitutionEnabled whether glyph substitution is enabled
     */
    public TextRenderingOptions(String documentLanguage, boolean glyphSubstitutionEnabled) {
        this.documentLanguage = documentLanguage;
        this.glyphSubstitutionEnabled = glyphSubstitutionEnabled;
    }

    /**
     * The default language of the document. Can be set to values like "en_US". This language is used in
     * FopGlyphProcessor to determine which glyphs are to be substituted.
     * <p/>
     * The default "dflt" means that all glyphs which can be replaced will be substituted.
     *
     * @return the current document language
     */
    public String getDocumentLanguage() {
        return documentLanguage;
    }

    /**
     * Sets the default language of the document.
     *
     * @param documentLanguage the document language
     * @see #getDocumentLanguage()
     */
    public void setDocumentLanguage(String documentLanguage) {
        this.documentLanguage = documentLanguage;
    }

    /**
     * Returns the glyph substitution enabled flag.
     *
     * @return the glyph substitution enabled flag #see {@link Document#setGlyphSubstitutionEnabled(boolean)}
     */
    public boolean isGlyphSubstitutionEnabled() {
        return glyphSubstitutionEnabled;
    }

    /**
     * Sets the font glyph substitution enabled flag.
     *
     * @param glyphSubstitutionEnabled whether glyph substitution is enabled
     */
    public void setGlyphSubstitutionEnabled(boolean glyphSubstitutionEnabled) {
        this.glyphSubstitutionEnabled = glyphSubstitutionEnabled;
    }
}
