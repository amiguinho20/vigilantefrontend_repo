package br.com.fences.vigilantefrontend.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import br.com.fences.fencesutils.verificador.Verificador;

public class ImagemHelper {
	
	public static byte[] redimencionar(byte[] imagem, int newWidth, int newHeight) {
        
		InputStream is = new ByteArrayInputStream(imagem);
		BufferedImage image = null;
		try {
			image = ImageIO.read(is);
			is.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		int currentWidth = image.getWidth();
        int currentHeight = image.getHeight();
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D graphics2d = newImage.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.drawImage(image, 0, 0, newWidth, newHeight, 0, 0,
                currentWidth, currentHeight, null);
        graphics2d.dispose();
        
        byte[] imageInByte = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	try {
			ImageIO.write( newImage, "png", baos);
	    	baos.flush();
	    	imageInByte = baos.toByteArray();
	    	baos.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        return imageInByte;
    }
	
	public static int[] recuperarAlturaLargura(byte[] imagem)
	{
		int[] alturaLargura = new int[2];
		if (imagem != null)
		{
			InputStream is = new ByteArrayInputStream(imagem);
			BufferedImage bi = null;
			try {
				bi = ImageIO.read(is);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			alturaLargura[0] = bi.getHeight();
			alturaLargura[1] = bi.getWidth();
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return alturaLargura;
	}
	
	public static String calcularLargura(byte[] imagem, String larguraDiv)
	{
		String largura = "";
		if (imagem != null && Verificador.isValorado(larguraDiv))
		{
			int larDiv = Integer.parseInt(larguraDiv);
			int[] larguraAltura = ImagemHelper.recuperarAlturaLargura(imagem);
			int larImg = larguraAltura[0];
			
			if (larImg > larDiv)
			{
				largura = "100%";
			}
			else
			{
				largura = larImg + "px";
			}
		}
		return largura;
	}

	public static int subtracaoPorcentual(int porcento, int valor)
	{
		int novoValor = Math.round(valor - valor * porcento / 100f);
		return novoValor;
	}
	
	/**
	 * De quanto o limite foi excedido em percentual.
	 * Eh utilizando o arredondamento.
	 */
	public static int variacaoPorcentual(int limite, int valor)
	{
		float flimite = limite;
		float fvalor = valor;
		int variacaoPercentual = Math.round((flimite - fvalor) / fvalor * -100f);
		variacaoPercentual++;
		return variacaoPercentual;
	}
	
	public static byte[] calcularRedimencionamento(byte[] imagem, int alturaLimitePx, int larguraLimitePx)
	{
		byte[] novaImagem = null;
		if (imagem != null)
		{
			int[] alturaLargura = ImagemHelper.recuperarAlturaLargura(imagem);
			int alturaImagem = alturaLargura[0];
			int larguraImagem = alturaLargura[1];
			
			if (alturaImagem > alturaLimitePx || larguraImagem > larguraLimitePx)
			{
				while (alturaImagem > alturaLimitePx || larguraImagem > larguraLimitePx)
				{
					int variacaoPorcentual = 0;
					if (alturaImagem > alturaLimitePx)
					{
						variacaoPorcentual = ImagemHelper.variacaoPorcentual(alturaLimitePx, alturaImagem);
						alturaImagem = ImagemHelper.subtracaoPorcentual(variacaoPorcentual, alturaImagem);
						larguraImagem = ImagemHelper.subtracaoPorcentual(variacaoPorcentual, larguraImagem);
					}
					if (larguraImagem > larguraLimitePx)
					{
						variacaoPorcentual = ImagemHelper.variacaoPorcentual(larguraLimitePx, larguraImagem);
						alturaImagem = ImagemHelper.subtracaoPorcentual(variacaoPorcentual, alturaImagem);
						larguraImagem = ImagemHelper.subtracaoPorcentual(variacaoPorcentual, larguraImagem);
					}
				}
				novaImagem = ImagemHelper.redimencionar(imagem, larguraImagem, alturaImagem);
			}
			else
			{
				novaImagem = imagem;
			}
		}
		return novaImagem;
	}
	
	public static byte[] converterParaPng(byte[] origem, String origemExtencao)
	{
		InputStream is = new ByteArrayInputStream(origem);
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(is);
		} catch (IOException e) {
			new RuntimeException(e);
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "png", os);
		} catch (IOException e) {
			new RuntimeException(e);
		}
		byte[] destino = null;
		destino = os.toByteArray();
		try {
			os.close();
		} catch (IOException e) {
			new RuntimeException(e);
		}
		return destino;
	}

	
}
