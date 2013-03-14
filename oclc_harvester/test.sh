#! /bin/tcsh -f
java -classpath .:log4j-1.2.12.jar:harvester2.jar:xalan.jar ORG.oclc.oai.harvester2.app.RawWrite http://alcme.oclc.org/oaicat/OAIHandler
