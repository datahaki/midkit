package ch.alpine.midkit.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ch.alpine.bridge.swing.SpinnerListener;
import ch.alpine.midkit.MidiInstrument;
import ch.alpine.midkit.VoiceGroup;
import sys.gui.StandardMenu;

public abstract class InstrumentMenu extends StandardMenu implements SpinnerListener<MidiInstrument> {
  @Override
  protected void design(JPopupMenu jPopupMenu) {
    {
      JMenu jMenu = new JMenu("Favorites");
      jMenu.add(create(MidiInstrument.GRAND_PIANO));
      jMenu.add(create(MidiInstrument.CHURCH_ORGAN));
      jMenu.add(create(MidiInstrument.VIOLIN));
      jPopupMenu.add(jMenu);
    }
    jPopupMenu.addSeparator();
    for (VoiceGroup voiceGroup : VoiceGroup.values()) {
      JMenu jMenu = new JMenu(voiceGroup.name());
      for (MidiInstrument midiInstrument : voiceGroup.list()) {
        jMenu.add(create(midiInstrument));
      }
      jPopupMenu.add(jMenu);
    }
    jPopupMenu.addSeparator();
    jPopupMenu.add(new JMenuItem("cancel"));
  }

  private JMenuItem create(MidiInstrument midiInstrument) {
    JMenuItem jMenuItem = new JMenuItem(midiInstrument.name());
    jMenuItem.addActionListener(_ -> spun(midiInstrument));
    return jMenuItem;
  }
}
