package br.com.rodrigo.dataintegration.batch.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultDTO {

    private String code;
    private String description;
    private String correlationId;
}
