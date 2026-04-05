package com.ecommerce.controller;

// Importação dos DTOs utilizados para entrada e saída de dados da API
import com.ecommerce.dto.AtualizarStatusPedidoDTO;
import com.ecommerce.dto.PedidoRequestDTO;
import com.ecommerce.dto.PedidoResponseDTO;

// Importação do mapper responsável por converter entidade Pedido em DTO
import com.ecommerce.mapper.PedidoMapper;

// Importação do serviço onde ficam as regras de negócio
import com.ecommerce.service.PedidoService;

// Validação dos dados recebidos na requisição
import jakarta.validation.Valid;

// Importações para paginação (Spring Data)
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Importação de códigos de status HTTP
import org.springframework.http.HttpStatus;

// Importações para criação de endpoints REST
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável por gerenciar os endpoints relacionados aos pedidos.
 *
 * Esta classe faz parte da camada de Controller na arquitetura em 3 camadas.
 * Sua responsabilidade é receber as requisições HTTP, delegar para a camada
 * de serviço (Service) e retornar os dados ao cliente em formato DTO.
 *
 * Também controla operações importantes como:
 * - criação de pedidos
 * - listagem com paginação
 * - atualização de status
 * - cancelamento de pedidos
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    // Serviço que contém as regras de negócio de pedidos
    private final PedidoService pedidoService;

    // Mapper para converter entidade Pedido em PedidoResponseDTO
    private final PedidoMapper pedidoMapper;

    /**
     * Construtor com injeção de dependências.
     * O Spring injeta automaticamente as instâncias necessárias.
     */
    public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    /**
     * Endpoint responsável por criar um novo pedido.
     *
     * Método HTTP: POST
     * URL: /pedidos
     *
     * @param dto dados do pedido recebidos no corpo da requisição
     * @return PedidoResponseDTO com os dados do pedido criado
     *
     * O status HTTP retornado é 201 (Created).
     * O cálculo do total e validação de estoque são feitos na camada de serviço.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO criar(@RequestBody @Valid PedidoRequestDTO dto) {
        return pedidoMapper.toResponseDTO(pedidoService.criarPedido(dto));
    }

    /**
     * Endpoint responsável por listar pedidos com suporte a paginação e filtro.
     *
     * Método HTTP: GET
     * URL: /pedidos
     *
     * @param clienteId (opcional) filtra pedidos por cliente
     * @param pageable informações de paginação (page, size, sort)
     * @return página de pedidos convertidos para DTO
     *
     * Exemplo:
     * /pedidos?page=0&size=10
     * /pedidos?clienteId=1
     */
    @GetMapping
    public Page<PedidoResponseDTO> listar(@RequestParam(required = false) Long clienteId,
                                          Pageable pageable) {
        return pedidoService.listar(clienteId, pageable)
                .map(pedidoMapper::toResponseDTO);
    }

    /**
     * Endpoint responsável por buscar um pedido pelo ID.
     *
     * Método HTTP: GET
     * URL: /pedidos/{id}
     *
     * @param id identificador do pedido
     * @return PedidoResponseDTO correspondente
     */
    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return pedidoMapper.toResponseDTO(pedidoService.buscarPorId(id));
    }

    /**
     * Endpoint responsável por atualizar o status de um pedido.
     *
     * Método HTTP: PATCH
     * URL: /pedidos/{id}/status
     *
     * @param id identificador do pedido
     * @param dto objeto contendo o novo status
     * @return PedidoResponseDTO com status atualizado
     *
     * Regras aplicadas na camada de serviço:
     * - Não permite pular etapas
     * - Não permite voltar status
     * - Não permite alterar pedido cancelado
     */
    @PatchMapping("/{id}/status")
    public PedidoResponseDTO atualizarStatus(@PathVariable Long id,
                                             @RequestBody @Valid AtualizarStatusPedidoDTO dto) {
        return pedidoMapper.toResponseDTO(pedidoService.atualizarStatus(id, dto));
    }

    /**
     * Endpoint responsável por cancelar um pedido.
     *
     * Método HTTP: DELETE
     * URL: /pedidos/{id}
     *
     * @param id identificador do pedido
     *
     * Regra de negócio:
     * - Apenas pedidos com status CRIADO podem ser cancelados
     * - O estoque dos produtos é devolvido
     *
     * Retorna status HTTP 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
    }
}
