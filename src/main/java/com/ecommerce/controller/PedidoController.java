package com.ecommerce.controller;

import com.ecommerce.dto.AtualizarStatusPedidoDTO;
import com.ecommerce.dto.PedidoRequestDTO;
import com.ecommerce.dto.PedidoResponseDTO;
import com.ecommerce.mapper.PedidoMapper;
import com.ecommerce.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO criar(@RequestBody @Valid PedidoRequestDTO dto) {
        return pedidoMapper.toResponseDTO(pedidoService.criarPedido(dto));
    }

    @GetMapping
    public Page<PedidoResponseDTO> listar(@RequestParam(required = false) Long clienteId,
                                          Pageable pageable) {
        return pedidoService.listar(clienteId, pageable)
                .map(pedidoMapper::toResponseDTO);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return pedidoMapper.toResponseDTO(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    public PedidoResponseDTO atualizarStatus(@PathVariable Long id,
                                             @RequestBody @Valid AtualizarStatusPedidoDTO dto) {
        return pedidoMapper.toResponseDTO(pedidoService.atualizarStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
    }
}
