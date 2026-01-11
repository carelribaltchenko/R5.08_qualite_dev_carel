package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

/**
 * DTO représentant un résumé de produit pour les listes paginées.
 *
 * <p>Contient un sous-ensemble allégé des propriétés d'un produit,
 * optimisé pour l'affichage en listes et résultats de recherche.</p>
 *
 * @param id l'UUID du produit
 * @param skuId le numéro SKU
 * @param name le nom du produit
 * @param status l'état du produit (ACTIVE ou RETIRED)
 * @param catalogs le nombre de catalogues contenant ce produit
 */
public record ProductSummaryDto(
    String id,
    String skuId,
    String name,
    String status,
    Integer catalogs
) {
    
}
