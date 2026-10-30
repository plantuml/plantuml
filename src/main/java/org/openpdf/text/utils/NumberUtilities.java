package org.openpdf.text.utils;

import java.util.Optional;

public final class NumberUtilities {

    private NumberUtilities() {
    }

    /**
     * Try parse float from string and return {@link Optional#empty()} in case of {@link NumberFormatException}
     *
     * @param value string value
     * @return {@link Optional} containing parsed value or empty
     */
    public static Optional<Float> parseFloat(String value) {
        try {
            return Optional.of(Float.parseFloat(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Try parse int from string and return {@link Optional#empty()} in case of {@link NumberFormatException}
     *
     * @param value string value
     * @return {@link Optional} containing parsed value or empty
     */
    public static Optional<Integer> parseInt(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
