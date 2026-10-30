package org.openpdf.text.alignment;

import org.openpdf.text.Element;
import java.util.Optional;

/**
 * Represents a possible horizontal alignment modes for document elements that can be aligned horizontally.
 *
 * @author noavarice
 * @see WithHorizontalAlignment
 * @since 1.2.7
 */
public enum HorizontalAlignment {

    UNDEFINED(Element.ALIGN_UNDEFINED),
    LEFT(Element.ALIGN_LEFT),
    CENTER(Element.ALIGN_CENTER),
    RIGHT(Element.ALIGN_RIGHT),
    JUSTIFIED(Element.ALIGN_JUSTIFIED),
    JUSTIFIED_ALL(Element.ALIGN_JUSTIFIED_ALL),
    ;

    private final int id;

    HorizontalAlignment(int id) {
        this.id = id;
    }

    /**
     * Constructs {@link HorizontalAlignment} instance from passed unique alignment {@code id}.
     *
     * @param id Alignment unique ID
     * @return {@link Optional} containing alignment instance. If {@code id} is not recognized, {@link Optional#empty()}
     * will be returned
     * @see Element#ALIGN_UNDEFINED
     * @see Element#ALIGN_LEFT
     * @see Element#ALIGN_CENTER
     * @see Element#ALIGN_RIGHT
     * @see Element#ALIGN_JUSTIFIED
     * @see Element#ALIGN_JUSTIFIED_ALL
     */
    public static Optional<HorizontalAlignment> of(final int id) {
        for (final HorizontalAlignment alignment : values()) {
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

