package org.ormi.priv.tfa.orderflow.productregistry;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d'entrée de l'application Product Registry Domain Service.
 *
 * <p>Service Quarkus responsable des opérations d'écriture (commandes)
 * et de la gestion du domaine (agrégats, événements).</p>
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
