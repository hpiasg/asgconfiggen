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

import java.util.LinkedHashMap;
import java.util.Map;

import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel.AbstractBooleanParam;
import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel.AbstractEnumParam;
import de.uni_potsdam.hpi.asg.common.gui.PropertiesPanel.AbstractTextParam;
import de.uni_potsdam.hpi.asg.common.iohelper.BasedirHelper;
import de.uni_potsdam.hpi.asg.common.misc.CommonConstants;
import de.uni_potsdam.hpi.asg.common.technology.TechnologyDirectory;

public class Configuration {

    public static final String  notapplicableStr = "N/A";
    private static final String basedirStr       = CommonConstants.BASEDIR_STR;

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
        //@formatter:off
        //                                                    Tool              Unix default                                    Windows default
        toolsValues.put(TextParam.DesiJ, new String[]{        "DesiJ",          basedirStr + "/bin/DesiJ",                      basedirStr + "/bin/DesiJ.bat"               });
        toolsValues.put(TextParam.BalsaC, new String[]{       "Balsa-c",        basedirStr + "/tools/balsa/bin/balsa-c",        notapplicableStr                            });
        toolsValues.put(TextParam.BalsaNetlist, new String[]{ "Balsa-netlist",  basedirStr + "/tools/balsa/bin/balsa-netlist",  notapplicableStr                            });
        toolsValues.put(TextParam.Petrify, new String[]{      "Petrify",        basedirStr + "/tools/petrify/petrify",          basedirStr + "/tools/petrify/petrify.exe"   });
        toolsValues.put(TextParam.PUNF, new String[]{         "PUNF",           basedirStr + "/tools/punf/punf",                basedirStr + "/tools/punf/punf.exe"         });
        toolsValues.put(TextParam.MPSAT, new String[]{        "MPSAT",          basedirStr + "/tools/mpsat/mpsat",              basedirStr + "/tools/mpsat/mpsat.exe"       });
        toolsValues.put(TextParam.Petreset, new String[]{     "Petreset",       notapplicableStr,                               notapplicableStr                            });
        toolsValues.put(TextParam.ASGlogic, new String[]{     "ASGlogic",       basedirStr + "/bin/ASGlogic",                   basedirStr + "/bin/ASGlogic.bat"            });
        toolsValues.put(TextParam.Espresso, new String[]{     "Espresso",       basedirStr + "/tools/espresso/espresso",        basedirStr + "/tools/espresso/espresso.exe" });
        //@formatter:on
    }

    private ConfigFrame frame;
    private String[]    techs;

    public Configuration(TechnologyDirectory techDir) {
        this.techs = techDir.getTechNames();
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

    public String[] getAvailableTechs() {
        return techs;
    }

    public String getTextValue(TextParam param) {
        String str = frame.getTextValue(param);
        if(param == TextParam.OutDir) {
            str = BasedirHelper.replaceBasedir(str);
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
}
