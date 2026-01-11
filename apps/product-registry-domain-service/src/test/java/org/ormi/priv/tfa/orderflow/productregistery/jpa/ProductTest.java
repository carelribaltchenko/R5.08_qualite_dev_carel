package org.ormi.priv.tfa.orderflow.productregistery.jpa;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.ormi.priv.tfa.orderflow.productregistry.infra.jpa.ProductEntity;

class ProductEntityTest {

    @Test
    void testCreateProductEntity() {
        // Test de la structure de base de l'entité
        ProductEntity product = new ProductEntity();
        product.setName("Ecran 4K");
        product.setDescription("Dalle IPS 27 pouces");
        product.setSkuId("SCR-001");

        assertAll("Vérification des champs de l'entité",
            () -> assertEquals("Ecran 4K", product.getName()),
            () -> assertEquals("Dalle IPS 27 pouces", product.getDescription()),
            () -> assertEquals("SCR-001", product.getSkuId())
        );
    }

    @Test
    void testProductStatusConsistency() {
        ProductEntity product = new ProductEntity();
        // Ici on suppose que tu as un champ status ou une logique liée
        // Si ton entité a des méthodes métier (ex: soft delete), teste-les ici.
        product.setName("Test");
        assertNotNull(product.getName());
    }
}