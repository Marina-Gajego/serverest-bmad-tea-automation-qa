package br.com.marina.qa.factory.Products;

import br.com.marina.qa.model.Products.GetProductsModel;

public final class GetProductsFactory {

    private GetProductsFactory() {
    }

    public static GetProductsModel getProductBy(String field, Object value) {
        return switch (field.toLowerCase()) {
            case "_id" -> GetProductsModel.builder().id((String) value).build();
            case "nome" -> GetProductsModel.builder().nome((String) value).build();
            case "preco" -> GetProductsModel.builder().preco(value).build();
            case "descricao" -> GetProductsModel.builder().descricao((String) value).build();
            case "quantidade" -> GetProductsModel.builder().quantidade(value).build();
            default -> throw new IllegalArgumentException("Field not supported: " + field);
        };
    }

    public static GetProductsModel getProductAllParams(String id, String nome, Object preco, String descricao, Object quantidade) {
        return GetProductsModel.builder()
                .id(id)
                .nome(nome)
                .preco(preco)
                .descricao(descricao)
                .quantidade(quantidade)
                .build();
    }
}
