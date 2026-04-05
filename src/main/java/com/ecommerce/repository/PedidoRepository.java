package com.ecommerce.repository;

import com.ecommerce.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"cliente", "enderecoEntrega"})
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"cliente", "enderecoEntrega"})
    Page<Pedido> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "enderecoEntrega", "itens", "itens.produto"})
    @Query("select p from Pedido p where p.id = :id")
    Optional<Pedido> findComDetalhesById(@Param("id") Long id);
}
