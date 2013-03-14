#! /bin/sh
java -Xms64m -Xmx128m -classpath .:./lib/*:lareferencia-0.jar org.oclc.oai.harvester2.app.LaReferenciaProtoHarvester $1 $2 $3

