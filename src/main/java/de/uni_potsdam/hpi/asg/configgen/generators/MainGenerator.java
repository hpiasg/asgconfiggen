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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_potsdam.hpi.asg.configgen.Configuration;
import de.uni_potsdam.hpi.asg.configgen.Configuration.BooleanParam;

public class MainGenerator {
    private static final Logger logger = LogManager.getLogger();

    private Configuration       config;

    public MainGenerator(Configuration config) {
        this.config = config;
    }

    public void generate() {
        StringBuilder msg = new StringBuilder();
        int fails = 0;
        int successes = 0;

        if(config.getBooleanValue(BooleanParam.resyn)) {
            ResynGenerator rgen = new ResynGenerator(config);
            if(rgen.generate()) {
                msg.append("SUCCESS: " + rgen.getFile().getAbsolutePath() + "\n");
                successes++;
            } else {
                msg.append("FAIL   : " + rgen.getFile().getAbsolutePath() + "\n");
                fails++;
            }
        }
        if(config.getBooleanValue(BooleanParam.logic)) {
            LogicGenerator lgen = new LogicGenerator(config);
            if(lgen.generate()) {
                msg.append("SUCCESS: " + lgen.getFile().getAbsolutePath() + "\n");
                successes++;
            } else {
                msg.append("FAIL   : " + lgen.getFile().getAbsolutePath() + "\n");
                fails++;
            }
        }
        if(config.getBooleanValue(BooleanParam.delaymatch)) {
            DelayMatchGenerator dmgen = new DelayMatchGenerator(config);
            if(dmgen.generate()) {
                msg.append("SUCCESS: " + dmgen.getFile().getAbsolutePath() + "\n");
                successes++;
            } else {
                msg.append("FAIL   : " + dmgen.getFile().getAbsolutePath() + "\n");
                fails++;
            }
        }

        if(fails > 0 && successes == 0) {
            logger.error(msg.toString());
        } else if(fails == 0 && successes > 0) {
            logger.info(msg.toString());
        } else {
            logger.warn(msg.toString());
        }

    }
}
