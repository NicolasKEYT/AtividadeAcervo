package nicolas_acervo.repositorio;


import nicolas_acervo.entidade.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutorIgnoreCase(String autor);

    List<Livro> findByAnoPublicacao(int anoPublicacao);

    List<Livro> findByTituloContainingIgnoreCase(String termo);

    boolean existsByTituloIgnoreCaseAndAutorIgnoreCase(String titulo, String autor);
}
