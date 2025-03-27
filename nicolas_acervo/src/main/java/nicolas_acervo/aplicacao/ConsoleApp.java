package nicolas_acervo.aplicacao;


import nicolas_acervo.entidade.Livro; 


import nicolas_acervo.repositorio.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleApp {

    @Autowired
    private LivroRepository livroRepository;

    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        while (true) {
            System.out.println("\n=== Sistema de Acervo de Livros ===");
            System.out.println("1. Cadastrar novo livro");
            System.out.println("2. Listar todos os livros");
            System.out.println("3. Buscar livros por autor");
            System.out.println("4. Buscar livros por ano de publicação");
            System.out.println("5. Buscar livros por termo no título");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> listarLivros();
                case 3 -> buscarPorAutor();
                case 4 -> buscarPorAno();
                case 5 -> buscarPorTermo();
                case 6 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarLivro() {
        System.out.println("\n[Cadastro de Livro]");
        System.out.print("Digite o título: ");
        String titulo = scanner.nextLine();
        System.out.print("Digite o autor: ");
        String autor = scanner.nextLine();
        System.out.print("Digite o ano de publicação: ");
        int anoPublicacao = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha
        System.out.print("Digite a editora: ");
        String editora = scanner.nextLine();

        if (livroRepository.existsByTituloIgnoreCaseAndAutorIgnoreCase(titulo, autor)) {
            System.out.println("Livro já cadastrado!");
            return;
        }

        Livro livro = new Livro(titulo, autor, anoPublicacao, editora);
        livroRepository.save(livro);
        System.out.println("Livro cadastrado com sucesso!");
    }

    private void listarLivros() {
        System.out.println("\n[Listagem Completa do Acervo]");
        List<Livro> livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            System.out.println("ID | Título | Autor | Ano | Editora");
            System.out.println("-------------------------------------------------");
            livros.forEach(System.out::println);
        }
    }

    private void buscarPorAutor() {
        System.out.println("\n[Busca por Autor]");
        System.out.print("Digite o nome do autor: ");
        String autor = scanner.nextLine();

        List<Livro> livros = livroRepository.findByAutorIgnoreCase(autor);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o autor " + autor);
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void buscarPorAno() {
        System.out.println("\n[Busca por Ano de Publicação]");
        System.out.print("Digite o ano desejado: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        List<Livro> livros = livroRepository.findByAnoPublicacao(ano);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o ano " + ano);
        } else {
            livros.forEach(System.out::println);
        }
    }

    private void buscarPorTermo() {
        System.out.println("\n[Busca por Termo no Título]");
        System.out.print("Digite o termo desejado: ");
        String termo = scanner.nextLine();

        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(termo);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado contendo '" + termo + "' no título.");
        } else {
            livros.forEach(System.out::println);
        }
    }
}
