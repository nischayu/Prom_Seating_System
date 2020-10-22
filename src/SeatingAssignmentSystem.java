/**
 * SeatingAssigmentSystem.java
 * Version 1
 * @author Andy Li
 * February 23, 2020
 * This class is used to assign students to tables for PROM based on their partner preferences.
 */

//Imports
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

class SeatingAssignmentSystem {

    //Global Variables
    private static ArrayList<Student> students;
    private static ArrayList<Table> tables;
    private static ArrayList<Integer> happiness;
    private static final int TIME_LIMIT = 30;

    /**
     * assignTables
     * This method assigns the students to tables by maximizing the mean happiness of the stundets.
     * For each student, if one of their chosen partners is at their table, the total happiness increases by 1.
     * @param //An arraylist of students representing the students that signed up, and 2 integers representing
     * the maximum amount of tables and table capacity respectively.
     * @return An arraylist of tables,with each table filled up with students, which represents 
     * the final arrangement of students for the event.
     */
    public static ArrayList<Table> assignTables(ArrayList<Student> inputStudents, int maxTables, int tableSize) {

        tables = new ArrayList<Table>();

        //Create tables
        for (int i = 0; i < maxTables; i++) {
            tables.add(new Table(tableSize));
        }

        students = inputStudents;

        //Randomize student order
        Collections.shuffle(students);


        int studentIndex = 0;
        int tableSpots = maxTables*tableSize;

        //Fill tables first
        for(Table table:tables) {

            while( (!table.isFull()) && (studentIndex < students.size())) {
                System.out.println("index: " + studentIndex);
                System.out.println(students.size());
                System.out.println(students.get(studentIndex).getName());
                table.addStudent(students.get(studentIndex));
                studentIndex++;
            }
        }

        int oldTotalHappiness = totalHappiness();
        double oldMeanHappiness = ((double)oldTotalHappiness)/students.size();
        double oldDeviationHappiness = deviationHappiness(oldMeanHappiness);

        //Print starting arrangement and total, mean, and deviation happiness
        System.out.println();
        System.out.println("Starting Arrangement: ");
        System.out.println(tables);
        System.out.println();
        System.out.println("Beginning Total Happiness: "+oldTotalHappiness);
        System.out.println("Beginning Mean Happiness: "+oldMeanHappiness);
        System.out.println("Beginning Standard Deviation Happiness "+oldDeviationHappiness);
        System.out.println();

        int swaps=  0;
        int iterations = 0;
        int totalHappiness = 0;
        int maxHappinessIncrease = 0;
        double startTime = System.nanoTime()/1000000000.0;
        double currentTime = startTime;
        double meanHappiness = 0;
        double deviationHappiness = 0;
        Student swapStudent1 = new Student("","");
        Student swapStudent2 = new Student("","");
        Table swapTable1 = new Table(0);
        Table swapTable2 = new Table(0);
        happiness = new ArrayList<Integer>();

        //If there is only 1 table, configuration doesn't matter
        if(tables.size() == 1) {
            System.out.println(tables.get(0).getStudents().size());
            return tables;
        }

        //While loop that check 2 tables every time and find the best swap for those 2 tables
        //Terminates if runtime exceeds time limit or if it loops a certain amount of iterations, which is based on the student amount, allowing it to terminate earlier.
        do {

            maxHappinessIncrease = 0;
            int tableIndex1 = (int)(Math.random()*tables.size());
            int tableIndex2 = (int)(Math.random()*tables.size());

            //Find 2 random tables that are different
            while(tableIndex1 == tableIndex2) {
                tableIndex2 = (int)(Math.random()*tables.size());
            }

            //Declare the tables that will be checked
            Table table1 = tables.get(tableIndex1);
            Table table2 = tables.get(tableIndex2);
            ArrayList<Student> table1Students = table1.getStudents();
            ArrayList<Student> table2Students = table2.getStudents();

            //Loop though the students of the first table
            for(int a=0; a<table1Students.size(); a++) {

                Student student1 = table1Students.get(a);

                //Loop through the students of the second table
                for(int b=0; b<table2Students.size(); b++) {

                    Student student2 = table2Students.get(b);
                    int oldMaxHappinessIncrease = maxHappinessIncrease;

                    maxHappinessIncrease = Math.max(oldMaxHappinessIncrease,happinessChange(student1,student2,table1,table2));

                    //If this swap increases total happiness more than the current best swap
                    if(maxHappinessIncrease > oldMaxHappinessIncrease) {

                        //Store the student and table object for the optimal swap
                        swapStudent1 = student1;
                        swapStudent2 = student2;
                        swapTable1 = table1;
                        swapTable2 = table2;
                    }
                }
            }

            //If the best swap increases total happiness
            if(maxHappinessIncrease > 0) {

                //Add 1 to the total amount of swaps
                swaps++;

                //Increase total happiness by the required amount
                totalHappiness += maxHappinessIncrease;

                //Perform the swap
                swapTable1.getStudents().add(swapStudent2);
                swapTable1.getStudents().remove(swapStudent1);
                swapTable2.getStudents().add(swapStudent1);
                swapTable2.getStudents().remove(swapStudent2);
            }

            //Store current time
            currentTime = System.nanoTime()/1000000000;

            //Add 1 to the total amount of iterations of the while loop
            iterations++;

        } while((currentTime-startTime < TIME_LIMIT) && (iterations < Math.pow(students.size(),1.75)+1000));

        //Print algorithm runtime statistics
        System.out.println("Time elapsed: "+(System.nanoTime()/1000000000.0-startTime)+"s");
        System.out.println("Swaps: "+swaps);
        System.out.println("Iterations: "+iterations);

        //Calculate ending total, mean, and deviation happiness.
        totalHappiness = totalHappiness();
        meanHappiness = ((double)totalHappiness)/students.size();
        deviationHappiness = deviationHappiness(meanHappiness);

        //Print ending totals and % improvement in mean happiness
        System.out.println("Total Happiness: "+totalHappiness);
        System.out.println("Mean Happiness: "+meanHappiness);
        System.out.println("Standard Deviation of Happiness: "+deviationHappiness);
        System.out.println("% improvement of mean happiness: "+((meanHappiness-oldMeanHappiness)/oldMeanHappiness)*100+"%");

        //Print student arrangements and tables: used for debugging
        System.out.println();
        System.out.println("Ending Arrangement:");
        System.out.println(tables);
        System.out.println();

        return tables;
    }

    /**
     * totalHappiness
     * This method computes the total happiness of the students given their current arrangement
     * @return An integer representing the current total happiness
     */
    private static int totalHappiness(){

        int totalHappiness = 0;
        happiness = new ArrayList<Integer>();

        //Loop through all tables
        for(Table table:tables) {

            ArrayList<Student>tableStudents = table.getStudents();

            //Loop through students in current table
            for(Student s : tableStudents) {

                int studentHappiness = 0;
                ArrayList<Student> partners = s.getPartners();

                //System.out.println(partners.size());

                if (partners != null) {

                    //Loop through the current student's partners
                    for (Student p : partners) {

                        //If the current table, which the student sits at, also contains this partner
                        if (table.containsStudent(p)) {

                            totalHappiness++;
                            studentHappiness++;
                        }
                    }

                }

                //Add each student's happiness to an array list, in order to compute deviation later
                happiness.add(studentHappiness);
            }
        }
        return totalHappiness;
    }

    /**
     * happinessChange
     * This method calculates the change in happiness of a swap compared to the previous arrangement
     * Student 1 currently sits at table 1, whereas student 2 currently sits at table 2. This is before the swap.
     * @param //2 students, representing the candidates to be swapped, and 2 tables, representing the tables of these 2 students
     * @return An integer, representing the change in happiness after this swap occurs, +ve if happiness increases and -ve if it decreases
     */
    private static int happinessChange(Student student1, Student student2, Table table1, Table table2){

        int happinessChange = 0;
        ArrayList<Student>tableStudents = table1.getStudents();

        //Loop through students of first table
        for(Student student: tableStudents) {

            ArrayList<Student> partners = student.getPartners();

            //If this student sitting at table1 has student 1 as a partner, decrease happiness as student 1 will leave
            //If this student sitting at table1 has student 2 as a partner, increase happiness as student 2 will enter
            if(partners.contains(student1)) {

                happinessChange--;
            } else if((partners.contains(student2)) && (!student.equals(student1))) {

                happinessChange++;
            }
        }

        tableStudents = table2.getStudents();

        //Loop through students of second table
        for(Student student: tableStudents) {

            ArrayList<Student> partners = student.getPartners();

            //If this student sitting at table2 has student 2 as a partner, decrease happiness as student 2 will leave
            //If this student sitting at table2 has student 1 as a partner, increase happiness as student 1 will enter
            if(partners.contains(student2)) {

                happinessChange--;
            } else if((partners.contains(student1)) && (!student.equals(student2))) {

                happinessChange++;
            }
        }

        //Loop through partners of student 1
        for(Student partner: student1.getPartners()) {

            //If this partner is sitting at table 2, increase happiness as student 1 will enter
            //If this partner is sitting at table 1, decrease happiness as student 1 will leave
            if((table2.containsStudent(partner)) && (!partner.equals(student2))) {

                happinessChange++;
            } else if(table1.containsStudent(partner)) {

                happinessChange--;
            }
        }

        //Loop through partners of student 2
        for(Student partner:student2.getPartners()) {

            //If this partner is sitting at table 1, increase happiness as student 2 will enter
            //If this partner is sitting at table 2, decrease happiness as student 2 will leave
            if((table1.containsStudent(partner)) && (!partner.equals(student1))) {

                happinessChange++;
            } else if(table2.containsStudent(partner)) {

                happinessChange--;
            }
        }

        return happinessChange;
    }

    /**
     * deviationHappiness
     * This method calculates the standard deviation of all students' happiness to the mean happiness
     * @param //A double representing the mean student happiness
     * @return A double representing the standard deviation of students' happiness
     */
    private static double deviationHappiness(double meanHappiness) {

        double sum = 0;

        //Standard deviation formula
        for(int happiness:happiness) {
            sum += Math.pow(happiness-meanHappiness,2);
        }

        double dev = Math.sqrt(sum/(students.size()-1));

        return dev;
    }
}