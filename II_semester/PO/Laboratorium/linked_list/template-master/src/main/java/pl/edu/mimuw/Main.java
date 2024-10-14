package pl.edu.mimuw;


import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        int numberOfPush = 100000000;
        AbstractStack stack = new DynamicArrayStack();
        AbstractStack stack2 = new LinkedListStack();



        System.out.println("dynamic array stack time:\n push:");
        long startTime = System.nanoTime();

        for (int i = 0; i < numberOfPush; i++) {
            stack.push(123);
        }
        System.out.println(System.nanoTime() - startTime);

        System.out.println("pop:");
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfPush; i++) {
            stack.pop();
        }
        System.out.println(System.nanoTime() - startTime);


        System.out.println("Linked list stack time:\n push:");
        startTime = System.nanoTime();

        for (int i = 0; i < numberOfPush; i++) {
            stack2.push(123);
        }
        System.out.println(System.nanoTime() - startTime);

        System.out.println("pop:");
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfPush; i++) {
            stack2.pop();
        }
        System.out.println(System.nanoTime() - startTime);



        for (int i = 0; i < 7; i++) {
            stack2.push(i+123);
            stack.push(i+123);
        }

        System.out.println(stack2.size()+" "+stack.size());

        for (int i = 0; i < 7; i++) {
            System.out.println(stack2.peek() +" "+stack.peek());
            stack2.pop();
            stack.pop();

        }


    }
}
