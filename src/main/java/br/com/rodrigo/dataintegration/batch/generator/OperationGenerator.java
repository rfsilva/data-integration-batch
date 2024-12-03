package br.com.rodrigo.dataintegration.batch.generator;

import br.com.rodrigo.dataintegration.batch.dto.*;
import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.text.*;
import java.util.*;

public class OperationGenerator {

    private final Random random;

    private final DecimalFormat df = new DecimalFormat("0000000000");

    public static void main(String []args)  {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            OperationGenerator generator = new OperationGenerator();
            List<OperationDTO> operations = generator.generateOperations();
            mapper.writeValue(new File("C:/develop/data-integration/massa.json"), operations);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationGenerator() {
        random = new Random();
    }

    public List<OperationDTO> generateOperations() {
        List<OperationDTO> operations = new ArrayList<>();
        for (int i = 0; i < random.nextInt(1000); i++) {
            operations.add(generate(random.nextInt(10)));
        }
        return operations;
    }

    private OperationDTO generate(int totalCampos) {
        List<FieldDTO> fields = new ArrayList<>();
        for (int i = 0; i < totalCampos; i++) {
            fields.add(generateFields());
        }
        fields.add(FieldDTO.builder()
                .value(String.valueOf(random.nextLong(100000000000L)))
                .classType("GENERIC")
                .name("PERSON_ID")
                .type("NUMERIC")
                .build());
        fields.add(FieldDTO.builder()
                .value(String.valueOf(random.nextLong(100000000000L)))
                .classType("GENERIC")
                .name("COMPANY_ID")
                .type("NUMERIC")
                .build());
        fields.add(FieldDTO.builder()
                .value(String.valueOf(random.nextDouble(10000000.0)))
                .classType("GENERIC")
                .name("VLROPER")
                .type("DECIMAL")
                .decimalPlaces("2")
                .build());
        generateFields();

        return OperationDTO.builder()
                .contextId("211120240003410015220000003")
                .movementType("MOVEMENTTYPE-" + df.format(random.nextInt(100000)))
                .operationType((random.nextInt(1000) % 2) == 0 ? "D" : "C")
                .operationName("OPERATIONNAME-" + df.format(random.nextInt(100000)))
                .operationNumber(String.valueOf(random.nextInt(100)))
                .description("DESCRIPTION-" + df.format(random.nextInt(100000)))
                .returnOperationCode("RETURNCODE-" + df.format(random.nextInt(100000)))
                .productOperationId(UUID.randomUUID().toString())
                .fields(fields)
                .build();
    }

    private FieldDTO generateFields() {
        return FieldDTO.builder()
                .type("TYPE-" + df.format(random.nextInt(100000)))
                .value(String.valueOf(random.nextLong(100000000L)))
                .classType("CLASSTYPE-" + df.format(random.nextInt(100000)))
                .name("FIELDNAME-" + df.format(random.nextInt(1000)))
                .build();
    }
}
