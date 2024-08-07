/*
 * Aoba Hacked Client
 * Copyright (C) 2019-2024 coltonk9043
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// An interface containing the most abstract definition of a Hud Element that will appear on the screen. 
package net.aoba.gui;

import net.aoba.gui.components.Component;

public interface IGuiElement {
    public Rectangle getSize();
    public Rectangle getActualSize();
    
    public void setSize(Rectangle size);
    public void addChild(Component child);
    public void onChildAdded(Component child);
    public void onChildChanged(Component child);
    public void removeChild(Component child);
    public void onChildRemoved(Component child);
    public void onParentChanged();
    public void onVisibilityChanged();
}
