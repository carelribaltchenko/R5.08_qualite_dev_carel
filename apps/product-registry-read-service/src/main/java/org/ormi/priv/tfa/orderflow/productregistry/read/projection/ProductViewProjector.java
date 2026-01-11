package org.ormi.priv.tfa.orderflow.productregistry.read.projection;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.ormi.priv.tfa.orderflow.cqrs.Projector;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductDescriptionUpdatedEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductNameUpdatedEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductRegisteredEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope.ProductRetiredEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductEventType;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewEvent;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Projecteur pour la projection des événements produit vers une vue lisible.
 *
 * <p>Cette classe implémente le pattern CQRS et gère la projection des événements produit
 * en état persistant (ProductView). Elle traite les événements suivants :
 * <ul>
 *   <li>ProductRegistered : enregistrement initial d'un produit</li>
 *   <li>ProductRetired : retraite d'un produit</li>
 *   <li>ProductNameUpdated : mise à jour du nom d'un produit</li>
 *   <li>ProductDescriptionUpdated : mise à jour de la description d'un produit</li>
 * </ul>
 * </p>
 *
 * <p>Le projecteur applique la logique métier suivante :
 * <ul>
 *   <li>Un produit ne peut être enregistré qu'une fois</li>
 *   <li>Seul un produit actif peut être retraité</li>
 *   <li>Seul un produit actif peut voir ses propriétés mises à jour</li>
 *   <li>Les événements obsolètes (avec une séquence inférieure à celle de la vue actuelle) sont ignorés</li>
 * </ul>
 * </p>
 */
@ApplicationScoped
public class ProductViewProjector implements Projector<ProductView, ProductEventV1Envelope<?>> {

	/**
	 * Projette un événement produit sur une vue optionnelle.
	 *
	 * <p>Aiguille l'événement vers le gestionnaire approprié selon son type.
	 * Si aucun gestionnaire n'est implémenté pour le type d'événement, retourne un résultat d'erreur.
	 * </p>
	 *
	 * @param current l'état actuel optionnel de la vue produit
	 * @param ev l'enveloppe d'événement produit à projeter
	 * @return le résultat de la projection (succès, échec ou sans opération)
	 */
	@Override
	public ProjectionResult<ProductView> project(Optional<ProductView> current, ProductEventV1Envelope<?> ev) {
		return switch (ev) {
			case ProductRegisteredEnvelope pre -> handleProjection(current, pre);
			case ProductRetiredEnvelope pre -> handleProjection(current, pre);
			case ProductNameUpdatedEnvelope pre -> handleProjection(current, pre);
			case ProductDescriptionUpdatedEnvelope pre -> handleProjection(current, pre);
			default -> ProjectionResult.failed("Unimplemented event type");
		};
	}

	/**
	 * Traite la projection d'un événement ProductRegistered.
	 *
	 * <p>Crée une nouvelle vue produit avec l'état initial ACTIVE.
	 * Vérifie que le produit n'existe pas déjà ou n'est pas déjà actif.
	 * </p>
	 *
	 * @param current l'état actuel optionnel de la vue produit
	 * @param ev l'enveloppe d'événement d'enregistrement produit
	 * @return le résultat de la projection avec la nouvelle vue créée, ou une erreur si le produit existe déjà
	 */
	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current,
			ProductRegisteredEnvelope ev) {
		if (current.isPresent() && current.get().getStatus() == ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Product already exists and is active");
		}
		ProductView newView = ProductView.Builder()
				.id(new ProductId(ev.event().productId().value()))
				.version(ev.sequence())
				.skuId(new SkuId(ev.event().payload().skuId()))
				.name(ev.event().payload().name())
				.description(ev.event().payload().description())
				.status(ProductLifecycle.ACTIVE)
				.events(List.of(
						new ProductViewEvent(
								ProductEventType.PRODUCT_REGISTERED,
								ev.timestamp(),
								ev.sequence(),
								ev.event().payload())))
				.catalogs(Collections.emptyList())
				.createdAt(ev.timestamp())
				.updatedAt(ev.timestamp())
				.build();
		return ProjectionResult.projected(newView);
	}

	/**
	 * Traite la projection d'un événement ProductRetired.
	 *
	 * <p>Met à jour le statut du produit en RETIRED et enregistre l'événement.
	 * Vérifie que le produit existe et est actif avant la retraite.
	 * Ignore les événements obsolètes (séquence inférieure à celle de la vue actuelle).
	 * </p>
	 *
	 * @param current l'état actuel optionnel de la vue produit
	 * @param ev l'enveloppe d'événement de retraite produit
	 * @return le résultat de la projection, une erreur si le produit n'existe pas ou est déjà retraité,
	 *         ou sans opération si l'événement est obsolète
	 */
	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current, ProductRetiredEnvelope ev) {
		if (current.isEmpty() || current.get().getStatus() != ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Already retired or never existed");
		}
		if (ev.sequence() <= current.get().getVersion()) {
			return ProjectionResult.noOp("Stale retirement ignored");
		}
		ProductView newView = ProductView.Builder()
				.with(current.get())
				.version(ev.sequence())
				.status(ProductLifecycle.RETIRED)
				.events(mergeEvents(current.get().getEvents(),
						new ProductViewEvent(
							ProductEventType.PRODUCT_RETIRED,
							ev.timestamp(),
							ev.sequence(),
							ev.event().payload())))
				.build();
		return ProjectionResult.projected(newView);
	}

	/**
	 * Traite la projection d'un événement ProductNameUpdated.
	 *
	 * <p>Met à jour le nom du produit et enregistre l'événement.
	 * Vérifie que le produit existe et est actif avant la mise à jour.
	 * Ignore les événements obsolètes (séquence inférieure à celle de la vue actuelle).
	 * </p>
	 *
	 * @param current l'état actuel optionnel de la vue produit
	 * @param ev l'enveloppe d'événement de mise à jour du nom
	 * @return le résultat de la projection, une erreur si le produit n'existe pas ou est retraité,
	 *         ou sans opération si l'événement est obsolète
	 */
	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current,
			ProductNameUpdatedEnvelope ev) {
		if (current.isEmpty() || current.get().getStatus() != ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Cannot update name of non-existent or retired product");
		}
		if (ev.sequence() <= current.get().getVersion()) {
			return ProjectionResult.noOp("Stale name update ignored");
		}
		ProductView newView = ProductView.Builder()
				.with(current.get())
				.version(ev.sequence())
				.name(ev.event().payload().newName())
				.events(mergeEvents(current.get().getEvents(),
						new ProductViewEvent(
							ProductEventType.PRODUCT_NAME_UPDATED,
							ev.timestamp(),
							ev.sequence(),
							ev.event().payload())))
				.build();
		return ProjectionResult.projected(newView);
	}

	/**
	 * Traite la projection d'un événement ProductDescriptionUpdated.
	 *
	 * <p>Met à jour la description du produit et enregistre l'événement.
	 * Vérifie que le produit existe et est actif avant la mise à jour.
	 * Ignore les événements obsolètes (séquence inférieure à celle de la vue actuelle).
	 * </p>
	 *
	 * @param current l'état actuel optionnel de la vue produit
	 * @param ev l'enveloppe d'événement de mise à jour de la description
	 * @return le résultat de la projection, une erreur si le produit n'existe pas ou est retraité,
	 *         ou sans opération si l'événement est obsolète
	 */
	private ProjectionResult<ProductView> handleProjection(Optional<ProductView> current,
			ProductDescriptionUpdatedEnvelope ev) {
		if (current.isEmpty() || current.get().getStatus() != ProductLifecycle.ACTIVE) {
			return ProjectionResult.failed("Cannot update description of non-existent or retired product");
		}
		if (ev.sequence() <= current.get().getVersion()) {
			return ProjectionResult.noOp("Stale description update ignored");
		}
		ProductView newView = ProductView.Builder()
				.with(current.get())
				.version(ev.sequence())
				.description(ev.event().payload().newDescription())
				.events(mergeEvents(current.get().getEvents(),
						new ProductViewEvent(
							ProductEventType.PRODUCT_DESCRIPTION_UPDATED,
							ev.timestamp(),
							ev.sequence(),
							ev.event().payload())))
				.build();
		return ProjectionResult.projected(newView);
	}

	/**
	 * Fusionne les événements existants avec un nouvel événement, triés par séquence.
	 *
	 * <p>Combine tous les événements dans une liste unique et les trie selon leur numéro de séquence
	 * pour garantir l'ordre temporel correct des événements.
	 * </p>
	 *
	 * @param existingEvents la liste des événements existants
	 * @param newEvent le nouvel événement à ajouter
	 * @return une liste triée de tous les événements (existants et nouveau)
	 */
	private static List<ProductViewEvent> mergeEvents(List<ProductViewEvent> existingEvents,
			ProductViewEvent newEvent) {
		return Stream.concat(existingEvents.stream(),
				Stream.of(newEvent))
				.sorted(Comparator.comparingLong(ProductViewEvent::getSequence))
				.toList();
	}
}
