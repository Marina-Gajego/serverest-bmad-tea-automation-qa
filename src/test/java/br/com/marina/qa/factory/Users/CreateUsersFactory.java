package br.com.marina.qa.factory.Users;

import br.com.marina.qa.model.Users.CreateUsersModel;
import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class CreateUsersFactory {

    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    public static CreateUsersModel validCreateUser(String administrador){
        return CreateUsersModel.builder()
                .nome(FAKER.name().fullName())
                .email(uniqueEmail())
                .password(String.valueOf(FAKER.number().randomNumber()))
                .administrador(administrador)
                .build();
    }

    private static String uniqueEmail() {
        String firstName = FAKER.name().firstName()
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "");
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);

        return firstName + "." + uniqueSuffix + "@qa.com";
    }

    public static CreateUsersModel missingField(String field){
        CreateUsersModel base = validCreateUser("true");
        switch (field) {
            case "nome": return base.toBuilder().nome(null).build();
            case "email": return base.toBuilder().email(null).build();
            case "password": return base.toBuilder().password(null).build();
            case "administrador": return base.toBuilder().administrador(null).build();
            default: throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    private static Map<String, Object> toMap(CreateUsersModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", model.getNome());
        map.put("email", model.getEmail());
        map.put("password", model.getPassword());
        map.put("administrador", model.getAdministrador());
        return map;
    }

    public static Map<String, Object> fieldAsInteger(String field) {
        Map<String, Object> map = toMap(validCreateUser("true"));
        map.put(field, 12345);
        return map;
    }

    public static CreateUsersModel invalidField(String condition){
        CreateUsersModel base = validCreateUser("true");

        if (condition.contains("email")) {
            return base.toBuilder()
                    .email(FAKER.name().firstName().trim().toLowerCase().replace(" ", "") + ".com")
                    .build();
        } else if (condition.contains("administrador")) {
            return base.toBuilder()
                    .administrador("teste")
                    .build();
        } else {
            throw new IllegalArgumentException("Condition not supported: " + condition);
        }
    }

    public static Object fieldAsNull(String field) {
        Map<String, Object> map = toMap(validCreateUser("true"));
        map.put(field, null);
        return map;
    }
}
