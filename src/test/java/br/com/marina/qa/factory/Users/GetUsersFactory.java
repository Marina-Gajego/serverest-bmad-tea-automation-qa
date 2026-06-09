package br.com.marina.qa.factory.Users;

import br.com.marina.qa.model.Users.GetUsersModel;

public final class GetUsersFactory {

    public static GetUsersModel getUserBy(String field, String value) {
        switch (field.toLowerCase()) {
            case "_id":
                return GetUsersModel.builder().id(value).build();
            case "nome":
                return GetUsersModel.builder().nome(value).build();
            case "email":
                return GetUsersModel.builder().email(value).build();
            case "password":
                return GetUsersModel.builder().password(value).build();
            case "administrador":
                return GetUsersModel.builder().administrador(value).build();
            default:
                throw new IllegalArgumentException("Field not supported: " + field);
        }
    }

    public static GetUsersModel getUserByTwoQueryParams(String field1, String value1, String field2, String value2) {
        GetUsersModel.GetUsersModelBuilder builder = GetUsersModel.builder();
        applyField(builder, field1, value1);
        applyField(builder, field2, value2);
        return builder.build();
    }

    public static GetUsersModel getUserAllParams(String nome, String email, String password, String administrador, String id){
        return GetUsersModel.builder()
                .nome(nome)
                .email(email)
                .password(password)
                .administrador(administrador)
                .id(id).build();
    }

    private static void applyField(GetUsersModel.GetUsersModelBuilder builder, String field, String value) {
        switch (field.toLowerCase()) {
            case "_id" -> builder.id(value);
            case "nome" -> builder.nome(value);
            case "email" -> builder.email(value);
            case "password" -> builder.password(value);
            case "administrador" -> builder.administrador(value);
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        }
    }
}
