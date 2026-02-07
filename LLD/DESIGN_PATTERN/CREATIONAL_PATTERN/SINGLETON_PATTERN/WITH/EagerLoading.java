package LLD.DESIGN_PATTERN.CREATIONAL_PATTERN.SINGLETON_PATTERN.WITH;
// Eager Initialization means we upfront 
// 1.) using static variable
class Employee{
    // It is thread safe as below line executes during class load time 
    // so once class is loaded then it has pre seeded instance of the class
    // Its just because of static
    public static final Employee employeeInstance = new Employee();
    private Employee(){}
}

// 2) using static method

class Teacher{
    private static final Teacher teacherInstance = new Teacher();
    private Teacher(){}
    public static Teacher getInstance(){
        return teacherInstance;
    }
}

public class EagerLoading {
    public static void main(String[] args) {
        Employee employee = Employee.employeeInstance;
        Employee employee1 = Employee.employeeInstance;
        Employee employee2 = Employee.employeeInstance;
        System.out.println(employee);
        System.out.println(employee1);
        System.out.println(employee2);

        Teacher teacher = Teacher.getInstance();
        Teacher teacher1 = Teacher.getInstance();
        Teacher teacher2 = Teacher.getInstance();
        System.out.println(teacher);
        System.out.println(teacher1);
        System.out.println(teacher2);
    }
}



