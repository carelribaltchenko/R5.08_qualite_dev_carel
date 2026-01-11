package org.ormi.priv.tfa.orderflow.kernel.product.views;

import java.time.Instant;
import java.util.List;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductEventV1Payload;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Vue de lecture (Read Model) représentant un produit avec son historique complet.
 *
 * <p>Cette classe implémente le côté lecture du pattern CQRS. Elle est l'état
 * projeté d'un produit après application de tous ses événements de domaine.
 * Elle est optimisée pour les requêtes de lecture et contient des informations
 * enrichies non présentes dans l'agrégat de domaine.</p>
 *
 * <p>Propriétés :</p>
 * <ul>
 *   <li>{@link #id} : l'identifiant unique du produit</li>
 *   <li>{@link #version} : numéro de version (séquence du dernier événement)</li>
 *   <li>{@link #skuId} : le numéro SKU du produit</li>
 *   <li>{@link #name} : nom commercial actuel du produit</li>
 *   <li>{@link #description} : description actuelle du produit</li>
 *   <li>{@link #status} : état du cycle de vie actuel</li>
 *   <li>{@link #catalogs} : liste des catalogues contenant ce produit</li>
 *   <li>{@link #events} : historique complet des événements</li>
 *   <li>{@link #createdAt} : date de création du produit</li>
 *   <li>{@link #updatedAt} : date de dernière modification</li>
 * </ul>
 *
 * @see ProductSummary pour une vue allégée du produit
 * @see ProductEventType pour les types d'événements
 */

@Getter
public class ProductView {
    
    @NotNull
    private final ProductId id;
    @NotNull
    private final Long version;
    @NotNull
    private final SkuId skuId;
    @NotBlank
    private final String name;
    @NotNull
    private final String description;
    @NotNull
    private final ProductLifecycle status;
    @NotNull
    private final List<ProductViewCatalogRef> catalogs;
    @NotNull
    private final List<ProductViewEvent> events;
    @NotNull
    private final Instant createdAt;
    @NotNull
    private final Instant updatedAt;

    private ProductView(
        ProductId id,
        Long version,
        SkuId skuId,
        String name,
        String description,
        ProductLifecycle status,
        List<ProductViewCatalogRef> catalogs,
        List<ProductViewEvent> events,
        Instant createdAt,
        Instant updatedAt
    ) {
        this.id = id;
        this.version = version;
        this.skuId = skuId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.catalogs = catalogs;
        this.events = events;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductViewBuilder Builder() {
        return new ProductViewBuilder();
    }

    public static final class ProductViewBuilder {
        private ProductId id;
        private Long version;
        private SkuId skuId;
        private String name;
        private String description;
        private ProductLifecycle status;
        private List<ProductViewCatalogRef> catalogs;
        private List<ProductViewEvent> events;
        private Instant createdAt;
        private Instant updatedAt;

        public ProductViewBuilder id(ProductId id) {
            this.id = id;
            return this;
        }

        public ProductViewBuilder version(Long version) {
            this.version = version;
            return this;
        }

        public ProductViewBuilder skuId(SkuId skuId) {
            this.skuId = skuId;
            return this;
        }

        public ProductViewBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductViewBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductViewBuilder status(ProductLifecycle status) {
            this.status = status;
            return this;
        }

        public ProductViewBuilder catalogs(List<ProductViewCatalogRef> catalogs) {
            this.catalogs = catalogs;
            return this;
        }

        public ProductViewBuilder events(List<ProductViewEvent> events) {
            this.events = events;
            return this;
        }

        public ProductViewBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductViewBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ProductViewBuilder with(ProductView view) {
            this.id = view.id;
            this.version = view.version;
            this.skuId = view.skuId;
            this.name = view.name;
            this.description = view.description;
            this.status = view.status;
            this.catalogs = view.catalogs;
            this.events = view.events;
            this.createdAt = view.createdAt;
            this.updatedAt = view.updatedAt;
            return this;
        }

        public ProductView build() throws ConstraintViolationException {
            ProductView view = new ProductView(id, version, skuId, name, description, status, catalogs, events, createdAt, updatedAt);
            final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            final var violations = validator.validate(view);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return view;
        }
    }

    public static class ProductViewCatalogRef {
        @NotNull
        private final ProductId id;
        @NotNull
        private final String name;

        public ProductViewCatalogRef(ProductId id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Getter
    public static class ProductViewEvent {
        @NotNull
        private final ProductEventType type;
        @NotNull
        private final Instant timestamp;
        @NotNull
        private final Long sequence;
        @NotNull
        private final ProductEventV1Payload payload;

        public ProductViewEvent(ProductEventType type, Instant timestamp, Long sequence, ProductEventV1Payload payload) {
            this.type = type;
            this.timestamp = timestamp;
            this.sequence = sequence;
            this.payload = payload;
        }
    }
}
