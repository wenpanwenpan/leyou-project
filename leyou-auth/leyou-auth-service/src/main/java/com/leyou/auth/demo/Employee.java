package com.leyou.auth.demo;

/**
 * description
 *
 * @author 文攀 2019/11/07 15:33
 */
public class Employee {

    String name;
    int number;
    int age;
    int salary;

    public Employee() {
    }

    public Employee(String name, int number, int age, int salary) {
        this.name = name;
        this.number = number;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
