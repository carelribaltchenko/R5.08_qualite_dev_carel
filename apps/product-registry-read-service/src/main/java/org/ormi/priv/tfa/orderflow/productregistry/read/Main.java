package org.ormi.priv.tfa.orderflow.productregistry.read;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d'entrée de l'application Product Registry Read Service.
 *
 * <p>Service Quarkus responsable des opérations de lecture (requêtes)
 * et de la projection des événements en vues de lecture.</p>
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
            ProductRegistryReadApplication.class,
            (exitCode, exception) -> {},
            args);
    }

    public static class ProductRegistryReadApplication implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
