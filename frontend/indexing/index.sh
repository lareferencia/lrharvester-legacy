#! /bin/sh
java -Xms256m -Xmx512m -classpath .:./lib/*:lareferencia-backend-1.0.0.CI-SNAPSHOT.jar  org.lareferencia.backend.OfflineIndexerByNetwork $1 $2 $3

