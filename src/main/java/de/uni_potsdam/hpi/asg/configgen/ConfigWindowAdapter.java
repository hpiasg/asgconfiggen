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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConfigWindowAdapter extends WindowAdapter {

    private boolean closed;

    public ConfigWindowAdapter() {
        this.closed = false;
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
}
