package br.com.fences.vigilantefrontend.util;

import org.omnifaces.util.Faces;

import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilanteentidade.negocio.Endereco;
import br.com.fences.vigilanteentidade.negocio.VeiculoImagem;

public class ImagemMBHelper {
	
	private VeiculoImagem visualizacaoImagem;
	private String visualizacaoImagemLargura;
	
	private String localizacaoEstaticaUrlLargura;

	public void visualizarImagem(VeiculoImagem veiculoImagem)
	{
		
		String larguraDiv = Faces.getRequestParameter("larguraDiv");
//		for (VeiculoImagem veiculoImagem : imagens)
//		{
//			if (veiculoImagem.getUltimaAtualizacao().equals(ultimaAtualizacao))
//			{
				setVisualizacaoImagem(veiculoImagem);
				String largura = ImagemHelper.calcularLargura(veiculoImagem.getImagemByte(), larguraDiv);
				setVisualizacaoImagemLargura(largura);
//			}
//		}
	}
	
	public void voltarVisualizacaoImagem()
	{
		setVisualizacaoImagem(null);
		setVisualizacaoImagemLargura(null);
	}
	
	public byte[] redimencionar(byte[] imagemOriginal, int widthPx, int heightPx)
	{
		byte[] imagemRedimencionada = ImagemHelper.redimencionar(imagemOriginal, widthPx, heightPx);
		return imagemRedimencionada;
	}
	
	public String localizacaoEstaticaURL(Endereco endereco)
	{
		double latitude = endereco.getLatitude();
		double longitude = endereco.getLongitude();
		//String mapa = "https://maps.googleapis.com/maps/api/staticmap?center=-23.5384618,-46.6355504000&size=400x400&zoom=15&markers=color:red|-23.5384618,-46.6355504000";
		
		String mapa = "";
		if (Verificador.isValorado(localizacaoEstaticaUrlLargura))
		{
			String novoValor = Integer.toString(ImagemHelper.subtracaoPorcentual(20, Integer.parseInt(localizacaoEstaticaUrlLargura)));
			mapa = String.format("https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&size=%sx%s&zoom=15&markers=color:red|%f,%f", latitude, longitude, novoValor, novoValor, latitude, longitude);
		}
		else
		{
			mapa = String.format("https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&size=400x400&zoom=15&markers=color:red|%f,%f", latitude, longitude, latitude, longitude);
		}
		return mapa;
	}

	public void registrarLocalizacaoEstaticaUrlLargura()
	{
		localizacaoEstaticaUrlLargura = Faces.getRequestParameter("localizacaoEstaticaUrlLargura");
	}
	
	public VeiculoImagem getVisualizacaoImagem() {
		return visualizacaoImagem;
	}

	public void setVisualizacaoImagem(VeiculoImagem visualizacaoImagem) {
		this.visualizacaoImagem = visualizacaoImagem;
	}
	
	public String getVisualizacaoImagemLargura() {
		return visualizacaoImagemLargura;
	}

	public void setVisualizacaoImagemLargura(String visualizacaoImagemLargura) {
		this.visualizacaoImagemLargura = visualizacaoImagemLargura;
	}

}
