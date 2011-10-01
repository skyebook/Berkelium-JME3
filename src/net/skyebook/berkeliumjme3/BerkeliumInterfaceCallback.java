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

import com.jme3.texture.Texture;

/**
 * @author Skye Book
 *
 */
public interface BerkeliumInterfaceCallback {
	
	/**
	 * Called when the Berkelium backed {@link Texture}
	 * is created
	 * @param texture the created texture
	 */
	public void textureCreated(Texture texture);

}
