export CLASSPATH=.:/home/rango/ITU/log/jar/servlet-api.jar
source ~/.bashrc
cd frakawork
javac -d . *.java 
jar -cvf ../company/WEB-INF/lib/etu1757.jar etu1757
cd ../company/WEB-INF/classes/
export CLASSPATH=.:/home/rango/ITU/codeFramework/company/WEB-INF/lib/etu1757.jar
javac -d . *.java 
cd ../../
jar -cvf ./company.war .
cp ./company.war /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/webapps/
sudo /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/bin/shutdown.sh
sudo /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/bin/startup.sh

