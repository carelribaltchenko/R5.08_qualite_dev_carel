package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.util.List;

/**
 * DTO contenant une page de résultats pour la recherche de produits.
 *
 * <p>Transfert d'objet pour les réponses paginées des requêtes GET /products.</p>
 *
 * @param products la liste des produits de la page actuelle
 * @param page le numéro de page actuel (1-basé)
 * @param pageSize le nombre d'éléments par page
 * @param totalElements le nombre total d'éléments correspondant au critère de recherche
 */
public record PaginatedProductListDto(
    List<ProductSummaryDto> products,
    int page,
    int pageSize,
    long totalElements
) {
}
