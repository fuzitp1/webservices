<?
ini_set("soap.wsdl_cache_enabled", "0");
  /*
    Autor...: Eloi
	Data....: 22/11/2015
	
  */

  // Inclusão do arquivo com a classe nusoap
  require_once('nusoap/lib/nusoap.php');
  // Criação de uma instância do servidor
  $server = new soap_server;
  // Inicia suporte ao WSDL
  $server->configureWSDL('server.listapedido','urn:server.listapedido');
  $server->wsdl->schemaTargetNamespace = 'urn:server.listapedido';
  
  // Registro do método
  $server->register('listapedido', // Nome do método
  					array('chave' => 'xsd:string'),
					// Parâmetros de entrada
					array('return' => 'xsd:string'), // Parametros de saída
					'urn:server.listapedido', // Namespace
					'urn:server.listapedido#listapedido', // soapaction
					'rpc', //style
					'encoded', // use
					'Lista os itens do pedido de uma mesa' // Documentação do serviço
					);


					
	function listapedido($chave) 
	{
      
	  	$itens = ExecutaTabela2($chave);
		return  $itens;
		
	  
	  
	}
	
     

	
// Requisição para uso do serviço
$HTTP_RAW_POST_DATA = isset($HTTP_RAW_POST_DATA) ? $HTTP_RAW_POST_DATA : '';
$server->service($HTTP_RAW_POST_DATA);
  

function ExecutaTabela2( $cod_mesa ) {
		include("banco.php");
        $cod_retorno = 1;
		$msg_erro  = "Teste de mensagem";
		$coln = 1;
        // Monta o XML da Resposta
        $xml = new XMLWriter();

        $xml->openMemory();
        $xml->startDocument ( '1.0' , 'utf-8', 'yes' ) ;
        $xml->setIndent(true);

        $xml->startElement('DADOS') ;
		$sql = "SELECT I.CODIGO, I.COD_PEDIDO, I.COD_PRODUTO, I.DESCRICAO, I.PRECO_UNITARIO, I.QUANTIDADE, I.TOTAL FROM ITEM_PEDIDO I ";
		$sql.= "JOIN PEDIDO P ON P.CODIGO=I.COD_PEDIDO ";
        $sql.= "WHERE P.COD_MESA=$cod_mesa AND P.STATUS=1";
   		$sqlrot = ibase_query($db, $sql);
		while($coluna=ibase_fetch_object($sqlrot))
    	{
        	$xml->startElement('ITEM_PEDIDO') ;
	   	 	$xml->writeElement('codigo', $coluna->CODIGO) ;
			$xml->writeElement('cod_pedido', $coluna->COD_PEDIDO ) ;
        	$xml->writeElement('cod_produto', $coluna->COD_PRODUTO ) ;
        	$xml->writeElement('descricao', trim($coluna->DESCRICAO) ) ;
			$xml->writeElement('preco', $coluna->PRECO_UNITARIO ) ;
			$xml->writeElement('quantidade', $coluna->QUANTIDADE ) ;
			$xml->writeElement('total', $coluna->TOTAL ) ;
        	$xml->endElement() ;// ITEM_PEDIDO
 		}
        $xml->endElement() ;// DADOS
        
        $texto = $xml->outputMemory ( true ) ;

        $xml->flush();
        
        return utf8_encode($texto);

    }
?>
