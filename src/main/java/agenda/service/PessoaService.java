package agenda.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import agenda.model.Pessoa;
import agenda.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	PessoaRepository pessoaRepository;
	
	public Pessoa salvarPessoa(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public int calculaIdadePessoa(Date dataNascimento) {
		int idade = 0;
		Date dataAtual = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		String data_atual = formatador.format(dataAtual);
		String data_nascimento = formatador.format(dataNascimento);
		
		String[] atualValores = data_atual.split("/");
		int diaAtual = Integer.valueOf(atualValores[0]);
		int mesAtual = Integer.valueOf(atualValores[1]);
		int anoAtual = Integer.valueOf(atualValores[2]);
		
		String[] nascValores = data_nascimento.split("/");
		int diaNasc = Integer.valueOf(nascValores[0]);
		int mesNasc = Integer.valueOf(nascValores[1]);
		int anoNasc = Integer.valueOf(nascValores[2]);

		if(diaNasc >= diaAtual && mesNasc == mesAtual) {
			idade = (anoAtual-1)-anoNasc;
		}else if(diaAtual > diaNasc && mesNasc == mesAtual) {
			idade = anoAtual-anoNasc;
		}else if(mesAtual < mesNasc) {
			idade = (anoAtual-1)-anoNasc;
		}else {
			idade = anoAtual-anoNasc;
		}
		
		return idade;
	}

	public List<Pessoa> getPessoas() {
		return pessoaRepository.getPessoas();
	}

	public List<Pessoa> getPessoas(String nome) {
		return pessoaRepository.getPessoasPorNome(nome);
	}
	
	public Pessoa getPessoa(Long id) {
		return pessoaRepository.findOne(id);
	}

	public Pessoa getPessoa(String cpf) {
		return pessoaRepository.getPessoaCPF(cpf);
	}
	
	public void excluirPessoa(Pessoa pessoa) {
		pessoaRepository.delete(pessoa);
	}
	
}
