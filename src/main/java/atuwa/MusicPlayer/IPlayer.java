package atuwa.MusicPlayer;

public interface IPlayer{

	void end();

	void start();

	float setVol(float volume);

	void stop(boolean stop);

	float getVolume();

}
