package edu.miami.util;

import java.awt.event.MouseEvent;

public class MouseCombo {

  public final int    button;
  public final int    modifiers;
  public final String text;

  public MouseCombo(int button, int modifiers) {
    this.button = button;
    this.modifiers = modifiers;

    String buttonText = null;
    switch (button) {
    case MouseEvent.BUTTON1:
      buttonText = "Button 1";
    case MouseEvent.BUTTON2:
      buttonText = "Button 2";
    case MouseEvent.BUTTON3:
      buttonText = "Button 3";
    }

    this.text = modifiers == 0 ? MouseEvent.getModifiersExText(modifiers) + " " + buttonText
        : buttonText;
  }
  
  public MouseCombo(MouseEvent event) {
    this(event.getButton(), event.getModifiersEx());
  }

  public MouseCombo(int code) {
    this(code, 0);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + button;
    result = prime * result + modifiers;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    MouseCombo other = (MouseCombo) obj;
    if (button != other.button) return false;
    if (modifiers != other.modifiers) return false;
    return true;
  }

  public boolean equals(MouseEvent e) {
    // Javadoc says to use bitwise operators to check masks, but I only want the KeyCombo to respond
    // to EXACTLY the same set of modifiers; a key combo (Shift-F) should not respond to
    // (Alt-Shift-F), for example.
    return (button == e.getButton()) && (e.getModifiersEx() == modifiers);
  }

  @Override
  public String toString() {
    return text;
  }
}
