package atuwa.MusicPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class App{
	private static String command;
	public static void main(String[] args) throws IOException{
		JFrame f=new JFrame("MusicPlayer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		InputStreamReader isr=new InputStreamReader(System.in);
		final BufferedReader br=new BufferedReader(isr);//コンソールから1行ずつ取得する為のオブジェクト
		if(args.length>0&&!args[0].equals("-"))command=args[0];
		else {
			System.out.println("サーバのポート(半角数字)");
			command=br.readLine();//1行取得する
		}
		//0文字だったらデフォルト、それ以外だったら数値化
		int proxy_port=command.isEmpty()?6000:Integer.parseInt(command);
		ServerSocket ss=new ServerSocket(proxy_port);
		new Thread(){
			public void run(){
				while(true){
					try{
						command=br.readLine();//1行取得する
						if(command==null) System.exit(1);//読み込み失敗した場合終了
						if("exit".equals(command)){
							ss.close();//サーバ終了
							System.exit(0);//プログラム終了
						}else if("play".equals(command)){
							WAVPlayer.play();
						}else if("playF".equals(command)){
							WAVPlayer.play("https://atuwa.github.io/test/test.mp3");
						}else if("Nvol".equals(command)){
							float vol=WAVPlayer.nowVolume();
							System.out.println(vol);
						}else if("Svol".equals(command)){
							float vol=WAVPlayer.setVolume(20);
							System.out.println(vol);
						}else if("stop".equals(command)){
							WAVPlayer.Stop();
						}
					}catch(IOException e){//コンソールから取得できない時終了
						System.exit(1);
					}
				}
			}
		}.start();
		//スレッドプールを用意(最低1スレッド維持、空きスレッド60秒維持)
		ExecutorService pool=new ThreadPoolExecutor(1, Integer.MAX_VALUE,60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
		while(true){//無限ループ
			try{
				Socket s=ss.accept();//サーバ受付
				NetworkConection r=new NetworkConection(s);//接続単位でインスタンス生成
				pool.execute(r);//スレッドプールで実行する
			}catch(IOException e){//例外は無視

			}
		}
	}
}
