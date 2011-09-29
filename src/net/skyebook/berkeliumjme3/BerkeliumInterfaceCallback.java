/**
 * 
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
