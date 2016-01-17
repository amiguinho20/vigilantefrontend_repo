package br.com.fences.vigilantefrontend.func.cadastrarusuario;

import java.io.Serializable;

import javax.enterprise.inject.New;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.com.fences.fencesutils.conversor.ExcecaoParaMessageHtml;
import br.com.fences.fencesutils.verificador.VerificadorDocumento;
import br.com.fences.vigilanteentidade.negocio.Usuario;
import br.com.fences.vigilantefrontend.config.mb.NavegacaoMB;

@Named
@ViewScoped
public class CadastrarUsuarioMB implements Serializable{

	private static final long serialVersionUID = 7020066849012425486L;
	
	@Inject
	private CadastrarUsuarioBO cadastrarUsuarioBO;
	
	@Inject @New
	private Usuario usuario;
	
	@Inject
	private NavegacaoMB navegacaoMB;
	
	private String emailConferencia;
	

	public String cadastrarSe()
	{
		
		String redirecionamento = "";
		if (!usuario.getEmail().equals(emailConferencia))
		{
			 Messages.create("O email deve coincidir com a email de conferência.").error().add("formCadastro:email1");
		}
		else if (!VerificadorDocumento.isCPF(usuario.getCpf()))
		{
			 Messages.create("O CPF informado é inválido, pois não coincide com o dígito verificador.").error().add("formCadastro:cpf");
		}
		else
		{
			try
			{
				boolean existeEmail = cadastrarUsuarioBO.existeEmail(usuario);
				if (existeEmail)
				{
					Messages.create("O email informado já possui cadastro prévio.").error().add("formCadastro:email1");
				}
				else
				{
					//-- encripta a senha do usuario, a autenticacao da senha eh configurada no shiro.ini
					usuario.setPassword(new Sha256Hash(usuario.getPassword()).toHex());
					cadastrarUsuarioBO.adicionar(usuario);
					
					String nomes[] = usuario.getNome().split("\\s");
					String primeiroNome = nomes[0];
					Faces.getFlash().put("nome", primeiroNome);
					redirecionamento = navegacaoMB.getCadastrarUsuarioSucesso();
				}
			}
			catch (Exception e)
			{
				String excecao = ExcecaoParaMessageHtml.converter(e);
				Messages.create("Erro no cadastrarSe.  {0}", excecao).error().add();
			}
		}
			
		
		return redirecionamento;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getEmailConferencia() {
		return emailConferencia;
	}

	public void setEmailConferencia(String emailConferencia) {
		this.emailConferencia = emailConferencia;
	}

}
