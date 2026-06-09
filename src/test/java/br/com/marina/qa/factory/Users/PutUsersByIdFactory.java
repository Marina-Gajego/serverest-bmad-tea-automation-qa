package br.com.marina.qa.factory.Users;

import br.com.marina.qa.model.Users.PutUsersByIdModel;
import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PutUsersByIdFactory {

    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    public static PutUsersByIdModel updateWithAllFields() {
        return PutUsersByIdModel.builder()
                .nome(FAKER.name().fullName())
                .email(FAKER.internet().emailAddress())
                .password(String.valueOf(FAKER.number().randomNumber()))
                .administrador("true")
                .build();
    }

    public static PutUsersByIdModel updateWithSpecificField(String field) {
        switch (field.toLowerCase()) {
            case "nome":
                return PutUsersByIdModel.builder().nome(FAKER.name().fullName()).build();
            case "email":
                return PutUsersByIdModel.builder().email(FAKER.internet().emailAddress()).build();
            case "password":
                return PutUsersByIdModel.builder().password(String.valueOf(FAKER.number().randomNumber())).build();
            case "administrador":
                return PutUsersByIdModel.builder().administrador("true").build();
            default:
                throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    public static PutUsersByIdModel updateWithUserData(String nome, String email, String password, String administrador) {
        return PutUsersByIdModel.builder()
                .nome(nome)
                .email(email)
                .password(password)
                .administrador(administrador)
                .build();
    }

    public static PutUsersByIdModel updateWithEmptyField(String field) {
        PutUsersByIdModel base = updateWithAllFields();
        switch (field.toLowerCase()) {
            case "nome": return base.toBuilder().nome("").build();
            case "email": return base.toBuilder().email("").build();
            case "password": return base.toBuilder().password("").build();
            case "administrador": return base.toBuilder().administrador("").build();
            default: throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    public static Object emptyBody() {
        return Collections.emptyMap();
    }

    public static Object updateWithInvalidFieldFormat(String field) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nome", FAKER.name().fullName());
        payload.put("email", FAKER.internet().emailAddress());
        payload.put("password", String.valueOf(FAKER.number().randomNumber()));
        payload.put("administrador", "true");

        switch (field.toLowerCase()) {
            case "nome":
                payload.put("nome", FAKER.number().randomNumber());
                return payload;
            case "email":
                payload.put("email", "invalid-email-format");
                return payload;
            case "password":
                payload.put("password", FAKER.number().randomNumber());
                return payload;
            case "administrador":
                payload.put("administrador", "teste");
                return payload;
            default:
                throw new IllegalArgumentException("Field not supported: " + field);
        }
    }
}
