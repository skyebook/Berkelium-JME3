package net.skyebook.berkeliumjme3;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
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

	private ExecutorService thread = Executors.newFixedThreadPool(1);

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
		//window.navigateTo("http://upload.wikimedia.org/wikipedia/commons/b/b5/I-15bis.ogg");

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

		/*============================== Mouse Setup ==============================*/
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



		/*============================== Keyboard Setup ==============================*/
		inputManager.addMapping("KEY_0",				new KeyTrigger(KeyInput.KEY_0));
		inputManager.addMapping("KEY_1",				new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("KEY_2",				new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("KEY_3",				new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("KEY_4",				new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping("KEY_5",				new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping("KEY_6",				new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping("KEY_7",				new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping("KEY_8",				new KeyTrigger(KeyInput.KEY_8));
		inputManager.addMapping("KEY_9",				new KeyTrigger(KeyInput.KEY_9));
		inputManager.addMapping("KEY_A",				new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("KEY_ADD",				new KeyTrigger(KeyInput.KEY_ADD));
		inputManager.addMapping("KEY_APOSTROPHE",		new KeyTrigger(KeyInput.KEY_APOSTROPHE));
		inputManager.addMapping("KEY_APPS",				new KeyTrigger(KeyInput.KEY_APPS));
		inputManager.addMapping("KEY_AT",				new KeyTrigger(KeyInput.KEY_AT));
		inputManager.addMapping("KEY_AX",				new KeyTrigger(KeyInput.KEY_AX));
		inputManager.addMapping("KEY_B",				new KeyTrigger(KeyInput.KEY_B));
		inputManager.addMapping("KEY_BACK",				new KeyTrigger(KeyInput.KEY_BACK));
		inputManager.addMapping("KEY_BACKSLASH",		new KeyTrigger(KeyInput.KEY_BACKSLASH));
		inputManager.addMapping("KEY_C",				new KeyTrigger(KeyInput.KEY_C));
		inputManager.addMapping("KEY_CAPITAL",			new KeyTrigger(KeyInput.KEY_CAPITAL));
		inputManager.addMapping("KEY_CIRCUMFLEX",		new KeyTrigger(KeyInput.KEY_CIRCUMFLEX));
		inputManager.addMapping("KEY_COLON",			new KeyTrigger(KeyInput.KEY_COLON));
		inputManager.addMapping("KEY_COMMA",			new KeyTrigger(KeyInput.KEY_COMMA));
		inputManager.addMapping("KEY_CONVERT",			new KeyTrigger(KeyInput.KEY_CONVERT));
		inputManager.addMapping("KEY_D",				new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("KEY_DECIMAL",			new KeyTrigger(KeyInput.KEY_DECIMAL));
		inputManager.addMapping("KEY_DELETE",			new KeyTrigger(KeyInput.KEY_DELETE));
		inputManager.addMapping("KEY_DIVIDE",			new KeyTrigger(KeyInput.KEY_DIVIDE));
		inputManager.addMapping("KEY_DOWN",				new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping("KEY_E",				new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping("KEY_END",				new KeyTrigger(KeyInput.KEY_END));
		inputManager.addMapping("KEY_EQUALS",			new KeyTrigger(KeyInput.KEY_EQUALS));
		inputManager.addMapping("KEY_ESCAPE",			new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping("KEY_F",				new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping("KEY_F1",				new KeyTrigger(KeyInput.KEY_F1));
		inputManager.addMapping("KEY_F2",				new KeyTrigger(KeyInput.KEY_F2));
		inputManager.addMapping("KEY_F3",				new KeyTrigger(KeyInput.KEY_F3));
		inputManager.addMapping("KEY_F4",				new KeyTrigger(KeyInput.KEY_F4));
		inputManager.addMapping("KEY_F5",				new KeyTrigger(KeyInput.KEY_F5));
		inputManager.addMapping("KEY_F6",				new KeyTrigger(KeyInput.KEY_F6));
		inputManager.addMapping("KEY_F7",				new KeyTrigger(KeyInput.KEY_F7));
		inputManager.addMapping("KEY_F8",				new KeyTrigger(KeyInput.KEY_F8));
		inputManager.addMapping("KEY_F9",				new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addMapping("KEY_F10",				new KeyTrigger(KeyInput.KEY_F10));
		inputManager.addMapping("KEY_F11",				new KeyTrigger(KeyInput.KEY_F11));
		inputManager.addMapping("KEY_F12",				new KeyTrigger(KeyInput.KEY_F12));
		inputManager.addMapping("KEY_F13",				new KeyTrigger(KeyInput.KEY_F13));
		inputManager.addMapping("KEY_F14",				new KeyTrigger(KeyInput.KEY_F14));
		inputManager.addMapping("KEY_F15",				new KeyTrigger(KeyInput.KEY_F15));
		inputManager.addMapping("KEY_G",				new KeyTrigger(KeyInput.KEY_G));
		inputManager.addMapping("KEY_GRAVE",			new KeyTrigger(KeyInput.KEY_GRAVE));
		inputManager.addMapping("KEY_H",				new KeyTrigger(KeyInput.KEY_H));
		inputManager.addMapping("KEY_HOME",				new KeyTrigger(KeyInput.KEY_HOME));
		inputManager.addMapping("KEY_I",				new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping("KEY_INSERT",			new KeyTrigger(KeyInput.KEY_INSERT));
		inputManager.addMapping("KEY_J",				new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping("KEY_K",				new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping("KEY_KANA",				new KeyTrigger(KeyInput.KEY_KANA));
		inputManager.addMapping("KEY_KANJI",			new KeyTrigger(KeyInput.KEY_KANJI));
		inputManager.addMapping("KEY_L",				new KeyTrigger(KeyInput.KEY_L));
		inputManager.addMapping("KEY_LBRACKET",			new KeyTrigger(KeyInput.KEY_LBRACKET));
		inputManager.addMapping("KEY_LCONTROL",			new KeyTrigger(KeyInput.KEY_LCONTROL));
		inputManager.addMapping("KEY_LEFT",				new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("KEY_LMENU",			new KeyTrigger(KeyInput.KEY_LMENU));
		inputManager.addMapping("KEY_LMETA",			new KeyTrigger(KeyInput.KEY_LMETA));
		inputManager.addMapping("KEY_LSHIFT",			new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("KEY_M",				new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping("KEY_MINUS",			new KeyTrigger(KeyInput.KEY_MINUS));
		inputManager.addMapping("KEY_MULTIPLY",			new KeyTrigger(KeyInput.KEY_MULTIPLY));
		inputManager.addMapping("KEY_N",				new KeyTrigger(KeyInput.KEY_N));
		inputManager.addMapping("KEY_NEXT",				new KeyTrigger(KeyInput.KEY_NEXT));
		inputManager.addMapping("KEY_NOCONVERT",		new KeyTrigger(KeyInput.KEY_NOCONVERT));
		inputManager.addMapping("KEY_NUMLOCK",			new KeyTrigger(KeyInput.KEY_NUMLOCK));
		inputManager.addMapping("KEY_NUMPAD0",			new KeyTrigger(KeyInput.KEY_NUMPAD0));
		inputManager.addMapping("KEY_NUMPAD1",			new KeyTrigger(KeyInput.KEY_NUMPAD1));
		inputManager.addMapping("KEY_NUMPAD2",			new KeyTrigger(KeyInput.KEY_NUMPAD2));
		inputManager.addMapping("KEY_NUMPAD3",			new KeyTrigger(KeyInput.KEY_NUMPAD3));
		inputManager.addMapping("KEY_NUMPAD4",			new KeyTrigger(KeyInput.KEY_NUMPAD4));
		inputManager.addMapping("KEY_NUMPAD5",			new KeyTrigger(KeyInput.KEY_NUMPAD5));
		inputManager.addMapping("KEY_NUMPAD6",			new KeyTrigger(KeyInput.KEY_NUMPAD6));
		inputManager.addMapping("KEY_NUMPAD7",			new KeyTrigger(KeyInput.KEY_NUMPAD7));
		inputManager.addMapping("KEY_NUMPAD8",			new KeyTrigger(KeyInput.KEY_NUMPAD8));
		inputManager.addMapping("KEY_NUMPAD9",			new KeyTrigger(KeyInput.KEY_NUMPAD9));
		inputManager.addMapping("KEY_NUMPADCOMMA",		new KeyTrigger(KeyInput.KEY_NUMPADCOMMA));
		inputManager.addMapping("KEY_NUMPADENTER",		new KeyTrigger(KeyInput.KEY_NUMPADENTER));
		inputManager.addMapping("KEY_NUMPADEQUALS",		new KeyTrigger(KeyInput.KEY_NUMPADEQUALS));
		inputManager.addMapping("KEY_O",				new KeyTrigger(KeyInput.KEY_O));
		inputManager.addMapping("KEY_P",				new KeyTrigger(KeyInput.KEY_P));
		inputManager.addMapping("KEY_PAUSE",			new KeyTrigger(KeyInput.KEY_PAUSE));
		inputManager.addMapping("KEY_PERIOD",			new KeyTrigger(KeyInput.KEY_PERIOD));
		inputManager.addMapping("KEY_PGDN",				new KeyTrigger(KeyInput.KEY_PGDN));
		inputManager.addMapping("KEY_PGUP",				new KeyTrigger(KeyInput.KEY_PGUP));
		inputManager.addMapping("KEY_POWER",			new KeyTrigger(KeyInput.KEY_POWER));
		inputManager.addMapping("KEY_PRIOR",			new KeyTrigger(KeyInput.KEY_PRIOR));
		inputManager.addMapping("KEY_Q",				new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("KEY_R",				new KeyTrigger(KeyInput.KEY_R));
		inputManager.addMapping("KEY_RBRACKET",			new KeyTrigger(KeyInput.KEY_RBRACKET));
		inputManager.addMapping("KEY_RCONTROL",			new KeyTrigger(KeyInput.KEY_RCONTROL));
		inputManager.addMapping("KEY_RETURN",			new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("KEY_RIGHT",			new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("KEY_RMENU",			new KeyTrigger(KeyInput.KEY_RMENU));
		inputManager.addMapping("KEY_RMETA",			new KeyTrigger(KeyInput.KEY_RMETA));
		inputManager.addMapping("KEY_RSHIFT",			new KeyTrigger(KeyInput.KEY_RSHIFT));
		inputManager.addMapping("KEY_S",				new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("KEY_SCROLL",			new KeyTrigger(KeyInput.KEY_SCROLL));
		inputManager.addMapping("KEY_SEMICOLON",		new KeyTrigger(KeyInput.KEY_SEMICOLON));
		inputManager.addMapping("KEY_SLASH",			new KeyTrigger(KeyInput.KEY_SLASH));
		inputManager.addMapping("KEY_SLEEP",			new KeyTrigger(KeyInput.KEY_SLEEP));
		inputManager.addMapping("KEY_SPACE",			new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("KEY_STOP",				new KeyTrigger(KeyInput.KEY_STOP));
		inputManager.addMapping("KEY_SUBTRACT",			new KeyTrigger(KeyInput.KEY_SUBTRACT));
		inputManager.addMapping("KEY_SYSRQ",			new KeyTrigger(KeyInput.KEY_SYSRQ));
		inputManager.addMapping("KEY_T",				new KeyTrigger(KeyInput.KEY_T));
		inputManager.addMapping("KEY_TAB",				new KeyTrigger(KeyInput.KEY_TAB));
		inputManager.addMapping("KEY_U",				new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("KEY_UNDERLINE",		new KeyTrigger(KeyInput.KEY_UNDERLINE));
		inputManager.addMapping("KEY_UNLABELED",		new KeyTrigger(KeyInput.KEY_UNLABELED));
		inputManager.addMapping("KEY_UP",				new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("KEY_V",				new KeyTrigger(KeyInput.KEY_V));
		inputManager.addMapping("KEY_W",				new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("KEY_X",				new KeyTrigger(KeyInput.KEY_X));
		inputManager.addMapping("KEY_Y",				new KeyTrigger(KeyInput.KEY_Y));
		inputManager.addMapping("KEY_YEN",				new KeyTrigger(KeyInput.KEY_YEN));
		inputManager.addMapping("KEY_Z",				new KeyTrigger(KeyInput.KEY_Z));

		addListener("KEY_0",				KeyInput.KEY_0);
		addListener("KEY_1",				KeyInput.KEY_1);
		addListener("KEY_2",				KeyInput.KEY_2);
		addListener("KEY_3",				KeyInput.KEY_3);
		addListener("KEY_4",				KeyInput.KEY_4);
		addListener("KEY_5",				KeyInput.KEY_5);
		addListener("KEY_6",				KeyInput.KEY_6);
		addListener("KEY_7",				KeyInput.KEY_7);
		addListener("KEY_8",				KeyInput.KEY_8);
		addListener("KEY_9",				KeyInput.KEY_9);
		addListener("KEY_A",				KeyInput.KEY_A);
		addListener("KEY_ADD",				KeyInput.KEY_ADD);
		addListener("KEY_APOSTROPHE",		KeyInput.KEY_APOSTROPHE);
		addListener("KEY_APPS",				KeyInput.KEY_APPS);
		addListener("KEY_AT",				KeyInput.KEY_AT);
		addListener("KEY_AX",				KeyInput.KEY_AX);
		addListener("KEY_B",				KeyInput.KEY_B);
		addListener("KEY_BACK",				KeyInput.KEY_BACK);
		addListener("KEY_BACKSLASH",		KeyInput.KEY_BACKSLASH);
		addListener("KEY_C",				KeyInput.KEY_C);
		addListener("KEY_CAPITAL",			KeyInput.KEY_CAPITAL);
		addListener("KEY_CIRCUMFLEX",		KeyInput.KEY_CIRCUMFLEX);
		addListener("KEY_COLON",			KeyInput.KEY_COLON);
		addListener("KEY_COMMA",			KeyInput.KEY_COMMA);
		addListener("KEY_CONVERT",			KeyInput.KEY_CONVERT);
		addListener("KEY_D",				KeyInput.KEY_D);
		addListener("KEY_DECIMAL",			KeyInput.KEY_DECIMAL);
		addListener("KEY_DELETE",			KeyInput.KEY_DELETE);
		addListener("KEY_DIVIDE",			KeyInput.KEY_DIVIDE);
		addListener("KEY_DOWN",				KeyInput.KEY_DOWN);
		addListener("KEY_E",				KeyInput.KEY_E);
		addListener("KEY_END",				KeyInput.KEY_END);
		addListener("KEY_EQUALS",			KeyInput.KEY_EQUALS);
		addListener("KEY_ESCAPE",			KeyInput.KEY_ESCAPE);
		addListener("KEY_F",				KeyInput.KEY_F);
		addListener("KEY_F1",				KeyInput.KEY_F1);
		addListener("KEY_F2",				KeyInput.KEY_F2);
		addListener("KEY_F3",				KeyInput.KEY_F3);
		addListener("KEY_F4",				KeyInput.KEY_F4);
		addListener("KEY_F5",				KeyInput.KEY_F5);
		addListener("KEY_F6",				KeyInput.KEY_F6);
		addListener("KEY_F7",				KeyInput.KEY_F7);
		addListener("KEY_F8",				KeyInput.KEY_F8);
		addListener("KEY_F9",				KeyInput.KEY_F9);
		addListener("KEY_F10",				KeyInput.KEY_F10);
		addListener("KEY_F11",				KeyInput.KEY_F11);
		addListener("KEY_F12",				KeyInput.KEY_F12);
		addListener("KEY_F13",				KeyInput.KEY_F13);
		addListener("KEY_F14",				KeyInput.KEY_F14);
		addListener("KEY_F15",				KeyInput.KEY_F15);
		addListener("KEY_G",				KeyInput.KEY_G);
		addListener("KEY_GRAVE",			KeyInput.KEY_GRAVE);
		addListener("KEY_H",				KeyInput.KEY_H);
		addListener("KEY_HOME",				KeyInput.KEY_HOME);
		addListener("KEY_I",				KeyInput.KEY_I);
		addListener("KEY_INSERT",			KeyInput.KEY_INSERT);
		addListener("KEY_J",				KeyInput.KEY_J);
		addListener("KEY_K",				KeyInput.KEY_K);
		addListener("KEY_KANA",				KeyInput.KEY_KANA);
		addListener("KEY_KANJI",			KeyInput.KEY_KANJI);
		addListener("KEY_L",				KeyInput.KEY_L);
		addListener("KEY_LBRACKET",			KeyInput.KEY_LBRACKET);
		addListener("KEY_LCONTROL",			KeyInput.KEY_LCONTROL);
		addListener("KEY_LEFT",				KeyInput.KEY_LEFT);
		addListener("KEY_LMENU",			KeyInput.KEY_LMENU);
		addListener("KEY_LMETA",			KeyInput.KEY_LMETA);
		addListener("KEY_LSHIFT",			KeyInput.KEY_LSHIFT);
		addListener("KEY_M",				KeyInput.KEY_M);
		addListener("KEY_MINUS",			KeyInput.KEY_MINUS);
		addListener("KEY_MULTIPLY",			KeyInput.KEY_MULTIPLY);
		addListener("KEY_N",				KeyInput.KEY_N);
		addListener("KEY_NEXT",				KeyInput.KEY_NEXT);
		addListener("KEY_NOCONVERT",		KeyInput.KEY_NOCONVERT);
		addListener("KEY_NUMLOCK",			KeyInput.KEY_NUMLOCK);
		addListener("KEY_NUMPAD0",			KeyInput.KEY_NUMPAD0);
		addListener("KEY_NUMPAD1",			KeyInput.KEY_NUMPAD1);
		addListener("KEY_NUMPAD2",			KeyInput.KEY_NUMPAD2);
		addListener("KEY_NUMPAD3",			KeyInput.KEY_NUMPAD3);
		addListener("KEY_NUMPAD4",			KeyInput.KEY_NUMPAD4);
		addListener("KEY_NUMPAD5",			KeyInput.KEY_NUMPAD5);
		addListener("KEY_NUMPAD6",			KeyInput.KEY_NUMPAD6);
		addListener("KEY_NUMPAD7",			KeyInput.KEY_NUMPAD7);
		addListener("KEY_NUMPAD8",			KeyInput.KEY_NUMPAD8);
		addListener("KEY_NUMPAD9",			KeyInput.KEY_NUMPAD9);
		addListener("KEY_NUMPADCOMMA",		KeyInput.KEY_NUMPADCOMMA);
		addListener("KEY_NUMPADENTER",		KeyInput.KEY_NUMPADENTER);
		addListener("KEY_NUMPADEQUALS",		KeyInput.KEY_NUMPADEQUALS);
		addListener("KEY_O",				KeyInput.KEY_O);
		addListener("KEY_P",				KeyInput.KEY_P);
		addListener("KEY_PAUSE",			KeyInput.KEY_PAUSE);
		addListener("KEY_PERIOD",			KeyInput.KEY_PERIOD);
		addListener("KEY_PGDN",				KeyInput.KEY_PGDN);
		addListener("KEY_PGUP",				KeyInput.KEY_PGUP);
		addListener("KEY_POWER",			KeyInput.KEY_POWER);
		addListener("KEY_PRIOR",			KeyInput.KEY_PRIOR);
		addListener("KEY_Q",				KeyInput.KEY_Q);
		addListener("KEY_R",				KeyInput.KEY_R);
		addListener("KEY_RBRACKET",			KeyInput.KEY_RBRACKET);
		addListener("KEY_RCONTROL",			KeyInput.KEY_RCONTROL);
		addListener("KEY_RETURN",			KeyInput.KEY_RETURN);
		addListener("KEY_RIGHT",			KeyInput.KEY_RIGHT);
		addListener("KEY_RMENU",			KeyInput.KEY_RMENU);
		addListener("KEY_RMETA",			KeyInput.KEY_RMETA);
		addListener("KEY_RSHIFT",			KeyInput.KEY_RSHIFT);
		addListener("KEY_S",				KeyInput.KEY_S);
		addListener("KEY_SCROLL",			KeyInput.KEY_SCROLL);
		addListener("KEY_SEMICOLON",		KeyInput.KEY_SEMICOLON);
		addListener("KEY_SLASH",			KeyInput.KEY_SLASH);
		addListener("KEY_SLEEP",			KeyInput.KEY_SLEEP);
		addListener("KEY_SPACE",			KeyInput.KEY_SPACE);
		addListener("KEY_STOP",				KeyInput.KEY_STOP);
		addListener("KEY_SUBTRACT",			KeyInput.KEY_SUBTRACT);
		addListener("KEY_SYSRQ",			KeyInput.KEY_SYSRQ);
		addListener("KEY_T",				KeyInput.KEY_T);
		addListener("KEY_TAB",				KeyInput.KEY_TAB);
		addListener("KEY_U",				KeyInput.KEY_U);
		addListener("KEY_UNDERLINE",		KeyInput.KEY_UNDERLINE);
		addListener("KEY_UNLABELED",		KeyInput.KEY_UNLABELED);
		addListener("KEY_UP",				KeyInput.KEY_UP);
		addListener("KEY_V",				KeyInput.KEY_V);
		addListener("KEY_W",				KeyInput.KEY_W);
		addListener("KEY_X",				KeyInput.KEY_X);
		addListener("KEY_Y",				KeyInput.KEY_Y);
		addListener("KEY_YEN",				KeyInput.KEY_YEN);
		addListener("KEY_Z",				KeyInput.KEY_Z);

	}

	private void addListener(String mapping, int keyCode){
		inputManager.addListener(new KeyListener(keyCode), mapping);
	}

	private class KeyListener implements ActionListener{

		private int keyCode;
		private String character;
		private boolean isEligibleForTextEvent;

		private KeyListener(int keyCode){
			isEligibleForTextEvent = isEligibleForTextEvent(keyCode);
			
			this.keyCode = convertKeyCode(keyCode);
		}

		private boolean isEligibleForTextEvent(int keyCode){
			return keyCode == KeyInput.KEY_0 ||
					keyCode == KeyInput.KEY_1 ||
					keyCode == KeyInput.KEY_2 ||
					keyCode == KeyInput.KEY_3 ||
					keyCode == KeyInput.KEY_4 ||
					keyCode == KeyInput.KEY_5 ||
					keyCode == KeyInput.KEY_6 ||
					keyCode == KeyInput.KEY_7 ||
					keyCode == KeyInput.KEY_8 ||
					keyCode == KeyInput.KEY_9 ||
					keyCode == KeyInput.KEY_A ||
					keyCode == KeyInput.KEY_B ||
					keyCode == KeyInput.KEY_C ||
					keyCode == KeyInput.KEY_D ||
					keyCode == KeyInput.KEY_E ||
					keyCode == KeyInput.KEY_F ||
					keyCode == KeyInput.KEY_G ||
					keyCode == KeyInput.KEY_H ||
					keyCode == KeyInput.KEY_I ||
					keyCode == KeyInput.KEY_J ||
					keyCode == KeyInput.KEY_K ||
					keyCode == KeyInput.KEY_L ||
					keyCode == KeyInput.KEY_M ||
					keyCode == KeyInput.KEY_N ||
					keyCode == KeyInput.KEY_O ||
					keyCode == KeyInput.KEY_P ||
					keyCode == KeyInput.KEY_Q ||
					keyCode == KeyInput.KEY_R ||
					keyCode == KeyInput.KEY_S ||
					keyCode == KeyInput.KEY_T ||
					keyCode == KeyInput.KEY_U ||
					keyCode == KeyInput.KEY_V ||
					keyCode == KeyInput.KEY_W ||
					keyCode == KeyInput.KEY_X ||
					keyCode == KeyInput.KEY_Y ||
					keyCode == KeyInput.KEY_Z ||
					keyCode == KeyInput.KEY_SPACE;
		}

		private int convertKeyCode(int keyCode){
			switch (keyCode) {
			case KeyInput.KEY_0:
				character="0";
				return 30;
			case KeyInput.KEY_1:
				character="1";
				return 31;
			case KeyInput.KEY_2:
				character="2";
				return 32;
			case KeyInput.KEY_3:
				character="3";
				return 33;
			case KeyInput.KEY_4:
				character="4";
				return 34;
			case KeyInput.KEY_5:
				character="5";
				return 35;
			case KeyInput.KEY_6:
				character="6";
				return 36;
			case KeyInput.KEY_7:
				character="7";
				return 37;
			case KeyInput.KEY_8:
				character="8";
				return 38;
			case KeyInput.KEY_9:
				character="9";
				return 39;
			case KeyInput.KEY_A:
				character="a";
				return 41;
			case KeyInput.KEY_ADD:
				break;
			case KeyInput.KEY_APOSTROPHE:
				break;
			case KeyInput.KEY_APPS:
				break;
			case KeyInput.KEY_AT:
				break;
			case KeyInput.KEY_AX:
				break;
			case KeyInput.KEY_B:
				character="b";
				return 42;
			case KeyInput.KEY_BACK:
				return 8;
			case KeyInput.KEY_BACKSLASH:
				break;
			case KeyInput.KEY_C:
				character="c";
				return 43;
			case KeyInput.KEY_CAPITAL:
				break;
			case KeyInput.KEY_CIRCUMFLEX:
				break;
			case KeyInput.KEY_COLON:
				break;
			case KeyInput.KEY_COMMA:
				break;
			case KeyInput.KEY_CONVERT:
				break;
			case KeyInput.KEY_D:
				character="d";
				return 44;
			case KeyInput.KEY_DECIMAL:
				break;
			case KeyInput.KEY_DELETE:
				break;
			case KeyInput.KEY_DIVIDE:
				break;
			case KeyInput.KEY_DOWN:
				break;
			case KeyInput.KEY_E:
				character="e";
				return 45;
			case KeyInput.KEY_END:
				break;
			case KeyInput.KEY_EQUALS:
				break;
			case KeyInput.KEY_ESCAPE:
				break;
			case KeyInput.KEY_F:
				character="f";
				return 46;
			case KeyInput.KEY_F1:
				break;
			case KeyInput.KEY_F2:
				break;
			case KeyInput.KEY_F3:
				break;
			case KeyInput.KEY_F4:
				break;
			case KeyInput.KEY_F5:
				break;
			case KeyInput.KEY_F6:
				break;
			case KeyInput.KEY_F7:
				break;
			case KeyInput.KEY_F8:
				break;
			case KeyInput.KEY_F9:
				break;
			case KeyInput.KEY_F10:
				break;
			case KeyInput.KEY_F11:
				break;
			case KeyInput.KEY_F12:
				break;
			case KeyInput.KEY_F13:
				break;
			case KeyInput.KEY_F14:
				break;
			case KeyInput.KEY_F15:
				break;
			case KeyInput.KEY_G:
				character="g";
				return 47;
			case KeyInput.KEY_GRAVE:
				break;
			case KeyInput.KEY_H:
				character="h";
				return 48;
			case KeyInput.KEY_HOME:
				break;
			case KeyInput.KEY_I:
				character="i";
				return 49;
			case KeyInput.KEY_INSERT:
				break;
			case KeyInput.KEY_J:
				character="j";
				return 0x4A;
			case KeyInput.KEY_K:
				character="k";
				return 0x4B;
			case KeyInput.KEY_KANA:
				break;
			case KeyInput.KEY_KANJI:
				break;
			case KeyInput.KEY_L:
				character="l";
				return 0x4C;
			case KeyInput.KEY_LBRACKET:
				break;
			case KeyInput.KEY_LCONTROL:
				break;
			case KeyInput.KEY_LEFT:
				break;
			case KeyInput.KEY_LMENU:
				break;
			case KeyInput.KEY_LMETA:
				break;
			case KeyInput.KEY_LSHIFT:
				return 10;
			case KeyInput.KEY_M:
				character="m";
				return 0x4D;
			case KeyInput.KEY_MINUS:
				break;
			case KeyInput.KEY_MULTIPLY:
				break;
			case KeyInput.KEY_N:
				character="n";
				return 0x4E;
			case KeyInput.KEY_NOCONVERT:
				break;
			case KeyInput.KEY_NUMLOCK:
				break;
			case KeyInput.KEY_NUMPAD0:
				break;
			case KeyInput.KEY_NUMPAD1:
				break;
			case KeyInput.KEY_NUMPAD2:
				break;
			case KeyInput.KEY_NUMPAD3:
				break;
			case KeyInput.KEY_NUMPAD4:
				break;
			case KeyInput.KEY_NUMPAD5:
				break;
			case KeyInput.KEY_NUMPAD6:
				break;
			case KeyInput.KEY_NUMPAD7:
				break;
			case KeyInput.KEY_NUMPAD8:
				break;
			case KeyInput.KEY_NUMPAD9:
				break;
			case KeyInput.KEY_NUMPADCOMMA:
				break;
			case KeyInput.KEY_NUMPADENTER:
				break;
			case KeyInput.KEY_NUMPADEQUALS:
				break;
			case KeyInput.KEY_O:
				character="o";
				return 0x4F;
			case KeyInput.KEY_P:
				character="p";
				return 50;
			case KeyInput.KEY_PAUSE:
				break;
			case KeyInput.KEY_PERIOD:
				break;
			case KeyInput.KEY_PGDN:
				break;
			case KeyInput.KEY_PGUP:
				break;
			case KeyInput.KEY_POWER:
				break;
			case KeyInput.KEY_Q:
				character="q";
				return 51;
			case KeyInput.KEY_R:
				character="r";
				return 52;
			case KeyInput.KEY_RBRACKET:
				break;
			case KeyInput.KEY_RCONTROL:
				break;
			case KeyInput.KEY_RETURN:
				return 0x0D;
			case KeyInput.KEY_RIGHT:
				break;
			case KeyInput.KEY_RMENU:
				break;
			case KeyInput.KEY_RMETA:
				break;
			case KeyInput.KEY_RSHIFT:
				return 10;
			case KeyInput.KEY_S:
				character="s";
				return 53;
			case KeyInput.KEY_SCROLL:
				break;
			case KeyInput.KEY_SEMICOLON:
				break;
			case KeyInput.KEY_SLASH:
				break;
			case KeyInput.KEY_SLEEP:
				break;
			case KeyInput.KEY_SPACE:
				character=" ";
				return 0;
			case KeyInput.KEY_STOP:
				break;
			case KeyInput.KEY_SUBTRACT:
				break;
			case KeyInput.KEY_SYSRQ:
				break;
			case KeyInput.KEY_T:
				character="t";
				return 54;
			case KeyInput.KEY_TAB:
				return 9;
			case KeyInput.KEY_U:
				character="u";
				return 55;
			case KeyInput.KEY_UNDERLINE:
				break;
			case KeyInput.KEY_UNLABELED:
				break;
			case KeyInput.KEY_UP:
				break;
			case KeyInput.KEY_V:
				character="v";
				return 56;
			case KeyInput.KEY_W:
				character="w";
				return 57;
			case KeyInput.KEY_X:
				character="x";
				return 58;
			case KeyInput.KEY_Y:
				character="y";
				return 59;
			case KeyInput.KEY_YEN:
				break;
			case KeyInput.KEY_Z:
				character="z";
				return 0x6a;
			}
			
			return keyCode;
		}
		/* (non-Javadoc)
		 * @see com.jme3.input.controls.ActionListener#onAction(java.lang.String, boolean, float)
		 */
		@Override
		public void onAction(String name, boolean isPressed, float tpf) {
			System.out.println(name + (isPressed?" pressed":" released"));

			if(isEligibleForTextEvent){
				if(isPressed) window.textEvent(character);
			}
			else{
				window.keyEvent(isPressed, 0, keyCode, 0);
			}
			
			
		}
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
		thread.shutdown();
	}
}
