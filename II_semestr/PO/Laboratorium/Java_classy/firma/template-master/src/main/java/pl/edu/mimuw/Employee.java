package pl.edu.mimuw;

import javax.swing.text.html.parser.TagElement;

public class Employee {
    private String name;
    private String surname;
    private int age;
    private String job;

    public Employee(String name, String surname,int age, String job){
        this.name = name;//TUTAJ 'this' żeby nie było problemu z wiadmością co wrzucić
        this.age = age;
        this.job = job;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public String getJob() {
        return job;
    }
}
