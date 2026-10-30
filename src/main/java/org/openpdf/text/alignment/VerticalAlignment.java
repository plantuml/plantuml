package org.openpdf.text.alignment;

import org.openpdf.text.Element;
import java.util.Optional;

/**
 * Represents a possible vertical alignment modes for document elements that can be aligned vertically.
 *
 * @author noavarice
 * @see WithVerticalAlignment
 * @since 1.2.7
 */
public enum VerticalAlignment {

    UNDEFINED(Element.ALIGN_UNDEFINED),
    TOP(Element.ALIGN_TOP),
    CENTER(Element.ALIGN_MIDDLE),
    BOTTOM(Element.ALIGN_BOTTOM),
    BASELINE(Element.ALIGN_BASELINE),
    ;

    private final int id;

    VerticalAlignment(int id) {
        this.id = id;
    }

    /**
     * Constructs {@link VerticalAlignment} instance from passed unique alignment {@code id}.
     *
     * @param id Alignment unique ID
     * @return {@link Optional} containing alignment instance. If {@code id} is not recognized, {@link Optional#empty()}
     * will be returned
     * @see Element#ALIGN_UNDEFINED
     * @see Element#ALIGN_TOP
     * @see Element#ALIGN_MIDDLE
     * @see Element#ALIGN_BOTTOM
     * @see Element#ALIGN_BASELINE
     */
    public static Optional<VerticalAlignment> of(final int id) {
        for (final VerticalAlignment alignment : values()) {
            if (alignment.id == id) {
                return Optional.of(alignment);
            }
        }

        return Optional.empty();
    }

    public int getId() {
        return id;
    }
}
