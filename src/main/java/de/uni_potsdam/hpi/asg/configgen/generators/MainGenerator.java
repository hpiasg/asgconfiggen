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

import de.uni_potsdam.hpi.asg.configgen.Configuration;
import de.uni_potsdam.hpi.asg.configgen.Configuration.BooleanParam;

public class MainGenerator {

    private Configuration config;

    public MainGenerator(Configuration config) {
        this.config = config;
    }

    public void generate() {
        if(config.getBooleanValue(BooleanParam.resyn)) {
            ResynGenerator rgen = new ResynGenerator(config);
            rgen.generate();
        }
        if(config.getBooleanValue(BooleanParam.logic)) {
            LogicGenerator lgen = new LogicGenerator(config);
            lgen.generate();
        }
        if(config.getBooleanValue(BooleanParam.delaymatch)) {
            DelayMatchGenerator dmgen = new DelayMatchGenerator(config);
            dmgen.generate();
        }
    }
}
