package br.aranda.controller;

import br.aranda.domain.entity.Produto;
import br.aranda.service.ProdutoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@Api("Produtos Api")
public class ProdutoController {
    private final ProdutoService service;

    @GetMapping("{id}")
    @ApiOperation("Obter detalhamento de um produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto encontrado"),
            @ApiResponse(code = 400, message = "Produto não encontrado para o id informado")
    })
    public Produto getProdutoById(@ApiParam(name = "id",
            value = "Id do produto a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable("id") Integer id) {
        return service.getProdutoById(id);
    }

    @GetMapping("listar-produtos")
    @ApiOperation("Obter listagem dos produtos cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto(s) encontrado(s)"),
            @ApiResponse(code = 400, message = "Nenhum produto encontrado na base de dados")
    })
    public List<Produto> getProdutos(Produto filtro) {
        return service.getProdutos(filtro);
    }

    @PostMapping("salvar")
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação")
    })
    public Produto saveProduto(@RequestBody @Valid Produto produto) {
        return service.saveProduto(produto);
    }

    @DeleteMapping("deletar/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Deleta um produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto deletado com sucesso"),
            @ApiResponse(code = 400, message = "Produto não encontrado para o id informado")
    })
    public void deleteProdutoById(@ApiParam(name = "id",
            value = "Id do produto a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable("id") Integer id) {
        service.deleteProdutoById(id);
    }

    @PutMapping("atualizar/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualiza um produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Produto não encontrado para o id informado")
    })
    public void updateProduto(@RequestBody @Valid Produto produto, @ApiParam(name = "id",
            value = "Id do produto a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable("id") Integer id) {
        service.updateProduto(produto, id);
    }
}
