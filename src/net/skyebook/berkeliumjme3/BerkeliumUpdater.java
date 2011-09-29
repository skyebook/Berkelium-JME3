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
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
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

	private InputManager inputManager;

	/**
	 * 
	 */
	public BerkeliumUpdater(InputManager inputManager, int width, int height) {
		this.inputManager = inputManager;
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

		inputSetup();

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

	private void inputSetup(){
		// mouse setup

		inputManager.addMapping("rightClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("leftClick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping("middleClick", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));


		inputManager.addListener(new ActionListener() {

			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				window.mouseButton(0, isPressed);
			}

		}, "leftClick");

		inputManager.addListener(new ActionListener() {

			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				window.mouseButton(1, isPressed);
			}

		}, "rightClick");

		inputManager.addListener(new ActionListener() {

			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				window.mouseButton(1, isPressed);
			}

		}, "middleClick");
		
		inputManager.addMapping("mouseXMovement", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping("mouseYMovement", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
		inputManager.addMapping("mouseWheelMovement", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		
		inputManager.addListener(new AnalogListener() {
			
			@Override
			public void onAnalog(String name, float value, float tpf) {
				window.mouseMoved((int)inputManager.getCursorPosition().getX(), (int)inputManager.getCursorPosition().getY());
			}
		}, "mouseXMovement");
		
		inputManager.addListener(new AnalogListener() {
			
			@Override
			public void onAnalog(String name, float value, float tpf) {
				window.mouseMoved((int)inputManager.getCursorPosition().getX(), (int)inputManager.getCursorPosition().getY());
			}
		}, "mouseYMovement");
		
		inputManager.addListener(new AnalogListener() {
			
			@Override
			public void onAnalog(String name, float value, float tpf) {
				window.mouseWheel(0, (int)value);
			}
		}, "mouseWheelMovement");
		
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
