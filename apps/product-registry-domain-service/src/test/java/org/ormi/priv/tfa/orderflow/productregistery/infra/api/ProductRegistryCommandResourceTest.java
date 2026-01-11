package org.ormi.priv.tfa.orderflow.productregistery.infra.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@QuarkusTest
public class ProductRegistryCommandResourceTest {

    @Test
    void testRegisterProductEndpoint() {
        // Tâche 4.1 : Test de l'enregistrement via POST
        // On simule l'envoi d'une commande JSON
        given()
            .contentType("application/json")
            .body("""
                {
                    "name": "Clavier Gamer",
                    "description": "Mécanique RGB",
                    "skuId": "KBD-999"
                }
                """)
        .when()
            .post("/api/product-registry/commands/register") 
        .then()
        .statusCode(anyOf(is(200), is(201), is(202)));
            // 202 est courant en CQRS (commande acceptée pour traitement)
    }

    @Test
    void testRegisterProductWithInvalidData() {
        // Tâche 4.1 : Test de validation (nom vide)
        given()
            .contentType("application/json")
            .body("""
                {
                    "name": "",
                    "description": "Invalide",
                    "skuId": "ERR-01"
                }
                """)
        .when()
            .post("/api/product-registry/commands/register")
        .then()
            .statusCode(400); // Bad Request attendu cause Bean Validation
    }

    @Test
    void testUnknownCommandEndpoint() {
        // Tâche 4.2 : Test d'un endpoint inexistant
        given()
        .when()
            .get("/api/product-registry/unknown")
        .then()
            .statusCode(404);
    }
}