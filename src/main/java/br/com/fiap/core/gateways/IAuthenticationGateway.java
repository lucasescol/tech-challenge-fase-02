package br.com.fiap.core.gateways;

public interface IAuthenticationGateway {
    String getUsuarioLogado();
    String getTipoUsuarioLogado();
    boolean isAdministrador();
    boolean isDonoRestaurante();
    boolean isCliente();
}
