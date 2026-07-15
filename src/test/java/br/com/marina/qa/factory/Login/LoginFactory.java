package br.com.marina.qa.factory.Login;

import br.com.marina.qa.model.Login.LoginModel;
import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoginFactory {

    private static final Faker FAKER = new Faker(new Locale("pt-BR"));

    public static LoginModel validLogin() {
        return LoginModel.builder()
                .email(FAKER.internet().emailAddress())
                .password(FAKER.internet().password(8, 16, true, true))
                .build();
    }

    public static LoginModel validCredentials(String email, String password) {
        return LoginModel.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static LoginModel missingEmail() {
        return validLogin().toBuilder().email(null).build();
    }

    public static LoginModel missingPassword() {
        return validLogin().toBuilder().password(null).build();
    }

    public static LoginModel invalidFormatEmail() {
        return validLogin().toBuilder()
                .email(FAKER.name().firstName().toLowerCase() + "atcom")
                .build();
    }

    private static Map<String, Object> toMap(LoginModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", model.getEmail());
        map.put("password", model.getPassword());
        return map;
    }

    public static Map<String, Object> fieldNull(String field) {
        Map<String, Object> map = toMap(validLogin());
        map.put(field, null);
        return map;
    }

    public static Map<String, Object> fieldAsInteger(String field) {
        Map<String, Object> map = toMap(validLogin());
        map.put(field, 1234);
        return map;
    }

    public static LoginModel fieldEmpty(String field) {
        if (field.equalsIgnoreCase("email")) {
            return validLogin().toBuilder().email("").build();
        } else if (field.equalsIgnoreCase("password")) {
            return validLogin().toBuilder().password("").build();
        }
        throw new IllegalArgumentException("Field not supported: " + field);
    }

    public static LoginModel wrongEmail() {
        return validLogin().toBuilder()
                .email("email-invalido")
                .build();
    }

    public static LoginModel wrongPassword(){
        return validLogin().toBuilder()
                .password("12345")
                .build();
    }

    public static LoginModel wrongEmailAndPassword(){
        return LoginModel.builder()
                .email("marina@qa.com.br")
                .password("12345")
                .build();
    }
}