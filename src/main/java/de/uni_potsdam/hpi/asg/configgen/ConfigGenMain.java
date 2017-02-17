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

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.uni_potsdam.hpi.asg.common.iohelper.LoggerHelper;
import de.uni_potsdam.hpi.asg.common.iohelper.LoggerHelper.Mode;
import de.uni_potsdam.hpi.asg.common.technology.TechnologyDirectory;
import de.uni_potsdam.hpi.asg.delaymatch.DelayMatchMain;
import de.uni_potsdam.hpi.asg.logictool.LogicMain;
import de.uni_potsdam.hpi.asg.resyntool.ResynMain;

public class ConfigGenMain {

    public static final String DELAYMATCH_CONFIG = DelayMatchMain.DEF_CONFIG_FILE_NAME;
    public static final String RESYN_CONFIG      = ResynMain.DEF_CONFIG_FILE_NAME;
    public static final String LOGIC_CONFIG      = LogicMain.DEF_CONFIG_FILE_NAME;

    public static void main(String[] args) {
        LoggerHelper.initLogger(3, null, false, Mode.gui);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            return;
        }

        TechnologyDirectory techDir = TechnologyDirectory.createDefault();
        if(techDir == null) {
            return;
        }

        ConfigWindowAdapter adapt = new ConfigWindowAdapter();
        ConfigFrame cframe = new ConfigFrame(new Configuration(techDir), adapt);
        cframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cframe.pack();
        cframe.setLocationRelativeTo(null); //center
        cframe.setVisible(true);

        while(!adapt.isClosed()) {
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
            }
        }
    }
}
