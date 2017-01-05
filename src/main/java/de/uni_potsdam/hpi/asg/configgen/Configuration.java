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

public class Configuration {

    private static String[]              remoteStrings = {"Hostname", "Username", "Password", "Working directory"};
    private static Map<String, String[]> defaultvalues;
    static {
        defaultvalues = new LinkedHashMap<>();
        defaultvalues.put("DesiJ", new String[]{"$BASEDIR/bin/DesiJ", "$BASEDIR/bin/DesiJ.bat"});
        defaultvalues.put("Balsa-c", new String[]{"$BASEDIR/tools/balsa/bin/balsa-c", "N/A"});
        defaultvalues.put("Balsa-netlist", new String[]{"$BASEDIR/tools/balsa/bin/balsa-netlist", "N/A"});
        defaultvalues.put("Petrify", new String[]{"$BASEDIR/tools/petrify/petrify", "$BASEDIR/tools/petrify/petrify.exe"});
        defaultvalues.put("PUNF", new String[]{"$BASEDIR/tools/punf/punf", "$BASEDIR/tools/punf/punf.exe"});
        defaultvalues.put("MPSAT", new String[]{"$BASEDIR/tools/mpsat/mpsat", "$BASEDIR/tools/mpsat/mpsat.exe"});
        defaultvalues.put("ASGlogic", new String[]{"$BASEDIR/bin/ASGlogic", "$BASEDIR/bin/ASGlogic.bat"});
        defaultvalues.put("Espresso", new String[]{"$BASEDIR/tools/espresso/espresso", "$BASEDIR/tools/espresso/espresso.exe"});
    }

    private ConfigFrame frame;

    public void setFrame(ConfigFrame frame) {
        this.frame = frame;
    }

    public static Map<String, String[]> getDefaultvalues() {
        return defaultvalues;
    }

    public static String[] getRemoteStrings() {
        return remoteStrings;
    }

    public String getDesijCmd() {
        return frame.getEntryValue("DesiJ");
    }

    public String getBalsaCCmd() {
        return frame.getEntryValue("Balsa-c");
    }

    public String getBalsaNetlistCmd() {
        return frame.getEntryValue("Balsa-netlist");
    }

    public String getPetrifyCmd() {
        return frame.getEntryValue("Petrify");
    }

    public String getPUNFCmd() {
        return frame.getEntryValue("PUNF");
    }

    public String getMPSATCmd() {
        return frame.getEntryValue("MPSAT");
    }

    public String getASGlogicCmd() {
        return frame.getEntryValue("ASGlogic");
    }

    public String getEspressoCmd() {
        return frame.getEntryValue("Espresso");
    }

    public String getRemoteHostname() {
        return frame.getEntryValue("Hostname");
    }

    public String getRemoteUsername() {
        return frame.getEntryValue("Username");
    }

    public String getRemotePassword() {
        return frame.getEntryValue("Password");
    }

    public String getRemotWorkingDirectory() {
        return frame.getEntryValue("Working directory");
    }

    public boolean isUnixSelected() {
        return frame.isUnixSelected();
    }

    public boolean isWindowsSelected() {
        return frame.isWindowsSelected();
    }

    public boolean isResynSelected() {
        return frame.isResynSelected();
    }

    public boolean isLogicSelected() {
        return frame.isLogicSelected();
    }

    public boolean isDelayMatchSelected() {
        return frame.isDelayMatchSelected();
    }

    public String getOutputDir() {
        return frame.getOutputDir();
    }
}
