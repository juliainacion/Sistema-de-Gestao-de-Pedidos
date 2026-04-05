package com.ecommerce.service;

import com.ecommerce.dto.AtualizarStatusPedidoDTO;
import com.ecommerce.exception.BusinessException;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.StatusPedido;
import com.ecommerce.repository.ClienteRepository;
import com.ecommerce.repository.EnderecoRepository;
import com.ecommerce.repository.PedidoRepository;
import com.ecommerce.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = Pedido.builder()
                .id(1L)
                .data(LocalDateTime.now())
                .status(StatusPedido.CRIADO)
                .total(BigDecimal.TEN)
                .itens(new ArrayList<>())
                .build();
    }

    @Test
    void deveAtualizarStatusDeCriadoParaPago() {
        when(pedidoRepository.findComDetalhesById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido atualizado = pedidoService.atualizarStatus(1L, new AtualizarStatusPedidoDTO(StatusPedido.PAGO));

        assertEquals(StatusPedido.PAGO, atualizado.getStatus());
    }

    @Test
    void naoDevePermitirPularEtapasDoFluxo() {
        when(pedidoRepository.findComDetalhesById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(BusinessException.class,
                () -> pedidoService.atualizarStatus(1L, new AtualizarStatusPedidoDTO(StatusPedido.ENVIADO)));
    }
}
