package general;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Menu extends JMenuBar implements ActionListener {

    private final JMenuItem itemA, itemB;
    private final Solitarios frame;

    Menu(Solitarios frame) {
        super();

        this.frame = frame;

        JMenu menu = new JMenu("Jugar a...");
        this.itemA = new JMenuItem("Solitario A");
        this.itemB = new JMenuItem("Solitario B");

        itemA.addActionListener(this);
        itemB.addActionListener(this);

        menu.add(itemA);
        menu.add(itemB);

        add(menu);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source.equals(itemA)) frame.mostrarA();

        else if (source.equals(itemB)) frame.mostrarB();
    }
}
