package br.com.fiap.infra.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.core.domain.Restaurante;
import br.com.fiap.core.usecases.restaurante.CadastrarRestauranteUseCase;
import br.com.fiap.infra.dto.NovoRestauranteDTO;
import br.com.fiap.infra.dto.RestauranteResponseDTO;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {
    
    private final CadastrarRestauranteUseCase cadastrarRestauranteUseCase;
    
    public RestauranteController(CadastrarRestauranteUseCase cadastrarRestauranteUseCase) {
        this.cadastrarRestauranteUseCase = cadastrarRestauranteUseCase;
    }
    
    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@RequestBody NovoRestauranteDTO dto) {
        var input = new CadastrarRestauranteUseCase.InputModel(
            dto.nome(),
            dto.logradouro(),
            dto.numero(),
            dto.complemento(),
            dto.bairro(),
            dto.cidade(),
            dto.estado(),
            dto.cep(),
            dto.tipoCozinha(),
            dto.horarioFuncionamento()
        );

        Restaurante restauranteSalvo = cadastrarRestauranteUseCase.execute(input);
        
        RestauranteResponseDTO response = new RestauranteResponseDTO(
            restauranteSalvo.getId(),
            restauranteSalvo.getNome(),
            restauranteSalvo.getEndereco().getEnderecoCompleto(),
            restauranteSalvo.getTipoCozinha().getValor(),
            restauranteSalvo.getHorarioFuncionamento().getValor()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    
}
