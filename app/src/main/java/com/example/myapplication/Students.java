package com.example.myapplication;

public class Students {


    String Name;
    String Roll;
    String Branch;
    String Semester;
    int Present;
    int Absent;
    int nsoAbsent;

    public Students(String Name, String Roll, String Branch, String Semester, int Absent, int Present, int nsoAbsent) {
        this.Name = Name;
        this.Roll = Roll;
        this.Branch = Branch;
        this.Semester = Semester;
        this.Present = Present;
        this.Absent = Absent;
        this.nsoAbsent = nsoAbsent;

    }

    public Students() {

    }

    public String getName() {
        return Name;
    }

    public String getRoll() {
        return Roll;
    }

    public String getBranch() {
        return Branch;
    }

    public String getSemester() {
        return Semester;
    }

    public int getPresent() {
        return Present;
    }

    public int getAbsent() {
        return Absent;
    }

    public int getNsoAbsent() {
        return nsoAbsent;
    }

    public void setTPresent(int TPresent) {
        this.Present = TPresent;
    }


}
