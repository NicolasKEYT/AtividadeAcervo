package nicolas_acervo.aplicacao;

import nicolas_acervo.entidade.Livro;
import nicolas_acervo.entidade.Biblioteca;
import nicolas_acervo.repositorio.LivroRepository;
import nicolas_acervo.repositorio.BibliotecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleApp {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private BibliotecaRepository bibliotecaRepository;

    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        while (true) {
            System.out.println("\n=== Sistema de Acervo de Livros ===");
            System.out.println("1. Cadastrar novo livro");
            System.out.println("2. Listar todos os livros");
            System.out.println("3. Buscar livros por autor");
            System.out.println("4. Buscar livros por ano de publicação");
            System.out.println("5. Buscar livros por termo no título");
            System.out.println("6. Cadastrar nova biblioteca");
            System.out.println("7. Listar bibliotecas");
            System.out.println("8. Associar livro a uma biblioteca");
            System.out.println("9. Listar livros de uma biblioteca");
            System.out.println("10. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> listarLivros();
                case 3 -> buscarPorAutor();
                case 4 -> buscarPorAno();
                case 5 -> buscarPorTermo();
                case 6 -> cadastrarBiblioteca();
                case 7 -> listarBibliotecas();
                case 8 -> associarLivroBiblioteca();
                case 9 -> listarLivrosBiblioteca();
                case 10 -> {
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

    private void cadastrarBiblioteca() {
        System.out.println("\n[Cadastro de Biblioteca]");
        System.out.print("Digite o nome da biblioteca: ");
        String nome = scanner.nextLine();

        Biblioteca biblioteca = new Biblioteca(nome);
        bibliotecaRepository.save(biblioteca);
        System.out.println("Biblioteca cadastrada com sucesso!");
    }

    private void listarBibliotecas() {
        System.out.println("\n[Listagem de Bibliotecas]");
        List<Biblioteca> bibliotecas = bibliotecaRepository.findAll();
        if (bibliotecas.isEmpty()) {
            System.out.println("Nenhuma biblioteca cadastrada.");
        } else {
            bibliotecas.forEach(b -> System.out.println("ID: " + b.getId() + " | Nome: " + b.getNome()));
        }
    }

    private void associarLivroBiblioteca() {
        System.out.println("\n[Associar Livro a Biblioteca]");
        System.out.print("Digite o ID do livro: ");
        Long livroId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Digite o ID da biblioteca: ");
        Long bibliotecaId = scanner.nextLong();
        scanner.nextLine();

        Livro livro = livroRepository.findById(livroId).orElse(null);
        Biblioteca biblioteca = bibliotecaRepository.findById(bibliotecaId).orElse(null);

        if (livro == null || biblioteca == null) {
            System.out.println("Livro ou biblioteca não encontrados.");
            return;
        }

        livro.setBiblioteca(biblioteca);
        livroRepository.save(livro);
        System.out.println("Livro associado à biblioteca com sucesso!");
    }

    @Transactional
    private void listarLivrosBiblioteca() {
        System.out.println("\n[Listar Livros de uma Biblioteca]");
        System.out.print("Digite o ID da biblioteca: ");
        Long bibliotecaId = scanner.nextLong();
        scanner.nextLine();

        Biblioteca biblioteca = bibliotecaRepository.findById(bibliotecaId).orElse(null);
        if (biblioteca == null) {
            System.out.println("Biblioteca não encontrada.");
            return;
        }

        List<Livro> livros = biblioteca.getLivros(); // Lazy Loading agora ocorre dentro da transação
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado nesta biblioteca.");
        } else {
            livros.forEach(System.out::println);
        }
    }
}
