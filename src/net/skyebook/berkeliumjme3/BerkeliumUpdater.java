package net.skyebook.berkeliumjme3;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.berkelium.java.Berkelium;
import org.berkelium.java.BufferedImageAdapter;
import org.berkelium.java.Window;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.TextureKey;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

/**
 * @author Skye Book
 *
 */
public class BerkeliumUpdater implements AppState {
	private boolean initialized = false;

	private Berkelium berk;
	private BufferedImageAdapter imageAdaper;
	private Window window;
	private Texture targetTexture;
	private int width;
	private int height;

	private ByteBuffer jmeImageBuffer;
	private Image jmeImage;

	private ArrayList<BerkeliumInterfaceCallback> callbacks;

	/**
	 * 
	 */
	public BerkeliumUpdater(int width, int height) {
		this.width = width;
		this.height = height;
		callbacks = new ArrayList<BerkeliumInterfaceCallback>();
	}

	public void addCallback(BerkeliumInterfaceCallback callback){
		callbacks.add(callback);
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#initialize(com.jme3.app.state.AppStateManager, com.jme3.app.Application)
	 */
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		// create Berkelium
		berk = Berkelium.createInstance();
		imageAdaper = new BufferedImageAdapter();
		window = berk.createWindow();
		window.setDelegate(imageAdaper);
		imageAdaper.resize(width, height);
		window.resize(width, height);
		window.navigateTo("http://google.com");

		berk.update();

		jmeImageBuffer = convertToByteBuffer(imageAdaper.getImage().getData().getDataBuffer(), null);
		jmeImage = new Image(Format.RGBA8, width, height, jmeImageBuffer);
		targetTexture = new Texture2D(jmeImage);

		//targetTexture.setKey(new TextureKey("", false));

		for(BerkeliumInterfaceCallback callback : callbacks){
			callback.textureCreated(targetTexture);
		}

		initialized = true;
	}

	private ByteBuffer convertToByteBuffer(DataBuffer data, ByteBuffer target){

		// check to ensure that we have the buffer and that it is the correct size
		int properCapacity = 4*4*imageAdaper.getImage().getWidth()*imageAdaper.getImage().getHeight();
		if(target==null || target.capacity()!=properCapacity){
			System.out.println("BUFFER NULL OR INCORRECTLY SIZED");
			target = ByteBuffer.allocateDirect(properCapacity);
		}

		// clear all data from the buffer
		target.clear();

		if(data instanceof DataBufferInt){
			int[] buffer = ((DataBufferInt)data).getData();

			//int x=0; x<buffer.length/width; x++
			for(int y=0; y<buffer.length/height; y++){
				for(int x=0; x<buffer.length/width; x++){

					int val = buffer[(y*height) + x];
					// unpack bytes from the buffer
					for(int j=0; j<4; j++){
						target.put((byte)val);
						val >>= 8;
					}
				}
			}
		}


		return target;
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return initialized;
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean active) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#stateAttached(com.jme3.app.state.AppStateManager)
	 */
	@Override
	public void stateAttached(AppStateManager stateManager) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#stateDetached(com.jme3.app.state.AppStateManager)
	 */
	@Override
	public void stateDetached(AppStateManager stateManager) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#update(float)
	 */
	@Override
	public void update(float tpf) {
		berk.update();

		jmeImageBuffer = convertToByteBuffer(imageAdaper.getImage().getData().getDataBuffer(), jmeImageBuffer);
		jmeImage = new Image(Format.RGBA8, width, height, jmeImageBuffer);
		targetTexture.setImage(jmeImage);
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#render(com.jme3.renderer.RenderManager)
	 */
	@Override
	public void render(RenderManager rm) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#postRender()
	 */
	@Override
	public void postRender() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#cleanup()
	 */
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
