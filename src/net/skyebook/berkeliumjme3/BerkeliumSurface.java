/**
 * 
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
