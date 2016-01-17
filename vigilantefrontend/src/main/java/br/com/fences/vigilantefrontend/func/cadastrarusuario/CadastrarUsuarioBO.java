package br.com.fences.vigilantefrontend.func.cadastrarusuario;

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

import br.com.fences.fencesutils.rest.tratamentoerro.util.VerificarErro;
import br.com.fences.vigilanteentidade.negocio.Usuario;
import br.com.fences.vigilantefrontend.autenticacao.UsuarioLogadoMB;
import br.com.fences.vigilantefrontend.config.AppConfig;

@RequestScoped
public class CadastrarUsuarioBO {
	
	
	@Inject
	private transient Logger logger;
	
	@Inject
	private AppConfig appConfig;
	
	@Inject
	private UsuarioLogadoMB usuarioLogadoMB;
	
	@Inject
	private VerificarErro verificarErro;
	
	private Gson gson = new GsonBuilder().create();
	
	private String host;
	private String port;
	
	@PostConstruct
	private void init()
	{
		host = appConfig.getServerBackendHost();
		port = appConfig.getServerBackendPort();	
	}

	public void adicionar(Usuario usuario)
	{
		
		String json = gson.toJson(usuario);
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"usuario/adicionar"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
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
	
	public void atualizar(Usuario usuario)
	{
		String json = gson.toJson(usuario);
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"usuario/atualizar"; 
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
		
		//---- ATUALIZAR INFORMACOES DO USUARIO NA SESSAO
		usuarioLogadoMB.refresh();
		
	}	

	public boolean existeEmail(Usuario usuario)
	{
		
		String json = gson.toJson(usuario);
		
		Client client = ClientBuilder.newClient();
		String servico = "http://" + host + ":"+ port + "/vigilantebackend/rest/" + 
				"usuario/existeEmail/{email}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("email", usuario.getEmail())
				.request(MediaType.APPLICATION_JSON)
				.get();
		json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}	
		return Boolean.parseBoolean(json);
	}	
	
	public Usuario consultar(String email)
	{
		Client client = ClientBuilder.newClient();
		String servico = "http://" + appConfig.getServerBackendHost() + ":"+ appConfig.getServerBackendPort() + "/vigilantebackend/rest/" + 
				"usuario/consultar/{email}"; 
		WebTarget webTarget = client
				.target(servico);
		Response response = webTarget
				.resolveTemplate("email", email)
				.request(MediaType.APPLICATION_JSON)
				.get();
		String json = response.readEntity(String.class);
		if (verificarErro.contemErro(response, json))
		{
			String msg = verificarErro.criarMensagem(response, json, servico);
			logger.error(msg);
			throw new RuntimeException(msg);
		}
		Usuario usuario = gson.fromJson(json, Usuario.class);
		return usuario;
	}
	

}
