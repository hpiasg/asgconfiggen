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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_potsdam.hpi.asg.configgen.Configuration;

public abstract class ConfigExportHelper {
    private static final Logger logger = LogManager.getLogger();

    @SuppressWarnings("rawtypes")
    public static boolean writeOut(Class cfgclass, Object cfg, File outfile) {
        if(!outfile.getParentFile().exists()) {
            if(!outfile.getParentFile().mkdirs()) {
                return false;
            }
        }
        String filepath = outfile.getAbsolutePath();

        Writer fw = null;
        try {
            try {
                fw = new FileWriter(filepath);
                JAXBContext context = JAXBContext.newInstance(cfgclass);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.marshal(cfg, fw);
                return true;
            } catch(JAXBException e) {
                logger.error(e.getLocalizedMessage());
                return false;
            } catch(IOException e) {
                logger.error(e.getLocalizedMessage());
                return false;
            } finally {
                fw.close();
            }
        } catch(IOException e) {
            return false;
        }
    }

    public static String formatCmd(String cmd) {
        if(cmd.equals(Configuration.notapplicableStr)) {
            return null;
        }
        return cmd;
    }
}
