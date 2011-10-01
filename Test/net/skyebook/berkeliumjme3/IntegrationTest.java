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

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;

/**
 * @author Skye Book
 *
 */
public class IntegrationTest extends SimpleApplication {
	
	private Geometry boxGeometry;
	private BerkeliumUpdater berkeliumState;

	/**
	 * 
	 */
	public IntegrationTest() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.SimpleApplication#simpleInitApp()
	 */
	@Override
	public void simpleInitApp() {
		//Box shape = new Box(new Vector3f(0, 0, 0), 4, 3, 4);
		BerkeliumQuad shape = new BerkeliumQuad(4, 3);
		boxGeometry = new Geometry("shape", shape);
		boxGeometry.setLocalTranslation(-.5f, .5f, 5);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		boxGeometry.setMaterial(mat);
		rootNode.attachChild(boxGeometry);
		
		cam.lookAt(boxGeometry.getLocalTranslation(), Vector3f.UNIT_Y);
		
		flyCam.setEnabled(false);
		
		berkeliumState = new BerkeliumUpdater(inputManager, 640, 480, shape, boxGeometry, cam);
		berkeliumState.addCallback(new BerkeliumInterfaceCallback() {
			
			@Override
			public void textureCreated(Texture texture) {
				boxGeometry.getMaterial().setTexture("ColorMap", texture);
			}
		});
		
		stateManager.attach(berkeliumState);
		mouseInput.setCursorVisible(true);
	}
	
	@Override
	public void simpleUpdate(float tpf){
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IntegrationTest test = new IntegrationTest();
		test.start();
	}

}
