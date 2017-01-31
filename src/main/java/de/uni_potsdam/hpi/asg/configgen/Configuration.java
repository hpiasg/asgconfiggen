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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel.AbstractBooleanParam;
import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel.AbstractEnumParam;
import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel.AbstractTextParam;
import de.uni_potsdam.hpi.asg.common.technology.Technology;
import de.uni_potsdam.hpi.asg.common.technology.TechnologyDirectory;

public class Configuration {
    public static String notapplicableStr = "N/A";

    //@formatter:off
    public enum TextParam implements AbstractTextParam {
        /*remote*/ Hostname, Username, Password, WorkingDir,
        /*tools*/ DesiJ, BalsaC, BalsaNetlist, Petrify, PUNF, MPSAT, Petreset, ASGlogic, Espresso,
        /*generate*/ OutDir
    }

    public enum BooleanParam implements AbstractBooleanParam {
        /*remote*/ 
        /*tools*/ 
        /*generate*/ unix, windows, resyn, logic, delaymatch, defaultTechDeActivated
    }
    
    public enum EnumParam implements AbstractEnumParam {
        /*generate*/ defaultTech
    }
    //@formatter:on

    private static String[]                 remoteStrings = {"Hostname", "Username", "Password", "Working directory"};
    private static Map<TextParam, String[]> toolsValues;
    static {
        toolsValues = new LinkedHashMap<>();
        toolsValues.put(TextParam.DesiJ, new String[]{"DesiJ", "$BASEDIR/bin/DesiJ", "$BASEDIR/bin/DesiJ.bat"});
        toolsValues.put(TextParam.BalsaC, new String[]{"Balsa-c", "$BASEDIR/tools/balsa/bin/balsa-c", notapplicableStr});
        toolsValues.put(TextParam.BalsaNetlist, new String[]{"Balsa-netlist", "$BASEDIR/tools/balsa/bin/balsa-netlist", notapplicableStr});
        toolsValues.put(TextParam.Petrify, new String[]{"Petrify", "$BASEDIR/tools/petrify/petrify", "$BASEDIR/tools/petrify/petrify.exe"});
        toolsValues.put(TextParam.PUNF, new String[]{"PUNF", "$BASEDIR/tools/punf/punf", "$BASEDIR/tools/punf/punf.exe"});
        toolsValues.put(TextParam.MPSAT, new String[]{"MPSAT", "$BASEDIR/tools/mpsat/mpsat", "$BASEDIR/tools/mpsat/mpsat.exe"});
        toolsValues.put(TextParam.Petreset, new String[]{"Petreset", notapplicableStr, notapplicableStr});
        toolsValues.put(TextParam.ASGlogic, new String[]{"ASGlogic", "$BASEDIR/bin/ASGlogic", "$BASEDIR/bin/ASGlogic.bat"});
        toolsValues.put(TextParam.Espresso, new String[]{"Espresso", "$BASEDIR/tools/espresso/espresso", "$BASEDIR/tools/espresso/espresso.exe"});
    }

    private ConfigFrame frame;
    private String[]    techs;

    public Configuration(TechnologyDirectory techDir) {
        this.techs = getAvailableTechs(techDir);
    }

    public void setFrame(ConfigFrame frame) {
        this.frame = frame;
    }

    public static Map<TextParam, String[]> getToolsValues() {
        return toolsValues;
    }

    public static String[] getRemoteStrings() {
        return remoteStrings;
    }

    private String[] getAvailableTechs(TechnologyDirectory techDir) {
        if(techDir.getTechs().isEmpty()) {
            return null;
        }

        List<String> techs = new ArrayList<>();
        for(Technology t : techDir.getTechs()) {
            techs.add(t.getName());
        }

        String[] retVal = new String[techs.size()];
        return techs.toArray(retVal);
    }

    public String[] getAvailableTechs() {
        return techs;
    }

    public String getTextValue(TextParam param) {
        String str = frame.getTextValue(param);
        if(param == TextParam.OutDir) {
            str = replaceBasedir(str);
        }
        return str;
    }

    public boolean getBooleanValue(BooleanParam param) {
        return frame.getBooleanValue(param);
    }

    public String getEnumValue(EnumParam param) {
        int index = frame.getEnumValue(param);
        switch(param) {
            case defaultTech:
                return techs[index];
            default:
                return null;
        }
    }

    private String replaceBasedir(String str) {
        String basedir = System.getProperty("basedir");
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")) {
            basedir = basedir.replaceAll("\\\\", "/");
        }
        return str.replaceAll("\\$BASEDIR", basedir);
    }
}
