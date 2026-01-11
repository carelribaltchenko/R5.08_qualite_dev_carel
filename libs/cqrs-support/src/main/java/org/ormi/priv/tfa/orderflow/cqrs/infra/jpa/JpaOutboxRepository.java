package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;

import io.quarkus.arc.DefaultBean;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * Implémentation JPA du référentiel OutboxRepository.
 *
 * <p>Utilise Quarkus Panache pour persister les messages dans la boîte de sortie.
 * Gère le cycle de vie des messages (création, récupération, suppression, retraitement).</p>
 *
 * <p>Fonctionnalités :</p>
 * <ul>
 *   <li>Publication de messages</li>
 *   <li>Récupération des messages prêts à être traités</li>
 *   <li>Suppression des messages après traitement réussi</li>
 *   <li>Marquage des messages en erreur avec retry scheduling</li>
 * </ul>
 *
 * @see OutboxRepository pour le contrat
 * @see OutboxEntity pour l'entité JPA
 */

@ApplicationScoped
@DefaultBean
public class JpaOutboxRepository implements PanacheRepository<OutboxEntity>, OutboxRepository {
	private static final int DEFAULT_DELAY_MS = 5000;
	private static final String SQL_FETCH_QUERY = loadSQLQueryFromFile("/db/queries/findReadyByAggregateTypeOrderByAggregateVersion.sql");

		/**
		 * Publie un message dans la boîte de sortie.
		 *
		 * @param entity le message à publier
		 */
	@Override
	public void publish(OutboxEntity entity) {
		persist(entity);
	}

		/**
		 * Récupère les messages prêts à être traités.
		 *
		 * <p>Retourne les messages filtrés par type d'agrégat, ordonnés par version,
		 * et limités aux messages qui n'ont pas dépassé le nombre de tentatives.</p>
		 *
		 * @param aggregateType le type d'agrégat pour filtrer
		 * @param limit le nombre maximum de messages à retourner
		 * @param maxRetries le nombre maximum de tentatives autorisées
		 * @return la liste des messages prêts à être traités
		 */
	@Override
	@SuppressWarnings("unchecked")
	public List<OutboxEntity> fetchReadyByAggregateTypeOrderByAggregateVersion(String aggregateType, int limit,
			int maxRetries) {
		return (List<OutboxEntity>) getEntityManager()
				.createNativeQuery(SQL_FETCH_QUERY, OutboxEntity.class)
				.setParameter("aggregateTypes", aggregateType)
				.setParameter("maxAttempts", maxRetries)
				.setMaxResults(limit)
				.getResultList();
	}

	@Transactional
	@Override
	public void delete(OutboxEntity entity) {
		deleteById(entity.getId());
	}

	@Override
	public void markFailed(OutboxEntity entity, String err) {
		markFailed(entity, err, DEFAULT_DELAY_MS);
	}

	@Transactional
	@Override
	public void markFailed(OutboxEntity entity, String err, int delayMs) {
		update("lastError = ?1, nextAttemptAt = ?2, attempts = attempts + 1 WHERE id = ?3",
				err, Instant.now().plusMillis(delayMs), entity.getId());
	}

	private static String loadSQLQueryFromFile(String classpath) {
		try (InputStream is = JpaOutboxRepository.class.getResourceAsStream(classpath)) {
			return new String(is.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
