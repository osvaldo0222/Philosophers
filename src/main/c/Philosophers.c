/*
 *  This code is based on Dining Philosopher Problem Using Semaphores on GeeksForGeeks, Author: Subham Biswas
 *  URL: https://www.geeksforgeeks.org/dining-philosopher-problem-using-semaphores/
*/
#include "Socket.c"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h> 
#include <semaphore.h> 
#include <string.h>
  
#define N 11
#define EATING 0 
#define HUNGRY 1 
#define THINKING 2 
#define LEFT (phnum + (N - 1)) % N
#define RIGHT (phnum + 1) % N 
#define TIME_THINKING 5 //Seconds
#define TIME_EATING 10

int state[N];
int phil[N];
int philEating = 0;
int philThinking = N; 
int philHungry = 0;
int socketDes = -1;
  
sem_t mutex; 
sem_t S[N]; 
  
void test(int phnum) { 
    if (state[phnum] == HUNGRY && state[LEFT] != EATING && state[RIGHT] != EATING) { 
        // state that eating 
        state[phnum] = EATING;
        philHungry--;
        philEating++; 
        printf("Philosopher %d takes fork %d and %d\n", phnum + 1, LEFT + 1, phnum + 1); 
        printf("Philosopher %d is Eating\n", phnum + 1); 
        sleep(TIME_EATING); 
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
    sleep(TIME_EATING); 
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
        sleep(TIME_THINKING); 
        take_fork(*i); 
        sleep(0); 
        put_fork(*i); 
    } 
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
            int writed = sprintf(message, "NPhil: %d, SEating: %d, SHungry: %d, SThinking: %d, TThinking: %d, TEating: %d, CEating: %d, CHungry: %d, CThinking: %d", N, EATING, HUNGRY, THINKING, TIME_THINKING, TIME_EATING, philEating, philHungry, philThinking);
            for (int i = 0; i < N; i++) {
                writed += sprintf(&message[writed], ", %d: %d", phil[i], state[i]); 
            }
            send(descriptor_client, message, writed, 0);
            close(descriptor_client);
        }
    }
}

int main(void){ 
    int i; 
    pthread_t thread_clients;
    pthread_t thread_id[N]; 
    socketDes = initSocket();

    // initialize the semaphores 
    sem_init(&mutex, 0, 1); 
  
    for (i = 0; i < N; i++) 
        sem_init(&S[i], 0, 0); 

    for (i = 0; i < N; i++) { 
        //create philosopher processes 
        state[i] = THINKING;
        phil[i] = i;
        pthread_create(&thread_id[i], NULL, philospher, &phil[i]); 
        printf("Philosopher %d is thinking\n", i + 1); 
    } 
  
    pthread_create(&thread_clients, NULL, listenClients, NULL);

    for (i = 0; i < N; i++) 
        pthread_join(thread_id[i], NULL);
} 