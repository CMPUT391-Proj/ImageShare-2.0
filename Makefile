# Makefile. 
# To compile all java source files, type:
#	make all

all:
	  cd src && ant build;
	  cp -r src/classes/imageshare WEB-INF/classes/imageshare

clean:
	  rm -rf WEB-INF/classes/imageshare
	  rm -rf src/classes
