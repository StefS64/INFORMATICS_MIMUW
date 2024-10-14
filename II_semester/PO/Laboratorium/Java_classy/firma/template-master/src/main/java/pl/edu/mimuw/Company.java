package pl.edu.mimuw;

public class Company {
    private final Employee[] listOfEmployees = new Employee[100];
    int numberOfEmployees = 0;
    public void addNewEmployee(String name, String surname, int age, String Job){
        listOfEmployees[numberOfEmployees] = new Employee(name, surname, age, Job);
        numberOfEmployees++;
    }
    public void printEmployes() {
        for(int i = 0 ; i < numberOfEmployees; i ++) {
            System.out.println(listOfEmployees[i].getName() +" "+ listOfEmployees[i].getSurname() +" "+ listOfEmployees[i].getAge() +" "+ listOfEmployees[i].getJob());
        }
    }

}
