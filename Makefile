# Makefile. 
# To compile all java source files, type:
#	make

LIBRARIES = -classpath /WEB-INF/lib/ojdbc6.jar

all:
	# TODO i.e.: javac Person.java $(LIBRARIES) 

clean:
	rm -rf /WEB-INF/classes/*.class
