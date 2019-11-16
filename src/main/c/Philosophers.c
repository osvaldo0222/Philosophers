/*
 *  This code is based on Dining Philosopher Problem Using Semaphores on GeeksForGeeks, Author: Subham Biswas
 *  URL: https://www.geeksforgeeks.org/dining-philosopher-problem-using-semaphores/
*/
#include "Socket.c"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h> 
#include <semaphore.h> 

#define EATING 0 
#define HUNGRY 1 
#define THINKING 2 
#define LEFT (phnum + (N - 1)) % N
#define RIGHT (phnum + 1) % N 
#define LAMBDA 0.1

int N = 0;
int* state;
int* phil;
int philEating = 0;
int philThinking = 0; 
int philHungry = 0;
int socketDes = -1;
  
sem_t mutex; 
sem_t* S;

double getTimeExponential(){
    double probability = ((double)rand())/(double)RAND_MAX;
    probability = (probability >= 1)?0.999:probability;
    probability = (probability <= 0)?0.001:probability;
    double time = -1.0 * (log(1 - probability)/(double)LAMBDA);
    return time;
}
  
void test(int phnum) { 
    if (state[phnum] == HUNGRY && state[LEFT] != EATING && state[RIGHT] != EATING) { 
        // state that eating 
        state[phnum] = EATING;
        philHungry--;
        philEating++; 
        printf("Philosopher %d takes fork %d and %d\n", phnum + 1, LEFT + 1, phnum + 1); 
        printf("Philosopher %d is Eating\n", phnum + 1); 
        sleep(getTimeExponential()); 
        // sem_post(&S[phnum]) has no effect 
        // during takefork 
        // used to wake up hungry philosophers 
        // during putfork 
        sem_post(&S[phnum]); 
    } 
} 
  
// take up chopsticks 
void take_fork(int phnum) { 
    sem_wait(&mutex); 
    // state that hungry 
    state[phnum] = HUNGRY; 
    philThinking--;
    philHungry++;
    printf("Philosopher %d is Hungry\n", phnum + 1); 
    // eat if neighbours are not eating 
    test(phnum); 
    sem_post(&mutex); 
    // if unable to eat wait to be signalled 
    sem_wait(&S[phnum]); 
    sleep(getTimeExponential()); 
} 
  
// put down chopsticks 
void put_fork(int phnum) { 
    sem_wait(&mutex); 
    // state that thinking 
    state[phnum] = THINKING; 
    philEating--;
    philThinking++;
    printf("Philosopher %d putting fork %d and %d down\n", phnum + 1, LEFT + 1, phnum + 1); 
    printf("Philosopher %d is thinking\n", phnum + 1); 
    test(LEFT); 
    test(RIGHT); 
    sem_post(&mutex); 
} 
  
void* philospher(void* num){ 
    while (1) { 
        int* i = num; 
        sleep(getTimeExponential()); 
        take_fork(*i); 
        sleep(0); 
        put_fork(*i); 
    } 
} 

void initProgram(){
    socketDes = initSocket();
    char buffer[10];
    struct sockaddr client;
    int client_size;
    
    printf("Waiting for the client...\n");
    int descriptor_client = accept(socketDes, &client, &client_size);
    if (descriptor_client == -1) {
        printf("Error while to handshaked to client...\n");
        sleep(2);
        exit(EXIT_FAILURE);
    } else {
        printf("Client connected!\n");
        read(descriptor_client , buffer, sizeof(buffer));
    }
    N = atoi(buffer);
    state = (int*)malloc(sizeof(int) * N);
    phil = (int*)malloc(sizeof(int) * N);
    S = (sem_t*)malloc(sizeof(sem_t) * N);
    philThinking = N;
    close(descriptor_client);
}

void* listenClients(){
    struct sockaddr client;
    int descriptor_client;
    int client_size;

    while(1) {
        descriptor_client = accept(socketDes, &client, &client_size);
        if (descriptor_client == -1) {
            printf ("Error while to handshaked to client...\n");
        } else {
            char message[1024];
           // sem_wait(&mutex);
            int writed = sprintf(message, "NPhil: %d, SEating: %d, SHungry: %d, SThinking: %d, CEating: %d, CHungry: %d, CThinking: %d", N, EATING, HUNGRY, THINKING, philEating, philHungry, philThinking);
            for (int i = 0; i < N; i++) {
                writed += sprintf(&message[writed], ", %d: %d", phil[i], state[i]); 
            }
            //sem_post(&mutex);
            send(descriptor_client, message, writed, 0);
            close(descriptor_client);
        }
    }
}

int main(void){
    srand(time(NULL));
    initProgram();
    pthread_t thread_clients;
    pthread_t thread_id[N]; 
    
    // initialize the semaphores 
    sem_init(&mutex, 0, 1); 
  
    for (int i = 0; i < N; i++) 
        sem_init(&S[i], 0, 0);

    for (int i = 0; i < N; i++) { 
        //create philosopher processes
        state[i] = THINKING;
        phil[i] = i; 
        pthread_create(&thread_id[i], NULL, philospher, &phil[i]); 
        printf("Philosopher %d is thinking\n", i + 1);
    } 

    pthread_create(&thread_clients, NULL, listenClients, NULL);

    for (int i = 0; i < N; i++) 
        pthread_join(thread_id[i], NULL);
} 