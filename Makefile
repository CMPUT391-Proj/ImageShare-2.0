# Makefile. 
# To compile all java source files, type:
#	make

LIBRARIES = -classpath /WEB-INF/lib/ojdbc6.jar:/WEB-INF/lib/servlet-api-2.3.jar:/WEB-INF/lib/commons-fileupload-1.3.1.jar

all:
	# TODO i.e.: javac Person.java $(LIBRARIES) 

clean:
	rm -rf /WEB-INF/classes/*.class
