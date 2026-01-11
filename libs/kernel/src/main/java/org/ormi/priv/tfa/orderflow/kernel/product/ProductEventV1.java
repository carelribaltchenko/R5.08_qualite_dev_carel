package org.ormi.priv.tfa.orderflow.kernel.product;

import java.util.UUID;

import org.ormi.priv.tfa.orderflow.cqrs.DomainEvent;
import org.ormi.priv.tfa.orderflow.kernel.Product;

/**
 * Interface scellée représentant les événements de domaine pour les produits (version 1).
 *
 * <p>Implémente le pattern Event Sourcing avec une version d'événement fixée à 1.
 * Tous les événements produit héritent de cette interface et doivent définir
 * leur payload associé.</p>
 *
 * <p>Événements supportés :</p>
 * <ul>
 *   <li>{@code ProductRegistered} : enregistrement initial d'un nouveau produit</li>
 *   <li>{@code ProductNameUpdated} : mise à jour du nom du produit</li>
 *   <li>{@code ProductDescriptionUpdated} : mise à jour de la description</li>
 *   <li>{@code ProductRetired} : retraite d'un produit existant</li>
 * </ul>
 *
 * @see DomainEvent pour l'interface parente
 * @see ProductEventV1Envelope pour les enveloppes d'événement
 */

public sealed interface ProductEventV1 extends DomainEvent {
    public static final int EVENT_VERSION = 1;

    /**
     * Retourne le type d'agrégat pour les événements produits.
     *
     * @return "Product" le type d'agrégat
     */
    default String aggregateType() {
        return Product.class.getSimpleName();
    }

    /**
     * Retourne l'ID du produit affecté par cet événement.
     *
     * @return l'ID du produit
     */
    ProductId productId();

    /**
     * Retourne l'UUID du produit (ID de l'agrégat).
     *
     * @return l'UUID du produit
     */
    default UUID aggregateId() {
        return productId().value();
    }

    /**
     * Retourne la version du schéma d'événement produit.
     *
     * @return 1 (version actuelle)
     */
    @Override
    default public int version() {
        return EVENT_VERSION;
    }

    public sealed interface ProductEventV1Payload extends DomainEventPayload {
        public static final class Empty implements ProductEventV1Payload {}
    }

    public final class ProductRegistered implements ProductEventV1 {
        private final ProductId productId;
        private final ProductRegisteredPayload payload;

        public ProductRegistered(ProductId productId, SkuId skuId, String name, String description) {
            this.productId = productId;
            this.payload = new ProductRegisteredPayload(skuId.value(), name, description);
        }

        @Override
        public ProductId productId() {
            return productId;
        }

        @Override
        public ProductRegisteredPayload payload() {
            return payload;
        }

        public static record ProductRegisteredPayload(
                String skuId,
                String name,
                String description) implements ProductEventV1Payload {
        }
    }

    public final class ProductRetired implements ProductEventV1 {
        private final ProductId productId;

        public ProductRetired(ProductId productId) {
            this.productId = productId;
        }

        @Override
        public ProductId productId() {
            return productId;
        }

        @Override
        public ProductEventV1Payload payload() {
            return new ProductEventV1Payload.Empty();
        }
    }

    public final class ProductNameUpdated implements ProductEventV1 {
        private final ProductId productId;
        private final ProductNameUpdatedPayload payload;

        public ProductNameUpdated(ProductId productId, String oldName, String newName) {
            this.productId = productId;
            this.payload = new ProductNameUpdatedPayload(oldName, newName);
        }

        @Override
        public ProductId productId() {
            return productId;
        }

        @Override
        public ProductNameUpdatedPayload payload() {
            return payload;
        }

        public static record ProductNameUpdatedPayload(
                String oldName,
                String newName) implements ProductEventV1Payload {
        }
    }

    public final class ProductDescriptionUpdated implements ProductEventV1 {
        private final ProductId productId;
        private final ProductDescriptionUpdatedPayload payload;

        public ProductDescriptionUpdated(ProductId productId, String oldDescription, String newDescription) {
            this.productId = productId;
            this.payload = new ProductDescriptionUpdatedPayload(oldDescription, newDescription);
        }

        @Override
        public ProductId productId() {
            return productId;
        }

        @Override
        public ProductDescriptionUpdatedPayload payload() {
            return payload;
        }

        public static record ProductDescriptionUpdatedPayload(
                String oldDescription,
                String newDescription) implements ProductEventV1Payload {
        }
    }
}
