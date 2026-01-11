package org.ormi.priv.tfa.orderflow.store;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d'entrée de l'application Store Backend.
 *
 * <p>Service Quarkus servant de façade pour les clients Web/Mobile.
 * Expose des endpoints RPC pour interagir avec les services de gestion de produits.</p>
 */

@QuarkusMain
public class Main {

    /**
     * Lance l'application Quarkus.
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String... args) {
        Quarkus.run(
            ProductRegistryDomainApplication.class,
            (exitCode, exception) -> {},
            args);
    }

    public static class ProductRegistryDomainApplication implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
