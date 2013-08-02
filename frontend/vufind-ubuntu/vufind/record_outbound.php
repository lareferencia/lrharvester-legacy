<?
 
    
function getRealIP() {
	if (!empty($_SERVER['HTTP_CLIENT_IP']))
		return $_SERVER['HTTP_CLIENT_IP'];
	   
	if (!empty($_SERVER['HTTP_X_FORWARDED_FOR']))
		return $_SERVER['HTTP_X_FORWARDED_FOR'];
   
	return $_SERVER['REMOTE_ADDR'];
}

 if (strpos($_GET['target'],'http://localhost/vufind/') === false) {

$getip=getRealIP();

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL,
		"http://localhost/vufind/getType.php?id=".$_GET['oid']);
	curl_setopt($ch, CURLOPT_TIMEOUT, 30);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
	$tipo = curl_exec ($ch);
	//echo $resultado;

curl_setopt($ch, CURLOPT_URL,
		"http://localhost/vufind/getTitle.php?id=".$_GET['oid']);
	curl_setopt($ch, CURLOPT_TIMEOUT, 30);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
	$title = curl_exec ($ch);
	
	
	// Make a MySQL Connection
mysql_connect("localhost", "vufind", "vufind") or die(mysql_error());
mysql_select_db("vufind") or die(mysql_error());
echo ("SELECT ccode FROM geoip WHERE INET_ATON('".$getip."') BETWEEN startipi AND endipi LIMIT 1");
// Get all the data from the "example" table
$result = mysql_query("SELECT ccode FROM geoip WHERE INET_ATON('".$getip."') BETWEEN startipi AND endipi LIMIT 1") 
or die(mysql_error()); 

while($row = mysql_fetch_array( $result )) {
$ccode= $row['ccode'];

}
//Escribe en un archivo, las solicitudes de informacion para obtener una base
$registro = "";

$file = "/registro_click.csv";
$fp = fopen("/usr/local/vufind/web".$file, "a+");

//$registro .= date("l dS of F Y h:i:s A") . "\t";
//$registro .= date("j/n/Y - h:i:s A") . ",";
$registro .= date("Y-m-d\TH:i:s\Z") . ",";
$registro .= $_GET['src'] . ",";
$registro .= $_GET['target'] . ",";
$registro .= $_GET['oid'] . ",";
$registro .= $getip . ",";
$registro .= $ccode . ",";
$registro .= $tipo . ",";
$registro .= $title . "\n";

fwrite($fp,$registro);
fclose($fp);

$con = mysql_connect("localhost","vufind","vufind");
	if (!$con)
	{
	die('Could not connect: ' . mysql_error());
	}
	mysql_select_db("vufind", $con);
	$cadena="INSERT INTO record (ipaddress,fecha,pagina,url,oid,red,type,title,ccode) VALUES('".$getip."','".date("Y-m-d\TH:i:s\Z")."','".$_GET['src']."','".$_GET['target']."','".$_GET['oid']."','".$_GET['oid']."','".$tipo."','".$title."','".$ccode."')";
	echo $cadena."<br>";
	//mysql_query("INSERT INTO archivos (FirstName, LastName, Age) VALUES ($anombre, 'Griffin', '35')");
	//mysql_query($cadena);
	if (!mysql_query($cadena,$con))
	{
		die('Error: ' . mysql_error());
	}
	echo "Se agrego un registro a la base de datos <br>";
	mysql_close($con);
}
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head >
  <meta http-equiv="Content-Type" content="text/html">
  <title>Registro Click</title>
 </head>
<body>
<div id="page">
<h1>Registro Click</h1>
<? echo $registro ?>
<br />
</div> <!--page -->
</body>
</html>

