import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The class for the JPanel containing the full floor plan system. The class utilizes a <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/BorderLayout.html">BorderLayout</a>
 * with four components:
 * <ul>
 * <li>A <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/JLabel.html">JLabel</a> with the title of the panel occupying the NORTH position.
 * <li>A <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/JPanel.html">JPanel</a> with settings occupying the WEST position
 * <li>A <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/JButton.html">JButton</a> allowing exit from the panel occupying the SOUTH position.
 * <li>A JPanel that serves as the main drawing window occupying the CENTRAL position.
 * </ul>
 * <p>
 * The FloorPlanSystem object initializes these default settings:
 * <ul>
 * <li>Max number of tables: 1
 * <li>Max number of students per table: 1
 * <li>Number of rows of tables: 1
 * </ul>
 * <p>
 * Note that these default settings mean only one table will be drawn. The user is required to
 * enter all parameters in order to display the {@link Student}s who have signed up for prom
 * arranged in tables. As the user updates parameters, the tables will be updated in real time. If
 * no students have signed up for prom, no tables will be drawn. This class provides no
 * functionality to change the students attending prom, only functionality to change the seating
 * arrangements of said students.
 *
 * @author Charles Wong, Cindy Wang
 * @version 1.0, 19/02/20
 */
public class FloorPlanSystem extends JPanel implements ActionListener {

    private Prom parent;

    private boolean currentlyVisible;

    private JButton exitButton;
    private JLabel titleLabel;

    private displayPanel display;
    private sidePanel side;

    private ArrayList <Student> students;

    private Table selected;

    private int maxTables;
    private int maxStudents;
    private int numRows;

    private int [] rowLengths;

    private ArrayList <Table> tables;

    /**
     * The constructor for the floor plan system. Creates all components contained within the system,
     * and initializes all variables to their default values.
     *
     * @param parent an <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html">ArrayList</a> of {@link Student}s to be arranged into tables
     */
    public FloorPlanSystem (Prom parent) {

        this.parent = parent;

        currentlyVisible = false;

        students = parent.getStudents();
        tables = new ArrayList <Table> ();
        tables.add(new Table(0));

        maxTables = 1;
        maxStudents = 1;
        numRows = 1;
        rowLengths = new int [] {1};

        this.setLayout(new BorderLayout());


        this.setBackground(Color.DARK_GRAY);

        titleLabel = new JLabel ("Floor Plan Viewer");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(titleLabel, BorderLayout.PAGE_START);

        exitButton = new JButton ("exit");
        this.add(exitButton, BorderLayout.PAGE_END);
        exitButton.addActionListener(this);

        display = new displayPanel();
        this.add(display, BorderLayout.CENTER);

        side = new sidePanel();
        this.add(side, BorderLayout.LINE_START);
    }

    /**
     * Inherited method from the <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/event/ActionListener.html">ActionListener</a> interface. Used to
     * listen to the exit <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/JButton.html">JButton</a>. Sets visibility of the panel to
     * <code>false</code>.
     *
     * @param e an <a href="https://docs.oracle.com/javase/8/docs/api/java/awt/event/ActionEvent.html">ActionEvent</a> object
     */
    @Override
    public void actionPerformed (ActionEvent e) {
        parent.remove(this);
        parent.add(parent.getMenu());
        parent.revalidate();
        parent.repaint();
    }

    /**
     * Sets the current <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/JPanel.html">JPanel</a> to a visible state for one frame. Used to track
     * internally the moment this panel is shown.
     */
    public void makeVisible () {
        currentlyVisible = true;
    }

    /**
     * The main display area for
     * @author charleswong
     *
     */
    private class displayPanel extends JPanel implements MouseListener {

        private displayPanel () {
            this.setBackground(Color.DARK_GRAY);
            addMouseListener(this);

            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    display.recalculate(0);
                }
            });
        }

        @Override
        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            setDoubleBuffered(true);

            if (currentlyVisible) {
                currentlyVisible = false;
                recalculate();

            }

            for (int i = 0; i < tables.size(); i++) {
                tables.get(i).draw(g, Color.WHITE);
                if (selected != null) {
                    if (i == tables.indexOf(selected)) {
                        tables.get(i).draw(g, new Color(1f, 0.4f, 0.5f));
                    }
                }
            }

            repaint();
        }

        private void recalculate () {
            tables = SeatingAssignmentSystem.assignTables(students, maxTables, maxStudents);

            int r = 0;

            int a = (int) ((this.getWidth() / rowLengths[0] * 0.9) / 2);
            int b = (int) ((this.getHeight() / numRows * 0.9) / 2);

            if (a < b) {
                r = a;
            } else {
                r = b;
            }

            int offsetY = (int) (this.getHeight() / (numRows * 2.0));
            int tableNum = 0;
            for (int i = 0; i < numRows; i++) {
                int offsetX = (int) (this.getWidth() / (rowLengths[i] * 2.0));
                for (int j = 0; j < rowLengths[i]; j++) {
                    tables.get(tableNum).setX(offsetX * (j * 2 + 1));
                    tables.get(tableNum).setY(offsetY * (i * 2 + 1));
                    tables.get(tableNum).setRadius(r);
                    tableNum++;
                }
            }

            this.revalidate();
        }

        private void recalculate (int w) {
            int r = 0;

            int a = (int) ((this.getWidth() / rowLengths[0] * 0.9) / 2);
            int b = (int) ((this.getHeight() / numRows * 0.9) / 2);

            if (a < b) {
                r = a;
            } else {
                r = b;
            }

            int offsetY = (int) (this.getHeight() / (numRows * 2.0));
            int tableNum = 0;
            for (int i = 0; i < numRows; i++) {
                int offsetX = (int) (this.getWidth() / (rowLengths[i] * 2.0));
                for (int j = 0; j < rowLengths[i]; j++) {
                    tables.get(tableNum).setX(offsetX * (j * 2 + 1));
                    tables.get(tableNum).setY(offsetY * (i * 2 + 1));
                    tables.get(tableNum).setRadius(r);
                    tableNum++;
                }
            }

            this.revalidate();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point l = this.getLocationOnScreen();
            int x = p.x - l.x;
            int y = p.y - l.y;

            // Sorry Mr. G
            check: {
                for (int i = 0; i < tables.size(); i++) {
                    Table t = tables.get(i);
                    if (t.getX() != 0 && t.getY() != 0) {
                        if (Utility.pointInCircle(t.getX(), t.getY(), t.getRadius(), x, y)) {
                            selected = tables.get(i);
                            break check;
                        } else {
                            selected = null;
                        }
                    }
                }
            }
            side.profile.setTree();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    private class sidePanel extends JPanel {

        private settingsPanel settings;
        private profilePanel profile;

        private sidePanel () {
            this.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            settings = new settingsPanel ();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            this.add(settings, c);

            profile = new profilePanel();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 1;
            c.weighty = 1.0;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(profile, c);
        }

        private class settingsPanel extends JPanel implements PropertyChangeListener{

            private JLabel maxTablesLabel;
            private JFormattedTextField maxTablesField;

            private JLabel maxStudentsLabel;
            private JFormattedTextField maxStudentsField;

            private JLabel numRowsLabel;
            private JFormattedTextField numRowsField;

            private settingsPanel () {
                this.setBackground(Color.DARK_GRAY);

                this.setLayout (new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                maxTablesLabel = new JLabel ("Max # of Tables");
                maxTablesLabel.setForeground(Color.WHITE);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 0;
                this.add(maxTablesLabel, c);

                NumberFormatter nf = new NumberFormatter();
                nf.setMinimum(0);

                maxTablesField = new JFormattedTextField(nf);
                maxTablesField.setValue(1);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = 0;
                c.ipadx = 30;
                this.add(maxTablesField, c);
                maxTablesField.addPropertyChangeListener("value", this);

                maxStudentsLabel = new JLabel ("<html>Max # of Students <br>at a table</html>");
                maxStudentsLabel.setForeground(Color.WHITE);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 1;
                this.add(maxStudentsLabel, c);

                maxStudentsField = new JFormattedTextField(nf);
                maxStudentsField.setValue(1);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = 1;
                this.add(maxStudentsField, c);
                maxStudentsField.addPropertyChangeListener("value", this);

                numRowsLabel = new JLabel ("# of Rows");
                numRowsLabel.setForeground(Color.WHITE);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 0;
                c.gridy = 2;
                this.add(numRowsLabel, c);

                numRowsField = new JFormattedTextField (nf);
                numRowsField.setValue(1);
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx = 1;
                c.gridy = 2;
                this.add(numRowsField, c);
                numRowsField.addPropertyChangeListener("value", this);
            }

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getSource() == maxTablesField) {
                    maxTables = ((Number) maxTablesField.getValue()).intValue();
                } else if (evt.getSource() == maxStudentsField) {
                    maxStudents = ((Number) maxStudentsField.getValue()).intValue();
                } else if (evt.getSource() == numRowsField) {
                    numRows = ((Number) numRowsField.getValue()).intValue();
                }

                rowLengths = new int [numRows];

                for (int i = 0; i < numRows; i++) {
                    rowLengths [i] = maxTables / numRows;
                }

                for (int i = 0; i < maxTables % numRows; i++) {
                    rowLengths[i] += 1;
                }

                display.recalculate();
            }
        }

        private class profilePanel extends JPanel {

            private JLabel headerLabel;

            private JScrollPane treeView;
            private JTree tree;
            private DefaultMutableTreeNode root;

            private profilePanel () {
                this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                this.setBackground(Color.DARK_GRAY);

                headerLabel = new JLabel ("Seated Students:");
                headerLabel.setForeground(Color.WHITE);
                headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                headerLabel.setFont(new Font(headerLabel.getFont().getName(), Font.PLAIN, 20));
                this.add(Box.createRigidArea(new Dimension(0, 15)));
                this.add(headerLabel);

                root = new DefaultMutableTreeNode("Select Table");
                tree = new JTree(root);

                treeView = new JScrollPane(tree);
                this.add(treeView);
            }

            private void setTree() {

                if (selected != null) {
                    root.removeAllChildren();
                    root.setUserObject("Table " + (tables.indexOf(selected) + 1));

                    for (int i = 0; i < selected.getNumSeated(); i++) {
                        Student selectedStudent = selected.getStudents().get(i);

                        DefaultMutableTreeNode s = new DefaultMutableTreeNode(selectedStudent.getName());
                        root.add(s);

                        DefaultMutableTreeNode n = new DefaultMutableTreeNode("#" + selectedStudent.getId());
                        s.add(n);

                        ArrayList <Student> prefs = selectedStudent.getPartners();

                        if (prefs.size() > 0) {
                            DefaultMutableTreeNode prefHeader = new DefaultMutableTreeNode("Preferences");
                            s.add(prefHeader);

                            for (int j = 0; j < prefs.size(); j++) {
                                DefaultMutableTreeNode p = new DefaultMutableTreeNode(prefs.get(j).getName());
                                prefHeader.add(p);
                            }
                        }

                        ArrayList<String> a = selectedStudent.getAccommodations();

                        if(a.size() > 0) {
                            DefaultMutableTreeNode accomodationsHeader = new DefaultMutableTreeNode("Accomodations");
                            s.add(accomodationsHeader);

                            for(int j = 0; j < a.size(); j++) {
                                DefaultMutableTreeNode accomodation = new DefaultMutableTreeNode(a.get(j));
                                accomodationsHeader.add(accomodation);
                            }
                        }
                    }
                } else {
                    root.removeAllChildren();
                    root.setUserObject("Select Table");
                }

                tree.updateUI();
            }
        }
    }
}
