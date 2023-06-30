package br.aranda.service;

import br.aranda.domain.entity.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente getClienteById(Integer id);
    List<Cliente> getClientes(Cliente filtro);
    Cliente saveCliente(Cliente cliente);
    void deleteClienteById(Integer id);
    void updateCliente(Cliente cliente, Integer id);
}
