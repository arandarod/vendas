package br.aranda.controller;

import br.aranda.domain.entity.Cliente;
import br.aranda.service.ClienteService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Api("Clientes Api")
public class ClienteController {
    private final ClienteService service;

    @GetMapping("{id}")
    @ApiOperation("Obter detalhamento de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 400, message = "Cliente não encontrado para o id informado")
    })
    public Cliente getClienteById(@ApiParam(name = "id",
            value = "Id do cliente a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable("id") Integer id) {
        return service.getClienteById(id);
    }

    @GetMapping("listar-clientes")
    @ApiOperation("Obter listagem dos clientes cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente(s) encontrado(s)"),
            @ApiResponse(code = 400, message = "Nenhum cliente encontrado na base de dados")
    })
    public List<Cliente> getClientes(Cliente cliente) {
        return service.getClientes(cliente);
    }

    @PostMapping("salvar")
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação")
    })
    public Cliente saveCliente(@RequestBody @Valid Cliente cliente) {
        return service.saveCliente(cliente);
    }

    @DeleteMapping("deletar/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Deleta um cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente deletado com sucesso"),
            @ApiResponse(code = 400, message = "Cliente não encontrado para o id informado")
    })
    public void deleteClienteById(@ApiParam(name = "id",
            value = "Id do cliente a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable("id") Integer id) {
        service.deleteClienteById(id);
    }

    @PutMapping("atualizar/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualiza um cliente")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Cliente não encontrado para o id informado")
    })
    public void updateCliente(@RequestBody @Valid Cliente cliente, @ApiParam(name = "id",
            value = "Id do cliente a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable("id") Integer id) {
        service.updateCliente(cliente, id);
    }
}
