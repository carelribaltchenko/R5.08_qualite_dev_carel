package org.ormi.priv.tfa.orderflow.productregistry.read.infra.jpa;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant une vue de produit (Read Model) dans la base de lecture.
 *
 * <p>Mappage JPA pour persister les vues produits optimisées pour la lecture.
 * Utilise un schéma dédié "read_product_registry" pour isoler les données de lecture.</p>
 *
 * <p>Colonnes :</p>
 * <ul>
 *   <li>{@link #id} : identifiant unique (UUID)</li>
 *   <li>{@link #version} : numéro de version (séquence du dernier événement)</li>
 *   <li>{@link #skuId} : numéro SKU (unique)</li>
 *   <li>{@link #name} : nom actuel du produit</li>
 *   <li>{@link #description} : description actuelle</li>
 *   <li>{@link #status} : état du cycle de vie</li>
 *   <li>{@link #events} : historique des événements en JSON</li>
 *   <li>{@link #catalogs} : liste des catalogues en JSON</li>
 *   <li>{@link #createdAt} : date de création</li>
 *   <li>{@link #updatedAt} : date de dernière mise à jour</li>
 * </ul>
 *
 * <p>Index :</p>
 * <ul>
 *   <li>Index sur SKU pour les recherches</li>
 * </ul>
 *
 * @see JpaProductViewRepository pour le repository
 * @see ProductViewJpaMapper pour le mappage
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(
    schema = "read_product_registry",
    name = "product_view",
    indexes = {
        @Index(name = "idx_prdview_sku", columnList = "sku_id")
    })
public class ProductViewEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;
    @Column(name = "_version", nullable = false, columnDefinition = "bigint")
    private Long version;
    @Column(name = "sku_id", nullable = false, length = 9, unique = true, columnDefinition = "varchar(9)")
    private String skuId;
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private ProductLifecycle status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "events", nullable = false, columnDefinition = "jsonb")
    private JsonNode events;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "catalogs", nullable = false, columnDefinition = "jsonb")
    private JsonNode catalogs;
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;
}
