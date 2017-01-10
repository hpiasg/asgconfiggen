package de.uni_potsdam.hpi.asg.configgen;

/*
 * Copyright (C) 2017 Norman Kluge
 * 
 * This file is part of ASGconfiggen.
 * 
 * ASGconfiggen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASGconfiggen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASGconfiggen.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import de.uni_potsdam.hpi.asg.configgen.generators.MainGenerator;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfigFrame extends JFrame {
    private static final long       serialVersionUID = -4879956586784429087L;
    private JFrame                  parent;

    private Map<String, JTextField> entries;

    private JRadioButton            unixButton;
    private JRadioButton            windowsButton;
    private JCheckBox               resyncheckbox;
    private JCheckBox               logiccheckbox;
    private JCheckBox               dmcheckbox;
    private JTextField              locationtext;

    private Configuration           config;

    public ConfigFrame(Configuration config, WindowAdapter adapt) {
        super("ASGconfiggen");
        this.config = config;
        this.config.setFrame(this);
        entries = new HashMap<>();
        parent = this;
        this.addWindowListener(adapt);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        constructRemotePanel(tabbedPane);
        constructToolsPanel(tabbedPane);
        constructGeneratePanel(tabbedPane);
    }

    private void constructGeneratePanel(JTabbedPane tabbedPane) {
        JPanel panel = new JPanel();
        tabbedPane.addTab("Generate", null, panel, null);
        GridBagLayout gbl_toolspanel = new GridBagLayout();
        gbl_toolspanel.columnWidths = new int[]{200, 300, 50, 0};
        gbl_toolspanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_toolspanel.rowHeights = new int[]{15, 15, 15, 15, 15, 15, 15, 15, 0};
        gbl_toolspanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_toolspanel);

        constructGenerateOsSection(panel);
        constructGenerateToolSection(panel);
        constructGenerateLocationSection(panel);

        final JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainGenerator gen = new MainGenerator(config);
                gen.generate();
            }
        });
        GridBagConstraints gbc_generatebutton = new GridBagConstraints();
        gbc_generatebutton.insets = new Insets(0, 0, 5, 5);
        gbc_generatebutton.gridx = 0;
        gbc_generatebutton.gridy = 7;
        gbc_generatebutton.gridwidth = 3;
        panel.add(generateButton, gbc_generatebutton);
    }

    private void constructGenerateLocationSection(JPanel panel) {
        JLabel locationLabel = new JLabel("Output directory");
        GridBagConstraints gbc_locationlabel = new GridBagConstraints();
        gbc_locationlabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_locationlabel.insets = new Insets(0, 0, 5, 5);
        gbc_locationlabel.gridx = 0;
        gbc_locationlabel.gridy = 5;
        panel.add(locationLabel, gbc_locationlabel);

        locationtext = new JTextField();
        GridBagConstraints gbc_locationtext = new GridBagConstraints();
        gbc_locationtext.fill = GridBagConstraints.HORIZONTAL;
        gbc_locationtext.insets = new Insets(0, 0, 5, 5);
        gbc_locationtext.gridx = 1;
        gbc_locationtext.gridy = 5;
        panel.add(locationtext, gbc_locationtext);
        locationtext.setColumns(10);
        locationtext.setText("$BASEDIR/config");

        final JButton loationbutton = new JButton("...");
        loationbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(parent);
                if(result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    locationtext.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        GridBagConstraints gbc_locationbutton = new GridBagConstraints();
        gbc_locationbutton.insets = new Insets(0, 0, 5, 5);
        gbc_locationbutton.gridx = 2;
        gbc_locationbutton.gridy = 5;
        panel.add(loationbutton, gbc_locationbutton);
    }

    private void constructGenerateToolSection(JPanel panel) {
        JLabel toolsLabel = new JLabel("Generate config files for");
        GridBagConstraints gbc_toolslabel = new GridBagConstraints();
        gbc_toolslabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_toolslabel.insets = new Insets(0, 0, 5, 5);
        gbc_toolslabel.gridx = 0;
        gbc_toolslabel.gridy = 2;
        panel.add(toolsLabel, gbc_toolslabel);

        resyncheckbox = new JCheckBox("ASGresyn");
        GridBagConstraints gbc_resyncheckbox = new GridBagConstraints();
        gbc_resyncheckbox.anchor = GridBagConstraints.NORTHWEST;
        gbc_resyncheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_resyncheckbox.gridx = 1;
        gbc_resyncheckbox.gridy = 2;
        panel.add(resyncheckbox, gbc_resyncheckbox);
        resyncheckbox.setSelected(true);

        logiccheckbox = new JCheckBox("ASGlogic");
        GridBagConstraints gbc_logiccheckbox = new GridBagConstraints();
        gbc_logiccheckbox.anchor = GridBagConstraints.NORTHWEST;
        gbc_logiccheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_logiccheckbox.gridx = 1;
        gbc_logiccheckbox.gridy = 3;
        panel.add(logiccheckbox, gbc_logiccheckbox);
        logiccheckbox.setSelected(true);

        dmcheckbox = new JCheckBox("ASGdelaymatch");
        GridBagConstraints gbc_dmcheckbox = new GridBagConstraints();
        gbc_dmcheckbox.anchor = GridBagConstraints.NORTHWEST;
        gbc_dmcheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_dmcheckbox.gridx = 1;
        gbc_dmcheckbox.gridy = 4;
        panel.add(dmcheckbox, gbc_dmcheckbox);
        dmcheckbox.setSelected(true);
    }

    private void constructGenerateOsSection(JPanel panel) {
        JLabel osLabel = new JLabel("Operating system");
        GridBagConstraints gbc_oslabel = new GridBagConstraints();
        gbc_oslabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_oslabel.insets = new Insets(0, 0, 5, 5);
        gbc_oslabel.gridx = 0;
        gbc_oslabel.gridy = 0;
        panel.add(osLabel, gbc_oslabel);

        unixButton = new JRadioButton("Unix-based");
        unixButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    for(Entry<String, String[]> entry : Configuration.getDefaultvalues().entrySet()) {
                        if(entries.get(entry.getKey()).getText().equals(entry.getValue()[1])) {
                            entries.get(entry.getKey()).setText(entry.getValue()[0]);
                        }
                    }
                    if(resyncheckbox != null && logiccheckbox != null && dmcheckbox != null) {
                        resyncheckbox.setEnabled(true);
                        resyncheckbox.setSelected(true);
                        dmcheckbox.setEnabled(true);
                        dmcheckbox.setSelected(true);
                    }
                } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                } else {
                    System.out.println("error");
                }
            }
        });
        GridBagConstraints gbc_unixButton = new GridBagConstraints();
        gbc_unixButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_unixButton.insets = new Insets(0, 0, 5, 5);
        gbc_unixButton.gridx = 1;
        gbc_unixButton.gridy = 0;
        panel.add(unixButton, gbc_unixButton);
        unixButton.setSelected(true);

        windowsButton = new JRadioButton("Windows");
        windowsButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    for(Entry<String, String[]> entry : Configuration.getDefaultvalues().entrySet()) {
                        if(entries.get(entry.getKey()).getText().equals(entry.getValue()[0])) {
                            entries.get(entry.getKey()).setText(entry.getValue()[1]);
                        }
                    }
                    if(resyncheckbox != null && logiccheckbox != null && dmcheckbox != null) {
                        resyncheckbox.setEnabled(false);
                        resyncheckbox.setSelected(false);
                        dmcheckbox.setEnabled(false);
                        dmcheckbox.setSelected(false);
                    }
                } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                } else {
                    System.out.println("error");
                }
            }
        });
        GridBagConstraints gbc_windowsButton = new GridBagConstraints();
        gbc_windowsButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_windowsButton.insets = new Insets(0, 0, 5, 5);
        gbc_windowsButton.gridx = 1;
        gbc_windowsButton.gridy = 1;
        panel.add(windowsButton, gbc_windowsButton);

        ButtonGroup group = new ButtonGroup();
        group.add(unixButton);
        group.add(windowsButton);
    }

    private void constructToolsPanel(JTabbedPane tabbedPane) {
        JPanel toolsPanel = new JPanel();
        tabbedPane.addTab("External tools", null, toolsPanel, null);
        GridBagLayout gbl_toolspanel = new GridBagLayout();
        gbl_toolspanel.columnWidths = new int[]{100, 300, 50, 100, 0};
        gbl_toolspanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_toolspanel.rowHeights = new int[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 0};
        gbl_toolspanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        toolsPanel.setLayout(gbl_toolspanel);

        int i = 0;
        for(Entry<String, String[]> entry : Configuration.getDefaultvalues().entrySet()) {
            generateToolEntry(toolsPanel, i++, entry.getKey(), entry.getValue()[0]);
        }
    }

    private void constructRemotePanel(JTabbedPane tabbedPane) {
        JPanel remotePanel = new JPanel();
        tabbedPane.addTab("Remote login", null, remotePanel, null);
        GridBagLayout gbl_remotepanel = new GridBagLayout();
        gbl_remotepanel.columnWidths = new int[]{150, 200, 0};
        gbl_remotepanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_remotepanel.rowHeights = new int[]{45, 15, 15, 15, 15, 15, 15, 15, 0};
        gbl_remotepanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        remotePanel.setLayout(gbl_remotepanel);

        JLabel whyLabel = new JLabel("<html><body>We assume that proprietary tools (like Design Compiler) are located<br>on a dedicated server. Please provide the appropiate login data:</body></html>");
        GridBagConstraints gbc_whylabel = new GridBagConstraints();
        gbc_whylabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_whylabel.insets = new Insets(0, 0, 5, 5);
        gbc_whylabel.gridx = 0;
        gbc_whylabel.gridwidth = 2;
        gbc_whylabel.gridy = 0;
        remotePanel.add(whyLabel, gbc_whylabel);

        generateRemoteEntry(remotePanel, 2, Configuration.getRemoteStrings()[0], false);
        generateRemoteEntry(remotePanel, 3, Configuration.getRemoteStrings()[1], false);
        generateRemoteEntry(remotePanel, 4, Configuration.getRemoteStrings()[2], true);
        generateRemoteEntry(remotePanel, 5, Configuration.getRemoteStrings()[3], false);

        JLabel warningLabel = new JLabel("! Note that passwords will be stored in the config file as plain text !");
        GridBagConstraints gbc_warning = new GridBagConstraints();
        gbc_warning.fill = GridBagConstraints.HORIZONTAL;
        gbc_warning.insets = new Insets(0, 0, 5, 5);
        gbc_warning.gridx = 0;
        gbc_warning.gridwidth = 2;
        gbc_warning.gridy = 7;
        remotePanel.add(warningLabel, gbc_warning);
    }

    private void generateRemoteEntry(JPanel panel, int row, String name, boolean isPassword) {
        JLabel label = new JLabel(name);
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.anchor = GridBagConstraints.WEST;
        gbc_label.gridx = 0;
        gbc_label.gridy = row;
        panel.add(label, gbc_label);

        JTextField text = null;
        if(isPassword) {
            text = new JPasswordField();
            text.setToolTipText("This is the textfield's tooltip");
        } else {
            text = new JTextField();
        }
        entries.put(name, text);

        GridBagConstraints gbc_text = new GridBagConstraints();
        gbc_text.fill = GridBagConstraints.HORIZONTAL;
        gbc_text.insets = new Insets(0, 0, 5, 5);
        gbc_text.gridx = 1;
        gbc_text.gridy = row;
        panel.add(text, gbc_text);
        text.setColumns(10);
        text.setText("");
        text.setEnabled(true);
    }

    private void generateToolEntry(JPanel panel, int row, final String name, final String defaultVal) {
        JLabel label = new JLabel(name);
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.anchor = GridBagConstraints.WEST;
        gbc_label.gridx = 0;
        gbc_label.gridy = row;
        panel.add(label, gbc_label);

        final JTextField pathtext = new JTextField();
        entries.put(name, pathtext);

        GridBagConstraints gbc_pathtext = new GridBagConstraints();
        gbc_pathtext.fill = GridBagConstraints.HORIZONTAL;
        gbc_pathtext.insets = new Insets(0, 0, 5, 5);
        gbc_pathtext.gridx = 1;
        gbc_pathtext.gridy = row;
        panel.add(pathtext, gbc_pathtext);
        pathtext.setColumns(10);
        pathtext.setText(defaultVal);
        pathtext.setEnabled(false);

        final JButton pathbutton = new JButton("...");
        pathbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(parent);
                if(result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    pathtext.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        GridBagConstraints gbc_pathbutton = new GridBagConstraints();
        gbc_pathbutton.insets = new Insets(0, 0, 5, 5);
        gbc_pathbutton.gridx = 2;
        gbc_pathbutton.gridy = row;
        panel.add(pathbutton, gbc_pathbutton);
        pathbutton.setEnabled(false);

        JCheckBox defaultcheckbox = new JCheckBox("Use default");
        defaultcheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    pathbutton.setEnabled(false);
                    if(unixButton != null && windowsButton != null) {
                        if(unixButton.isSelected()) {
                            pathtext.setText(Configuration.getDefaultvalues().get(name)[0]);
                        } else if(windowsButton.isSelected()) {
                            pathtext.setText(Configuration.getDefaultvalues().get(name)[1]);
                        } else {
                            System.out.println("error");
                        }
                    }
                    pathtext.setEnabled(false);
                } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                    pathbutton.setEnabled(true);
                    pathtext.setText("");
                    pathtext.setEnabled(true);
                } else {
                    System.out.println("error");
                }
            }
        });
        GridBagConstraints gbc_defaultcheckbox = new GridBagConstraints();
        gbc_defaultcheckbox.anchor = GridBagConstraints.NORTHWEST;
        gbc_defaultcheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_defaultcheckbox.gridx = 3;
        gbc_defaultcheckbox.gridy = row;
        panel.add(defaultcheckbox, gbc_defaultcheckbox);
        defaultcheckbox.setSelected(true);
    }

    public String getEntryValue(String key) {
        return entries.get(key).getText();
    }

    public boolean isUnixSelected() {
        return unixButton.isSelected();
    }

    public boolean isWindowsSelected() {
        return windowsButton.isSelected();
    }

    public boolean isResynSelected() {
        return resyncheckbox.isSelected();
    }

    public boolean isLogicSelected() {
        return logiccheckbox.isSelected();
    }

    public boolean isDelayMatchSelected() {
        return dmcheckbox.isSelected();
    }

    public String getOutputDir() {
        return locationtext.getText();
    }
}
