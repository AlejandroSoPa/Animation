package com.mygdx.animation;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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

	// A variable for tracking elapsed time for the animation
	float stateTime;

	@Override
	public void create() {

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

		background = new Texture(Gdx.files.internal("background12.jpg"));
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		bgRegion = new TextureRegion(background);
		posx2 = 0;
		posy = 0;
		background.setWrap( Texture.TextureWrap.MirroredRepeat,
							Texture.TextureWrap.MirroredRepeat);

		batch = new SpriteBatch();
		stateTime = 0f;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		TextureRegion frame = walkAnimation.getKeyFrame(stateTime,true);

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