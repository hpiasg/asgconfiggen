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

import de.uni_potsdam.hpi.asg.configgen.Configuration;
import de.uni_potsdam.hpi.asg.delaymatch.io.Config;
import de.uni_potsdam.hpi.asg.delaymatch.io.RemoteInvocation;
import de.uni_potsdam.hpi.asg.delaymatch.io.ToolConfig;

public class DelayMatchGenerator {
    private static final String outfile = "delaymatchconfig.xml";

    private Configuration       config;

    public DelayMatchGenerator(Configuration config) {
        this.config = config;
    }

    public void generate() {
        Config delaymatchconfig = new Config();
        delaymatchconfig.workdir = "";
        delaymatchconfig.toolconfig = new ToolConfig();
        delaymatchconfig.toolconfig.designCompilerCmd = new RemoteInvocation();
        delaymatchconfig.toolconfig.designCompilerCmd.hostname = config.getRemoteHostname();
        delaymatchconfig.toolconfig.designCompilerCmd.username = config.getRemoteUsername();
        delaymatchconfig.toolconfig.designCompilerCmd.password = config.getRemotePassword();
        delaymatchconfig.toolconfig.designCompilerCmd.workingdir = config.getRemotWorkingDirectory();

        File file = new File(config.getOutputDir(), outfile);
        if(!ConfigExportHelper.writeOut(Config.class, delaymatchconfig, file)) {
            System.err.println("Failed to generate " + file.getAbsolutePath());
            return;
        }
        System.out.println("Generated " + file.getAbsolutePath());
    }
}
