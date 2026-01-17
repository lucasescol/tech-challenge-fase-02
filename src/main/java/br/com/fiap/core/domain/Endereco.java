package br.com.fiap.core.domain;

import br.com.fiap.core.exceptions.EnderecoInvalidoException;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode
public class Endereco {
    
    private static final List<String> UFS_VALIDAS = Arrays.asList(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
        "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
        "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );
    
    private final String logradouro;
    private final String numero;
    private final String complemento;
    private final String bairro;
    private final String cidade;
    private final String estado;
    private final String cep;
    
    public Endereco(String logradouro, String numero, String complemento, 
                    String bairro, String cidade, String estado, String cep) {
        
        if (logradouro == null || logradouro.trim().isEmpty()) {
            throw new EnderecoInvalidoException("Logradouro não pode ser vazio");
        }
        if (numero == null || numero.trim().isEmpty()) {
            throw new EnderecoInvalidoException("Número não pode ser vazio");
        }
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new EnderecoInvalidoException("Cidade não pode ser vazia");
        }
        if (estado == null || estado.trim().isEmpty()) {
            throw new EnderecoInvalidoException("Estado não pode ser vazio");
        }
        
        String estadoNormalizado = estado.trim().toUpperCase();
        if (!UFS_VALIDAS.contains(estadoNormalizado)) {
            throw new EnderecoInvalidoException("Estado inválido: " + estado + ". Informe uma UF válida (ex: SP, RJ, MG)");
        }
        
        if (cep == null || cep.trim().isEmpty()) {
            throw new EnderecoInvalidoException("CEP não pode ser vazio");
        }
        
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        if (cepLimpo.length() != 8) {
            throw new EnderecoInvalidoException("CEP inválido: " + cep);
        }
        
        this.logradouro = logradouro.trim();
        this.numero = numero.trim();
        this.complemento = complemento != null ? complemento.trim() : "";
        this.bairro = bairro != null ? bairro.trim() : "";
        this.cidade = cidade.trim();
        this.estado = estadoNormalizado;
        this.cep = cepLimpo;
    }
    
    public String getLogradouro() {
        return logradouro;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public String getComplemento() {
        return complemento;
    }
    
    public String getBairro() {
        return bairro;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public String getCep() {
        return formatarCep(this.cep);
    }
    
    public String getCepLimpo() {
        return this.cep;
    }
    
    private String formatarCep(String cep) {
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }
    
    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(logradouro).append(", ").append(numero);
        if (!complemento.isEmpty()) {
            sb.append(" - ").append(complemento);
        }
        if (!bairro.isEmpty()) {
            sb.append(", ").append(bairro);
        }
        sb.append(" - ").append(cidade).append("/").append(estado);
        sb.append(" - CEP: ").append(getCep());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return getEnderecoCompleto();
    }
}
