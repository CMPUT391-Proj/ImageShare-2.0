# Makefile. 
# To compile all java source files, type:
#	make

#LIBRARIES = -classpath /WEB-INF/lib/ojdbc6.jar:/WEB-INF/lib/servlet-api-2.3.jar:/WEB-INF/lib/commons-#fileupload-1.3.1.jar
#SOURCE = -sourcepath src/:model:/oraclehandler
#DEST = -d WEB-INF/classes/

all:
	cp -r src/bin/imageshare WEB-INF/classes/imageshare

clean:
	rm -r WEB-INF/classes/imageshare
