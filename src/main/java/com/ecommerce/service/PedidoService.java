package com.ecommerce.service;

import com.ecommerce.dto.AtualizarStatusPedidoDTO;
import com.ecommerce.dto.ItemPedidoRequestDTO;
import com.ecommerce.dto.PedidoRequestDTO;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.UnauthorizedAddressException;
import com.ecommerce.model.*;
import com.ecommerce.repository.ClienteRepository;
import com.ecommerce.repository.EnderecoRepository;
import com.ecommerce.repository.PedidoRepository;
import com.ecommerce.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         EnderecoRepository enderecoRepository,
                         ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Pedido criarPedido(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        Endereco endereco = enderecoRepository.findById(dto.enderecoId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado."));

        if (!endereco.getCliente().getId().equals(cliente.getId())) {
            throw new UnauthorizedAddressException("O endereço informado não pertence ao cliente do pedido.");
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .enderecoEntrega(endereco)
                .data(LocalDateTime.now())
                .status(StatusPedido.CRIADO)
                .total(BigDecimal.ZERO)
                .itens(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequestDTO itemDTO : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDTO.produtoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Produto não encontrado: " + itemDTO.produtoId()));

            if (produto.getEstoque() < itemDTO.quantidade()) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - itemDTO.quantidade());

            ItemPedido item = ItemPedido.builder()
                    .pedido(pedido)
                    .produto(produto)
                    .quantidade(itemDTO.quantidade())
                    .precoUnitario(produto.getPreco())
                    .build();

            pedido.getItens().add(item);
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.quantidade())));
        }

        pedido.setTotal(total);
        return pedidoRepository.save(pedido);
    }

    public Page<Pedido> listar(Long clienteId, Pageable pageable) {
        if (clienteId != null) {
            return pedidoRepository.findByClienteId(clienteId, pageable);
        }
        return pedidoRepository.findAll(pageable);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findComDetalhesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado."));
    }

    @Transactional
    public Pedido atualizarStatus(Long id, AtualizarStatusPedidoDTO dto) {
        Pedido pedido = buscarPorId(id);
        StatusPedido atual = pedido.getStatus();
        StatusPedido novo = dto.novoStatus();

        if (atual == StatusPedido.CANCELADO) {
            throw new BusinessException("Pedido cancelado não pode ter status alterado.");
        }

        boolean transicaoValida =
                (atual == StatusPedido.CRIADO && novo == StatusPedido.PAGO) ||
                (atual == StatusPedido.PAGO && novo == StatusPedido.ENVIADO);

        if (!transicaoValida) {
            throw new BusinessException("Fluxo de status inválido. Permitido: CRIADO -> PAGO -> ENVIADO.");
        }

        pedido.setStatus(novo);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new BusinessException("Apenas pedidos com status CRIADO podem ser cancelados.");
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}
