package com.flappybird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.rmi.runtime.Log;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background,GameOver;
	Circle birdCircle;

	ShapeRenderer shapeRenderer;
	int score=0;
	int scoringTube=0;
	Texture[] birds;

	Texture topTube;
	Texture bottomTube;

	int flapstate=0;
	float birdY=0;
	float velocity=0;

	int gameState=0;
	float gravity= 2;
	float gap=550;
	float maxTubeOffSet;
	Random RandomGenerator;

	int numberOfTubes=4;
	float[] tubeX =new float[numberOfTubes];
	float[] tubeOffset=new float[numberOfTubes];
	float tubeVelocity=4;
	float distanceBwTubes;

	Rectangle[] topRectangle;
	Rectangle[] bottomrectangle;

	BitmapFont font;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background= new Texture("bg.png");
		GameOver=new Texture("gameover.png");
		birds= new Texture[2];

		birdCircle=new Circle();
		shapeRenderer=new ShapeRenderer();

		topRectangle=new Rectangle[numberOfTubes];
		bottomrectangle=new Rectangle[numberOfTubes];

		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");

		topTube=new Texture("toptube.png");
		bottomTube=new Texture("bottomtube.png");

		maxTubeOffSet=Gdx.graphics.getHeight()/2-gap/2-100;
		RandomGenerator=new Random();
		distanceBwTubes=Gdx.graphics.getWidth()/2;

		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		StartGame();

	}

	public void StartGame(){
		birdY=Gdx.graphics.getHeight()/2 - birds[flapstate].getHeight()/2;

		for (int i=0;i<numberOfTubes;i++){
			tubeOffset[i]=(RandomGenerator.nextFloat()-0.5f)*maxTubeOffSet;
			tubeX[i]=Gdx.graphics.getWidth() - topTube.getWidth() / 2+i*distanceBwTubes;

			topRectangle[i]=new Rectangle();
			bottomrectangle[i]=new Rectangle();


		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState==1) {

			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2-topTube.getWidth()){
				score++;
				Gdx.app.log("score",score+"");
				if (scoringTube < numberOfTubes-1){
					scoringTube++;
				}
				else{
					scoringTube=0;
				}
			}

			if (Gdx.input.justTouched()){
				velocity=-30;
				//tubeOffset=(RandomGenerator.nextFloat()-0.5f)*maxTubeOffSet;
				/*Gdx.app.log("Random",tubeOffset+"");
				Gdx.app.log("Height",Gdx.graphics.getHeight()+"");*/
				//tubeX=Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2;
			}
			for(int i=0;i<numberOfTubes;i++) {
				if(tubeX[i]<-topTube.getWidth()){
					tubeX[i]+=numberOfTubes*distanceBwTubes;
					tubeOffset[i]=(RandomGenerator.nextFloat()-0.5f)*maxTubeOffSet;
				}
				else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topRectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomrectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2- bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight ());


			}


			if (birdY>0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}
			else{
				gameState=2;
			}
		}
		else if(gameState==0){
			if(Gdx.input.justTouched()){

				gameState=1;
			}
		}
		else if (gameState==2){


			for(int i=0;i<numberOfTubes;i++){
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
			}
			batch.draw(GameOver,Gdx.graphics.getWidth()/2-GameOver.getWidth()/2,Gdx.graphics.getHeight()/2-GameOver.getHeight()/2);

			if(Gdx.input.justTouched()){

				gameState=1;
				StartGame();
				score=0;
				scoringTube=0;
				velocity=0;
			}

		}
		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}


		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdY);

		font.draw(batch,String.valueOf(score),Gdx.graphics.getWidth()/2-20,Gdx.graphics.getHeight()-200);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2-10);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);*/

		for (int i=0;i<numberOfTubes;i++){
			/*shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2-bottomTube.getHeight() + tubeOffset[i],topTube.getWidth(),topTube.getHeight());*/

			if (Intersector.overlaps(birdCircle,topRectangle[i])||Intersector.overlaps(birdCircle,bottomrectangle[i])){
				//Gdx.app.log("Collosion","Lad gaya bhenchio");
				gameState=2;
			}

		}

		shapeRenderer.end();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
