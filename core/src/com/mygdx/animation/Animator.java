package com.mygdx.animation;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;

import java.awt.Rectangle;


public class Animator implements ApplicationListener {

	// Constant rows and columns of the sprite sheet
	private static final int FRAME_COLS = 6, FRAME_ROWS = 1;

	// Objects used
	public Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
	Texture sheet;
	Texture background;
	SpriteBatch batch;
	TextureRegion bgRegion;
	int posx = 100;
	int posy;
	int posx2;
	int posy2 = 90;
	Rectangle up, down, left, right, fire;
	final int IDLE=0, UP=1, DOWN=2, LEFT=3, RIGHT=4;
	WebSocket socket;
	String address = "localhost";
	int port = 8888;

	// A variable for tracking elapsed time for the animation
	float stateTime;
	float lastSend=0;

	public Animator() {

	}

	@Override
	public void create() {
		if( Gdx.app.getType()== Application.ApplicationType.Android )
			// en Android el host és accessible per 10.0.2.2
			address = "10.0.2.2";
		socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
		socket.setSendGracefully(false);
		socket.addListener((WebSocketListener) new MyWSListener());
		socket.connect();
		socket.send("Enviar dades");

		// Load the sprite sheet as a Texture
		sheet = new Texture(Gdx.files.internal("ragnarok_izquierda.PNG"));
		TextureRegion[][] tmp = TextureRegion.split(sheet,
				sheet.getWidth() / FRAME_COLS,
				sheet.getHeight() / FRAME_ROWS);
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation<TextureRegion>(0.100f, walkFrames);
		up = new Rectangle(0, 800*2/3, 600, 800/3);
		down = new Rectangle(0, 0, 600, 800/3);
		left = new Rectangle(0, 0, 600/3, 800);
		right = new Rectangle(600*2/3, 0, 700/3, 800);

		background = new Texture(Gdx.files.internal("background12.jpg"));
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		bgRegion = new TextureRegion(background);
		posx2 = 0;
		posy = 0;
		batch = new SpriteBatch();
		stateTime = 0f;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		if( stateTime-lastSend > 1.0f ) {
			lastSend = stateTime;
			socket.send("Enviar dades");
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		TextureRegion frame = walkAnimation.getKeyFrame(stateTime,true);
		/*
		if (virtual_joystick_control() == UP) {
			posy2 += 2;
		} else if (virtual_joystick_control() == DOWN) {
			posy2 -= 2;
		} else if (virtual_joystick_control() == LEFT) {
			posx -= 2;
		} else if (virtual_joystick_control() == RIGHT) {
			posx += 2;
		} else {
			System.out.println("aaaaaaa");
		}*/
		// (1) CALCULAR
		//...calculem posx i posy del personatge..
		batch.begin();
		// TextureRegion ens permet retallar un fragment de la Texture
		// retallem el fragment de background des de la posició del personatge (posx, posy)
		bgRegion.setRegion(posx2,posy,800,600);
		// (2) PINTAR
		// primer pintem el background
		batch.draw(bgRegion,0,0);
		// ...després pintem altres coses...
		// si volem invertir el sentit, ho podem fer amb el paràmetre scaleX=-1
		//batch.draw(frame, 200, 100, 0, frame.getRegionWidth(),frame.getRegionHeight(),-1,1,0);
		posx2 += 2;
		batch.draw(frame, posx, 90);

		// finalitzem main loop
		batch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
		batch.dispose();
		sheet.dispose();
	}

}

// COMUNICACIONS (rebuda de missatges)
/////////////////////////////////////////////
class MyWSListener implements WebSocketListener {

	@Override
	public boolean onOpen(WebSocket webSocket) {
		System.out.println("Opening...");
		return false;
	}

	@Override
	public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
		System.out.println("Closing...");
		return false;
	}

	@Override
	public boolean onMessage(WebSocket webSocket, String packet) {
		System.out.println("Message:");
		return false;
	}

	@Override
	public boolean onMessage(WebSocket webSocket, byte[] packet) {
		System.out.println("Message:");
		return false;
	}

	@Override
	public boolean onError(WebSocket webSocket, Throwable error) {
		System.out.println("ERROR:"+error.toString());
		return false;
	}
}