package br.com.fences.vigilantefrontend.func.cadastraralerta;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.com.fences.fencesutils.conversor.converter.ColecaoJsonAdapter;
import br.com.fences.fencesutils.rest.tratamentoerro.util.VerificarErro;
import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilanteentidade.negocio.Alerta;
import br.com.fences.vigilanteentidade.negocio.Endereco;
import br.com.fences.vigilantefrontend.autenticacao.UsuarioLogadoMB;
import br.com.fences.vigilantefrontend.config.AppConfig;

@RequestScoped
public class CadastrarAlertaBO {
	
	
	@Inject
	private transient Logger logger;
	
	@Inject
	private AppConfig appConfig;
		
	@Inject
	private VerificarErro verificarErro;
	
	@Inject
	private UsuarioLogadoMB usuarioLogadoMB;

	private Gson gson = new GsonBuilder()
			.registerTypeHierarchyAdapter(Collection.class, new ColecaoJsonAdapter())
			.create();
		
	private String host;
	private String port;
	
	@PostConstruct
	private void init()
	{
		host = appConfig.getServerBackendHost();
		port = appConfig.getServerBackendPort();	
	}

	public Endereco consultarEndereco(String latitude, String longitude)
	{
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"mapa/consultarEndereco/{latitude}/{longitude}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("latitude", latitude)
				.resolveTemplate("longitude", longitude)
				.request(MediaType.APPLICATION_JSON)
				.get();
		String json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}	
		Endereco endereco = gson.fromJson(json, Endereco.class);
		if (endereco == null || !Verificador.isValorado(endereco.getLogradouro()))
		{
			endereco = null;
		}
		return endereco;
	}	
	
	public void emitir(String emailUsuarioLogado, Alerta alerta)
	{
		String json = gson.toJson(alerta);
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"alerta/emitir/{email}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("email", emailUsuarioLogado)
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.json(json));
		json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}	
	}
	
	public void recepcionar(String emailUsuarioLogado, String idAlerta)
	{
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"alerta/recepcionar/{idAlerta}/{email}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("idAlerta", idAlerta)
				.resolveTemplate("email", emailUsuarioLogado)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(null));
		String json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}	
	}

	public List<Alerta> listarAlertasSemImagemDoUsuario()
	{
		Client client = ClientBuilder.newClient();
		String servico = "http://" + appConfig.getServerBackendHost() + ":"+ appConfig.getServerBackendPort() + "/vigilantebackend/rest/" + 
				"alerta/listarAlertasPorEmailUsuario/{id}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("id", usuarioLogadoMB.getUsuario().getId())
				.request(MediaType.APPLICATION_JSON)
				.get();
		String json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}
		
		Type collectionType = new TypeToken<List<Alerta>>(){}.getType();
		List<Alerta> alertas = (List<Alerta>) gson.fromJson(json, collectionType);
		return alertas;
	}
	
	public List<Alerta> listarAlertasSemImagemGeral()
	{
		Client client = ClientBuilder.newClient();
		String servico = "http://" + appConfig.getServerBackendHost() + ":"+ appConfig.getServerBackendPort() + "/vigilantebackend/rest/" + 
				"alerta/listarAlertasPorEmailUsuario/{id}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("id", usuarioLogadoMB.getUsuario().getId())
				.request(MediaType.APPLICATION_JSON)
				.get();
		String json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}
		
		Type collectionType = new TypeToken<List<Alerta>>(){}.getType();
		List<Alerta> alertas = (List<Alerta>) gson.fromJson(json, collectionType);
		return alertas;
	}
	
	public Alerta consultarPorId(String id)
	{
		Client client = ClientBuilder.newClient();
		String servico = "http://" + appConfig.getServerBackendHost() + ":"+ appConfig.getServerBackendPort() + "/vigilantebackend/rest/" + 
				"alerta/consultar/{id}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("id", id)
				.request(MediaType.APPLICATION_JSON)
				.get();
		String json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}
		
		Alerta alerta =  gson.fromJson(json, Alerta.class);
		return alerta;
	}

	public List<Alerta> pesquisarLazy(Object filtros, int primeiroRegistro, int registrosPorPagina)
	{
		List<Alerta> alertas = new ArrayList<>();
		
		host = appConfig.getServerBackendHost(); 
		port = appConfig.getServerBackendPort();
		
		//String json = gson.toJson(filtro.montarPesquisaMap(), Map.class);
		String json = "";
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"alerta/pesquisarLazy/{primeiroRegistro}/{registrosPorPagina}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("primeiroRegistro", primeiroRegistro)
				.resolveTemplate("registrosPorPagina", registrosPorPagina)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(json));
				//.post();
		json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}	
		Type collectionType = new TypeToken<List<Alerta>>(){}.getType();
		alertas = (List<Alerta>) gson.fromJson(json, collectionType);
	    return alertas;		
		
	}
	
	public int contar(final Object filtro)
	{
		
		host = appConfig.getServerBackendHost();
		port = appConfig.getServerBackendPort();
		
		//String json = gson.toJson(filtro.montarPesquisaMap(), Map.class);
		String json = "";
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"alerta/pesquisarLazyContar"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(json));
		json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}	
	    int quantidade = Integer.parseInt(json);
	    return quantidade;
	}	
	
	
	public int contarNaoRecepcionado(final Object filtro)
	{
		
		host = appConfig.getServerBackendHost();
		port = appConfig.getServerBackendPort();
		
		//String json = gson.toJson(filtro.montarPesquisaMap(), Map.class);
		String json = "";
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"alerta/contarNaoRecepcionado"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(json));
		json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}	
	    int quantidade = Integer.parseInt(json);
	    return quantidade;
	}	
	
}
