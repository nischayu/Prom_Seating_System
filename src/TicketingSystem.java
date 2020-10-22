/*Class Name: TicketingSystem
 * @version: 1.0
 * @author: Nischay Uppal & Aryan Abed
 * @date: February 25,2018
 * @description: Running Core Ticketing System for user
 * */

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TicketingSystem extends JPanel implements ActionListener {

    private Prom parent;

    //Max Partners
    private final int MAX_PARTNERS = 7;

    //Image
    private final String imagePath = "Richmond_Hill_HS_COA.jpg";

    //Master List
    private ArrayList<Student> students;

    //layout
    private JPanel column,fields,buttonRow,partnerBtnRow;

    //Formatted Layout
    private GroupLayout layout;
    GroupLayout.SequentialGroup hGroup,vGroup;
    GroupLayout.ParallelGroup pGroupLabel,pGroupField;

    //Buttons
    private JButton submit,clear,cancel,addPartner,removePartner;

    //Text Fields
    private JTextField firstNameField,lastNameField, restrictionsField, studentNumField;
    private ArrayList<JTextField> partnerFields,partnerNums;

    //Labels
    private JLabel firstNameLabel,lastNameLabel,restrictions, studentNumLabel,invalid;
    private ArrayList<JLabel> partnerLabels,partnerNumLabels;

    TicketingSystem(Prom parent) {

        this.parent = parent;
        this.students = parent.getStudents();

        //Init Images
        ImageIcon icon = new ImageIcon(imagePath);
        icon.getImage().flush();

        //Background
        JLabel background = new JLabel();
        background.setIcon(icon);
        background.setLayout(new BorderLayout());

        //Warning Label
        invalid = new JLabel();
        invalid.setForeground(Color.red);
        invalid.setVisible(false);
        invalid.setHorizontalAlignment(SwingConstants.LEFT);
        invalid.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Formatting
        fields = new JPanel();
        layout = new GroupLayout(fields);
        fields.setLayout(layout);

        buttonRow = new JPanel();
        partnerBtnRow = new JPanel();
        column = new JPanel();

        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 5), "Sign Up Form"));

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        firstNameField.setPreferredSize(new Dimension(300, 25));

        lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        lastNameField.setPreferredSize(new Dimension(300, 25));

        restrictions = new JLabel("Accommodations:");
        restrictionsField = new JTextField();
        restrictionsField.setPreferredSize(new Dimension(300, 25));

        studentNumLabel = new JLabel("Student Num:");
        studentNumField = new JTextField();
        studentNumField.setPreferredSize(new Dimension(300, 25));

        //Minimum 1 partner per student
        partnerFields = new ArrayList<>();
        partnerLabels = new ArrayList<>();
        partnerNums = new ArrayList<>();
        partnerNumLabels = new ArrayList<>();

        partnerFields.add(new JTextField());
        partnerFields.get(0).setPreferredSize(new Dimension(300, 25));
        partnerLabels.add(new JLabel("Partner 1 Name:"));

        partnerNums.add(new JTextField());
        partnerNums.get(0).setPreferredSize(new Dimension(300, 25));
        partnerNumLabels.add(new JLabel("Partner 1 Num:"));

        //Buttons
        addPartner = new JButton("Add Partner");
        addPartner.addActionListener(this);
        partnerBtnRow.add(addPartner);

        removePartner = new JButton("Remove Partner");
        removePartner.addActionListener(this);
        partnerBtnRow.add(removePartner);

        submit = new JButton("Submit");
        submit.addActionListener(this);
        buttonRow.add(submit);

        clear = new JButton("Clear All");
        clear.addActionListener(this);
        buttonRow.add(clear);

        cancel = new JButton("Exit");
        cancel.addActionListener(this);
        buttonRow.add(cancel);

        //Groupings
        pGroupLabel = layout.createParallelGroup();
        pGroupField = layout.createParallelGroup();

        hGroup = layout.createSequentialGroup();
        hGroup.addGroup(pGroupLabel.addComponent(firstNameLabel).addComponent(lastNameLabel).addComponent(studentNumLabel).addComponent(restrictions).addComponent(partnerLabels.get(0)).addComponent(partnerNumLabels.get(0)));
        hGroup.addGroup(pGroupField.addComponent(firstNameField).addComponent(lastNameField).addComponent(studentNumField).addComponent(restrictionsField).addComponent(partnerFields.get(0)).addComponent(partnerNums.get(0)));

        layout.setHorizontalGroup(hGroup);

        vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(firstNameLabel).addComponent(firstNameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lastNameLabel).addComponent(lastNameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(studentNumLabel).addComponent(studentNumField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(restrictions).addComponent(restrictionsField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(partnerLabels.get(0)).addComponent(partnerFields.get(0)));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(partnerNumLabels.get(0)).addComponent(partnerNums.get(0)));
        layout.setVerticalGroup(vGroup);

        column.add(fields);
        column.add(partnerBtnRow);
        column.add(buttonRow);
        column.add(invalid);

        this.add(background);
        this.add(column);
    }

    /**
    clearPanel()
    This method clears all user input
    This method is a void
     */
    private void clearPanel() {
        //Clears JTextFields
        firstNameField.setText("");
        lastNameField.setText("");
        studentNumField.setText("");
        restrictionsField.setText("");

        //Clears all partner textfields
        for (int i = 0; i < partnerFields.size(); i++) {
            partnerFields.get(i).setText("");
            partnerNums.get(i).setText("");
        }
    }

    /**
     partnerAdded()
     This method adds a partner name and number text field to the panel
     This method is a void
     */
    private void partnerAdded() {
        //Adds JTextFields needed for Partner
        JTextField partner = new JTextField();
        partner.setPreferredSize(new Dimension(300, 25));
        partnerFields.add(partner);

        JTextField num = new JTextField();
        num.setPreferredSize(new Dimension(300, 25));
        partnerNums.add(num);

        JLabel label = new JLabel("Partner " + (partnerLabels.size() + 1) + " Name: ");
        partnerLabels.add(label);

        JLabel labelNum = new JLabel("Partner " + (partnerNumLabels.size() + 1) + " Num: ");
        partnerNumLabels.add(labelNum);

        //Grouping
        pGroupLabel.addComponent(label);
        pGroupField.addComponent(partner);
        pGroupLabel.addComponent(labelNum);
        pGroupField.addComponent(num);

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(partnerLabels.get(partnerLabels.size() - 1)).addComponent(partnerFields.get(partnerFields.size() - 1)));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(partnerNumLabels.get(partnerNumLabels.size() - 1)).addComponent(partnerNums.get(partnerNums.size() - 1)));

        //Update
        column.revalidate();
        column.repaint();
    }

    /**
     partnerRemoved()
     This method removes a partner name and number text field to the panel
     This method is a void
     */
    private void partnerRemoved() {
        //Removes JTextField and Labels of last partner
        partnerFields.get(partnerFields.size() - 1).setVisible(false);
        partnerNums.get(partnerNums.size() - 1).setVisible(false);
        partnerLabels.get(partnerLabels.size() - 1).setVisible(false);
        partnerNumLabels.get(partnerNumLabels.size() - 1).setVisible(false);

        //Updates ArrayList sizes
        partnerLabels.remove(partnerLabels.size() - 1);
        partnerFields.remove(partnerFields.size() - 1);
        partnerNums.remove(partnerNums.size() - 1);
        partnerNumLabels.remove(partnerNumLabels.size() - 1);

        //Update
        column.revalidate();
        column.repaint();
    }

    /**
     areFieldsFilled()
     This method checks if all fields are filled
     @return Boolean, true if All fields are filled, false if all fields are filled
     */
    private boolean areFieldsFilled() {
        //Checks if Student fields are empty. Returns false if they are
        if (firstNameField.getText().equals("")) {
            return false;
        } else if (lastNameField.getText().equals("")) {
            return false;
        } else if (studentNumField.getText().equals("")) {
            return false;
        }

        //Checks if partner fields are empty. returns false if they are
        for (int i = 0; i < partnerFields.size(); i++) {
            if (partnerFields.get(i).getText().equals("")) {
                return false;
            }
        }
        //Return true if all fields filled
        return true;
    }

    /**
     isParseable()
     This method checks if a text field contains only integers
     @param //JTextField, textfield to analyze
     @return Boolean, true if text field only contains integers, false if it does not
     */
    private boolean isParseable(JTextField number) {
        try {
            Integer.parseInt(number.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     isSubmitted()
     This method checks if the student's profile can be successfully submitted
     @return Boolean, true if profile successfully submitted, false if it cannot
     */
    private boolean isSubmitted() {
        //Checks if Student Number is parseable
        if (!isParseable(studentNumField)) {
            //Update and illuminate flag
            invalid.setText("Student Number not appropriate. Please input properly.");
            invalid.setVisible(true);
            return false;
        }
        //Checks if each Partner Number is parseable
        for (int i = 0; i < partnerNums.size(); i++) {
            if (!isParseable((partnerNums.get(i)))) {
                //Update and illuminate flag
                invalid.setForeground(Color.red);
                invalid.setText("A Partner Number " + (i + 1) + " is invalid.");
                invalid.setVisible(true);
                return false;
            } else {
                //Eliminate FLag
                invalid.setVisible(false);
            }
        }

        //Add user and data to Student object and add student to master list
        Student user = new Student(firstNameField.getText() + " " + lastNameField.getText(), studentNumField.getText(), createPartnersList());
        user.setPaid(true);

        //Convert String[] of accommodations to ArrayList<String>s
        String[] accomm = restrictionsField.getText().split(", ");

        if (students.contains(user)) {
            students.get(students.indexOf(user)).setPartners(createPartnersList());
            if (accomm.length != 0) {
                System.out.println("Hello?");
                ArrayList<String> rest = new ArrayList<>();
                for (String c : accomm) {
                    System.out.println(c);
                    rest.add(c);
                }
                students.get(students.indexOf(user)).setAccommodations(rest);
            }
            students.get(students.indexOf(user)).setPaid(true);
        } else {
            students.add(user);
            if (accomm.length != 0) {
                System.out.println("Hello?");
                ArrayList<String> rest = new ArrayList<>();
                for (String c : accomm) {
                    System.out.println(c);
                    rest.add(c);
                }
                students.get(students.indexOf(user)).setAccommodations(rest);
            }
        }

        //Add partners to students master list declared in Prom class
        for (int i = 0; i < partnerFields.size(); i++) {
            Student partner = new Student(partnerFields.get(i).getText(), partnerNums.get(i).getText());
            if (!students.contains(partner)) {
                students.add(partner);
            }
        }

        return true;
    }

    /**
     createPartnerList()
     This method creates an arrayList of partners
     @return ArrayList<Student> returns arrayList of partners which student inputted
     */
    ArrayList<Student> createPartnersList() {
        ArrayList<Student> partners = new ArrayList<>();
        //Create new Student for Each partner and add partner to partners.
        for (int i = 0; i < partnerFields.size(); i++) {
            Student partner = new Student(partnerFields.get(i).getText(), partnerNums.get(i).getText());
            partners.add(partner);
        }
        return partners;
    }

    /**
     addStudent()
     */
    private void addStudent() {}

    /**
     removeStudent()
     */
    private void removeStudent() {}

    /**
     actionPerformed()
     Handles all inputs, flags, and buttons
     @param //ActionEvent e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //If Submit button pressed, verify validity of profile being submitted
        if (e.getSource() == submit) {
            if (areFieldsFilled() && isSubmitted()) {
                invalid.setText("Profile submitted successfully!");
                invalid.setForeground(Color.GREEN.darker());
                invalid.setVisible(true);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                //Clear Panel when successfully submitted for next student
                clearPanel();
            } else if (!areFieldsFilled()) {
                //Not all field filled, present flag
                System.out.println("All fields are not filled");
                invalid.setText("Error: One or more fields are not filled");
                invalid.setVisible(true);
            }
        } else if (e.getSource() == clear) {
            //Clear the panel
            clearPanel();
        } else if (e.getSource() == cancel) {
            parent.remove(this);
            parent.add(parent.getMenu());
            parent.revalidate();
            parent.repaint();
        } else if (e.getSource() == addPartner) {
            //Add additional partner if student does not reach max partners allowed
            if (partnerFields.size() < MAX_PARTNERS) {
                partnerAdded();
            } else {
                invalid.setText("Unable to add more partners");
                invalid.setVisible(true);
            }
        } else if (e.getSource() == removePartner) {
            //Removes partner fields
            if (partnerFields.size() > 1) {
                partnerRemoved();
            } else {
                invalid.setText("You need at least one partner!");
                invalid.setVisible(true);
            }
        }
    }
}