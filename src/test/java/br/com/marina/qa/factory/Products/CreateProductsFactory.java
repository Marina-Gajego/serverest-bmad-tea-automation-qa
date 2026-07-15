package br.com.marina.qa.factory.Products;

import br.com.marina.qa.model.Products.CreateProductsModel;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Slf4j
public final class CreateProductsFactory {

    private static final Faker FAKER = new Faker(new Locale("pt-BR"));
    private static Integer generatePrice() {
        return FAKER.number().numberBetween(1, 1000);
    }

    public static CreateProductsModel validProduct() {
        return CreateProductsModel.builder()
                .nome((FAKER.commerce().productName() + " " + UUID.randomUUID().toString().substring(0, 8)))
                .preco(generatePrice())
                .descricao(FAKER.commerce().department())
                .quantidade(FAKER.number().numberBetween(1, 1000))
                .build();
    }

    public static CreateProductsModel productWithMissingField(String field) {
        switch (field.toLowerCase()) {
            case "nome":      return CreateProductsModel.builder()
                    .preco(generatePrice())
                    .descricao(FAKER.commerce().department())
                    .quantidade(FAKER.number().numberBetween(1, 1000))
                    .build();
            case "preco":     return CreateProductsModel.builder()
                    .nome(FAKER.commerce().productName())
                    .descricao(FAKER.commerce().department())
                    .quantidade(FAKER.number().numberBetween(1, 1000))
                    .build();
            case "descricao": return CreateProductsModel.builder()
                    .nome(FAKER.commerce().productName())
                    .preco(generatePrice())
                    .quantidade(FAKER.number().numberBetween(1, 1000))
                    .build();
            case "quantidade": return CreateProductsModel.builder()
                    .nome(FAKER.commerce().productName())
                    .preco(generatePrice())
                    .descricao(FAKER.commerce().department())
                    .build();
            default: throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    public static CreateProductsModel productWithNullField(String field) {
        CreateProductsModel base = validProduct();
        switch (field.toLowerCase()) {
            case "nome":       return base.toBuilder().nome(null).build();
            case "preco":      return base.toBuilder().preco(null).build();
            case "descricao":  return base.toBuilder().descricao(null).build();
            case "quantidade": return base.toBuilder().quantidade(null).build();
            default: throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    public static CreateProductsModel productWithEmptyField(String field) {
        CreateProductsModel base = validProduct();
        switch (field.toLowerCase()) {
            case "nome":       return base.toBuilder().nome("").build();
            case "descricao":  return base.toBuilder().descricao("").build();
            case "preco":      return base.toBuilder().preco(null).build();
            case "quantidade": return base.toBuilder().quantidade(null).build();
            default: throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    private static Map<String, Object> toMap(CreateProductsModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", model.getNome());
        map.put("preco", model.getPreco());
        map.put("descricao", model.getDescricao());
        map.put("quantidade", model.getQuantidade());
        return map;
    }

    public static Map<String, Object> productWithStringPrice() {
        Map<String, Object> map = toMap(validProduct());
        map.put("preco", "preco-invalido");
        return map;
    }

    public static Map<String, Object> productWithIntegerField(String field) {
        Map<String, Object> map = toMap(validProduct());
        map.put(field, 12345);
        return map;
    }

    public static Object productWithField(String field, String value) {
        if (value.equalsIgnoreCase("null")) {
            return productWithNullField(field);
        }

        CreateProductsModel base = validProduct();
        switch (field.toLowerCase()) {
            case "preco": {
                try {
                    return base.toBuilder().preco(Integer.parseInt(value)).build();
                } catch (NumberFormatException e) {
                    Map<String, Object> map = toMap(base);
                    map.put("preco", value);
                    return map;
                }
            }
            case "quantidade": {
                try {
                    return base.toBuilder().quantidade(Integer.parseInt(value)).build();
                } catch (NumberFormatException e) {
                    Map<String, Object> map = toMap(base);
                    map.put("quantidade", value);
                    return map;
                }
            }
            case "nome":      return base.toBuilder().nome(value + "-" + UUID.randomUUID().toString().substring(0, 8)).build();
            case "descricao": return base.toBuilder().descricao(value).build();
            default: throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    public static CreateProductsModel productWithShortName() {
        return validProduct().toBuilder()
                .nome("A" + "-" + UUID.randomUUID().toString().substring(0, 2))
                .build();
    }

    public static CreateProductsModel productWithLongName() {
        String longName = "A very long product name with more than 150 characters " + "A".repeat(100);
        return validProduct().toBuilder()
                .nome(longName + "-" + UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    public static CreateProductsModel productWithInvalidEmailFormat(String field) {
        CreateProductsModel base = validProduct();

        if ("nome".equalsIgnoreCase(field)) {
            return base.toBuilder().nome("nome-invalido").build();
        }

        if ("descricao".equalsIgnoreCase(field)) {
            return base.toBuilder().descricao("descricao-invalida").build();
        }

        return base.toBuilder().nome("nome-invalido").build();
    }

    public static Map<String, Object> productWithExtraUnknownFields() {
        Map<String, Object> map = toMap(validProduct());
        map.put("campo_desconhecido", "valor-extra");
        return map;
    }
}
