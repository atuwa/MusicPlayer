package atuwa.MusicPlayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class NetworkConection implements Runnable{
	private Socket soc;
	public NetworkConection(Socket s){
		soc=s;
	}
	@Override
	public void run(){
		try{
			InputStream is=soc.getInputStream();
			DataInputStream dis=new DataInputStream(is);
			int com=dis.read();
			if(com==1) {
				WAVPlayer.Stop();
			}else if(com==2) {
				float vol=dis.readFloat();
				WAVPlayer.setVolume(vol);
			}else if(com==3) {
				DataOutputStream dos=new DataOutputStream(soc.getOutputStream());
				float vol=WAVPlayer.nowVolume();
				dos.writeFloat(vol);
			}else if(com==4) {
				String url=dis.readUTF();
				WAVPlayer.play(url);
			}else if(com==5) {
				WAVPlayer.play();
			}else {
				System.out.println("謎コマンド"+com);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}