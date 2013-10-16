package edu.miami.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * Specialized text field for recording and displaying KeyStrokes.
 * 
 * @author justin
 */
public class KeyStrokeField extends JTextField implements KeyListener {

  private KeyStroke keystroke;
  private String    fieldText;

  public KeyStrokeField() {
    addKeyListener(this);
  }

  public KeyStrokeField(KeyStroke defaultKeystroke) {
    this();
    setKeyStroke(defaultKeystroke);
    setText(fieldText);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    e.consume();
    setText("");
    KeyStroke keystroke = KeyStroke.getKeyStrokeForEvent(e);
    if (validKeystroke(keystroke)) setKeyStroke(keystroke);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    setText(fieldText);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    e.consume();
    setText("");
  }

  public KeyStroke getKeyStroke() {
    return keystroke;
  }

  private boolean validKeystroke(KeyStroke keystroke) {
    // don't allow modifier keys to be used as keystrokes on their own
    return !KeyEvent.getModifiersExText(keystroke.getModifiers()).contains(
        KeyEvent.getKeyText(keystroke.getKeyCode()));
  }

  public void setKeyStroke(KeyStroke keyStroke) {
    this.keystroke = keyStroke;
    if (keyStroke.getModifiers() == 0) {
      fieldText = KeyEvent.getKeyText(keyStroke.getKeyCode());
    } else {
      fieldText = KeyEvent.getModifiersExText(keyStroke.getModifiers()) + "+"
          + KeyEvent.getKeyText(keyStroke.getKeyCode());
    }
  }
}
