package org.ormi.priv.tfa.orderflow.kernel.common;

import org.ormi.priv.tfa.orderflow.kernel.Product;

/**
 * Énumération des types d'agrégats supportés par le système.
 *
 * <p>Énumère tous les agrégats du domaine qui produisent des événements
 * traçables via l'Event Store.</p>
 */
public enum AggregateType {
    /**
     * Type d'agrégat pour les produits.
     */
    PRODUCT(Product.class.getSimpleName());

    private final String value;

    AggregateType(String value) {
        this.value = value;
    }

    /**
     * Récupère la valeur textuelle du type d'agrégat.
     *
     * @return le nom du type d'agrégat
     */
    public String value() {
        return value;
    }
}
