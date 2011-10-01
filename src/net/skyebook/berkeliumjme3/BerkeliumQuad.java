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

import java.nio.FloatBuffer;

import com.jme3.math.Vector3f;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Quad;

/**
 * An implementation of {@link BerkeliumSurface} using a jME3 {@link Quad}
 * @author Skye Book
 * TODO: Implement caching
 */
public class BerkeliumQuad extends Quad implements BerkeliumSurface {

	public BerkeliumQuad() {
		super();
	}

	public BerkeliumQuad(float width, float height, boolean flipCoords) {
		super(width, height, flipCoords);
	}

	public BerkeliumQuad(float width, float height) {
		super(width, height);
	}

	/* (non-Javadoc)
	 * @see net.skyebook.berkeliumjme3.BerkeliumSurface#getLowerLeft()
	 */
	@Override
	public Vector3f getLowerLeft() {
		// position 0
		return getPositionAtIndex(0);
	}

	/* (non-Javadoc)
	 * @see net.skyebook.berkeliumjme3.BerkeliumSurface#getLowerRight()
	 */
	@Override
	public Vector3f getLowerRight() {
		// position 1
		return getPositionAtIndex(1);
	}

	/* (non-Javadoc)
	 * @see net.skyebook.berkeliumjme3.BerkeliumSurface#getUpperLeft()
	 */
	@Override
	public Vector3f getUpperLeft() {
		// position 3
		return getPositionAtIndex(3);
	}

	/* (non-Javadoc)
	 * @see net.skyebook.berkeliumjme3.BerkeliumSurface#getUpperRight()
	 */
	@Override
	public Vector3f getUpperRight() {
		// position 2
		return getPositionAtIndex(2);
	}
	
	/**
	 * 
	 * @param index The index (face) to search for
	 * @return The requested {@link Vector3f} or null if it
	 * was not found
	 */
	private Vector3f getPositionAtIndex(int index){
		FloatBuffer buffer = (FloatBuffer)getBuffer(Type.Position).getData();
		buffer.rewind();
		
		int currentPosition = 0;
		while(buffer.hasRemaining()){
			float x = buffer.get();
			float y = buffer.get();
			float z = buffer.get();
			
			if(currentPosition==index){
				return new Vector3f(x, y, z);
			}
			
			currentPosition++;
		}
		
		System.out.println("Could not find position " + index);
		System.out.println("Buffer capacity: " + buffer.capacity());
		return null;
	}

}
