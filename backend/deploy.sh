mvn clean package
sudo /etc/init.d/tomcat7 stop
sudo cp target/backend-2.0.war /var/lib/tomcat7/webapps/backend.war
sudo rm -r /var/lib/tomcat7/webapps/backend
sudo /etc/init.d/tomcat7 start
