./gradlew copyJar

cd debug_server

sudo archlinux-java set liberica-jdk-25-full

java -jar server.jar nogui

sudo archlinux-java set java-21-openjdk

cd ..