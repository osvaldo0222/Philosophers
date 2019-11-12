#! /bin/sh

gcc -o src/main/c/PhilosophersServer src/main/c/Philosophers.c -lpthread
gnome-terminal --command='sudo ./src/main/c/PhilosophersServer'