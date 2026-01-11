package org.ormi.priv.tfa.orderflow.store.infra.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.PaginatedProductListDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/**
 * Client REST pour le service de lecture Product Registry.
 *
 * <p>Interface Quarkus MicroProfile Rest Client pour communiquer avec
 * le service de lecture Product Registry Read Service.</p>
 *
 * <p>Endpoints :</p>
 * <ul>
 *   <li>GET /products : rechercher les produits (paginé)</li>
 *   <li>GET /products/{id} : récupérer un produit spécifique</li>
 * </ul>
 */

@ApplicationScoped
@Path("/products")
@RegisterRestClient(configKey = "product-registry-read-api")
/**
 * Interface ProductRegistryService.
 *
 * <p>Package: org.ormi.priv.tfa.orderflow.store.infra.rest.client</p>
 */
public interface ProductRegistryService {

    @GET
    RestResponse<PaginatedProductListDto> searchProducts(
            @QueryParam("sku") String sku,
            @QueryParam("page") int page,
            @QueryParam("size") int size);

    @GET
    @Path("/{id}")
    RestResponse<ProductViewDto> getProductById(@PathParam("id") String id);

    // TODO: implement [Exercice 5] (streamProductEventsByProductId) (Utiliser Multi de SmallRye Mutiny)
}
