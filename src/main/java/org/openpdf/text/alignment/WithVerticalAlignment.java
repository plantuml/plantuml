package org.openpdf.text.alignment;

/**
 * Marks objects that can be aligned vertically.
 *
 * @author noavarice
 * @since 1.2.7
 */
public interface WithVerticalAlignment {

    /**
     * Sets vertical alignment mode.
     *
     * @param alignment New alignment mode. If null, current alignment must be left unchanged
     */
    void setVerticalAlignment(final VerticalAlignment alignment);
}
