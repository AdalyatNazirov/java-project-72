#Makefile
.PHONY: build
run-dist:
	./build/install/app/bin/app
clean:
	./gradlew clean
build:
	./gradlew build
installDist:
	./gradlew installDist
run:
	./gradlew run
check:
	./gradlew check
lint:
	./gradlew checkstyleMain
lintTest:
	./gradlew checkstyleTest
test:
	./gradlew test
report:
	./gradlew jacocoTestReport
build-run:
	build run