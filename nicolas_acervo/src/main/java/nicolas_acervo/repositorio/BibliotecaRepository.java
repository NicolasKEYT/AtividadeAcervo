package nicolas_acervo.repositorio;

import nicolas_acervo.entidade.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, Long> {
}