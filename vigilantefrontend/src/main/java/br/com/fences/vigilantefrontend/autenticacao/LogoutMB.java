package br.com.fences.vigilantefrontend.autenticacao;

import java.io.IOException;

import javax.enterprise.inject.Model;

import org.apache.shiro.SecurityUtils;
import org.omnifaces.util.Faces;

@Model
public class LogoutMB {

	public static final String HOME_URL = "privado/home/home.xhtml?faces-redirect=true"; //"publico.xhtml";

    public String desautenticar() throws IOException {
        SecurityUtils.getSubject().logout();
        Faces.invalidateSession();
        Faces.redirect(HOME_URL);
        return "";
    }
	
}
