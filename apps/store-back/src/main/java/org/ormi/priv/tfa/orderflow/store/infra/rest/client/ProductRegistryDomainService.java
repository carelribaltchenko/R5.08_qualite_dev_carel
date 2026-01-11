package org.ormi.priv.tfa.orderflow.store.infra.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductDescriptionParamsDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductNameParamsDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * Client REST pour le service de domaine Product Registry.
 *
 * <p>Interface Quarkus MicroProfile Rest Client pour communiquer avec
 * le service de domaine Product Registry Domain Service.</p>
 *
 * <p>Endpoints :</p>
 * <ul>
 *   <li>POST /products : enregistrer un nouveau produit</li>
 *   <li>DELETE /products/{id} : retirer un produit</li>
 *   <li>PATCH /products/{id}/name : mettre à jour le nom</li>
 *   <li>PATCH /products/{id}/description : mettre à jour la description</li>
 * </ul>
 */

@ApplicationScoped
@Path("/products")
@RegisterRestClient(configKey = "product-registry-api")
public interface ProductRegistryDomainService {
    
    @POST
    RestResponse<Void> registerProduct(RegisterProductCommandDto cmd);

    @DELETE
    @Path("/{id}")
    RestResponse<Void> retireProduct(@PathParam("id") String productId);

    @PATCH
    @Path("/{id}/name")
    RestResponse<Void> updateProductName(@PathParam("id") String productId, UpdateProductNameParamsDto params);

    @PATCH
    @Path("/{id}/description")
    RestResponse<Void> updateProductDescription(@PathParam("id") String productId, UpdateProductDescriptionParamsDto params);
}
