export CLASSPATH=.:/home/rango/ITU/log/jar/servlet-api.jar
source ~/.bashrc
cd frakawork
javac -parameters -d . *.java 
jar -cvf ../company/WEB-INF/lib/etu1757.jar etu1757
cd ../company/WEB-INF/classes/
cp /home/rango/ITU/log/jar/postgresql-42.5.0.jar ../lib/
export CLASSPATH=.:/home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/webapps/company/WEB-INF/lib/etu1757.jar
source ~/.bashrc
javac -parameters -d . *.java 
cd ../../
jar -cvf ./company.war .
cp ./company.war /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/webapps/
sudo /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/bin/shutdown.sh
sudo /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/bin/startup.sh

