package Students;

import java.util.Scanner;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        //创建集合对象，用于存储学生数据

        //不让程序结束
        while (true) {
            System.out.println("-----欢迎来到学生管理系统-----");
            System.out.println("1.增加");
            System.out.println("2.打印某班级全部学生");
            System.out.println("3.查询");
            System.out.println("4.更改");
            System.out.println("5.删除学生信息");
            System.out.println("6.统计信息: 打印当前在读学生数量");
            System.out.println("7.统计信息: 打印某班级学生数量");
            System.out.println("请输入你的选择");
            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    addStudents();
                    break;
                case "2":
                    callPrintStudentsByClass();
                    break;
                case "3":
                    callFindStudents();
                    break;
                case "4":
                    callupdateStudents();
                    break;
                case "5":
                    callDeleteStudent();
                    break;
                case "6":
                    callcountAll();
                    break;
                case "7":
                    callcountClass();
                    break;
                default:
                    System.exit(0);
                    break;
            }
        }
    }

    public static void callcountClass() {
        Connection c;
        Statement stmt;
        System.out.println("请输入班级号");
        Scanner sc = new Scanner(System.in);
        String classNum = sc.nextLine();
        int sum = 0;
        try {
            c = connectDataBase();
            if (c == null) throw new NullPointerException("Connect failed");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students");
            while (rs.next()) {
                String id = rs.getString("id");
                if (id.substring(6, 8).equals(classNum)) {
                    sum++;
                }
            }
            System.out.println("班级号为" + classNum + "的班级中, 学生数量为: " + sum);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Opened database UNsuccessfully");
        }

    }

    public static void callcountAll() {
        Connection c;
        Statement stmt;
        int sum = 0;
        try {
            c = connectDataBase();
            if (c == null) throw new NullPointerException("Connect failed");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students");
            while (rs.next()) {
                sum++;
            }
            System.out.println("全校在读学生数量为: " + sum);
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Opened database UNsuccessfully");
        }
    }

    public static void callDeleteStudent() {
        Connection c;
        Statement stmt;
        System.out.println("请输入需要修改信息的学生学号");
        try {
            Scanner sc = new Scanner(System.in);
            String ID = sc.nextLine();

            c = connectDataBase();
            if (c == null) throw new NullPointerException("Connect failed");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students WHERE ID = " + ID);
            if (rs.next()) {
                System.out.println("已查询到该学生, 请输入1以确定删除信息");
                Scanner sc1 = new Scanner(System.in);
                String confirm = sc.nextLine();
                if (confirm.equals("1")) {
                    String query = "DELETE FROM Students WHERE ID = ?";
                    PreparedStatement preparedStmt = c.prepareStatement(query);
                    preparedStmt.setString(1, ID);
                    preparedStmt.executeUpdate();
                    c.close();
                    System.out.println("删除学生信息成功！");
                    return;
                } else {
                    System.out.println("已取消删除操作");
                    return;
                }

            } else {
                System.out.println("查无此人");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Opened database UNsuccessfully");
            return;
        }
        System.out.println("删除成功");

    }

    public static void callupdateStudents() {
        Connection c;
        Statement stmt;
        System.out.println("请输入需要修改信息的学生学号");
        try {
            Scanner sc = new Scanner(System.in);
            String ID = sc.nextLine();

            c = connectDataBase();
            if (c == null) throw new NullPointerException("Connect failed");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students WHERE ID = " + ID);
            if (rs.next()) {
                System.out.println("请输入需要修改后的学生姓名");
                Scanner sc2 = new Scanner(System.in);
                String newName = sc2.nextLine();
                System.out.println("请输入需要修改后的学生学院");
                Scanner sc3 = new Scanner(System.in);
                String newSchool = sc3.nextLine();
                System.out.println("请输入需要修改后的学生性别");
                Scanner sc4 = new Scanner(System.in);
                String newSex = sc4.nextLine();
                String query = "UPDATE Students SET NAME = ?, SCHOOL = ?,SEX=? WHERE ID = ?";
                PreparedStatement preparedStmt = c.prepareStatement(query);
                preparedStmt.setString(1, newName);
                preparedStmt.setString(2, newSchool);
                preparedStmt.setString(3, newSex);
                preparedStmt.setString(4, ID);

                // execute the java preparedstatement
                preparedStmt.executeUpdate();
                c.close();
                System.out.println("更新学生信息成功！");
            } else {
                System.out.println("查无此人");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Opened database UNsuccessfully");
            return;
        }
        System.out.println("更改成功");

    }

    public static void callPrintStudentsByClass() {
        Connection c;
        Statement stmt;
        System.out.println("请输入班级号");
        Scanner sc = new Scanner(System.in);
        String classNum = sc.nextLine();
        try {
            c = connectDataBase();
            if (c == null) throw new NullPointerException("Connect failed");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students");
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String school = rs.getString("school");
                String sex = rs.getString("sex");

                if (id.substring(6, 8).equals(classNum)) {
                    System.out.println("ID = " + id);
                    System.out.println("NAME = " + name);
                    System.out.println("SCHOOL = " + school);
                    System.out.println("SEX = " + sex);
                    System.out.println();

                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Opened database UNsuccessfully");
        }
    }

    public static Connection connectDataBase() {
        Connection c;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:main.sqlite3");
            System.out.println("Opened database successfully");
            return c;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Opened database UNsuccessfully");
            return null;
        }
    }

    public static boolean findStudents(String ID, boolean print) {
        Connection c;
        Statement stmt;
        try {
            c = connectDataBase();
            if (c == null) throw new NullPointerException("Connect failed");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Students WHERE ID = " + ID);
            if (rs.next()) {
                if (print) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    String school = rs.getString("school");
                    String sex = rs.getString("sex");
                    System.out.println("ID = " + id);
                    System.out.println("NAME = " + name);
                    System.out.println("SCHOOL = " + school);
                    System.out.println("SEX = " + sex);
                    System.out.println();
                    c.close();

                    return true;
                } else {
                    c.close();

                    return true;
                }
            } else {
                c.close();

                return false;
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("Opened database UNsuccessfully");
            return false;
        }
    }

    public static void callFindStudents() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入学生学号：");
            String ID = sc.nextLine();
            boolean exit = findStudents(ID, true);
            if (exit) {
                return;
            } else {
                System.out.println("学号不存在请重新输入");
            }
        }
    }

    public static void addStudents() {
        Scanner sc = new Scanner(System.in);
        Connection c;

        String newID;
        try {
            c = connectDataBase();
            if (c == null) throw new NullPointerException("Connect failed");
            c.setAutoCommit(false);
            while (true) {
                System.out.println("请输入学生学号：");
                //String Sno=sc.nextLine();
                newID = sc.nextLine();

                //定义标记
                boolean exit = findStudents(newID, false);
                if (exit) {
                    System.out.println("该学号已存在，请重新输入");
                } else {
                    break;
                }
            }
            System.out.println("请输入学生姓名：");
            String newName = sc.nextLine();
            System.out.println("请输入学生学院：");
            String newSchool = sc.nextLine();
            System.out.println("请输入学生性别：");
            String sex = sc.nextLine();
            PreparedStatement stm = c.prepareStatement("INSERT INTO Students (ID,NAME,SCHOOL,SEX) VALUES(?,?,?,?)");
            stm.setString(1, newID);
            stm.setString(2, newName);
            stm.setString(3, newSchool);
            stm.setString(4, sex);
            stm.executeUpdate();
            c.commit();
            c.close();
            System.out.println("添加成功");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("addStudents UNsuccessfully");
            System.exit(0);
        }
    }
}


