package globalHelper;
/*
 * A singleton class hold email of the user to get its information whenever needed
 */
public final class CurrentUserEmail {

	private static CurrentUserEmail currentUserEmail = null;
	private static String email = null;
	
	private CurrentUserEmail() {
	}
	
	public static CurrentUserEmail getInstance() {
		if(currentUserEmail == null) {
			currentUserEmail = new CurrentUserEmail();
		}
		return currentUserEmail;
	}
	
	
	public  String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		currentUserEmail.email = email;
	}
	
	
	
	
}
