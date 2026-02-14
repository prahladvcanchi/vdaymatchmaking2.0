package src;

import java.util.Arrays;

public class Person {
    private String name;
    private String email;
    private String thirdPeriod;
    private String contact;
    private int grade;
    private int gender;
    private int[] answers;
    private int orientation;
    private int height;
    private int trait;
    private int ghost;
    private int freeTime;
    private int notice;
    private int friendAboutYou;
    private int stress;
    private int[] pHeight;
    private int pTrait;
    private int pGhost;
    private int pDate;
    private int pApproach;
    private int pAboutThem;
    private int pStress;
    private Person[][] people;

    public Person() {
        
    }

    // Main Constructor
    public Person(String n, String e, String tP, String c, String gr, int g, int[] a, int o,
    int he, int tr, int gh, int fT, int no, int fAY, int st,
    int[] pH, int pT, int pG, int pD, int pA, int pAT,
    int pS) {
        this.name = n;
        this.email = e;
        this.thirdPeriod = tP;
        this.contact = c;
        this.grade = Integer.parseInt(gr);
        this.gender = g;
        this.answers = a;
        this.orientation = o;
        this.people = new Person[2][2];
        this.height=he;
        this.trait=tr;
        this.ghost=gh;
        this.freeTime=fT;
        this.notice=no;
        this.friendAboutYou=fAY;
        this.stress=st;
        this.pHeight=pH;
        this.pTrait=pT;
        this.pGhost=pG;
        this.pDate=pD;
        this.pApproach=pA;
        this.pStress=pS;
        this.pAboutThem=pAT;
    }

    // Getters and Setters

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getThirdPeriod() {
        return this.thirdPeriod;
    }

    public String getContact() {
        return this.contact;
    }

    public int getGrade() {
        return this.grade;
    }

    public int getGender() {
        return this.gender;
    }

    public int[] getAnswers() {
        return this.answers;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTrait() {
        return this.trait;
    }

    public int getGhost() {
        return this.ghost;
    }

    public int getFreeTime() {
        return this.freeTime;
    }

    public int getNotice() {
        return this.notice;
    }

    public int getFriendAboutYou() {
        return this.friendAboutYou;
    }

    public int getStress() {
        return this.stress;
    }

    public int[] getPHeight() {
        return this.pHeight;
    }

    public int getPTrait() {
        return this.pTrait;
    }

    public int getPGhost() {
        return this.pGhost;
    }

    public int getPDate() {
        return this.pDate;
    }
    
    public int getPApproach() {
        return this.pApproach;
    }

    public int getPStress() {
        return this.pStress;
    }

    public int getPAboutThem() {
        return this.pAboutThem;
    }

    public void setAnswers(int[] a) {
        this.answers = a;
    }

    public void setAnswers(int i, int j) {
        this.answers[i] = j;
    }

    public void setPeople(Person[][] p) {
        this.people = p;
    }

    public String toTitleString() {
        return this.name + ", " + this.grade + ", " + this.thirdPeriod;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return grade == person.grade &&
                name.equals(person.name) &&
                email.equals(person.email) &&
                thirdPeriod.equals(person.thirdPeriod) &&
                contact.equals(person.contact) &&
                gender == person.gender &&
                orientation == person.orientation &&
                Arrays.equals(answers, person.answers);
    }

    // Old toString test method
    // public String toString() {
    //     return this.name;
    //     // String fin = this.name + ", " + this.grade + ", ";
    //     // if (this.contact == null || this.contact == "") fin += "No Contact Given";
    //     // else fin += this.contact;
    //     // return fin;
    // }

    // Current toString method used for testing
    public String toString() {
        return name + ";" +
                email + ";" +
                thirdPeriod + ";" +
                contact + ";" +
                grade + ";" +
                gender + ";" +
                orientation + ";" +
                height + ";" +
                trait + ";" +
                ghost + ";" +
                freeTime + ";" +
                notice + ";" +
                friendAboutYou + ";" +
                stress + ";" +
                Arrays.toString(pHeight) + ";" +
                pTrait + ";" +
                pGhost + ";" +
                pDate + ";" +
                pApproach + ";" +
                pAboutThem + ";" +
                pStress + ";" +
                Arrays.toString(answers);
    }
}    