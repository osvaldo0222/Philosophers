#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

#define PORT 4567

int initSocket(){
    //Opening the socket (the endpoint)
    //File descriptor
    //socket return -1 if was an error
    int socketDescriptor = socket(AF_INET, SOCK_STREAM, 0);
    if (socketDescriptor == -1) {
        printf("Error while opening the socket. Server shutting down...\n");
        sleep(2);
        exit(EXIT_FAILURE);
    } else {
        printf("Server is active\n");
    }

    //Structure describing an Internet socket address.
    struct sockaddr_in direction;
    direction.sin_family = AF_INET;
    direction.sin_port = htons(PORT);
    direction.sin_addr.s_addr = INADDR_ANY;

    if (bind(socketDescriptor, (struct sockaddr *)&direction, sizeof (direction)) == -1) {
        printf ("Address or port error, may be used by other socket or service. Try Again!\nServer shutting down...\n");
        close(socketDescriptor);
        sleep(2);
        exit(EXIT_FAILURE);
    } else {
        printf ("Service is active on port %d\n", PORT);
    }

    if (listen(socketDescriptor, 5) == -1) {
        printf ("Error while preparing to accept connections. Server shutting down...\n");
        close(socketDescriptor);
        sleep(2);
        exit(EXIT_FAILURE);
    }
    return socketDescriptor;
}

void closeSocket(int socketDescriptor) {
    if(close(socketDescriptor)== -1) {
        printf("Error trying to close socket...\n");
    } else {
        printf("Socket closed...\n");
    }
}