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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import de.uni_potsdam.hpi.asg.common.gui.PropertiesFrame;
import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel;
import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel.AbstractTextParam;
import de.uni_potsdam.hpi.asg.configgen.Configuration.BooleanParam;
import de.uni_potsdam.hpi.asg.configgen.Configuration.TextParam;
import de.uni_potsdam.hpi.asg.configgen.generators.MainGenerator;

public class ConfigFrame extends PropertiesFrame {
    private static final long serialVersionUID = -4879956586784429087L;

    private Configuration     config;

    public ConfigFrame(Configuration config, WindowAdapter adapt) {
        super("ASGconfiggen");
        this.config = config;
        this.config.setFrame(this);
        this.addWindowListener(adapt);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        constructRemotePanel(tabbedPane);
        constructToolsPanel(tabbedPane);
        constructGeneratePanel(tabbedPane);
    }

    private void constructRemotePanel(JTabbedPane tabbedPane) {
        PropertiesPanel remotePanel = new PropertiesPanel(this);
        tabbedPane.addTab("Remote login", null, remotePanel, null);
        GridBagLayout gbl_remotepanel = new GridBagLayout();
        gbl_remotepanel.columnWidths = new int[]{150, 200, 0};
        gbl_remotepanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_remotepanel.rowHeights = new int[]{45, 15, 15, 15, 15, 15, 15, 15, 0};
        gbl_remotepanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        remotePanel.setLayout(gbl_remotepanel);

        JLabel whyLabel = new JLabel("<html><body>We assume that proprietary tools (like Design Compiler) are located<br>on a dedicated server. Please provide the appropiate login data:</body></html>");
        GridBagConstraints gbc_whylabel = new GridBagConstraints();
        gbc_whylabel.anchor = GridBagConstraints.LINE_START;
        gbc_whylabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_whylabel.insets = new Insets(0, 0, 5, 5);
        gbc_whylabel.gridx = 0;
        gbc_whylabel.gridwidth = 2;
        gbc_whylabel.gridy = 0;
        remotePanel.add(whyLabel, gbc_whylabel);

        remotePanel.addTextEntry(2, TextParam.Hostname, Configuration.getRemoteStrings()[0], "", false, null, false);
        remotePanel.addTextEntry(3, TextParam.Username, Configuration.getRemoteStrings()[1], "", false, null, false);
        addPasswordEntry(remotePanel, 4, TextParam.Password, Configuration.getRemoteStrings()[2], "");
        remotePanel.addTextEntry(5, TextParam.WorkingDir, Configuration.getRemoteStrings()[3], "", false, null, false);

        JLabel warningLabel = new JLabel("! Note that passwords will be stored in the config file as plain text !");
        GridBagConstraints gbc_warning = new GridBagConstraints();
        gbc_warning.anchor = GridBagConstraints.LINE_START;
        gbc_warning.fill = GridBagConstraints.HORIZONTAL;
        gbc_warning.insets = new Insets(0, 0, 5, 5);
        gbc_warning.gridx = 0;
        gbc_warning.gridwidth = 2;
        gbc_warning.gridy = 7;
        remotePanel.add(warningLabel, gbc_warning);

        getDataFromPanel(remotePanel);
    }

    private void constructToolsPanel(JTabbedPane tabbedPane) {
        PropertiesPanel toolsPanel = new PropertiesPanel(this);
        tabbedPane.addTab("External tools", null, toolsPanel, null);
        GridBagLayout gbl_toolspanel = new GridBagLayout();
        gbl_toolspanel.columnWidths = new int[]{100, 300, 50, 100, 0};
        gbl_toolspanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_toolspanel.rowHeights = new int[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 0};
        gbl_toolspanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        toolsPanel.setLayout(gbl_toolspanel);

        int i = 0;
        for(Entry<TextParam, String[]> entry : Configuration.getToolsValues().entrySet()) {
            constructToolsEntry(toolsPanel, i++, entry.getKey(), entry.getValue()[0]);
        }

        getDataFromPanel(toolsPanel);
    }

    private void constructToolsEntry(PropertiesPanel panel, int row, AbstractTextParam paramName, String labelStr) {
        panel.addLabelCell(row, labelStr);
        final JTextField textfield = panel.addTextfieldCell(row, paramName, "", true);
        final JButton pathbutton = panel.addPathButtonCell(row, JFileChooser.FILES_ONLY, true, textfield);
        constructToolsDefaultCheckboxCell(panel, row, paramName, textfield, pathbutton);
    }

    private void constructToolsDefaultCheckboxCell(JPanel panel, int row, final AbstractTextParam paramName, final JTextField textfield, final JButton pathbutton) {
        JCheckBox defaultcheckbox = new JCheckBox("Use default");
        defaultcheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    final AbstractButton unixButton = buttons.get(BooleanParam.unix);
                    final AbstractButton windowsButton = buttons.get(BooleanParam.windows);
                    pathbutton.setEnabled(false);
                    if(unixButton != null && windowsButton != null) {
                        if(unixButton.isSelected()) {
                            textfield.setText(Configuration.getToolsValues().get(paramName)[1]);
                        } else if(windowsButton.isSelected()) {
                            textfield.setText(Configuration.getToolsValues().get(paramName)[2]);
                        } else {
                            System.out.println("error");
                        }
                    }
                    textfield.setEnabled(false);
                } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                    pathbutton.setEnabled(true);
                    textfield.setText("");
                    textfield.setEnabled(true);
                } else {
                    System.out.println("error");
                }
            }
        });
        GridBagConstraints gbc_defaultcheckbox = new GridBagConstraints();
        gbc_defaultcheckbox.anchor = GridBagConstraints.LINE_START;
        gbc_defaultcheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_defaultcheckbox.gridx = 3;
        gbc_defaultcheckbox.gridy = row;
        panel.add(defaultcheckbox, gbc_defaultcheckbox);
        defaultcheckbox.setSelected(true);
    }

    private void constructGeneratePanel(JTabbedPane tabbedPane) {
        PropertiesPanel panel = new PropertiesPanel(this);
        tabbedPane.addTab("Generate", null, panel, null);
        GridBagLayout gbl_genpanel = new GridBagLayout();
        gbl_genpanel.columnWidths = new int[]{170, 300, 50, 80, 0};
        gbl_genpanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_genpanel.rowHeights = new int[]{15, 15, 15, 15, 15, 15, 15, 15, 0};
        gbl_genpanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_genpanel);

        constructGenerateOsSection(panel);
        constructGenerateToolSection(panel);
        panel.addTextEntry(5, TextParam.OutDir, "Output directory", ConfigGenMain.defaultConfigDir, true, JFileChooser.DIRECTORIES_ONLY, true);

        final JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainGenerator gen = new MainGenerator(config);
                gen.generate();
            }
        });
        GridBagConstraints gbc_generatebutton = new GridBagConstraints();
        gbc_generatebutton.anchor = GridBagConstraints.CENTER;
        gbc_generatebutton.insets = new Insets(0, 0, 5, 5);
        gbc_generatebutton.gridx = 0;
        gbc_generatebutton.gridy = 7;
        gbc_generatebutton.gridwidth = 4;
        panel.add(generateButton, gbc_generatebutton);

        getDataFromPanel(panel);
        detectOperatingSystem();
    }

    private void detectOperatingSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")) {
            buttons.get(BooleanParam.windows).setSelected(true);
            buttons.get(BooleanParam.unix).setSelected(false);
            setWindows(true);
        } else {
            buttons.get(BooleanParam.windows).setSelected(false);
            buttons.get(BooleanParam.unix).setSelected(true);
            setUnix(true);
        }
    }

    private void constructGenerateToolSection(PropertiesPanel panel) {
        panel.addLabelCell(2, "Generate config files for");

        AbstractButton resyncheckbox = new JCheckBox("ASGresyn");
        buttons.put(BooleanParam.resyn, resyncheckbox);
        GridBagConstraints gbc_resyncheckbox = new GridBagConstraints();
        gbc_resyncheckbox.anchor = GridBagConstraints.LINE_START;
        gbc_resyncheckbox.anchor = GridBagConstraints.NORTHWEST;
        gbc_resyncheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_resyncheckbox.gridx = 1;
        gbc_resyncheckbox.gridy = 2;
        panel.add(resyncheckbox, gbc_resyncheckbox);
        resyncheckbox.setSelected(true);

        AbstractButton logiccheckbox = new JCheckBox("ASGlogic");
        buttons.put(BooleanParam.logic, logiccheckbox);
        GridBagConstraints gbc_logiccheckbox = new GridBagConstraints();
        gbc_logiccheckbox.anchor = GridBagConstraints.LINE_START;
        gbc_logiccheckbox.anchor = GridBagConstraints.NORTHWEST;
        gbc_logiccheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_logiccheckbox.gridx = 1;
        gbc_logiccheckbox.gridy = 3;
        panel.add(logiccheckbox, gbc_logiccheckbox);
        logiccheckbox.setSelected(true);

        AbstractButton dmcheckbox = new JCheckBox("ASGdelaymatch");
        buttons.put(BooleanParam.delaymatch, dmcheckbox);
        GridBagConstraints gbc_dmcheckbox = new GridBagConstraints();
        gbc_dmcheckbox.anchor = GridBagConstraints.LINE_START;
        gbc_dmcheckbox.anchor = GridBagConstraints.NORTHWEST;
        gbc_dmcheckbox.insets = new Insets(0, 0, 5, 0);
        gbc_dmcheckbox.gridx = 1;
        gbc_dmcheckbox.gridy = 4;
        panel.add(dmcheckbox, gbc_dmcheckbox);
        dmcheckbox.setSelected(true);
    }

    private void constructGenerateOsSection(PropertiesPanel panel) {
        panel.addLabelCell(0, "Operating system");

        AbstractButton unixButton = new JRadioButton("Unix-like");
        buttons.put(BooleanParam.unix, unixButton);
        unixButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    setUnix(false);
                } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                } else {
                    System.out.println("error");
                }
            }
        });
        GridBagConstraints gbc_unixButton = new GridBagConstraints();
        gbc_unixButton.anchor = GridBagConstraints.LINE_START;
        gbc_unixButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_unixButton.insets = new Insets(0, 0, 5, 5);
        gbc_unixButton.gridx = 1;
        gbc_unixButton.gridy = 0;
        panel.add(unixButton, gbc_unixButton);
        unixButton.setSelected(true);

        AbstractButton windowsButton = new JRadioButton("Windows");
        buttons.put(BooleanParam.windows, windowsButton);
        windowsButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    setWindows(false);
                } else if(e.getStateChange() == ItemEvent.DESELECTED) {
                } else {
                    System.out.println("error");
                }
            }
        });
        GridBagConstraints gbc_windowsButton = new GridBagConstraints();
        gbc_windowsButton.anchor = GridBagConstraints.LINE_START;
        gbc_windowsButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_windowsButton.insets = new Insets(0, 0, 5, 5);
        gbc_windowsButton.gridx = 1;
        gbc_windowsButton.gridy = 1;
        panel.add(windowsButton, gbc_windowsButton);

        ButtonGroup group = new ButtonGroup();
        group.add(unixButton);
        group.add(windowsButton);
    }

    private void setUnix(boolean enforce) {
        for(Entry<TextParam, String[]> entry : Configuration.getToolsValues().entrySet()) {
            if(textfields.get(entry.getKey()).getText().equals(entry.getValue()[2]) || enforce) {
                textfields.get(entry.getKey()).setText(entry.getValue()[1]);
            }
        }
        if(buttons.get(BooleanParam.resyn) != null && buttons.get(BooleanParam.logic) != null && buttons.get(BooleanParam.delaymatch) != null) {
            buttons.get(BooleanParam.resyn).setEnabled(true);
            buttons.get(BooleanParam.resyn).setSelected(true);
            buttons.get(BooleanParam.delaymatch).setEnabled(true);
            buttons.get(BooleanParam.delaymatch).setSelected(true);
        }
    }

    private void setWindows(boolean enforce) {
        for(Entry<TextParam, String[]> entry : Configuration.getToolsValues().entrySet()) {
            if(textfields.get(entry.getKey()).getText().equals(entry.getValue()[1]) || enforce) {
                textfields.get(entry.getKey()).setText(entry.getValue()[2]);
            }
        }
        if(buttons.get(BooleanParam.resyn) != null && buttons.get(BooleanParam.logic) != null && buttons.get(BooleanParam.delaymatch) != null) {
            buttons.get(BooleanParam.resyn).setEnabled(false);
            buttons.get(BooleanParam.resyn).setSelected(false);
            buttons.get(BooleanParam.delaymatch).setEnabled(false);
            buttons.get(BooleanParam.delaymatch).setSelected(false);
        }
    }

    private void addPasswordEntry(PropertiesPanel panel, int row, AbstractTextParam paramName, String labelStr, final String defaultvalue) {
        panel.addLabelCell(row, labelStr);

        JTextField text = new JPasswordField();
        textfields.put(paramName, text);

        GridBagConstraints gbc_text = new GridBagConstraints();
        gbc_text.anchor = GridBagConstraints.LINE_START;
        gbc_text.fill = GridBagConstraints.HORIZONTAL;
        gbc_text.insets = new Insets(0, 0, 5, 5);
        gbc_text.gridx = 1;
        gbc_text.gridy = row;
        panel.add(text, gbc_text);
        text.setColumns(10);
        text.setText(defaultvalue);
        text.setEnabled(true);
    }
}
