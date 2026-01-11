package org.ormi.priv.tfa.orderflow.productregistry.read.infra.api;

import jakarta.ws.rs.Path;

/**
 * Ressource REST pour le streaming en temps réel des événements produits.
 *
 * <p>Fournira à terme les endpoints SSE/WebSocket pour que les clients
 * reçoivent les événements produits en temps réel.</p>
 *
 * <p>Endpoints prévus (à implémenter) :</p>
 * <ul>
 *   <li>GET /products/{id}/pending/stream : streaming des événements d'un produit</li>
 *   <li>GET /products/search/stream : streaming des événements d'une recherche</li>
 * </ul>
 *
 * @see ProductEventBroadcaster pour la diffusion d'événements
 * @see ReadProductService pour la logique de recherche
 */

@Path("/products")
/**
 * Class ProductStreamResource.
 *
 * <p>Package: org.ormi.priv.tfa.orderflow.productregistry.read.infra.api</p>
 *
 * <p>Documentation générée automatiquement : compléter si besoin avec des détails métier.</p>
 */
public class ProductStreamResource {

    // TODO: implement [Exercice 5]
    // private final ReadProductService readProductService;
    // private final ProductIdMapper productIdMapper;

    // @Inject
    // public ProductStreamResource(
    //         ReadProductService readProductService,
    //         ProductIdMapper productIdMapper) {
    //     this.readProductService = readProductService;
    //     this.productIdMapper = productIdMapper;
    // }

    // TODO: implement [Exercice 5]
    // @GET
    // @Path("/{id}/pending/stream")
    // @RestStreamElementType(MediaType.APPLICATION_JSON)
    // public Multi<ProductStreamElementDto> streamPendingOutboxMessagesByProdutId(
    //         @PathParam("id") String id) {
    //     throw new UnsupportedOperationException("TODO: implement [Exercice 5]");
    // }
}
