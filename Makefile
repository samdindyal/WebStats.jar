build:
	mkdir -p bin
	chmod +x build.sh
	./build.sh
	echo "Main-Class: WebStats" > Manifest
	jar cfvm WebStats.jar Manifest -C bin  .
clean:
	rm -vf bin/*.class Manifest WebStats.jar
