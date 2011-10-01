/**
 *  Berkelium-JME3 | Berkelium integration into jMonkeyEngine
 *  Copyright (C) 2011  Skye Book
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.skyebook.berkeliumjme3;

import com.jme3.math.Vector3f;

/**
 * @author Skye Book
 *
 */
public interface BerkeliumSurface{
	
	/**
	 * Gets the lower left vertex of the Berkelium display
	 * @return
	 */
	public Vector3f getLowerLeft();
	
	/**
	 * Gets the lower right vertex of the Berkelium display
	 * @return
	 */
	public Vector3f getLowerRight();
	
	/**
	 * Gets the upper left vertex of the Berkelium display
	 * @return
	 */
	public Vector3f getUpperLeft();
	
	/**
	 * Gets the upper right vertex of the Berkelium display
	 * @return
	 */
	public Vector3f getUpperRight();
}
