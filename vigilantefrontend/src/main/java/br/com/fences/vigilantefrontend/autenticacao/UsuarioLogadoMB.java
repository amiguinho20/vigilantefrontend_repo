package br.com.fences.vigilantefrontend.autenticacao;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.omnifaces.util.Faces;

import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilanteentidade.negocio.Usuario;
import br.com.fences.vigilantefrontend.config.Constantes;
import br.com.fences.vigilantefrontend.config.mb.NavegacaoMB;
import br.com.fences.vigilantefrontend.func.cadastrarusuario.CadastrarUsuarioBO;

@Named
@SessionScoped
public class UsuarioLogadoMB implements Serializable{

	private static final long serialVersionUID = 5513251990555251017L;
	
	@Inject
	private transient Logger logger;
	
	@Inject
	private CadastrarUsuarioBO cadastroUsuarioBO;
	
	@Inject
	private LogoutMB logoutMB;
	
	private Usuario usuario;

	private String latitude;
	private String longitude;
	
	@PostConstruct
	private void init()
	{
		String username = (String) SecurityUtils.getSubject().getSession().getAttribute(Constantes.USERNAME);
		if (Verificador.isValorado(username))
		{
			setUsuario(cadastroUsuarioBO.consultar(username));
		}
		else
		{
			logger.error("Erro na criação da sessão autenticada, não foi possível recuperar o username.");
			try {
				logoutMB.desautenticar();
			} catch (IOException e) {
				logger.error("Erro ao desautenticar: " + e.getMessage(), e);
			}
		}
		
	}

	/**
	 * Recuperar informacoes do usuario cadastradas no banco.
	 * Usar isso apos uma atualizacao.
	 */
	public void refresh()
	{
		init();
	}
	
	public String getPrimeiroNome(){
		return usuario.getNome().split("\\s")[0];
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
