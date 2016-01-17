package br.com.fences.vigilantefrontend.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.fences.vigilanteentidade.negocio.Alerta;
import br.com.fences.vigilantefrontend.func.cadastraralerta.CadastrarAlertaBO;

public class ListarAlertasGeralLazyDataModel extends LazyDataModel<Alerta> {

	private static final long serialVersionUID = 8313096364754460374L;
	
	private Logger logger =  Logger.getLogger(ListarAlertasGeralLazyDataModel.class);  

	private CadastrarAlertaBO cadastrarAlertaBO;

	private List<Alerta> alertas;
	
	/**
	 * o metodo getRowData eh chamado pelo primefaces duas vezes, esse cache visa evitar a segunda chamada.
	 */
	private Map<String, Alerta> mapCache = new HashMap<>();
	

	public ListarAlertasGeralLazyDataModel(CadastrarAlertaBO cadastrarAlertaBO) {
		this.alertas = new ArrayList<>();
		this.cadastrarAlertaBO = cadastrarAlertaBO;
	}

	/**
	 * Metodo necessario para o "cache" dos registros selecionados via
	 * rowSelectMode = checkbox
	 */
	@Override
	public Alerta getRowData(String rowKey) {
		Alerta alerta = null;
		if (!mapCache.containsKey(rowKey))
		{
			//--nao contem no cache, faz a chamada
			alerta = cadastrarAlertaBO.consultarPorId(rowKey);
			mapCache.put(rowKey, alerta);
		}
		else
		{
			//--usa o do cache e apaga-o.
			alerta = mapCache.remove(rowKey);
		}
		return alerta;
	}

	@Override
	public List<Alerta> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
	
		//filtro.setPrimeMapFiltro(filters);
		
		alertas = cadastrarAlertaBO.pesquisarLazy(null, first, pageSize);

		int count = cadastrarAlertaBO.contar(null);
		setRowCount(count);

		//filtro.setPrimeMapFiltro(null);
		
		return alertas;
	}

}
