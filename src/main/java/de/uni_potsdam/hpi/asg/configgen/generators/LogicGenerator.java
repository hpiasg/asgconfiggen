package de.uni_potsdam.hpi.asg.configgen.generators;

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

import java.io.File;

import de.uni_potsdam.hpi.asg.common.technology.TechnologyDirectory;
import de.uni_potsdam.hpi.asg.configgen.ConfigGenMain;
import de.uni_potsdam.hpi.asg.configgen.Configuration;
import de.uni_potsdam.hpi.asg.configgen.Configuration.BooleanParam;
import de.uni_potsdam.hpi.asg.configgen.Configuration.EnumParam;
import de.uni_potsdam.hpi.asg.configgen.Configuration.TextParam;
import de.uni_potsdam.hpi.asg.logictool.io.Config;
import de.uni_potsdam.hpi.asg.logictool.io.ToolConfig;

public class LogicGenerator {
    private static final String outfile = "logicconfig.xml";

    private Configuration       config;
    private File                file;

    public LogicGenerator(Configuration config) {
        this.config = config;
    }

    public boolean generate() {
        Config logicconfig = new Config();
        logicconfig.workdir = "";
        if(!config.getBooleanValue(BooleanParam.defaultTechDeActivated)) {
            String techname = config.getEnumValue(EnumParam.defaultTech);
            logicconfig.defaultTech = ConfigGenMain.techdir + "/" + techname + TechnologyDirectory.genlibfileExtension;
        }
        logicconfig.toolconfig = new ToolConfig();
        logicconfig.toolconfig.desijcmd = ConfigExportHelper.formatCmd(config.getTextValue(TextParam.DesiJ));
        logicconfig.toolconfig.petrifycmd = ConfigExportHelper.formatCmd(config.getTextValue(TextParam.Petrify));
        logicconfig.toolconfig.mpsatcmd = ConfigExportHelper.formatCmd(config.getTextValue(TextParam.MPSAT));
        logicconfig.toolconfig.punfcmd = ConfigExportHelper.formatCmd(config.getTextValue(TextParam.PUNF));
        logicconfig.toolconfig.espressocmd = ConfigExportHelper.formatCmd(config.getTextValue(TextParam.Espresso));

        file = new File(config.getTextValue(TextParam.OutDir), outfile);
        return ConfigExportHelper.writeOut(Config.class, logicconfig, file);
    }

    public File getFile() {
        return file;
    }
}
