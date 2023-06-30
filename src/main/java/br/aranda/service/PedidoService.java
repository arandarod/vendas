package br.aranda.service;

import br.aranda.domain.entity.Pedido;
import br.aranda.domain.enums.StatusPedido;
import br.aranda.dto.in.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
