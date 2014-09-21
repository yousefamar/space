JAVAC=javac
#sources = $(wildcard *.java)
#classes = $(sources:.java=.class)

all:
	javac -d bin -sourcepath src -classpath "lib/*" src/core/ParadigmShiftGame.java
#$(classes)

clean :
	find . -name "*.class" -type f -delete

#%.class : %.java
#	$(JAVAC) $<
