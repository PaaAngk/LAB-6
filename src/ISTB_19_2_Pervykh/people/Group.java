package ISTB_19_2_Pervykh.people;

import ISTB_19_2_Pervykh.DBWorker;
import ISTB_19_2_Pervykh.menu.Menu_GUI;

import java.sql.SQLException;
import java.util.ArrayList;

public class Group {
    public static ArrayList<Staff> staff = new ArrayList<Staff>();
    public static ArrayList<Staff> staffSearch = new ArrayList<Staff>();

    // Метод добавления сотрудника
    public static void add (String[] list) throws SQLException {
        DBWorker.addStudent(new Staff(list[1],Integer.parseInt(list[2]),list[0],list[3]));
    }

    // Метод удаление сотрудника
    public static void delete(String name) throws SQLException {
        int i = -1;
        if ((staff.removeIf(nextStaff -> nextStaff.getName().equals(name))) && DBWorker.deleteStudent(name)){
            i++;
        }
        if (i == -1) {
            Menu_GUI.infoBox("Сотрудник не найден");
        }
    }

    // Метод поиска сотрудника
    public static ArrayList<Staff> searchName (String name) {
        int check = 0;
        staffSearch.clear();
        for (Staff stf : staff) {
            if (name.equals(stf.getName())) {
                staffSearch.add(stf);
                check++;
            }
        }
        if (check == 0) {Menu_GUI.infoBox("Сотрудник не найден"); return null;}
        else {
            return staffSearch;
        }
    }

    public static ArrayList<Staff> searchProff (String proff) {
        int check = 0;
        staffSearch.clear();
        for (Staff stf : staff) {
            if (proff.equals(stf.getProfession())) {
                staffSearch.add(stf);
                check++;
            }
        }
        if (check == 0) {Menu_GUI.infoBox("Сотрудник не найден"); return null;}
        else {
            return staffSearch;
        }
    }

    public static String[] getWorkStaff(Staff staff) throws SQLException {
        int workId = DBWorker.getWorkID(staff);
        return DBWorker.getDuty(workId);
    }


    public int getSize(){
        return staff.size();
    }

    public static Staff getStaff(int index){
        return staff.get(index);
    }

}