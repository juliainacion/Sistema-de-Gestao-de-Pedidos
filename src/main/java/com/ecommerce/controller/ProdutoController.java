package com.ecommerce.controller;

import com.ecommerce.dto.ProdutoRequestDTO;
import com.ecommerce.dto.ProdutoResponseDTO;
import com.ecommerce.mapper.ProdutoMapper;
import com.ecommerce.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ProdutoMapper produtoMapper;

    public ProdutoController(ProdutoService produtoService, ProdutoMapper produtoMapper) {
        this.produtoService = produtoService;
        this.produtoMapper = produtoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO criar(@RequestBody @Valid ProdutoRequestDTO dto) {
        return produtoMapper.toResponseDTO(produtoService.criar(dto));
    }

    @GetMapping
    public Page<ProdutoResponseDTO> listar(Pageable pageable) {
        return produtoService.listar(pageable).map(produtoMapper::toResponseDTO);
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarPorId(@PathVariable Long id) {
        return produtoMapper.toResponseDTO(produtoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO dto) {
        return produtoMapper.toResponseDTO(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }
}
