package org.ormi.priv.tfa.orderflow.productregistry.infra.web.dto;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;


@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = true),
    uses = { SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
/**
 * Interface CommandDtoMapper.
 *
 * <p>Package: org.ormi.priv.tfa.orderflow.productregistry.infra.web.dto</p>
 *
 * <p>Documentation générée automatiquement : compléter si besoin avec des détails métier.</p>
 */
public interface CommandDtoMapper {
    public RegisterProductCommand toCommand(RegisterProductCommandDto dto);
    public RegisterProductCommandDto toDto(RegisterProductCommand command);
}
