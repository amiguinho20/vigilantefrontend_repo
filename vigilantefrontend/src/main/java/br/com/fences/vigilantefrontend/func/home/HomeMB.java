package br.com.fences.vigilantefrontend.func.home;

import java.io.Serializable;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilantefrontend.autenticacao.UsuarioLogadoMB;
import br.com.fences.vigilantefrontend.config.mb.NavegacaoMB;

@Model
public class HomeMB implements Serializable{

	private static final long serialVersionUID = -2902891549389774519L;

	@Inject
	private NavegacaoMB navegacaoMB;
	
	@Inject
	private UsuarioLogadoMB usuarioLogadoMB;
	
	public String funcionalidadeNaoImplementada()
	{
		Messages.addGlobalWarn("Funcionalidade nao implementada");
		return "";
	}
	
	public String emitirAlerta()
	{
		String navegacao = "";
		if (usuarioLogadoMB.getUsuario().getVeiculo() != null && Verificador.isValorado(usuarioLogadoMB.getUsuario().getVeiculo().getPlaca()))
		{
			navegacao = navegacaoMB.getCadastrarAlerta();
		}
		else
		{
			Messages.addGlobalError("É necessário possuir o veículo cadastrado para a emitir o alerta.");
		}
		return navegacao;
	}
	
}
