package agenda.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import agenda.model.Pessoa;
import agenda.model.Telefone;
import agenda.service.PessoaService;
import agenda.service.TelefoneService;

@Controller
@RequestMapping(path="/pessoa")
public class PessoaController {
	
	@Autowired
	PessoaService pessoaService;
	
	@Autowired
	TelefoneService telefoneService;
	
	private List<Long> idTelefones = new ArrayList<Long>();
	
	
	@GetMapping(path="/cadastrar")
	public ModelAndView cadastrarPessoa() {
		ModelAndView model = new ModelAndView("formCadastroPessoa");
		model.addObject("pessoa", new Pessoa());
		return model;
	}
	
	@PostMapping(path="/cadastrar")
	public String salvarPessoa(Pessoa pessoa) {	
		
		if (!verificarFormulario(pessoa)) return "formCadastroPessoa";
		
		int idade = pessoaService.calculaIdadePessoa(pessoa.getDataNascimento());
		
		List<Telefone> telefones = new ArrayList<Telefone>();		
		
		for (int i = 0; i < idTelefones.size(); i++) {
			Telefone telefone = telefoneService.getTelefone(idTelefones.get(i));
			telefones.add(telefone);
		}
		
		pessoa.setTelefones(telefones);
		pessoa.setIdade(idade);
		
		for (Telefone telefone : pessoa.getTelefones()) {
			telefone.setPessoa(pessoa);
		}
		pessoaService.salvarPessoa(pessoa);
		
		return "redirect:/pessoa/listar";
	}

	@GetMapping(path="/editar/{id}")
	public ModelAndView editarPessoa(@PathVariable("id") Long id) {
		ModelAndView model = new ModelAndView("formEditarPessoa");
		Pessoa pessoa = pessoaService.getPessoa(id);
		model.addObject("pessoa", pessoa);
		return model;
	}
	

	@PostMapping(path="/editar")
	public String editarPessoa(Pessoa pessoa, Long pessoa_id) {
				
		if (!verificarFormulario(pessoa)) return "formEditarPessoa";
		
		Pessoa salva = pessoaService.getPessoa(pessoa_id);
		int idade = pessoaService.calculaIdadePessoa(pessoa.getDataNascimento());
			
		List<Telefone> telefones_salvos = salva.getTelefones();
		List<Telefone> telefones = new ArrayList<Telefone>();
			
		for (int i = 0; i < idTelefones.size(); i++) {
			Telefone telefone = telefoneService.getTelefone(idTelefones.get(i));
			telefones.add(telefone);
		}

		idTelefones.removeAll(idTelefones);

		pessoa.setId(pessoa_id);
		pessoa.setIdade(idade);
		
		for (Telefone telefone : telefones) {
			telefone.setPessoa(pessoa);
			telefones_salvos.add(telefone);
		}
		
		pessoa.setTelefones(telefones_salvos);
		
		pessoaService.salvarPessoa(pessoa);
		
		return "redirect:/pessoa/listar";		
	}
	
	@GetMapping(path="/listar")
	public ModelAndView listarPessoas() {
		ModelAndView model = new ModelAndView("listarPessoas");
		List<Pessoa> pessoas = pessoaService.getPessoas();
		model.addObject("pessoas", pessoas);
		model.addObject("pessoa", new Pessoa());
		return model;		
	}
	
	@GetMapping(path = "/lista/cadastrar")
	public ModelAndView cadastrarListaPessoa(Pessoa pessoa) {
		ModelAndView model = new ModelAndView("formCadastroPessoa");
		model.addObject("pessoa", pessoa);
		return model;
	}
	
	@GetMapping(path="/pesquisar")
	public ModelAndView pesquisarPessoa(String nome, String cpf) {

		ModelAndView model = new ModelAndView("visualizarPessoas");
		Pessoa pessoa = new Pessoa();
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		if(!nome.isEmpty() && cpf.isEmpty()) {
			pessoas = pessoaService.getPessoas(nome);
			if(pessoas.size() >= 1) {
				model.addObject("pessoas", pessoas);
				return model;				
			}else {
				return new ModelAndView("mensagemErro");
			}
		}else if(nome.isEmpty() && !cpf.isEmpty()){
			pessoa = pessoaService.getPessoa(cpf);
			if(pessoa != null) {
				pessoas.add(pessoa);
				model.addObject("pessoas", pessoas);
				return model;				
			}else {
				return new ModelAndView("mensagemErro");
			}
		}else {
			pessoa = pessoaService.getPessoa(cpf);
			if(pessoa != null) {
				pessoas.add(pessoa);
				model.addObject("pessoas", pessoas);
				return model;				
			}else {
				return new ModelAndView("mensagemErro");
			}
		}
	}
	
	@GetMapping(path="/excluir/{id}")
	public String excluirPessoa(@PathVariable("id") Long id) {
		Pessoa pessoa = pessoaService.getPessoa(id);
		pessoaService.excluirPessoa(pessoa);
		return "redirect:/pessoa/listar";
	}
	
	@RequestMapping(value = "/telefone/cadastrar", method=RequestMethod.POST)
	public @ResponseBody Telefone salvarTelefone(String ddd, String numero) {

		if(!verificarTelefone(ddd, numero)) return null;
		
		Telefone telefoneSalvo = telefoneService.salvarTelefone(new Telefone(ddd,numero,null));
		if(telefoneSalvo == null) {
			return null;
		}
		idTelefones.add(telefoneSalvo.getId());
		return telefoneSalvo;
	}
	
	@RequestMapping(value = "/telefone/deletar", method=RequestMethod.GET)
	public @ResponseBody boolean deletarTelefone(Long idTelefone) {
		if(idTelefone == null) return false;
		
		telefoneService.deletarTelefone(idTelefone);
		idTelefones.remove(idTelefone);
		
		return true;
	}

	public boolean verificarFormulario(Pessoa pessoa) {
		if(!verificarNome(pessoa.getNome())
				|| !verificarCPF(pessoa.getCpf())) return false;
				
		return true;
	}
	
	public boolean verificarNome(String nome) {
		
		if (nome.isEmpty()) return false;
		
		for (int i = 0; i < nome.length(); i++) {
			if(!Character.isLetter(nome.charAt(i)) && !Character.isSpaceChar(nome.charAt(i))) {
				return false;
			}
		}		
		return true;
	}
	
	public boolean verificarCPF(String cpf) {
		
		if (cpf.isEmpty()) return false;
		
		int tamanhocpf = 12;
		
		if(cpf.length() > tamanhocpf || cpf.length() < tamanhocpf || !cpf.contains("-")) return false;
		
		for (int i = 0; i < cpf.length(); i++) {
			if(Character.isLetter(cpf.charAt(i))) {
				return false;
			}			
			if(!Character.isDigit(cpf.charAt(i)) && cpf.charAt(i) != '-') {
				return false;
			}
		}
		return true;
	}
	
	public boolean verificarTelefone(String ddd, String numero) {
		int tamanhoDDD = 2;
		int tamanhoNumero = 10;
		String[] partes = numero.split("-");
		
		if(ddd.length() < tamanhoDDD || ddd.length() > tamanhoDDD) return false;
		
		if(numero.length() < tamanhoNumero || numero.length() > (tamanhoNumero+1) || !numero.contains("-")) return false;
		
		if(partes.length < 3 || partes.length > 3) return false;
		
		for (int i = 0; i < ddd.length(); i++) {
			if(!Character.isDigit(ddd.charAt(i))) {
				return false;
			}
		}
		
		for (int i = 0; i < numero.length(); i++) {		
			if(!Character.isDigit(numero.charAt(i)) && numero.charAt(i) != '-') {
				return false;
			}
		}
				
		return true;
	}
}
