/*
 * This file is part of the Esc/Java2 plugin project.
 * Copyright 2004-2005 David R. Cok
 * 
 * Created on Feb 2, 2005
 */
package mobius.util.plugin.widgets;

import mobius.util.plugin.AbstractPreference;
import mobius.util.plugin.AbstractPreference.BooleanOption;
import mobius.util.plugin.AbstractPreference.ChoiceOption;
import mobius.util.plugin.AbstractPreference.StringOption;
import mobius.util.plugin.widgets.Widgets.FileTextField;
import mobius.util.plugin.widgets.Widgets.HComposite;
import mobius.util.plugin.widgets.Widgets.LabeledSeparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;


/**
 * This class is a base class for the controls that are used in
 * the property page; each control is connected to an Option object.
 * 
 * @author David Cok
 */
public abstract class PreferenceWidget {
  
  /** The option object represented by this PreferenceWidget. */
  AbstractPreference option;
  
  /**
   * Base class constructor, taking some common arguments.
   * 
   * @param option The option that this widget represents.
   */
  public PreferenceWidget(final AbstractPreference option) {
    this.option = option;
  }
  
  /**
   * Creates and adds the widget to the given control.
   * @param parent The composite to add the widget to.
   */
  public abstract void addWidget(final Composite parent);
  
  /**
   * Sets the UI widget to the built-in default value.
   *
   */
  public abstract void setDefault();
  
  /**
   * Sets the value of the associated option based on the
   * current UI setting.
   * 
   */
  public abstract void setOptionValue();

  
  /**
   * A UI widget that offers a selection from a fixed 
   * set of strings,
   * corresponding to a ChoiceOption.
   *  
   * @author David Cok
   *
   */
  public static class ChoiceWidget extends PreferenceWidget {
    
    /** The widget used to get input from the user. */
    protected Combo widget = null;
    
    /**
     * A constructor that creates a Combo widget, initializing
     * it from the associated property.
     * 
     * @param option The option with which the widget is associated
     */
    //@ pure
    public ChoiceWidget(final AbstractPreference.ChoiceOption option) {
      super(option);
    }
    
    /**
     * Creates the corresponding widget and adds it to the given
     * Composite widget; the UI widgets are recreated for each
     * instance of a property page.
     * @param parent The Composite that holds all of the option widgets
     */
    //@ ensures widget != null;
    public void addWidget(final Composite parent) {
      final ChoiceOption opt = (ChoiceOption)option;
      final Composite composite = new Widgets.HComposite(parent, 2);
      widget = new Combo(composite, SWT.READ_ONLY);
      widget.setItems(opt.choices());
      widget.select(opt.getIndexValue());
      widget.setVisibleItemCount(opt.choices().length);
      widget.setToolTipText(opt.tooltip());
      final org.eclipse.swt.widgets.Label label2 = 
        new org.eclipse.swt.widgets.Label(composite, SWT.NONE);
      label2.setText(opt.label());
      label2.setToolTipText(opt.tooltip());
    }
    
    /**
     * Returns the current setting of the widget; this method
     * may be called only when there is a current Properties Page.
     * @return The current setting of the widget
     */
    //@ requires widget != null;
    //@ ensures \result != null;
    //@ pure
    public String value() { return widget.getText(); }
    
    
    /**
     * Sets the UI widget to the option's default value.
     *
     */
    public void setDefault() {
      widget.select(((ChoiceOption)option).getDefaultIndex());
    }    
    
    /**
     * Sets the option value to be consistent with the
     * current setting of the UI's widget.
     */
    public void setOptionValue() {
      ((AbstractPreference.ChoiceOption)option).setValue(value());
    } 
  }
  
  /**
   * This class implements an OptionWidget for a text field
   * property that holds a file name, using the FileTextField widget, which 
   * incorporates a Browse button.
   * 
   * @author David Cok
   *
   */
  public static class FileWidget extends PreferenceWidget {
    
    /** The UI widget representing a file browser. */
    protected Widgets.FileTextField widget = null;

    /**
     * Creates a FileWidget (the underlying widget is not
     * created until createContents is called).
     * @param option The option on which this widget is based
     */
    //@ reqiures option != null;
    //@ ensures widget == null;
    //@ ensures this.option == option;
    //@ pure
    public FileWidget(final AbstractPreference.StringOption option) {
      super(option);
    }
    
    
    /**
     * Creates the corresponding widget and adds it to the given
     * Composite widget; the UI widgets are recreated for each
     * instance of a property page.
     * @param parent The Composite that holds all of the option widgets
     */
    //@ ensures widget != null;
    public void addWidget(final Composite parent) {
      final String fn = ((AbstractPreference.StringOption)option).getValue();
      widget = new Widgets.FileTextField(parent, option.label(), fn, option.tooltip(), 50);
    }
    
    /**
     * Returns the current setting of the widget; this method
     * may be called only when there is a current Properties Page.
     * @return The current setting of the widget
     */
    //@ requires widget != null;
    public String value() { return widget.value().trim(); }
    
    /**
     * Sets the UI widget to the built-in default value.
     *
     */
    //@ requires widget != null;
    //@ requires option != null;
    public void setDefault() {
      widget.setText(((AbstractPreference.StringOption)option).getDefault());
    }
        
    /**
     * Sets the option value to be consistent with the
     * current setting of the UI's widget.
     */
    //@ requires widget != null;
    //@ requires option != null;
    public void setOptionValue() { 
      ((AbstractPreference.StringOption)option).setValue(value());
    } 
  }
  
  /**
   * This class implements a PropertyWidget for a boolean-valued
   * property, associating it with a check-box Button in the UI.
   * 
   * @author David Cok
   *
   */
  public static class BooleanWidget extends PreferenceWidget {
    
    /** The UI widget representing a boolean choice. */
    protected Button widget = null;
    
    /**
     * Creates a checkbox widget on the given parent Composite widget;
     * initializes the widget with the value of the given option.
     * @param option The option on which this widget is based
     */
    //@ requires option != null;
    //@ ensures this.option == option;
    //@ pure
    public BooleanWidget(final AbstractPreference.BooleanOption option) {
      super(option);
    }
    
    /**
     * Creates the corresponding widget and adds it to the given
     * Composite widget; the UI widgets are recreated for each
     * instance of a property page.
     * @param parent The Composite that holds all of the option widgets
     */
    //@ requires option != null;
    //@ ensures widget != null;
    public void addWidget(final Composite parent) {
      widget = new Button(parent, SWT.CHECK);
      widget.setText(option.label());
      widget.setToolTipText(option.tooltip());
      widget.setSelection(((AbstractPreference.BooleanOption)option).getValue());
    }
    
    /**
     * Returns the current setting of the widget; this method
     * may be called only when there is a current Properties Page.
     * @return The current setting of the widget
     */
    //@ requires widget != null;
    public boolean value() { return widget.getSelection(); }
    
    /**
     * Sets the UI widget to the built-in default value.
     *
     */
    //@ requires widget != null;
    //@ requires option != null;
    public void setDefault() {
      widget.setSelection(((AbstractPreference.BooleanOption)option).getDefault());
    }
    
    /**
     * Sets the workspace property value to be consistent with the
     * current setting of the UI's widget.
     */
    //@ requires widget != null;
    //@ requires option != null;
    public void setOptionValue() {
      ((AbstractPreference.BooleanOption)option).setValue(value());
    } 
  }
  
  /**
   * This class implements an PreferenceWidget that is a Label, so
   * that it can sit in lists of PreferenceWidgets.  However, it does
   * not have an option associated with it.
   * 
   * @author David Cok
   *
   */
  public static class Label extends PreferenceWidget {
    
    /** The UI widget that is a label. */
    private Widgets.LabeledSeparator widget = null;
    
    /** The label value. */
    private String label;
  
    /** 
     * Creates a Label widget on the given parent Composite widget.
     * @param label The label text for the widget
     */
    //@ requires label != null;
    //@ ensures this.label == label;
    //@ pure
    public Label(final String label) {
      super(null);
      this.label = label;
    }
    
    /**
     * Creates the corresponding widget and adds it to the given
     * Composite widget; the UI widgets are recreated for each
     * instance of a property page.
     * @param parent The Composite that holds all of the option widgets
     */
    //@ ensures widget != null;
    public void addWidget(final Composite parent) {
      widget = new Widgets.LabeledSeparator(parent, label);
    }

    /**
     * Does nothing.
     */
    public void setDefault() { } 
    
    /**
     * Does nothing.
     */
    public void setOptionValue() { } 
  }

}
