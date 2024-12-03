package br.com.rodrigo.dataintegration.batch.processor;

import br.com.rodrigo.dataintegration.batch.config.*;
import br.com.rodrigo.dataintegration.batch.dto.*;
import lombok.extern.slf4j.*;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;
import org.springframework.web.client.*;

import java.util.*;

@Slf4j
@Component
public class OperationItemProcessor implements ItemProcessor<OperationDTO, OperationDTO> {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private SSOConfig ssoConfig;

	private String keycloakToken;

	@Override
	public OperationDTO process(final OperationDTO operation) {
		ResultDTO result = sendToApi(operation);

		try {
			OperationDTO operationResult = (OperationDTO) operation.clone();
			operationResult.setResult(result);
			return operationResult;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	private ResultDTO sendToApi(OperationDTO operation) {
		if (keycloakToken == null) {
			keycloakToken = getKeycloakToken();
			log.info("Token: {}", keycloakToken);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + keycloakToken);

		HttpEntity<OperationDTO> entity = new HttpEntity<>(operation, headers);
		try {
			ResponseEntity<String> response = restTemplate.exchange(ssoConfig.getHost(), HttpMethod.POST, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				System.out.println("Operation " + operation.getProductOperationId() + " sent to the API successfully, with the following corrsponding ID: " + response.getBody());
				return ResultDTO.builder()
						.correlationId(response.getBody())
						.code(response.getStatusCode().toString())
						.description("Success")
						.build();
			} else {
				System.out.println("Error sending operation " + operation.getProductOperationId() + " to the API. Status: " + response.getStatusCode());
				return ResultDTO.builder()
						.correlationId(null)
						.code(response.getStatusCode().toString())
						.description("Failure")
						.build();
			}
		} catch (Exception e) {
			System.err.println("Error while sending operation " + operation.getProductOperationId() + " to the API: " + ssoConfig.getHost());
			return ResultDTO.builder()
					.correlationId(null)
					.code("500")
					.description("Failure")
					.build();
		}
	}

	private String getKeycloakToken() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", ssoConfig.getClientId());
		params.add("client_secret", ssoConfig.getClientSecret());
		params.add("grant_type", "client_credentials");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
		ResponseEntity<Map> response = restTemplate.postForEntity(ssoConfig.getTokenPath(), request, Map.class);
		return (String) response.getBody().get("access_token");
	}
}
