package com.example.literr;

import com.example.literr.classes.objects.Autor;
import com.example.literr.classes.objects.Livro;
import com.example.literr.classes.objects.Resultado;
import com.example.literr.classes.services.UsoAPI;
import com.example.literr.LivroRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
public class LiterrApplication implements CommandLineRunner {

	@Autowired
	private LivroRepository livroRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiterrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		UsoAPI api = new UsoAPI();
		Scanner scanner = new Scanner(System.in);

		System.out.println("""
            ---------------------
            1 - buscar livro
            2 - buscar autor
            3 - buscas feitas(livros)
            4 - buscas feitas(autor)
            ---------------------
            """);

		System.out.print("escolha: ");
		var escolha = scanner.nextInt();
		scanner.nextLine();

		switch (escolha) {
			case 1:
				System.out.print("Digite o título ou autor: ");
				var termoDeBusca = scanner.nextLine();
				ObjectMapper mapper = new ObjectMapper();
				Resultado resultado = mapper.readValue(api.client(termoDeBusca), Resultado.class);

				System.out.println("\nLivros encontrados:");
				for (Livro livro : resultado.resultado()) {
					System.out.println("Título: " + livro.getTitulo() + " // Autores: " + livro.getAutores());
				}
				break;
			case 2:
				System.out.print("Fale o autor: ");
				var termoAutor = scanner.nextLine();
				var termoBusca = termoAutor.toLowerCase();

				ObjectMapper mapper2 = new ObjectMapper();
				String jsonResponse = api.client(termoBusca);

				Resultado resultado2 = mapper2.readValue(jsonResponse, Resultado.class);

				List<Livro> livrosEncontrados = resultado2.resultado().stream()
						.filter(livro -> livro.getAutores() != null &&
								!livro.getAutores().isEmpty() &&
								livro.getAutores().get(0).getNome().toLowerCase().contains(termoBusca))
						.collect(Collectors.toList());

				if (livrosEncontrados.isEmpty()) {
					System.out.println("\nNenhum livro encontrado para o autor: " + termoAutor);
				} else {
					System.out.println("\nLivros encontrados para o autor '" + termoAutor + "':");
					for (Livro livro : livrosEncontrados) {
						System.out.println("Título: " + livro.getTitulo() + " // Autor: " + livro.getAutores().get(0).getNome());
						livro.setId(null);
						livro.getAutores().forEach(autor -> autor.setId(null));
					}

					livroRepository.saveAll(livrosEncontrados);
					System.out.println("\nOs livros foram salvos no banco de dados!");
				}
				break;
			case 3:
				System.out.println("\n--- Livros Salvos no Banco de Dados ---");
				List<Livro> livrosSalvos = livroRepository.findAll();
				if (livrosSalvos.isEmpty()) {
					System.out.println("Nenhum livro foi salvo ainda.");
				} else {
					for (Livro livro : livrosSalvos) {
						System.out.println("Título: " + livro.getTitulo());
						System.out.println("Autores: " + livro.getAutores().stream().map(Autor::getNome).collect(Collectors.joining(", ")));
						System.out.println("---");
					}
				}
				break;
			case 4:
				System.out.println("\n--- Autores Salvos no Banco de Dados ---");
				List<Autor> autoresSalvos = livroRepository.findAllAutores();
				if (autoresSalvos.isEmpty()) {
					System.out.println("Nenhum autor foi salvo ainda.");
				} else {
					for (Autor autor : autoresSalvos) {
						System.out.println("Nome: " + autor.getNome());
					}
				}
				break;
			default:
				System.out.println("Opção inválida.");
				break;
		}
		scanner.close();
	}
}