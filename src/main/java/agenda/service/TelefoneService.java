package agenda.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import agenda.model.Telefone;
import agenda.repository.TelefoneRepository;

@Service
public class TelefoneService {
	
	@Autowired
	TelefoneRepository telefoneRepository;
		
	public Telefone salvarTelefone(Telefone telefone) {
		Telefone telefoneBanco = telefoneRepository.getTelefone(telefone.getDdd(), telefone.getNumero());
		if(telefoneBanco == null) {
			return telefoneRepository.save(telefone);			
		}else {
			return null;
		}
	}
	
	public void deletarTelefone(Long id) {
		Telefone telefone = getTelefone(id);
		telefoneRepository.delete(telefone);		
	}
	
	public Telefone getTelefone(Long id) {
		return telefoneRepository.getTelefonePorId(id);
	}
	
	
}
