<?php
require "banco.php";
$metodoHttp = $_SERVER['REQUEST_METHOD'];

$delimitador = "#";
if ($metodoHttp == 'POST') {
	$json = json_decode(file_get_contents('php://input'));
	$mesa     = $json->{'mesa'};
	$cod_mesa = 5;
	
    $sql = "SELECT I.CODIGO, I.COD_PRODUTO, I.COD_PEDIDO, I.DESCRICAO, I.PRECO_UNITARIO, I.QUANTIDADE, I.TOTAL FROM ITEM_PEDIDO I ";
	$sql.= "JOIN PEDIDO P ON P.CODIGO=I.COD_PEDIDO ";
    $sql.= "WHERE P.COD_MESA=$cod_mesa AND P.STATUS=1";
	$sqlrot = ibase_query($db, $sql);
	$data = array();
	while($coluna=ibase_fetch_object($sqlrot))
    {

	  $data['ITEM_PEDIDO'][] = array(
        'codigo' =>  $coluna->CODIGO,
        'cod_pedido'   => $coluna->COD_PEDIDO,
		'cod_produto'  => $coluna->COD_PRODUTO,
		'descricao'    => trim($coluna->DESCRICAO),
		'preco'        => $coluna->PRECO_UNITARIO,
		'quantidade'   => $coluna->QUANTIDADE,
		'total'        => $coluna->TOTAL,
    );
	}
    

$data2 = array();
$data2['DADOS'][]=$data;

	//echo json_encode($jsonArray);
    header('Content-Type: application/xml');
    echo xml_encode($data2);
} 

function xml_encode($mixed, $domElement=null, $DOMDocument=null) {
    if (is_null($DOMDocument)) {
        $DOMDocument =new DOMDocument;
        $DOMDocument->formatOutput = true;
        xml_encode($mixed, $DOMDocument, $DOMDocument);
        echo $DOMDocument->saveXML();
    }
    else {
        if (is_array($mixed)) {
            foreach ($mixed as $index => $mixedElement) {
                if (is_int($index)) {
                    if ($index === 0) {
                        $node = $domElement;
                    }
                    else {
                        $node = $DOMDocument->createElement($domElement->tagName);
                        $domElement->parentNode->appendChild($node);
                    }
                }
                else {
                    $plural = $DOMDocument->createElement($index);
                    $domElement->appendChild($plural);
                    $node = $plural;
                    if (!(rtrim($index, 's') === $index)) {
                        $singular = $DOMDocument->createElement(rtrim($index, 's'));
                        $plural->appendChild($singular);
                        $node = $singular;
                    }
                }

                xml_encode($mixedElement, $node, $DOMDocument);
            }
        }
        else {
            $mixed = is_bool($mixed) ? ($mixed ? 'true' : 'false') : $mixed;
            $domElement->appendChild($DOMDocument->createTextNode($mixed));
        }
    }
}
?>
