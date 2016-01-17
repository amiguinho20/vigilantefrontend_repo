package br.com.fences.vigilantefrontend.func.listaralertas;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import br.com.fences.vigilanteentidade.negocio.Alerta;
import br.com.fences.vigilantefrontend.autenticacao.UsuarioLogadoMB;
import br.com.fences.vigilantefrontend.func.cadastraralerta.CadastrarAlertaBO;
import br.com.fences.vigilantefrontend.util.ImagemMBHelper;
import br.com.fences.vigilantefrontend.util.ListarAlertasGeralLazyDataModel;

@Named
@ViewScoped
public class ListarAlertasGeralMB extends ImagemMBHelper implements Serializable{

	private static final long serialVersionUID = 3092974856082089359L;
	
	@Inject
	private CadastrarAlertaBO cadastrarAlertaBO;
	
	@Inject
	private UsuarioLogadoMB usuarioLogadoMB;
	
	private LazyDataModel<Alerta> alertasLazy;
	
	private int quantidadeAlertaNaoRecepcionado;
	
	private Alerta alertaSelecionado;
	
	
	@PostConstruct
	private void init()
	{
		pesquisar();
	}
	
	public void pesquisar(){
		setAlertasLazy(new ListarAlertasGeralLazyDataModel(cadastrarAlertaBO));
		setQuantidadeAlertaNaoRecepcionado(cadastrarAlertaBO.contarNaoRecepcionado(null));
	}
	
	public void recepcionarAlerta()
	{
		String idAlerta = alertaSelecionado.getId();
		String emailUsuarioLogado = usuarioLogadoMB.getUsuario().getEmail();
		try
		{
			cadastrarAlertaBO.recepcionar(emailUsuarioLogado, idAlerta);
			Messages.addGlobalInfo("Alerta recepcionado com sucesso.");
			desselecionarAlerta();
			setQuantidadeAlertaNaoRecepcionado(cadastrarAlertaBO.contarNaoRecepcionado(null));
		}
		catch (Exception e)
		{
			Messages.addGlobalError("Erro na recepção do alerta: " + e.getMessage());
		}
	}
	
	public void onRowSelect(SelectEvent event) {
		Alerta alertaCompleto = (Alerta) event.getObject();
		setAlertaSelecionado(alertaCompleto);
    }

	public void desselecionarAlerta()
	{
		setAlertaSelecionado(null);
	}

	public Alerta getAlertaSelecionado() {
		return alertaSelecionado;
	}

	public void setAlertaSelecionado(Alerta alertaSelecionado) {
		this.alertaSelecionado = alertaSelecionado;
	}

	public LazyDataModel<Alerta> getAlertasLazy() {
		return alertasLazy;
	}

	public void setAlertasLazy(LazyDataModel<Alerta> alertasLazy) {
		this.alertasLazy = alertasLazy;
	}

	public int getQuantidadeAlertaNaoRecepcionado() {
		return quantidadeAlertaNaoRecepcionado;
	}

	public void setQuantidadeAlertaNaoRecepcionado(int quantidadeAlertaNaoRecepcionado) {
		this.quantidadeAlertaNaoRecepcionado = quantidadeAlertaNaoRecepcionado;
	}

}
