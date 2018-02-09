package agenda.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import agenda.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository  extends JpaRepository<Pessoa, Long>{

	@Query("from Pessoa p where p.cpf = ?1")
	Pessoa getPessoaCPF(String cpf);

	@Query("from Pessoa p where upper(p.nome) = upper(?1)")
	List<Pessoa> getPessoasPorNome(String nome);

	@Query("from Pessoa p order by p.id asc")
	List<Pessoa> getPessoas();

}
