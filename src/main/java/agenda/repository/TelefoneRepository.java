package agenda.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import agenda.model.Telefone;

@Repository
@Transactional
public interface TelefoneRepository extends JpaRepository<Telefone,	Long> {

	@Query("from Telefone t where t.ddd = ?1 and t.numero = ?2")
	Telefone getTelefone(String ddd, String numero);
	
	@Query("from Telefone t where t.id = ?1")
	Telefone getTelefonePorId(Long id);
	
	@Modifying
	@Query("delete from Telefone where id = ?1 ")
	void excluirTelefone(Long idTelefone);		
}
