package br.aranda.service.impl;

import br.aranda.domain.entity.Cliente;
import br.aranda.domain.entity.ItemPedido;
import br.aranda.domain.entity.Pedido;
import br.aranda.domain.entity.Produto;
import br.aranda.domain.enums.StatusPedido;
import br.aranda.domain.repository.ClienteRepository;
import br.aranda.domain.repository.ItemPedidoRepository;
import br.aranda.domain.repository.PedidoRepository;
import br.aranda.domain.repository.ProdutoRepository;
import br.aranda.dto.in.ItemPedidoDTO;
import br.aranda.dto.in.PedidoDTO;
import br.aranda.exception.PedidoNaoEncontradoException;
import br.aranda.exception.RegraNegocioException;
import br.aranda.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() -> new RegraNegocioException("Código de cliente inválido!"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemsPedido);

        pedido.setItemsPedido(itemsPedido);

        return pedido;
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if (items.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
        }

        return items.stream().map(itemPedidoDTO -> {
            Integer idProduto = itemPedidoDTO.getProduto();
            Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(itemPedidoDTO.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);

            return itemPedido;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);

            return pedidoRepository.save(pedido);
        }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }
}
