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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.uni_potsdam.hpi.asg.configgen.Configuration;
import de.uni_potsdam.hpi.asg.logictool.io.Config;
import de.uni_potsdam.hpi.asg.logictool.io.ToolConfig;

public class LogicGenerator {
    private static final String outfile = "logicconfig.xml";

    private Configuration       config;

    public LogicGenerator(Configuration config) {
        this.config = config;
    }

    public void generate() {
        Config logicconfig = new Config();
        logicconfig.workdir = "";
        logicconfig.toolconfig = new ToolConfig();
        logicconfig.toolconfig.desijcmd = formatCmd(config.getDesijCmd());
        logicconfig.toolconfig.petrifycmd = formatCmd(config.getPetrifyCmd());
        logicconfig.toolconfig.mpsatcmd = formatCmd(config.getMPSATCmd());
        logicconfig.toolconfig.punfcmd = formatCmd(config.getPUNFCmd());
        logicconfig.toolconfig.espressocmd = formatCmd(config.getEspressoCmd());

        File file = new File(config.getOutputDir(), outfile);
        if(!file.getParentFile().exists()) {
            if(!file.getParentFile().mkdirs()) {
                System.err.println("Failed to mkdir");
            }
        }
        LogicGenerator.writeOut(logicconfig, file.getAbsolutePath());
        System.out.println("Generated " + file.getAbsolutePath());
    }

    private String formatCmd(String cmd) {
        if(cmd.equals(Configuration.notapplicableStr)) {
            return null;
        }
        return cmd;
    }

    public static boolean writeOut(Config cfg, String filename) {
        try {
            Writer fw = new FileWriter(filename);
            JAXBContext context = JAXBContext.newInstance(Config.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(cfg, fw);
            return true;
        } catch(JAXBException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        } catch(IOException e) {
            System.err.println(e.getLocalizedMessage());
            return false;
        }
    }
}
