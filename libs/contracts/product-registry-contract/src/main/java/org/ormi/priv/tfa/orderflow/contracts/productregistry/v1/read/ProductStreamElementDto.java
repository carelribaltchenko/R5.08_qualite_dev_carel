package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.time.Instant;

/**
 * DTO représentant un élément d'événement produit à diffuser.
 *
 * <p>Utilisé pour le streaming en temps réel des événements produits
 * vers les clients via SSE ou WebSocket.</p>
 *
 * @param type le type d'événement (ex: ProductRegistered, ProductNameUpdated)
 * @param productId l'UUID du produit affecté
 * @param occuredAt l'instant d'occurrence de l'événement
 */

public record ProductStreamElementDto(
    String type,
    String productId,
    Instant occuredAt
) {
}
