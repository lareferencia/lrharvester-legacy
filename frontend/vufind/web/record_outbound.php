<?
 
//Escribe en un archivo, las solicitudes de informacion para obtener una base
$registro = "";
$file = "/registro_click.csv";
$fp = fopen("/usr/local/vufind/web".$file, "a+");

//$registro .= date("l dS of F Y h:i:s A") . "\t";
$registro .= date("j/n/Y - h:i:s A") . ",";

$registro .= $_GET['src'] . ",";
$registro .= $_GET['target'] . "\n";

fwrite($fp,$registro);
fclose($fp);

$con = mysql_connect("localhost","vufind","vufind");
		if (!$con)
	{
	die('Could not connect: ' . mysql_error());
	}
	mysql_select_db("vufind", $con);
	$cadena="INSERT INTO record (fecha,pagina,url) VALUES ('".date("j/n/Y - h:i:s A")."','".$_GET['src']."','".$_GET['target']."')";
	echo $cadena."<br>";
	//mysql_query("INSERT INTO archivos (FirstName, LastName, Age) VALUES ($anombre, 'Griffin', '35')");
	//mysql_query($cadena);
	if (!mysql_query($cadena,$con))
	{
		die('Error: ' . mysql_error());
	}
	echo "Se agrego un registro a la base de datos <br>";
	mysql_close($con);

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

