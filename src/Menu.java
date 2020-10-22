import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {

    private JButton ticketingPanelButton, floorPanelButton;
    private Prom parent;

    Menu(Prom parent) {

        this.parent = parent;

        ticketingPanelButton = new JButton("Ticketing System");
        ticketingPanelButton.addActionListener(this);
        floorPanelButton = new JButton("Floor Plan System");
        floorPanelButton.addActionListener(this);

        this.add(ticketingPanelButton);
        this.add(floorPanelButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ticketingPanelButton) {
            parent.add(parent.getTicketingPanel());
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        } else if (e.getSource() == floorPanelButton) {
            //parent.getStudents().size();
            parent.getFloorPanel().makeVisible();
            parent.add(parent.getFloorPanel());
            parent.remove(this);
            //this.setVisible(false);
            parent.revalidate();
            parent.repaint();
        }
    }

}
