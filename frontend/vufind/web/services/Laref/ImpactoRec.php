<?php
/**

 */
require_once 'Action.php';

/**
 * Impacto action for LaRef module
 *
 */
class ImpactoRec extends Action
{
    /**
     * Process parameters and display the page.
     *
     * @return void
     * @access public
     */
    public function launch()
    {
        global $configArray;
        global $interface;
		global $output; 
		 $vurl=$configArray['Site']['url'];
		 $vbiblio=$configArray['Index']['url'];
		 $vstats=$configArray['Statistics']['solr'];

	$output1="";
	$output2="";	
	$output3="";	
	$output4="";
	$output5="";
	$output6="";		
	$output8="";
	
		$output10="";
		$output11="";
		$banks = array();

	global $ccode;$ccode="";
	
			$url="http://lareferencia.shell.la:8090/public/listProviderStats";
		$json = file_get_contents($url);
		$data = json_decode($json, TRUE);
		
		// Make a MySQL Connection
		mysql_connect("localhost", "vufind", "vufind") or die(mysql_error());
		mysql_select_db("vufind") or die(mysql_error());
		
		//echo $url." :";
		
		$output10.= "<table border='1'>";
		$output10.=  "<tr> <th>Pais</th> <th>IP</th><th>Cuenta</th></tr>";
//		foreach($data as $red){
			foreach($data as $key => $value){
			//  echo print_r($value)."-";
			  if ($key==="content")
			  {

			  	   foreach($value as $snap){
				   foreach($snap as $key2 => $value2){
					 { 
					
					 // echo print_r($value2)."******";
					  if ($key2==="ipAddress")
					{	

						
						
							// Get all the data from the "example" table
							$result = mysql_query("SELECT ccode FROM geoip WHERE INET_ATON('".$value2."') BETWEEN startipi AND endipi LIMIT 1") 
							or die(mysql_error()); 
							while($row = mysql_fetch_array( $result )) {
							$ccode= $row['ccode'];
							}
						
					  	$output10.=  "<tr><td>$ccode</td>";
						$output10.= "<td>".$value2."</td>";
						$valtem=$value2;
					 }
					  else if ($key2==="requestCount")
					  {
					  $banks[] = array('name'=>$ccode,'amount'=>$value2);
					  //echo $ccode."-".$value2;
					   $output10.= "<td>".$value2."</td></tr>";
					 }

					  }

				    }
			        }
			  
			  
			  
				}
			}
//		}
		 $output10 .= '</table>';

		 // for search if a bank has been added into $amount, returns the key (index)
function bank_exists($bankname, $array) {
    $result = -1;
    for($i=0; $i<sizeof($array); $i++) {
        if ($array[$i]['name'] == $bankname) {
            $result = $i;
            break;
        }
    }
    return $result;
}	
		 
	// begin the iteration for grouping bank name and calculate the amount
$amount = array();
foreach($banks as $bank) {
    $index = bank_exists($bank['name'], $amount);
    if ($index < 0) {
        $amount[] = $bank;
    }
    else {
        $amount[$index]['amount'] +=  (int)$bank['amount'];
    }
}
//print_r($amount); //display 	 
//['Country', 'Consultas'],['AR',2],['AR',2]
$output11.="['Country', 'Consultas']";
foreach($amount as $a) {
$output11.=",['".$a['name']."',".$a['amount']."]";
}
//echo $output11;


	// Make a MySQL Connection
	mysql_connect("localhost", "vufind", "vufind") or die(mysql_error());
	mysql_select_db("vufind") or die(mysql_error());

	// Get all the data from the "example" table
	$result = mysql_query("SELECT count(*) as acceso,ccode FROM record WHERE ccode<>'' GROUP BY ccode ORDER BY ccode ") 
	or die(mysql_error());  
	$table="";
	$list="";
	$list.="['Country', 'Consultas']";
	$table.= "<table border='1'>";
	$table.=  "<tr> <th>Pa&iacute;s</th> <th>Consultas</th></tr>";
	// keeps getting the next row until there are no more to get
	while($row = mysql_fetch_array( $result )) {
		// Print out the contents of each row into a table
		$table.=  "<tr><td>"; 
		$table.=  $row['ccode'];
		$table.=  "</td><td>"; 
		$table.=  $row['acceso'];
		$table.=  "</td>"; 
		$table.=  "</td></tr>"; 
		$list.=",['".$row['ccode']."',".$row['acceso']."]";
	}	
	$list.="]);";
	$table.=  "</table>";			 
		 
// Get all the data from the "example" table
$result = mysql_query("SELECT count(*) as total,ccode,type from record group by ccode,type order by ccode") 
or die(mysql_error());  

$output6.="<table border='1' >";
$output6.="<tr> <th>Pa&iacute;s</th> <th>Material</th> <th>Consultas</th></tr>";
// keeps getting the next row until there are no more to get
while($row = mysql_fetch_array( $result )) {
	// Print out the contents of each row into a table
	$output6.="<tr><td>"; 
	$output6.=$row['ccode'];
	$output6.="</td><td>"; 
	$output6.=$row['type'];
	$output6.="</td><td>"; 
	$output6.=$row['total'];
		$output6.="</td><td>"; 	 
}
		 $output6.="</table>";	

// Get all the data from the "example" table
$result = mysql_query("SELECT count(*) as total,red,type from record group by red,type order by total desc") 
or die(mysql_error());  

$output8.="<table border='1' >";
$output8.="<tr>Material<th></th> <th>Pa&iacute;s</th> <th>Consultas</th></tr>";
// keeps getting the next row until there are no more to get
while($row = mysql_fetch_array( $result )) {
	// Print out the contents of each row into a table
	$output8.="<tr><td>"; 
	$output8.=$row['red'];
	$output8.="</td><td>"; 
	$output8.=$row['type'];
	$output8.="</td><td>"; 
	$output8.=$row['total'];
		$output8.="</td><td>"; 	 
}
		 $output8.="</table>";			 
		 
		 
		 
		$output = '<ul>';

		$url2 = $vbiblio.'/biblio/select?facet=true&facet.field=country&fl=country&q=type'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=index';
		$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);
		//$output .=$url2;

		//echo $url2;
		if($xml2 ===  FALSE)
		{
		   //deal with error
		$output .=  '<h2>ERROR</h1>';
		}
		else 
		{ //do stuff 
			$sum=0;
			$num=0;
			$output .=  '<ul >';
	 	foreach ($xml2->xpath("//lst[@name='country']/int") as $country) 
			{
			$num=number_format((int)$country);
			
			$pais=$country['name'];
			
		  //  echo '<li><a href="http://200.0.206.214/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=country%3A%22',$pais,'%22">',$pais,'</a> - ',$num,'</li>',PHP_EOL;
		  
		  $output .=  '<li>'.$pais.'</li>';
		 
					$sum+=$country;

		$url3 = $vbiblio.'/biblio/select?facet=true&facet.field=instname&fl=instname&q=country:'.urlencode('"'.$pais.'"').'&facet.limit=5&rows=0&facet.sort=index';
	//$output .=$url3;
	
		
		//echo $url3;
		$xml3 = simpleXML_load_file($url3,"SimpleXMLElement",LIBXML_NOCDATA);
		if($xml3 ===  FALSE)
		{
		   //deal with error
		}
		else { 

			$output .=  '<ul>';
		foreach ($xml3->xpath("//lst[@name='instname']/int") as $inst) 
			{
			$num=number_format((int)$inst);
			if($inst>0)
			$output .=  '<li><a href="'.$vurl.'/Search/Results?lookfor=&type=AllFields&filter%5B%5D=instname%3A%22'.$inst['name'].'%22">'.$inst['name'].'</a> - '.$inst.'</li>';
				$sum+=$inst;
			}/**/
		}
		//$output .=  '<li>&nbsp;</li>';

		$output .=  '</ul>';
		}
		} 

		$output .=  '</ul>';		

/////////////////////////////////////////////////////////////////////

$output2 .=  '<ul>';
$url2 = $vbiblio.'/biblio/select?facet=true&facet.field=country&fl=country&q=type'.urlencode(':[* TO *]').'&facet.limit=100&rows=0&facet.sort=count';
$xml2 = simpleXML_load_file($url2,"SimpleXMLElement",LIBXML_NOCDATA);

//echo $url2;
if($xml2 ===  FALSE)
{
   //deal with error
$output2 .= '<h2>ERROR</h1>';
}
else 
{ //do stuff 
    $sum=0;
	$num=0;
	$output .='<ul>';
foreach ($xml2->xpath("//lst[@name='country']/int") as $country) 
    {
	$num=number_format((int)$country);
	
	$pais=$country['name'];
	
    $output2 .='<li><a href="'.$vurl.'/Search/Results?lookfor=&type=AllFields&filter%5B%5D=country%3A%22'.$pais.'%22">'.$pais.'</a> - '.$num.'</li>';
		$sum+=$country;

$tipoa=0;
$tipom=0;
$tipod=0;
$tipor=0;
$tipotot=0;


$url3 = $vbiblio.'/biblio/select?facet=true&facet.field=type&fl=type&q=country:'.urlencode('"'.$pais.'"').'&facet.limit=5&rows=0&facet.sort=index';
	
//echo $url3;
$output2 .='<ul >';
$xml3 = simpleXML_load_file($url3,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml3 ===  FALSE)
{
   //deal with error
	$output2 .= '<h2>ERROR</h1>';
}
else { 

//do stuff 
foreach ($xml3->xpath("//lst[@name='type']/int") as $tipo) {
	

	$tipos=$tipo['name'];
	
	$output2 .='<li>'.$tipos.': '.number_format((int)$tipo).' </li>';

	}
}

$output2 .= '</ul>';	
}
} 
$output2 .='<li>Total '.number_format($sum).'</li>';
$output2 .='</ul>';


////////////////////////////////////////////////////////////////////		

$url4 = $vbiblio.'/biblio/select?q='.urlencode('topic_browse:[* TO *]').'&wt=xml&facet=true&facet.field=topic_browse&fl=topic_browse&facet.limit=1000&rows=0&facet.mincount=500&facet.sort=index';
//$output4=$url4;
$xml4 = simpleXML_load_file($url4,"SimpleXMLElement",LIBXML_NOCDATA);
if($xml4 ===  FALSE)
{
   //deal with error
	$output4 .= '<h2>ERROR</h1>';
}
else { //do stuff 

//echo $xml;

foreach ($xml4->xpath("//lst[@name='topic_browse']/int") as $busqueda) {
    $output4.="<a style='font-size:".($busqueda/80)."px;text-decoration:none;' href='".$vurl."/Search/Results?lookfor=%22".$busqueda["name"]."%22&type=topic_browse'> ".$busqueda["name"]."(".$busqueda.") </a>";
}

} 
////////////////////////////////////////////////////////////////////		
		
        // Load SOLR Socios
        $solr = ConnectionManager::connectToIndex();

		        // Paises Socios
        $result = $solr->search(
            '*:*', null, null, 0, null,
            array('field' => array('country', 'type','institution','country_iso'),'sort' => 'index'), '', null, null, null,
            HTTP_REQUEST_METHOD_GET
        );
        if (!PEAR::isError($result)) {
            if (isset($result['facet_counts']['facet_fields']['country'])) {
                $interface->assign(
                    'countryList', $result['facet_counts']['facet_fields']['country']
                );
            }
            if (isset($result['facet_counts']['facet_fields']['type'])) {
                $interface->assign(
                    'typeList',
                    $result['facet_counts']['facet_fields']['type']
                );
            }
            if (isset($result['facet_counts']['facet_fields']['institution'])) {
                $interface->assign(
                    'institutionList',
                    $result['facet_counts']['facet_fields']['institution']
                );
            } 
            if (isset($result['facet_counts']['facet_fields']['country_iso'])) {
                $interface->assign(
                    'cisoList',
                    $result['facet_counts']['facet_fields']['country_iso']
                );
            } 
 }

        // Load SOLR Statistics
        $solr = ConnectionManager::connectToIndex('SolrStats');
		

        // Search Statistics
        $result = $solr->search(
            'phrase:[* TO *]', null, null, 0, null,
            array('field' => array('noresults', 'phrase')),
            '', null, null, null, HTTP_REQUEST_METHOD_GET
        );
        if (!PEAR::isError($result)) {
            $interface->assign('searchCount', $result['response']['numFound']);

            // Extract the count of no hit results by finding the "no hit" facet
            // entry set to boolean true.
            $nohitCount = 0;
            $nhFacet = & $result['facet_counts']['facet_fields']['noresults'];
            if (isset($nhFacet) && is_array($nhFacet)) {
                foreach ($nhFacet as $nhRow) {
                    if ($nhRow[0] == 'true') {
                        $nohitCount = $nhRow[1];
                    }
                }
            }
            $interface->assign('nohitCount', $nohitCount);

            $interface->assign(
                'termList', $result['facet_counts']['facet_fields']['phrase']
            );
        }

        // Record View Statistics
        $result = $solr->search(
            'recordId:[* TO *]', null, null, 0, null,
            array('field' => array('recordId')),
            '', null, null, null, HTTP_REQUEST_METHOD_GET
        );
        if (!PEAR::isError($result)) {
            $interface->assign('recordViews', $result['response']['numFound']);
            $interface->assign(
                'recordList', $result['facet_counts']['facet_fields']['recordId']
            );
        }
		$interface->assign('list',$list);
		$interface->assign('table',$table);		
		$interface->assign('output',$output);
		$interface->assign('output2',$output2);
		$interface->assign('output4',$output4);
		$interface->assign('output6',$output6);
		$interface->assign('output8',$output8);
		$interface->assign('output10',$output10);
		$interface->assign('output11',$output11);
        $interface->setTemplate('impactorec.tpl');
        $interface->setPageTitle('Impacto Por Pais Recolector');
        $interface->display('layout.tpl');
    }
}

?>