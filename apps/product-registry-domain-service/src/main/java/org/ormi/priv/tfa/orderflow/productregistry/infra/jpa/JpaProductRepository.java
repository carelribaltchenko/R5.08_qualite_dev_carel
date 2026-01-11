package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implémentation JPA du référentiel ProductRepository.
 *
 * <p>Utilise Quarkus Panache et MapStruct pour persister les produits
 * dans la base de données. Convertit entre l'entité JPA et l'agrégat de domaine.</p>
 *
 * <p>Opérations :</p>
 * <ul>
 *   <li>Sauvegarde (create ou update)</li>
 *   <li>Recherche par ID</li>
 *   <li>Vérification d'existence par SKU</li>
 * </ul>
 *
 * @see ProductRepository pour le contrat
 * @see ProductEntity pour l'entité JPA
 * @see ProductJpaMapper pour le mappage domain/JPA
 */

@ApplicationScoped
public class JpaProductRepository implements PanacheRepositoryBase<ProductEntity, UUID>, ProductRepository {

    ProductJpaMapper mapper;
    ProductIdMapper productIdMapper;    
    SkuIdMapper skuIdMapper;

    @Inject
     /**
      * Constructeur avec injection de dépendances.
      *
      * @param mapper le mappeur domain/JPA
      * @param productIdMapper le mappeur ProductId/UUID
      * @param skuIdMapper le mappeur SkuId/String
      */
    public JpaProductRepository(ProductJpaMapper mapper, ProductIdMapper productIdMapper, SkuIdMapper skuIdMapper) {
        this.mapper = mapper;
        this.productIdMapper = productIdMapper;
        this.skuIdMapper = skuIdMapper;
    }

    @Override
    @Transactional
    public void save(Product product) {
        findByIdOptional(productIdMapper.map(product.getId()))
                .ifPresentOrElse(e -> {
                    mapper.updateEntity(product, e);
                }, () -> {
                    ProductEntity newEntity = mapper.toEntity(product);
                    getEntityManager().merge(newEntity);
                });
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return findByIdOptional(productIdMapper.map(id))
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsBySkuId(SkuId skuId) {
        return count("skuId", skuIdMapper.map(skuId)) > 0;
    }
}
