package org.openpdf.text.alignment;

/**
 * Marks objects that can be aligned horizontally.
 *
 * @author noavarice
 * @since 1.2.7
 */
public interface WithHorizontalAlignment {

    /**
     * Sets horizontal alignment mode.
     *
     * @param alignment New alignment mode. If null, current alignment must be left unchanged
     */
    void setHorizontalAlignment(final HorizontalAlignment alignment);
}
