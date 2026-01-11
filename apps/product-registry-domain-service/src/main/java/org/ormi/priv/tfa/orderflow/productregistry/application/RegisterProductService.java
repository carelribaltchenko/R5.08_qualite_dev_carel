package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d'application pour l'enregistrement de nouveaux produits.
 *
 * <p>Gère le cas d'usage d'enregistrement d'un produit dans le catalogue.
 * Encapsule la logique métier suivante :</p>
 * <ul>
 *   <li>Validation de l'unicité du SKU</li>
 *   <li>Création du produit via l'agrégat Product</li>
 *   <li>Persistence de l'agrégat</li>
 *   <li>Enregistrement de l'événement ProductRegistered dans le journal d'événements</li>
 *   <li>Publication de l'événement dans la boîte de sortie (Outbox) pour propagation vers la lecture</li>
 * </ul>
 *
 * <p>Implémente le pattern CQRS et Event Sourcing.</p>
 *
 * @see RegisterProductService#handle(RegisterProductCommand) pour traiter une commande
 */

@ApplicationScoped
public class RegisterProductService {

    ProductRepository repository;
    EventLogRepository eventLog;
    OutboxRepository outbox;

    @Inject
     /**
      * Constructeur du service avec injection de dépendances.
      *
      * @param repository le référentiel des produits
      * @param eventLog le référentiel du journal d'événements
      * @param outbox le référentiel de la boîte de sortie
      */
    public RegisterProductService(
        ProductRepository repository,
        EventLogRepository eventLog,
        OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

     /**
      * Traite une commande d'enregistrement de produit.
      *
      * <p>Crée un nouveau produit, le persiste, enregistre l'événement
      * et publie le message via la boîte de sortie.</p>
      *
      * @param cmd la commande d'enregistrement
      * @return l'identifiant du produit créé
      * @throws IllegalArgumentException si le SKU existe déjà
      */
    @Transactional
    public ProductId handle(RegisterProductCommand cmd) throws IllegalArgumentException {
        if (repository.existsBySkuId(cmd.skuId())) {
            throw new IllegalArgumentException(String.format("SKU already exists: %s", cmd.skuId()));
        }
        Product product = Product.create(
                cmd.name(),
                cmd.description(),
                cmd.skuId());
        // Save domain object
        repository.save(product);
        EventEnvelope<ProductRegistered> evt = EventEnvelope.with(new ProductRegistered(product.getId(), product.getSkuId(), cmd.name(), cmd.description()), product.getVersion());
        // Appends event to the log
        final EventLogEntity persistedEvent = eventLog.append(evt);
        // Publish outbox
        outbox.publish(OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build());
        return product.getId();
    }
}
