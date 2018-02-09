 function salvarTelefone() {

    var formData = {
    		'ddd': $('input[name=ddd]').val(),
            'numero': $('input[name=numero]').val()
    };
        
    $.ajax({
       type:'POST',
       url: '/pessoa/telefone/cadastrar',
       data: formData,
       dataType: 'json',
       success: function(telefone){
            $('table tr:last')
            .after('<tr><td id="idTelefone">'+telefone.id+'</td>'+'<td>'+telefone.ddd+'</td>'+'<td>'+telefone.numero+'</td>' +
                    '<td><a href="javascript:void(0);" onclick="deletarTelefone()">Deletar</a></td></tr>');
       },
       error: function(data){
           alert('Erro: Número de telefone já foi cadastrado ou inválido!');
       }
     });               
 }
         
 function deletarTelefone(){
	 	 
     $("#listaFones tr:gt(0)").click(function () {
    	 var linha = $(this);
         var id = $.trim(linha.find('td:eq(0)').html());
            
         $.ajax({
             type:'GET',
             url: '/pessoa/telefone/deletar',
             data: {'idTelefone':id},
             dataType: 'json'
         });        
         return false;            	 
     });
     
     $('#listaFones tr').click(function(){
            $(this).remove();
     });
     
 }
 
 function pesquisar(){

	var formData = {
			'nome': $('input[name=nome]').val(),
	        'cpf': $('input[name=cpf]').val()
	};
	    
	if(formData.nome || formData.cpf){
	    document.getElementById('id_form').action = "/pessoa/pesquisar";
	    document.getElementById('id_form').submit();
	        
	}else{
		alert("Informe um dos campos de pesquisa!!!");
	}
}

function cadastrar(){
	document.getElementById('id_form').action = "/pessoa/lista/cadastrar";
	document.getElementById('id_form').submit();
}

