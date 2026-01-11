package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1;

/**
 * Énumération des versions du schéma d'événement produit supportées.
 *
 * <p>Enumère les versions des schémas d'événement pour les produits.
 * Actuellement, seule la version 1 est supportée.</p>
 */

public enum ProductEventVersion {
    /**
     * Version 1 du schéma d'événement produit.
     */
    V1(ProductEventV1.EVENT_VERSION);

    private final int value;

    ProductEventVersion(int value) {
        this.value = value;
    }

    /**
     * Récupère la valeur numérique de la version.
     *
     * @return le numéro de version
     */
    public int getValue() {
        return value;
    }
}
