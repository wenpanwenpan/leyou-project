package com.leyou.auth.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author 文攀 2019/11/07 15:32
 */
public class EmployeTest {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您要生成的员工个数：");
        //员工数量
        int employeCount = scanner.nextInt();
        //员工数组
        Employee emps[]= new Employee[employeCount];

        //生成指定个数员工
        System.out.println("开始生成员工，员工信息如下：");
        for (int i = 0; i < employeCount; i++) {
            emps[i] = new Employee("wenpan: " + i,100+i,generatorRadom(20,80),generatorRadom(2000,100000));
            System.out.println(emps[i]);
        }

        //是计算年龄还是薪水？
        String ageOrSalary = scanner.next();
        //到底是做什么计算？
        String doWhat = scanner.next();

        //计算员工的年龄，将计算的方法都封装好，用到时直接调用即可
        if(ageOrSalary.equals("age")){
            Double age = calculationWorkAge(emps, doWhat);
            System.out.println("员工的年龄是： " + age);
        }else if(ageOrSalary.equals("salary")){
            Double salary = colculationSalary(emps, doWhat);
            System.out.println("员工的薪水是：" + salary);
        }
    }

    /**
     * 生成指定范围的整数随机数
     * @param min
     * @param max
     * @return
     */
    public static Integer generatorRadom(Integer min,Integer max){

        Random random = new Random();
        int number = random.nextInt(max)%(max-min+1) + min;
        return number;
    }

    /**
     * 计算员工年龄，包括平均年龄、最大、最小年龄。主要看传的参数doWhat是社么
     * 计算员工工龄
     * @param emps  传入员工数组
     * @param doWhat 到底是求最大值 还是最小值 还是平均值
     * @return
     */
    public static Double calculationWorkAge(Employee[] emps,String doWhat){

        Double result = 0.0;
        //将数组转换为集合，方便调用现成的API进行求值
        ArrayList<Employee> employees = new ArrayList<>(Arrays.asList(emps));
        switch (doWhat){
            case "avg":
                //求平均值
                result = employees.stream().collect(Collectors.averagingInt(Employee::getAge));
                break;
            case "max":
                result = employees.stream().mapToDouble(Employee::getAge).max().getAsDouble();
                break;
            case "min":
                result = employees.stream().mapToDouble(Employee::getAge).max().getAsDouble();
                break;
                default:
                    System.out.println("输入数据格式有误， 计算类型只能是ave或min或max,请重新输入.......");
                    break;
        }

        //返回计算结果
        return result;
    }

    /**
     * 计算员工薪资，包括平均薪资、最大、最小薪资。主要看传的参数doWhat是社么
     * @param emps
     * @param doWhat
     * @return
     */
    public static Double colculationSalary(Employee[] emps,String doWhat){

        Double result = 0.0;
        //将数组转换为集合，方便调用现成的API进行求值
        ArrayList<Employee> employees = new ArrayList<>(Arrays.asList(emps));
        switch (doWhat){
            case "avg":
                //求平均值
                result = employees.stream().collect(Collectors.averagingInt(Employee::getSalary));
                break;
            case "max":
                result = employees.stream().mapToDouble(Employee::getSalary).max().getAsDouble();
                break;
            case "min":
                result = employees.stream().mapToDouble(Employee::getSalary).max().getAsDouble();
                break;
                default:
                    System.out.println("输入数据格式有误，计算类型只能是ave或min或max,请重新输入.......");
                    break;
        }

        //返回计算结果
        return result;
    }
}
