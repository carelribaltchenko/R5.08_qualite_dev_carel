package org.ormi.priv.tfa.orderflow.kernel.product;

import jakarta.validation.constraints.NotNull;

/**
 * Unité de gestion des stocks (Stock Keeping Unit) pour identifier les variantes de produits.
 *
 * <p>Ce record encapsule une chaîne de caractères validée qui suit un format spécifique :
 * trois lettres majuscules, un trait d'union, et cinq chiffres (ex: ABC-12345).</p>
 *
 * <p>Le constructeur compact valide le format et lève une {@link IllegalArgumentException}
 * si le format ne correspond pas au pattern attendu.</p>
 *
 * @param value le SKU unique du produit au format [A-Z]{3}-[0-9]{5}, non-null
 * @throws IllegalArgumentException si le format du SKU est invalide
 */

public record SkuId(@NotNull String value) {
    private static final java.util.regex.Pattern SKU_PATTERN =
        java.util.regex.Pattern.compile("^[A-Z]{3}-\\d{5}$");

    public SkuId {
        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid SKU format, expected [Alpha]{3}-[Digit]{5}");
        }
    }
}
