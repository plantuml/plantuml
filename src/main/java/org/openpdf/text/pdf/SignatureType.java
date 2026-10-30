package org.openpdf.text.pdf;

public enum SignatureType {

    /**
     * Represents an unsigned (empty) signature field
     */
    UNSIGNED,

    /**
     * Represents an incomplete or partly signed signature field. This signature should be considered broken.
     */
    INCOMPLETE,

    /**
     * Signed signature type. A document can contain one or more approval signatures. These signatures appear in
     * signature form fields. *dobe Reader shows a green check mark when a valid approval signature, signed with an
     * official certificate is present.
     */
    APPROVAL,

    /**
     * Signed signature type. A document can contain (only) one certification signature. This signature can define
     * different permissions. *dobe Reader shows a blue ribbon when a document was certified with an official
     * certificate.
     * <p>
     * All changes to the document are permitted; no change to the document shall invalidate the signature.
     */
    CERTIFICATION_ALL_CHANGES,

    /**
     * Signed signature type. A document can contain (only) one certification signature. This signature can define
     * different permissions. *dobe Reader shows a blue ribbon when a document was certified with an official
     * certificate.
     * <p>
     * No changes to the document shall be permitted; any change to the document shall invalidate the signature.
     */
    CERTIFICATION_NO_CHANGES,

    /**
     * Signed signature type. A document can contain (only) one certification signature. This signature can define
     * different permissions. *dobe Reader shows a blue ribbon when a document was certified with an official
     * certificate.
     * <p>
     * Permitted changes shall be filling in forms, instantiating page templates, and signing; other changes shall
     * invalidate the signature.
     */
    CERTIFICATION_FILLINGFORMS_SIGNING,

    /**
     * Signed signature type. A document can contain (only) one certification signature. This signature can define
     * different permissions. *dobe Reader shows a blue ribbon when a document was certified with an official
     * certificate.
     * <p>
     * Permitted changes shall be the same as for 2, as well as annotation creation,deletion, and modification; other
     * changes shall invalidate the signature.
     */
    CERTIFICATION_FILLINGFORMS_SIGNING_ANNOTATIONS,

    /**
     * Signed signature type. A document can contain one usage rights signature (in previous PDF specs two). This
     * signature is created by certain manufacturers using their own, proprietary key. When a PDF document is signed
     * with such a usage rights signature certain features might be unlocked. For instance *dobe has such a key which
     * unlocks certain features ('Reader enabled') in its reader software. But other vendors can use their own private
     * key to enforce similar usage rights in their own software.
     */
    USAGE_RIGHTS;

    public boolean isCertificationType() {
        return this == CERTIFICATION_NO_CHANGES || this == CERTIFICATION_FILLINGFORMS_SIGNING
                || this == CERTIFICATION_FILLINGFORMS_SIGNING_ANNOTATIONS || this == CERTIFICATION_ALL_CHANGES;
    }
}
