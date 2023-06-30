package br.aranda.domain.repository;

import br.aranda.domain.entity.Cliente;
import br.aranda.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Set<Pedido> findByCliente(Cliente cliente);

    @Query("select p from Pedido p left join fetch p.itemsPedido where p.id = :id")
    Optional<Pedido> findByIdFetchItems(@Param("id") Integer id);
}
