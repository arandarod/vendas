package br.aranda.service;

import br.aranda.domain.entity.Produto;

import java.util.List;

public interface ProdutoService {
    Produto getProdutoById(Integer id);
    List<Produto> getProdutos(Produto filtro);
    Produto saveProduto(Produto produto);
    void deleteProdutoById(Integer id);
    void updateProduto(Produto produto, Integer id);
}
