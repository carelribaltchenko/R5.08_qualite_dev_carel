package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.DefaultBean;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implémentation JPA du référentiel EventLogRepository.
 *
 * <p>Utilise Quarkus Panache pour persister les événements dans l'Event Store.
 * Chaque événement est convertis en entité JPA via MapStruct.</p>
 *
 * @see EventLogRepository pour le contrat
 * @see EventLogEntity pour l'entité JPA
 * @see EventLogJpaMapper pour le mappage
 */

@ApplicationScoped
@DefaultBean
public class JpaEventLogRepository implements PanacheRepository<EventLogEntity>, EventLogRepository {

    private final EventLogJpaMapper mapper;
    private final ObjectMapper objectMapper;

    @Inject
    /**
     * Constructeur avec injection de dépendances.
     *
     * @param mapper le mappeur Event Envelope/Entity
     * @param objectMapper le mappeur JSON
     */
    public JpaEventLogRepository(EventLogJpaMapper mapper, ObjectMapper objectMapper) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    /**
     * Persiste un événement dans le journal d'événements.
     *
     * <p>Convertit l'enveloppe d'événement en entité et la persiste.</p>
     *
     * @param eventLog l'enveloppe d'événement à persister
     * @return l'entité persistée
     */
	@Override
    @Transactional
	public EventLogEntity append(EventEnvelope<?> eventLog) {
		EventLogEntity entity = mapper.toEntity(eventLog, objectMapper);
		persist(entity);
		return entity;
	}
}
