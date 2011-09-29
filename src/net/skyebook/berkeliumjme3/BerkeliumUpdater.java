package net.skyebook.berkeliumjme3;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.berkelium.java.Berkelium;
import org.berkelium.java.BufferedImageAdapter;
import org.berkelium.java.Window;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;

/**
 * @author Skye Book
 *
 */
public class BerkeliumUpdater implements AppState {
	private boolean initialized = false;
	private boolean enabled = true;

	private Berkelium berk;
	private BufferedImageAdapter imageAdaper;
	private Window window;
	private Texture targetTexture;
	private int width;
	private int height;

	private Image jmeImage;

	private AWTLoader loader;

	private ArrayList<BerkeliumInterfaceCallback> callbacks;
	
	private boolean killTrigger = false;
	
	private Executor thread = Executors.newFixedThreadPool(1);

	/**
	 * 
	 */
	public BerkeliumUpdater(int width, int height) {
		this.width = width;
		this.height = height;
		callbacks = new ArrayList<BerkeliumInterfaceCallback>();
		loader = new AWTLoader();
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
		

		jmeImage = loader.load(imageAdaper.getImage(), true);
		targetTexture = new Texture2D(jmeImage);

		for(BerkeliumInterfaceCallback callback : callbacks){
			callback.textureCreated(targetTexture);
		}
		
		thread.execute(new Runnable() {
			
			@Override
			public void run() {
				while(!killTrigger){
					try {
						Thread.sleep(10);
						jmeImage = loader.load(imageAdaper.getImage(), true);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});

		initialized = true;
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
		enabled = active;
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#stateAttached(com.jme3.app.state.AppStateManager)
	 */
	@Override
	public void stateAttached(AppStateManager stateManager) {
		// nothing to see here
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#stateDetached(com.jme3.app.state.AppStateManager)
	 */
	@Override
	public void stateDetached(AppStateManager stateManager) {
		// nothing to see here
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#update(float)
	 */
	@Override
	public void update(float tpf) {
		berk.update();

		targetTexture.setImage(jmeImage);
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#render(com.jme3.renderer.RenderManager)
	 */
	@Override
	public void render(RenderManager rm) {
		// nothing to see here
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#postRender()
	 */
	@Override
	public void postRender() {
		// nothing to see here
	}

	/* (non-Javadoc)
	 * @see com.jme3.app.state.AppState#cleanup()
	 */
	@Override
	public void cleanup() {
		killTrigger=true;
	}
}
