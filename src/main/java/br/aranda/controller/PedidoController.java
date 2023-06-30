package br.aranda.controller;

import br.aranda.domain.entity.ItemPedido;
import br.aranda.domain.entity.Pedido;
import br.aranda.domain.enums.StatusPedido;
import br.aranda.dto.in.AtualizacaoStatusPedidoDTO;
import br.aranda.dto.out.InformacaoItemPedidoDTO;
import br.aranda.dto.out.InformacoesPedidoDTO;
import br.aranda.dto.in.PedidoDTO;
import br.aranda.service.PedidoService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Api("Pedidos Api")
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping("salvar")
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro de validação")
    })
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = pedidoService.salvar(dto);

        return pedido.getId();
    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhamento de um pedido")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedido encontrado"),
            @ApiResponse(code = 400, message = "Pedido não encontrado para o id informado")
    })
    public InformacoesPedidoDTO getById(@ApiParam(name = "id",
            value = "Id do pedido a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable Integer id) {
        return pedidoService.obterPedidoCompleto(id).map(pedido -> converter(pedido)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .items(converter(pedido.getItemsPedido()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itemsPedido) {
        if (CollectionUtils.isEmpty(itemsPedido)) {
            return Collections.emptyList();
        }

        return itemsPedido.stream().map(itemPedido ->
                InformacaoItemPedidoDTO.builder()
                        .descricaoProduto(itemPedido.getProduto().getDescricao())
                        .precoUnitario(itemPedido.getProduto().getPreco())
                        .quantidade(itemPedido.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }

    @PatchMapping("atualizarStatus/{id}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation("Atualiza um pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido atualizado com sucesso"),
            @ApiResponse(code = 400, message = "Pedido não encontrado para o id informado")
    })
    public void updateStatus(@ApiParam(name = "id",
            value = "Id do pedido a ser obtido. Não pode ser vazio.",
            example = "1",
            required = true) @PathVariable Integer id, @RequestBody AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        pedidoService.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
    }
}
