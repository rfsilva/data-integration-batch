package br.com.rodrigo.dataintegration.batch.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationDTO implements Cloneable {

    @JsonProperty(required = true)
    @JsonPropertyDescription("ID do Contexto informado pelo canal")
    private String contextId;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Código do tipo de movimentação")
    private String movementType;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Código do tipo de operação")
    private String operationType;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Nome da Operação")
    private String operationName;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Numero da Operação")
    private String operationNumber;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Descricao completa da funcionalidade")
    private String description;

    @JsonProperty(required = true)
    @JsonPropertyDescription("URL da operação de estorno")
    private String returnOperationCode;

    @JsonProperty(required = true)
    @JsonPropertyDescription("ID de Operação do Produto")
    private String productOperationId;

    @JsonProperty(value = "operation", required = true)
    @JsonPropertyDescription("Complemento de campos de produto / operação")
    private List<FieldDTO> fields;

    private ResultDTO result;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
