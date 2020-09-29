/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * @author Nicol�s Penagos Montoya
 * nicolas.penagosm98@gmail.com
 **~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package model;

public class User {

	// -------------------------------------
    // Attributes
    // -------------------------------------
	private String username;
	private String password;
	
	// -------------------------------------
    // Constructors
    // -------------------------------------
	public User() {
		
	}
	
	public User(String username, String password) {
		
		this.username = username;
		this.password = password;
		
	}

	// -------------------------------------
    // Getters and setters
    // -------------------------------------
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
