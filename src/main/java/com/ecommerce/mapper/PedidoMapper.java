package com.ecommerce.mapper;

import com.ecommerce.dto.EnderecoResponseDTO;
import com.ecommerce.dto.ItemPedidoResponseDTO;
import com.ecommerce.dto.PedidoResponseDTO;
import com.ecommerce.model.ItemPedido;
import com.ecommerce.model.Pedido;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<ItemPedidoResponseDTO> itens = pedido.getItens()
                .stream()
                .map(this::toItemResponse)
                .toList();

        EnderecoResponseDTO endereco = new EnderecoResponseDTO(
                pedido.getEnderecoEntrega().getId(),
                pedido.getEnderecoEntrega().getRua(),
                pedido.getEnderecoEntrega().getCidade(),
                pedido.getEnderecoEntrega().getCep()
        );

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getData(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                endereco,
                itens
        );
    }

    private ItemPedidoResponseDTO toItemResponse(ItemPedido item) {
        BigDecimal subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
        return new ItemPedidoResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                subtotal
        );
    }
}
