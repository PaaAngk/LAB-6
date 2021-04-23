package ISTB_19_2_Pervykh;

import ISTB_19_2_Pervykh.people.Group;
import ISTB_19_2_Pervykh.people.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBWorker {
    public static final String PATH_TO_DB_FILE = "database.db";
    public static final String URL = "jdbc:sqlite:" + PATH_TO_DB_FILE;
    public static Connection conn;

    public static void initDB() {
        try {
            conn = DriverManager.getConnection(URL);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Драйвер: " + meta.getDriverName());
                //createDB();
                DBWorker.getAllStudents();
            }
        } catch (SQLException ex) {
            System.out.println("Ошибка подключения к БД: " + ex);
        }
    }

    public static void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public static void createDB() throws SQLException {
        Statement statmt = conn.createStatement();
        statmt.execute("CREATE TABLE if not exists 'Profession' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'title' text);");
        System.out.println("Таблица создана или уже существует.");
        statmt.execute("CREATE TABLE if not exists 'Duty' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'title' text, 'rate' text);");
        System.out.println("Таблица создана или уже существует.");
        statmt.execute("CREATE TABLE if not exists 'Staff' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'age' INTEGER, 'Profession_id' INTEGER NOT NULL, 'Duty_id' INTEGER NOT NULL, FOREIGN KEY (Profession_id) REFERENCES Profession (id), FOREIGN KEY (Duty_id) REFERENCES Duty (id));");
        System.out.println("Таблица создана или уже существует.");
    }

    public static void addProfession(String title) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Profession(`title`) " +
                        "VALUES(?)");
        statement.setString(1, title);
        statement.execute();
        statement.close();
    }

    public static ArrayList<String> getAllProfession() throws SQLException {
        ArrayList<String> allProfession = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id, title FROM Profession");
        while (resultSet.next()) {
            allProfession.add(resultSet.getString("title"));
            System.out.println(resultSet.getInt("id") + " " + resultSet.getString("title"));
        }
        resultSet.close();
        statement.close();
        return allProfession;
    }

    public static int getProfessionId(String profTitle) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id FROM Profession WHERE Profession.title ='"+profTitle+"'");
        int profId = -1;
        profId = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return profId;
    }

    public static String getProfessionName(int profId) throws SQLException {
        String profTitle = "";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT title FROM Profession WHERE Profession.id ='"+profId+"'");
        profTitle = resultSet.getString(1);
        resultSet.close();
        statement.close();
        return profTitle;
    }

    public static void addDuty(String title, String rate) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Duty(title,rate) " +
                        "VALUES(?,?)");
        statement.setString(1, title);
        statement.setString(2, rate);
        statement.execute();
        statement.close();
    }

    public static int getDutyId(String dutyTitle) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id FROM Duty WHERE Duty.title ='"+dutyTitle+"'");
        int dutyId = -1;
        dutyId = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return dutyId;
    }

    public static String getDutyName(int dutyId) throws SQLException {
        String dutyTitle = "";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT title FROM Duty WHERE Duty.id ='"+dutyId+"'");
        dutyTitle = resultSet.getString(1);
        resultSet.close();
        statement.close();
        return dutyTitle;
    }
    public static String[] getDuty(int dutyId) throws SQLException {
        String[] dutyTitle = new String[2];
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT title, rate FROM Duty WHERE Duty.id ='"+dutyId+"'");
        dutyTitle[0] = resultSet.getString(1);
        dutyTitle[1] = resultSet.getString(2);
        resultSet.close();
        statement.close();
        return dutyTitle;
    }

    public static int getWorkID(Staff staff) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT Duty_id FROM Staff WHERE Staff.name ='"+staff.getName()+"' AND Staff.age ='"+staff.getAge()+"'");
        int workId = resultSet.getInt(1);
        resultSet.close();
        statement.close();
        return workId;
    }

    public static ArrayList<String> getAllDuty() throws SQLException {
        ArrayList<String> allWork = new ArrayList<String>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id, title, rate FROM Duty");
        while (resultSet.next()) {
            allWork.add(resultSet.getString("title"));
        }
        resultSet.close();
        statement.close();
        return allWork;
    }

    public static void addStudent(Staff staff) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Staff(name,age,Profession_id,Duty_id) " +
                        "VALUES(?,?,?,?)");
        statement.setObject(1, staff.getName());
        statement.setObject(2, staff.getAge());
        statement.setObject(3, getProfessionId(staff.getProfession()));
        statement.setObject(4, getDutyId(staff.getWork()));
        statement.execute();
        statement.close();
    }

    public static void getAllStudents() throws SQLException {
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT Staff.name, Staff.age, Staff.Profession_id, Staff.Duty_id, Profession.title, Duty.title, Duty.rate FROM Staff JOIN Profession,Duty ON Profession.id = Staff.Profession_id AND Duty.id = Staff.Duty_id");
            Group.staff.clear();
            while (resultSet.next()) {
                Group.staff.add(new Staff(resultSet.getString("name"), resultSet.getInt("age"), getProfessionName(resultSet.getInt("Profession_id")), getDutyName(resultSet.getInt("Duty_id")) ));
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Если произошла ошибка - возвращаем пустую коллекцию
        }
    }

    public static boolean deleteStudent(String name) throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("DELETE FROM Staff WHERE Staff.name ='"+name+"'");
        statement.close();
        return true;
    }
}
