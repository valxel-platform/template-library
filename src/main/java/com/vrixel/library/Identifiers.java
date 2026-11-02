package com.vrixel.library;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Example API: validation for opaque identifiers that are safe to log. Identifiers that could
 * carry PHI (medical record numbers, names) must never pass this check.
 */
public final class Identifiers {

    private static final Pattern OPAQUE = Pattern.compile("^[a-z]{2,8}_[0-9A-HJKMNP-TV-Z]{26}$");

    private Identifiers() {}

    /**
     * Whether {@code id} is an opaque, prefixed ULID such as {@code pat_01J9Z3M8Q4R7T2V5W8X1Y3Z6A9}.
     *
     * @param id the identifier to check; may be null
     * @return true when it is opaque and safe to log
     */
    public static boolean isOpaque(String id) {
        return id != null && OPAQUE.matcher(id).matches();
    }

    /**
     * Returns {@code id} when it is opaque, or throws.
     *
     * @param id the identifier to check
     * @return the same identifier
     * @throws IllegalArgumentException when it is not opaque; the message never includes the value
     */
    public static String requireOpaque(String id) {
        Objects.requireNonNull(id, "id");
        if (!isOpaque(id)) {
            throw new IllegalArgumentException("identifier is not an opaque prefixed ULID");
        }
        return id;
    }
}
