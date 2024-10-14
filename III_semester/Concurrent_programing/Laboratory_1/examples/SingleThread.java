package lab01.examples;

public class SingleThread {
    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        t.setName("My main");
        System.out.println("Thread: " + t);
        System.out.println("Name: " + t.getName());
        System.out.println("Priority: " + t.getPriority());
        System.out.println("Group: " + t.getThreadGroup().getName());
    }
}
