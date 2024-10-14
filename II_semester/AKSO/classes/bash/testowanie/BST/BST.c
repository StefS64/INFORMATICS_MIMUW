#include <stdio.h>
#include <stdlib.h>

struct Node;
typedef struct Node* Tree;

struct Node{
   int value;
   Tree left, right;
}Node;



void insert(Tree *treePtr, int x){
   if(*treePtr == NULL){
      *treePtr = (Tree)malloc(sizeof(struct Node));
      (*treePtr)->value = x;
      (*treePtr)->left = NULL;
      (*treePtr)->right = NULL;
   } else if (x < (*treePtr)->value) {
      insert(&((*treePtr)->left), x);
   } else if (x > (*treePtr)->value) {
      insert(&((*treePtr)->right), x);
   }
}

void printAll(Tree t){
   if(t != NULL){
      printAll(t->left);
      printf("%d", t->value);
      printAll(t->right);
   }
}

void removeAll(Tree t){
   if( t != NULL){
      removeAll(t->left);
      removeAll(t->right);
   } else {
      free(t);
   }
}


int main(){
   Tree tree = NULL;
   int n;
   while(scanf("%d", &n) == 1){
      insert(&tree, n);
   }
   printAll(tree);
   removeAll(tree);
   return 0;   
}