package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;

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
 * Entité JPA représentant un produit dans la base de données de domaine.
 *
 * <p>Mappage JPA pour persister l'agrégat Product. Utilise un schéma dédié "domain"
 * pour isoler les données de domaine des données de lecture.</p>
 *
 * <p>Colonnes :</p>
 * <ul>
 *   <li>{@link #id} : identifiant unique (UUID)</li>
 *   <li>{@link #name} : nom du produit</li>
 *   <li>{@link #description} : description du produit</li>
 *   <li>{@link #skuId} : numéro SKU (unique, 9 caractères max)</li>
 *   <li>{@link #status} : état du cycle de vie (ACTIVE ou RETIRED)</li>
 *   <li>{@link #version} : numéro de version pour le contrôle optimiste</li>
 * </ul>
 *
 * <p>Index :</p>
 * <ul>
 *   <li>Clé unique sur skuId pour garantir l'unicité</li>
 * </ul>
 *
 * @see JpaProductRepository pour l'implémentation du repository
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(
    schema = "domain",
    name = "products",
    indexes = {
        @Index(name = "ux_products_sku", columnList = "sku", unique = true)
    })
public class ProductEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;
    @Column(name = "sku_id", nullable = false, updatable = false, length = 9, unique = true, columnDefinition = "varchar(9)")
    private String skuId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private ProductLifecycle status;
    @Column(name = "version", nullable = false, columnDefinition = "bigint")
    private Long version;
}
