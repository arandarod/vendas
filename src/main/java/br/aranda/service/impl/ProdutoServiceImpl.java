package br.aranda.service.impl;

import br.aranda.domain.entity.Produto;
import br.aranda.domain.repository.ProdutoRepository;
import br.aranda.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {
    private final ProdutoRepository repository;

    @Override
    public Produto getProdutoById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }

    @Override
    public List<Produto> getProdutos(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Produto> example = Example.of(filtro, matcher);

        return repository.findAll(example);
    }

    @Override
    public Produto saveProduto(Produto produto) {
        return repository.save(produto);
    }

    @Override
    public void deleteProdutoById(Integer id) {
        repository.findById(id).map(produto -> {
            repository.delete(produto);
            return Void.TYPE;
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }

    @Override
    public void updateProduto(Produto produto, Integer id) {
        repository.findById(id).map(produtoExistente -> {
            produto.setId(produtoExistente.getId());
            repository.save(produto);
            return produtoExistente;
        }).orElseThrow(() -> {
            return new ResponseStatusException(NOT_FOUND, "Produto não encontrado");
        });
    }
}
