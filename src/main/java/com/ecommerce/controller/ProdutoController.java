package com.ecommerce.controller;

// Importação dos DTOs utilizados para entrada e saída de dados
import com.ecommerce.dto.ProdutoRequestDTO;
import com.ecommerce.dto.ProdutoResponseDTO;

// Mapper responsável por converter entidade Produto em DTO
import com.ecommerce.mapper.ProdutoMapper;

// Serviço onde ficam as regras de negócio relacionadas ao produto
import com.ecommerce.service.ProdutoService;

// Validação dos dados recebidos na requisição
import jakarta.validation.Valid;

// Importações para paginação
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Importação de códigos de status HTTP
import org.springframework.http.HttpStatus;

// Importações para criação de endpoints REST
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável por gerenciar os endpoints relacionados aos produtos.
 *
 * Faz parte da camada de Controller na arquitetura em 3 camadas.
 * Sua função é receber requisições HTTP, delegar para a camada de serviço
 * e retornar respostas no formato DTO.
 *
 * Este controller implementa o CRUD completo de produtos,
 * além de suportar paginação na listagem.
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    // Serviço responsável pelas regras de negócio de produto
    private final ProdutoService produtoService;

    // Mapper para converter entidade Produto em ProdutoResponseDTO
    private final ProdutoMapper produtoMapper;

    /**
     * Construtor com injeção de dependências.
     * O Spring injeta automaticamente as instâncias necessárias.
     */
    public ProdutoController(ProdutoService produtoService, ProdutoMapper produtoMapper) {
        this.produtoService = produtoService;
        this.produtoMapper = produtoMapper;
    }

    /**
     * Endpoint responsável por criar um novo produto.
     *
     * Método HTTP: POST
     * URL: /produtos
     *
     * @param dto dados do produto recebidos no corpo da requisição
     * @return ProdutoResponseDTO com os dados do produto criado
     *
     * A anotação @Valid garante a validação dos dados.
     * Retorna status HTTP 201 (Created).
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO criar(@RequestBody @Valid ProdutoRequestDTO dto) {
        return produtoMapper.toResponseDTO(produtoService.criar(dto));
    }

    /**
     * Endpoint responsável por listar produtos com paginação.
     *
     * Método HTTP: GET
     * URL: /produtos
     *
     * @param pageable objeto que contém informações de paginação (page, size, sort)
     * @return página de produtos convertidos para DTO
     *
     * Exemplo:
     * /produtos?page=0&size=10
     */
    @GetMapping
    public Page<ProdutoResponseDTO> listar(Pageable pageable) {
        return produtoService.listar(pageable)
                .map(produtoMapper::toResponseDTO);
    }

    /**
     * Endpoint responsável por buscar um produto pelo ID.
     *
     * Método HTTP: GET
     * URL: /produtos/{id}
     *
     * @param id identificador do produto
     * @return ProdutoResponseDTO correspondente
     */
    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarPorId(@PathVariable Long id) {
        return produtoMapper.toResponseDTO(produtoService.buscarPorId(id));
    }

    /**
     * Endpoint responsável por atualizar os dados de um produto.
     *
     * Método HTTP: PUT
     * URL: /produtos/{id}
     *
     * @param id identificador do produto
     * @param dto novos dados do produto
     * @return ProdutoResponseDTO com os dados atualizados
     */
    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizar(@PathVariable Long id,
                                        @RequestBody @Valid ProdutoRequestDTO dto) {
        return produtoMapper.toResponseDTO(produtoService.atualizar(id, dto));
    }

    /**
     * Endpoint responsável por remover um produto do sistema.
     *
     * Método HTTP: DELETE
     * URL: /produtos/{id}
     *
     * @param id identificador do produto
     *
     * Retorna status HTTP 204 (No Content), indicando sucesso sem retorno de conteúdo.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }
}
