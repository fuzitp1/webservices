<?php
require "banco.php";
$metodoHttp = $_SERVER['REQUEST_METHOD'];

$delimitador = "#";
if ($metodoHttp == 'POST') {
	$json = json_decode(file_get_contents('php://input'));
	$mesa     = $json->{'codMesa'};
	$cod_mesa = 5;
	
    $sql = "SELECT I.CODIGO, I.COD_PRODUTO, I.COD_PEDIDO, I.DESCRICAO, I.PRECO_UNITARIO, I.QUANTIDADE, I.TOTAL FROM ITEM_PEDIDO I ";
	$sql.= "JOIN PEDIDO P ON P.CODIGO=I.COD_PEDIDO ";
    $sql.= "WHERE P.COD_MESA=$cod_mesa AND P.STATUS=1";
	$sqlrot = ibase_query($db, $sql);

	while($coluna=ibase_fetch_object($sqlrot))
    {

	  $jsonLinha = array(
				"codigo"       => $coluna->CODIGO,
				"cod_pedido"   => $coluna->COD_PEDIDO,
				"cod_produto"  => $coluna->COD_PRODUTO,
				"descricao"    => trim($coluna->DESCRICAO),
				"preco"        => $coluna->PRECO_UNITARIO,
				"quantidade"   => $coluna->QUANTIDADE,
				"total"        => $coluna->TOTAL);
      $jsonArray[] = $jsonLinha;
	}
    
	echo json_encode($jsonArray);
} 
?>
