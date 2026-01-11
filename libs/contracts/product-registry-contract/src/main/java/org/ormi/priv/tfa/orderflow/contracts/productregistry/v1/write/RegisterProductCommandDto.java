package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write;

/**
 * DTO pour la commande d'enregistrement d'un nouveau produit.
 *
 * <p>Transfert d'objet pour la requête POST /products.</p>
 *
 * @param name le nom commercial du produit
 * @param description la description détaillée du produit
 * @param skuId le numéro SKU unique du produit
 */
public record RegisterProductCommandDto(
        String name,
        String description,
        String skuId) {
}
