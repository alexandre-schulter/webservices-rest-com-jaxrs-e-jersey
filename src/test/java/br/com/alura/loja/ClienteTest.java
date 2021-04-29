package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;

public class ClienteTest {
	
	private HttpServer server;
	private Client client;

	@Before
	public void inicializaServidor() {
		server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
        config.register(new LoggingFilter());
        this.client = ClientBuilder.newClient(config);
	}
	
	@After
	public void mataServidor() {
		server.stop();
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		//String conteudo = target.path("/carrinhos/1").request().get(String.class);
		//Carrinho carrinho =(Carrinho)new XStream().fromXML(conteudo);
		
		Carrinho carrinho = target.path("/carrinhos/1").request().get(Carrinho.class);
		
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testaQueBuscarUmProjetoTrazOProjetoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos").request().get(String.class);
		
		Projeto projeto =(Projeto)new XStream().fromXML(conteudo);
		Assert.assertEquals("Minha loja", projeto.getNome());
	}

	@Test
	public void testaAdicionaCarrinho() {
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080");
		
		Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");

        //String xml = carrinho.toXML();
        //Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
        Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

        Response response = target.path("/carrinhos").request().post(entity);
        
        Assert.assertEquals(HttpStatus.CREATED_201.getStatusCode(), response.getStatus());
        //Assert.assertEquals("<status>sucesso</status>", response.readEntity(String.class));
	}
	
//	@Test
//    public void testaQueAConexaoComOServidorFunciona() {
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client.target("http://www.mocky.io");
//		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
//		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
//    }
	
//	@Test
//    public void testaQueAConexaoComOServidorFuncionaNoPathDeProjetos() {
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target("http://localhost:8080");
//        String conteudo = target.path("/projetos").request().get(String.class);
//        Assert.assertTrue(conteudo.contains("<nome>Minha loja"));
//
//
//    }
	
	@Test
    public void testaQueAConexaoComOServidorFuncionaNoPathDeProjetos() {
        WebTarget target = client.target("http://localhost:8080");
        Projeto projeto = target.path("/projetos/1").request().get(Projeto.class);
        Assert.assertEquals(1L, projeto.getId(),0);
    }
	
}
