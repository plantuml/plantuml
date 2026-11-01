/* TeXUnicodeBlock.java
 * =========================================================================
 * This file is NOT part of upstream JLaTeXMath, it was added for PLAN.md
 * (phase 3, step 3c). It replaces java.lang.Character.UnicodeBlock, which the TeaVM class library does not have.
 * It is released under the same terms as the library: GNU GPL version 2 or
 * later, with the GNU linking exception (see the header of the other files of
 * this package).
 * =========================================================================
 */

package org.scilab.forge.jlatexmath;

/**
 * The Unicode blocks that the library tells apart: Basic Latin, the two blocks of
 * the Greek alphabet, Cyrillic, and all the others together. They are the ones that
 * have, or may have, TeX fonts ({@link AlphabetRegistration}); for every other
 * character there is nothing to choose, so {@link #OTHER} is enough.
 * <p>
 * The ranges are those of {@code java.lang.Character.UnicodeBlock} (checked for
 * the 65,536 characters by {@code TeXUnicodeBlockTest}). Each block is one
 * object, so {@code ==} and {@code equals} are the same, and it can be a key of a
 * map.
 */
public final class TeXUnicodeBlock {

    public static final TeXUnicodeBlock BASIC_LATIN = new TeXUnicodeBlock("BASIC_LATIN");
    public static final TeXUnicodeBlock GREEK = new TeXUnicodeBlock("GREEK");
    public static final TeXUnicodeBlock GREEK_EXTENDED = new TeXUnicodeBlock("GREEK_EXTENDED");
    public static final TeXUnicodeBlock CYRILLIC = new TeXUnicodeBlock("CYRILLIC");
    /** Any character that is in none of the blocks above. */
    public static final TeXUnicodeBlock OTHER = new TeXUnicodeBlock("OTHER");

    private final String name;

    private TeXUnicodeBlock(String name) {
        this.name = name;
    }

    public static TeXUnicodeBlock of(char c) {
        if (c < 0x0080) {
            return BASIC_LATIN;
        }
        if (c >= 0x0370 && c <= 0x03FF) {
            return GREEK;
        }
        if (c >= 0x0400 && c <= 0x04FF) {
            return CYRILLIC;
        }
        if (c >= 0x1F00 && c <= 0x1FFF) {
            return GREEK_EXTENDED;
        }
        return OTHER;
    }

    public String toString() {
        return name;
    }
}
