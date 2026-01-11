package org.ormi.priv.tfa.orderflow.kernel.product;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Identifiant unique d'un produit dans le domaine métier de gestion de catalogue.
 *
 * <p>Ce record encapsule un UUID pour garantir la type-safety et faciliter
 * la traçabilité des produits à travers les services du système.</p>
 *
 * @param value l'UUID unique du produit, non-null
 */

public record ProductId(@NotNull UUID value) {
    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }
}
