package org.ormi.priv.tfa.orderflow.cqrs.infra.persistence;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;


/**
 * Interface EventLogRepository.
 *
 * <p>Package: org.ormi.priv.tfa.orderflow.cqrs.infra.persistence</p>
 *
 * <p>Documentation générée automatiquement : compléter si besoin avec des détails métier.</p>
 */
public interface EventLogRepository {
    EventLogEntity append(EventEnvelope<?> eventLog);
}
