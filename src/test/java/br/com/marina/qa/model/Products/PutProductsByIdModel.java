package br.com.marina.qa.model.Products;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PutProductsByIdModel {
    private String nome;
    private Integer preco;
    private String descricao;
    private Integer quantidade;
}
