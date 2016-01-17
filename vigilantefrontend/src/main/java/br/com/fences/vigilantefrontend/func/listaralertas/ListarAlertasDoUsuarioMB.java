package br.com.fences.vigilantefrontend.func.listaralertas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.fences.vigilanteentidade.negocio.Alerta;
import br.com.fences.vigilantefrontend.config.mb.NavegacaoMB;
import br.com.fences.vigilantefrontend.func.cadastraralerta.CadastrarAlertaBO;
import br.com.fences.vigilantefrontend.util.ImagemMBHelper;

@Named
@ViewScoped
public class ListarAlertasDoUsuarioMB extends ImagemMBHelper implements Serializable{

	private static final long serialVersionUID = 3092974856082089359L;
	
	@Inject
	private CadastrarAlertaBO cadastrarAlertaBO;
	
	@Inject
	private NavegacaoMB navegacaoMB;
	
	private List<Alerta> alertas;
	
	private Alerta alertaSelecionado;
	
	private Map<String, Alerta> alertaCache = new HashMap<>();
	
	@PostConstruct
	private void init()
	{
		setAlertas(cadastrarAlertaBO.listarAlertasSemImagemDoUsuario());
	}
	
	public void onRowSelect(SelectEvent event) {
		//Alerta alerta = (Alerta) event.getObject();
		
		Alerta alertaCompleto = null;
		String id = getAlertaSelecionado().getId();
		
		if (alertaCache.containsKey(id))
		{
			alertaCompleto = alertaCache.get(id);
		}
		else
		{
			alertaCompleto = cadastrarAlertaBO.consultarPorId(id);
			alertaCache.put(id, alertaCompleto);
		}
		setAlertaSelecionado(alertaCompleto);

		//		try {
//			Faces.redirect(navegacaoMB.getHome() + "?faces-redirect=true");
//		} catch (IOException e) {
//			Messages.addGlobalError("Erro no redirect: " + e.getMessage());
//		}
    }

	
	public void desselecionarAlerta()
	{
		setAlertaSelecionado(null);
	}
	
	public List<Alerta> getAlertas() {
		return alertas;
	}

	public void setAlertas(List<Alerta> alertas) {
		this.alertas = alertas;
	}

	public Alerta getAlertaSelecionado() {
		return alertaSelecionado;
	}

	public void setAlertaSelecionado(Alerta alertaSelecionado) {
		this.alertaSelecionado = alertaSelecionado;
	}

}
