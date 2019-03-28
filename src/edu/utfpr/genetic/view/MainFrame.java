/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author henrique
 */
public class MainFrame extends JFrame{

    public MainFrame() throws HeadlessException {
        super();
        
        setup();
        this.setVisible(true);
    }
    
    private final void setup(){
        this.setSize(800, 600);
        
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JComboBox<String> stopConditionType = new JComboBox(new String[]{"Generation count", "Fitness evaluation count"});
        JSlider stopConditionValue = new JSlider(JSlider.HORIZONTAL, 1, 100000000, 1000);
        
        JSlider poolSize = new JSlider(JSlider.HORIZONTAL, 1, 10000, 100);
        JSlider childSize = new JSlider(JSlider.HORIZONTAL, 1, 100000, 100);
        
        try {
            JTextField seed = new JFormattedTextField(new MaskFormatter("#####################"));
        } catch (ParseException ex) {
        }
        
        JSpinner mutationChance = new JSpinner(new SpinnerNumberModel(0.05, 0.0, 1.0, 0.01));
        JSpinner crossOverChance = new JSpinner(new SpinnerNumberModel(0.80, 0.0, 1.0, 0.01));
    }
}
