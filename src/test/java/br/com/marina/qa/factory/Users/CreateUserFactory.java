package br.com.marina.qa.factory.Users;

import br.com.marina.qa.model.Users.CreateUserModel;
import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class CreateUserFactory {

    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    public static CreateUserModel validCreateUser(){
        return CreateUserModel.builder()
                .nome(FAKER.name().fullName())
                .email(FAKER.name().firstName().trim().toLowerCase().replace(" ", "") + "@qa.com")
                .password(String.valueOf(FAKER.number().randomNumber()))
                .administrador("true")
                .build();
    }

    public static CreateUserModel missingField(String field){
        CreateUserModel base = validCreateUser();
        switch (field) {
            case "nome": return base.toBuilder().nome(null).build();
            case "email": return base.toBuilder().email(null).build();
            case "password": return base.toBuilder().password(null).build();
            case "administrador": return base.toBuilder().administrador(null).build();
            default: throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    private static Map<String, Object> toMap(CreateUserModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("nome", model.getNome());
        map.put("email", model.getEmail());
        map.put("password", model.getPassword());
        map.put("administrador", model.getAdministrador());
        return map;
    }

    public static Map<String, Object> fieldAsInteger(String field) {
        Map<String, Object> map = toMap(validCreateUser());
        map.put(field, 12345);
        return map;
    }

    public static CreateUserModel invalidField(String condition){
        CreateUserModel base = validCreateUser();

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
        Map<String, Object> map = toMap(validCreateUser());
        map.put(field, null);
        return map;
    }
}