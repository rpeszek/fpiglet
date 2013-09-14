VERSION=0.0.1
JAR=gruesome-${VERSION}.jar

jar: Gruesome.groovy README.md
	jar cf ${JAR} Gruesome.groovy README.md

install: jar
	echo "\n# Gruesome" >> ~/.profile
	echo "export CLASSPATH=\$$CLASSPATH:${CURDIR}/${JAR}" >> ~/.profile

clean:
	-rm ${JAR}