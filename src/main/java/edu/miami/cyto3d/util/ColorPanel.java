package edu.miami.cyto3d.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.Border;

import edu.miami.math.vector.RVec4;
import edu.miami.math.vector.Vec4;

/**
 * Panel that displays a color. When clicked, a JColorChooser will appear that allows the user to
 * change the color.
 * 
 * @author justin
 */
public class ColorPanel extends JPanel implements MouseListener {

    private String         colorName;
    private List<Listener> listeners = new ArrayList<Listener>();

    public ColorPanel(String colorName, RVec4 initialColor) {
        this.colorName = colorName;
        setOpaque(true);
        Border b1 = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
        Border b2 = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.lightGray);
        Border b = BorderFactory.createCompoundBorder(b1, b2);
        setBorder(b);
        setBackground(toColor(initialColor));
        addMouseListener(this);
        setPreferredSize(new Dimension(22, 22));
        setMinimumSize(new Dimension(22, 22));
    }

    public void addListener(ColorPanel.Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(ColorPanel.Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Color color = JColorChooser.showDialog(ColorPanel.this, colorName, getBackground());
        if (color != null) {
            setBackground(color);
            Vec4 c = toVec4(color);
            for (Listener l : listeners)
                l.colorChanged(this, c);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    static Color toColor(RVec4 v) {
        return new Color(v.x(), v.y(), v.z());
    }

    static Vec4 toVec4(Color c) {
        return new Vec4(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1.0f);
    }

    public interface Listener {
        void colorChanged(ColorPanel panel, Vec4 newColor);
    }
}