/*
 * Table.java
 * Version 1
 * @author Andy Li
 * March 1, 2020
 * Creates a student object
 */

//import statements
import javax.swing.text.Utilities;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Table {

    //Variables
    private int size;
    private ArrayList<Student> students;
    private int x;
    private int y;
    private int r;

    //Constructors

    /**
     * Table
     * This constructor creates a table of students
     * @param //An integer representing the capacity of the table
     */
    public Table(int size) {
        this.size = size;
        this.students = new ArrayList<Student>();
    }

    //Methods

    public int getRadius() {
        return r;
    }

    public void setRadius(int r) {
        this.r = r;
    }

    public int getNumSeated() {
        return students.size();
    }

    public void draw (Graphics g, Color c ) {
        int tableR = (int) (r * 0.8);
        int sR = (r - tableR) / 2;

        double angle = 2 * Math.PI / this.getNumSeated();

        if (x != 0 && y != 0) {
            Utility.drawCenteredCircle(g, x, y, tableR, c);

            for (int i = 0; i < this.getNumSeated(); i++) {
                double theta = angle + i;

                int sX = (int) ((tableR + sR) * Math.sin(theta) + x);
                int sY = (int) (y - (tableR + sR) * Math.cos(theta));

                Utility.drawCenteredCircle(g, sX, sY, sR, c);
                //System.out.println("Size: " + students.get(i).getAccommodations().size());
                if (students.get(i).getAccommodations().size() > 0) {
                    Utility.drawCenteredTextBox(g, sX, sY - sR - 3, students.get(i).getName(), Color.CYAN);
                } else {
                    Utility.drawCenteredTextBox(g, sX, sY - sR - 3, students.get(i).getName(), Color.WHITE);
                }
            }
        }

    }


    /**
     * getSize
     * This method gets the size of the table
     * @return An integer representing the capacity of the table
     */
    public int getSize() {
        return this.size;
    }

    /**
     * addStudent
     * This method adds a student to the table
     * @param //A student representing the new member of the table
     */
    public void addStudent(Student s) {
        this.students.add(s);
    }

    /**
     * removeStudent
     * This method removes a student from the table
     * @param //A student representing the student to be removed
     */
    public void removeStudent(Student s) {
        students.remove(s);
    }

    /**
     * getStudent
     * This method gets the students of the table
     * @return An arraylist of students representing the students of the table
     */
    public ArrayList<Student> getStudents() {
        return this.students;
    }

    /**
     * setStudents
     * This method sets the students of the table
     * @param //An arraylist of students representing the students of the table
     */
    public void setStudents(ArrayList<Student>students) {
        this.students = students;
    }

    /**
     * isFull
     * This method checks if the table is full
     * @return A boolean representing if the table is full or not
     */
    public boolean isFull() {
        return this.students.size() == this.size;
    }

    /**
     * containsStudent
     * This method checks if the table contains the given student
     * @param //A student representing the given student
     * @return A boolean representing if the table contains the given student or not
     */
    public boolean containsStudent(Student s) {
        return this.students.contains(s);
    }

    /**
     * getX
     * This method gets the x position of the table
     * @return An integer representing the x position of the table
     */
    public int getX() {
        return this.x;
    }

    /**
     * getY
     * This method gets the y position of the table
     * @return An integer representing the y position of the table
     */
    public int getY() {
        return this.y;
    }

    /**
     * setX
     * This method sets the x position of the table
     * @param //An integer representing the x position of the table
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * setY
     * This method sets the y position of the table
     * @param //An integer representing the y position of the table
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * toString()
     * This method returns a string of the formatted info of the table
     * @return A string, representing the table's information
     */
    @Override
    public String toString() {
        String out = "{";
        int i = 0;
        for (final Student s : this.students) {
            out += s.toString();
            if (i < this.size - 1) {
                out += ", ";
            }
            ++i;
        }
        for (int j = i; j < this.size; ++j) {
            out += "empty";
            if (i < this.size - 1) {
                out += ", ";
            }
        }
        out += "}";
        return out;
    }
}
