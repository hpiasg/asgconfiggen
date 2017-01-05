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
import de.uni_potsdam.hpi.asg.resyntool.io.Config;
import de.uni_potsdam.hpi.asg.resyntool.io.RemoteInvocation;
import de.uni_potsdam.hpi.asg.resyntool.io.ToolConfig;

public class ResynGenerator {
    private static final String outfile = "resynconfig.xml";

    private Configuration       config;

    public ResynGenerator(Configuration config) {
        this.config = config;
    }

    public void generate() {
        Config resynconfig = new Config();
        resynconfig.componentconfig = "";
        resynconfig.workdir = "";
        resynconfig.toolconfig = new ToolConfig();
        resynconfig.toolconfig.desijcmd = formatCmd(config.getDesijCmd());
        resynconfig.toolconfig.balsaccmd = formatCmd(config.getBalsaCCmd());
        resynconfig.toolconfig.balsanetlistcmd = formatCmd(config.getBalsaNetlistCmd());
        resynconfig.toolconfig.petrifycmd = formatCmd(config.getPetrifyCmd());
        resynconfig.toolconfig.petresetcmd = formatCmd(config.getPetresetCmd());
        resynconfig.toolconfig.mpsatcmd = formatCmd(config.getMPSATCmd());
        resynconfig.toolconfig.punfcmd = formatCmd(config.getPUNFCmd());
        resynconfig.toolconfig.asglogiccmd = formatCmd(config.getASGlogicCmd());
        resynconfig.toolconfig.designCompilerCmd = new RemoteInvocation();
        resynconfig.toolconfig.designCompilerCmd.hostname = config.getRemoteHostname();
        resynconfig.toolconfig.designCompilerCmd.username = config.getRemoteUsername();
        resynconfig.toolconfig.designCompilerCmd.password = config.getRemotePassword();
        resynconfig.toolconfig.designCompilerCmd.workingdir = config.getRemotWorkingDirectory();

        File file = new File(config.getOutputDir(), outfile);
        if(!file.getParentFile().exists()) {
            if(!file.getParentFile().mkdirs()) {
                System.err.println("Failed to mkdir");
            }
        }
        ResynGenerator.writeOut(resynconfig, file.getAbsolutePath());
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
