package br.com.fences.vigilantefrontend.func.cadastraralerta;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.New;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilanteentidade.negocio.Alerta;
import br.com.fences.vigilanteentidade.negocio.Endereco;
import br.com.fences.vigilantefrontend.autenticacao.UsuarioLogadoMB;
import br.com.fences.vigilantefrontend.config.mb.NavegacaoMB;
import br.com.fences.vigilantefrontend.util.ImagemMBHelper;

@Named
@ViewScoped
public class CadastrarAlertaMB extends ImagemMBHelper implements Serializable{

	private static final long serialVersionUID = -6201504759855081290L;
	
	@Inject
	private transient Logger logger;
	
	@Inject
	private UsuarioLogadoMB usuarioLogadoMB;
	
	@Inject
	private CadastrarAlertaBO cadastrarAlertaBO;
	
	@Inject
	private NavegacaoMB navegacaoMB;
	
	@Inject @New
	private Alerta alerta;
	
	@PostConstruct
	private void init()
	{  
		if (alerta.getEndereco() == null)
		{
			alerta.setEndereco(new Endereco());
		}
		logger.info("lat[" + usuarioLogadoMB.getLatitude() + "] lng[" + usuarioLogadoMB.getLongitude() + "]");
		if (Verificador.isValorado(usuarioLogadoMB.getLatitude(), usuarioLogadoMB.getLongitude()))
		{
			Endereco endereco = cadastrarAlertaBO.consultarEndereco(usuarioLogadoMB.getLatitude(), usuarioLogadoMB.getLongitude());
			if (endereco != null)
			{
				alerta.setEndereco(endereco);
			}
		}
	}

	public String emitirAlerta()
	{
		String navegacao = "";
		try
		{
			cadastrarAlertaBO.emitir(usuarioLogadoMB.getUsuario().getEmail(), alerta);
			Messages.addGlobalInfo("Alerta emitido com sucesso. ");
			
			//-- Isso eh fundamental para manter as mensagens em redirecionamentos.
			//-- Tambem eh necessario que o p:messages nao tenha o autoUpdate.
			Faces.getFlash().setKeepMessages(true);
			navegacao = navegacaoMB.getHome();
		}
		catch (Exception e)
		{
			Messages.addGlobalError("Erro na emissão do alerta: " + e.getMessage());
		}
		return navegacao;
	}
	
	public Alerta getAlerta() {
		return alerta;
	}

	public void setAlerta(Alerta alerta) {
		this.alerta = alerta;
	}


}
