package org.openpdf.text.pdf;

import java.util.List;
import java.util.Map;

/**
 * Represents the basic needs for reading fields.
 */
public interface FieldReader {

    Map<String, String> getAllFields();

    String getFieldValue(String fieldKey);

    List<String> getListValues(String fieldKey);

}
