package ISTB_19_2_Pervykh.people;

public class Staff {
    private String name;
    private int age;
    private String profession;
    private String work;

    public Staff(String name, int age, String profession, String work) {
        this.name = name;
        this.age = age;
        this.profession = profession;
        this.work = work;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

}
