package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
/**
 * Interface scellée regroupant toutes les commandes métier pour les produits.
 *
 * <p>Les commandes représentent les intentions métier pour modifier l'état des produits.
 * Elles sont traitées par les services de domaine correspondants.</p>
 *
 * <p>Commandes disponibles :</p>
 * <ul>
 *   <li>{@code RegisterProductCommand} : enregistrer un nouveau produit</li>
 *   <li>{@code RetireProductCommand} : retirer un produit du catalogue</li>
 *   <li>{@code UpdateProductNameCommand} : mettre à jour le nom d'un produit</li>
 *   <li>{@code UpdateProductDescriptionCommand} : mettre à jour la description d'un produit</li>
 * </ul>
 
 */
    /**
     * Commande pour enregistrer un nouveau produit dans le catalogue.
     *
     * @param name le nom commercial du produit
     * @param description la description détaillée du produit
     * @param skuId le numéro SKU du produit
     */

public sealed interface ProductCommand {
    public record RegisterProductCommand(
            String name,
            String description,
            SkuId skuId) implements ProductCommand {
    /**
     * Commande pour retirer un produit du catalogue.
     *
     * @param productId l'identifiant du produit à retirer
     */
    }

    public record RetireProductCommand(ProductId productId) implements ProductCommand {
    /**
     * Commande pour mettre à jour le nom d'un produit.
     *
     * @param productId l'identifiant du produit à modifier
     * @param newName le nouveau nom du produit
     */
    }

    public record UpdateProductNameCommand(ProductId productId, String newName) implements ProductCommand {
    /**
     * Commande pour mettre à jour la description d'un produit.
     *
     * @param productId l'identifiant du produit à modifier
     * @param newDescription la nouvelle description du produit
     */
    }

    public record UpdateProductDescriptionCommand(ProductId productId, String newDescription) implements ProductCommand {
    }
}
