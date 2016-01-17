package br.com.fences.vigilantefrontend.func.cadastrarveiculo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.fences.fencesutils.conversor.converter.ColecaoJsonAdapter;
import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilanteentidade.negocio.Veiculo;
import br.com.fences.vigilanteentidade.negocio.VeiculoImagem;
import br.com.fences.vigilanteentidade.usuario.constante.CorVeiculo;
import br.com.fences.vigilanteentidade.usuario.constante.TipoVeiculo;
import br.com.fences.vigilantefrontend.autenticacao.UsuarioLogadoMB;
import br.com.fences.vigilantefrontend.config.mb.NavegacaoMB;
import br.com.fences.vigilantefrontend.func.cadastrarusuario.CadastrarUsuarioBO;
import br.com.fences.vigilantefrontend.util.ImagemHelper;
import br.com.fences.vigilantefrontend.util.ImagemMBHelper;

@Named
@ViewScoped
public class CadastrarVeiculoMB extends ImagemMBHelper implements Serializable {

	private static final long serialVersionUID = -8243734493895592110L;
	
	@Inject
	private transient Logger logger;
	
	@Inject
	private UsuarioLogadoMB usuarioLogadoMB;
	
	@Inject
	private CadastrarUsuarioBO cadastrarUsuarioBO;
	
	@Inject
	private NavegacaoMB navegacaoMB;
	
	private Veiculo veiculo;
	
//	private Veiculo veiculoAnterior; //-- copia de recuperacao - necessario caso houver manipulacao de imagem sem salvamento.
	
	@PostConstruct
	private void init()
	{
		setVeiculo(usuarioLogadoMB.getUsuario().getVeiculo());
//		veiculoAnterior = clonarVeiculo(getVeiculo());
	}
	
	public TipoVeiculo[] getListaDeTipos()
	{
		return TipoVeiculo.values();
	}
	
	public CorVeiculo[] getListaDeCores()
	{
		return CorVeiculo.values();
	}
	
	public String salvar()
	{
		String navegacao = "";
		if (!Verificador.isValorado(getVeiculo().getImagens()))
		{
			Messages.create("Pelo menos uma imagem é necessária.").error().add("formCadastro:upload");
		}
		else
		{
			try
			{
				cadastrarUsuarioBO.atualizar(usuarioLogadoMB.getUsuario());
				navegacao = navegacaoMB.getHome();
			}
			catch (Exception e)
			{
				Messages.addGlobalError("Erro na atualização do usuário. Erro:[" + e.getMessage() + "]");
			}
		}
		return navegacao;
	}
	
//	public void cancelar()
//	{
//		//-- devolve o veiculo anterior
//		setVeiculo(veiculoAnterior);
//	}

	public void upload(FileUploadEvent event) {
		
		byte[] arqByte = event.getFile().getContents();
		String nomeArquivo = event.getFile().getFileName();
		String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf("."));
		//String arqBase64 = Base64.encodeToString(arqByte, true);
		if (!extensao.equalsIgnoreCase("png"))
		{
			arqByte = ImagemHelper.converterParaPng(arqByte, extensao);
		}
		arqByte = ImagemHelper.calcularRedimencionamento(arqByte, 600, 800);
		
		VeiculoImagem veiculoImagem = new VeiculoImagem();
		veiculoImagem.setUltimaAtualizacao(new Date());
		veiculoImagem.setImagemByte(arqByte);
		getVeiculo().getImagens().add(veiculoImagem);

	}
	
	public void excluirImagem(Date ultimaAtualizacao)
	{
		for (int indice = 0; indice < veiculo.getImagens().size(); indice++)
		{
			VeiculoImagem veiculoImagem = veiculo.getImagens().get(indice);
			if (veiculoImagem.getUltimaAtualizacao().equals(ultimaAtualizacao))
			{
				veiculo.getImagens().remove(indice);
				break;
			}
		}
	}
	
	/**
	 * Criar copia de valores
	 */
	private Veiculo clonarVeiculo(Veiculo veiculo)
	{
		Gson gson = new GsonBuilder()
				.registerTypeHierarchyAdapter(Collection.class, new ColecaoJsonAdapter())
				.create();
		
		String json = gson.toJson(veiculo);
		Veiculo veiculoClonado = gson.fromJson(json, Veiculo.class);
		return veiculoClonado;
	}
	

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}


	

	
}
