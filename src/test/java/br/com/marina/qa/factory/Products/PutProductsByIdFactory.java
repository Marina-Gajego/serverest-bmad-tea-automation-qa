package br.com.marina.qa.factory.Products;

import br.com.marina.qa.model.Products.PutProductsByIdModel;
import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class PutProductsByIdFactory {

    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    private PutProductsByIdFactory() {
    }

    public static PutProductsByIdModel updateWithAllFields() {
        return PutProductsByIdModel.builder()
                .nome(FAKER.commerce().productName() + " " + UUID.randomUUID().toString().substring(0, 8))
                .preco(FAKER.number().numberBetween(1, 1000))
                .descricao(FAKER.commerce().department())
                .quantidade(FAKER.number().numberBetween(0, 1000))
                .build();
    }

    public static PutProductsByIdModel updateWithSpecificField(String field) {
        PutProductsByIdModel base = updateWithAllFields();

        return switch (field.toLowerCase()) {
            case "nome" -> PutProductsByIdModel.builder().nome(base.getNome()).build();
            case "preco" -> PutProductsByIdModel.builder().preco(base.getPreco()).build();
            case "descricao" -> PutProductsByIdModel.builder().descricao(base.getDescricao()).build();
            case "quantidade" -> PutProductsByIdModel.builder().quantidade(base.getQuantidade()).build();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    public static PutProductsByIdModel updateWithProductData(String nome, Integer preco, String descricao, Integer quantidade) {
        return PutProductsByIdModel.builder()
                .nome(nome)
                .preco(preco)
                .descricao(descricao)
                .quantidade(quantidade)
                .build();
    }

    public static Object updateWithMissingField(String field) {
        PutProductsByIdModel base = updateWithAllFields();

        return switch (field.toLowerCase()) {
            case "nome" -> PutProductsByIdModel.builder()
                    .preco(base.getPreco())
                    .descricao(base.getDescricao())
                    .quantidade(base.getQuantidade())
                    .build();
            case "preco" -> PutProductsByIdModel.builder()
                    .nome(base.getNome())
                    .descricao(base.getDescricao())
                    .quantidade(base.getQuantidade())
                    .build();
            case "descricao" -> PutProductsByIdModel.builder()
                    .nome(base.getNome())
                    .preco(base.getPreco())
                    .quantidade(base.getQuantidade())
                    .build();
            case "quantidade" -> PutProductsByIdModel.builder()
                    .nome(base.getNome())
                    .preco(base.getPreco())
                    .descricao(base.getDescricao())
                    .build();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    public static Object updateWithNullField(String field) {
        PutProductsByIdModel base = updateWithAllFields();

        return switch (field.toLowerCase()) {
            case "nome" -> base.toBuilder().nome(null).build();
            case "preco" -> base.toBuilder().preco(null).build();
            case "descricao" -> base.toBuilder().descricao(null).build();
            case "quantidade" -> base.toBuilder().quantidade(null).build();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    public static Object updateWithEmptyField(String field) {
        PutProductsByIdModel base = updateWithAllFields();

        return switch (field.toLowerCase()) {
            case "nome" -> base.toBuilder().nome("").build();
            case "descricao" -> base.toBuilder().descricao("").build();
            case "preco" -> base.toBuilder().preco(null).build();
            case "quantidade" -> base.toBuilder().quantidade(null).build();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    public static Object updateWithInvalidFieldFormat(String field) {
        Map<String, Object> payload = toMap(updateWithAllFields());

        switch (field.toLowerCase()) {
            case "preco" -> payload.put("preco", "preco-invalido");
            case "quantidade" -> payload.put("quantidade", "quantidade-invalida");
            case "nome" -> payload.put("nome", FAKER.number().randomNumber());
            case "descricao" -> payload.put("descricao", FAKER.number().randomNumber());
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        }

        return payload;
    }

    public static Object updateWithFieldValue(String field, String value) {
        if (value.equalsIgnoreCase("null")) {
            return updateWithNullField(field);
        }

        PutProductsByIdModel base = updateWithAllFields();

        return switch (field.toLowerCase()) {
            case "preco" -> base.toBuilder().preco(Integer.parseInt(value)).build();
            case "quantidade" -> base.toBuilder().quantidade(Integer.parseInt(value)).build();
            case "nome" -> base.toBuilder().nome(value + "-" + UUID.randomUUID().toString().substring(0, 8)).build();
            case "descricao" -> base.toBuilder().descricao(value).build();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    public static Object updateWithExtraUnknownFields() {
        Map<String, Object> payload = toMap(updateWithAllFields());
        payload.put("campo_desconhecido", "valor-extra");
        return payload;
    }

    public static Object emptyBody() {
        return Collections.emptyMap();
    }

    private static Map<String, Object> toMap(PutProductsByIdModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", model.getNome());
        map.put("preco", model.getPreco());
        map.put("descricao", model.getDescricao());
        map.put("quantidade", model.getQuantidade());
        return map;
    }
}
