/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * @author Nicolás Penagos Montoya
 * nicolas.penagosm98@gmail.com
 **~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package tcpserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import com.google.gson.Gson;

import model.User;
import processing.core.PApplet;


public class Main extends PApplet {

	// -------------------------------------
	// Global variables
	// -------------------------------------
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private boolean isAlive;

	// -------------------------------------
	// Attributes
	// -------------------------------------
	private Hashtable<String, String> users;
	private String msg;
	private String currentUser;
	private boolean matched;
	private boolean connected;
	private int dim;

	// -------------------------------------
	// Main method
	// -------------------------------------
	public static void main(String[] args) {
		PApplet.main("tcpserver.Main");
	}

	// -------------------------------------
	// Processing methods
	// -------------------------------------
	public void settings() {
		size(500, 250);
	}

	public void setup() {

		msg = "                Waiting for client..";
		currentUser = "";
		matched = false;
		connected = false;
		
		users = new Hashtable<String, String>();
		users.put("jester", "jr12345678");
		users.put("catmoon", "c4tm00n");
		users.put("zarcot", "z12345cot");
		users.put("theKiller", "Killer9820");

		isAlive = true;
		initServer();
		
		//Background animation
		dim = width / 2;
		background(0);
		colorMode(HSB, 360, 100, 100);
		noStroke();
		ellipseMode(RADIUS);
		frameRate(5);

	

	}

	public void draw() {

		//Background animation
		background(0);
		colorMode(HSB, 360, 100, 100);

		for (int x = 0; x <= width; x += dim) {
			drawGradient(x, height / 2);
		}

		//Message from client
		if (!currentUser.equals("")) {
			msg = (matched) ? "                       Welcome! \n                          " + currentUser : "Failed attempt";
		} else {
			
			if(connected)
				msg = "Please enter your username and login";
			else
				msg = "              Waiting for client...";
		}

		colorMode(RGB, 255);
		fill(0);
		rect(0, height / 2 - 55, width, 100);

		fill(255);
		textSize(18);
		text(msg, 80, 110);

	}

	public void drawGradient(float x, float y) {
		int radius = dim / 2;
		float h = random(0, 360);
		for (int r = radius; r > 0; --r) {
			fill(h, 90, 90);
			ellipse(x, y, r, r);
			h = (h + 1) % 360;
		}
	}

	// -------------------------------------
	// Connection method
	// -------------------------------------
	private void initServer() {

		new Thread(

				() -> {

					try {

						System.out.println("Esperando cliente");
						InetAddress i = InetAddress.getLocalHost();
						System.out.println(i.getHostAddress());
						ServerSocket serverSocket = new ServerSocket(5000);
						socket = serverSocket.accept();
						connected = true;
						System.out.println("Cliente conectado");

						InputStream inputStream = socket.getInputStream();
						InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
						reader = new BufferedReader(inputStreamReader);

						OutputStream ouputStream = socket.getOutputStream();
						OutputStreamWriter ouputStreamWriter = new OutputStreamWriter(ouputStream);
						writer = new BufferedWriter(ouputStreamWriter);

						Gson gson = new Gson();

						while (isAlive) {

							String json = reader.readLine();
						
							if(json==null) {
								
								msg = "                Waiting for client..";
								
							}else if (json.equals("logout")) {
								
								msg = "Please enter your username and login";
								currentUser = "";
								
							} else {

								User user = gson.fromJson(json, User.class);

								String username = user.getUsername();
								String password = user.getPassword();
								System.out.println(username + " " + password);
								String savedPassword = users.get(username);

								if (savedPassword == null || !savedPassword.equals(password)) {

									matched = false;
									sendMsg("false");

								} else {

									matched = true;
									currentUser = username;
									sendMsg("true");

								}

							}

						}

					} catch(NullPointerException e) {
						System.out.println("Hola");
						msg="Client disconnected, reestart both applications";
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

		).start();

	}

	private void sendMsg(String msg) {

		try {

			writer.write(msg + "\n");
			writer.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
