package com.ecommerce.service;

import com.ecommerce.dto.ProdutoRequestDTO;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Produto;
import com.ecommerce.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto criar(ProdutoRequestDTO dto) {
        Produto produto = Produto.builder()
                .nome(dto.nome())
                .preco(dto.preco())
                .estoque(dto.estoque())
                .build();

        return produtoRepository.save(produto);
    }

    public Page<Produto> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = buscarPorId(id);
        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        return produtoRepository.save(produto);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}
