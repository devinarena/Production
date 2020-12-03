package io.github.imunsmart.employee;

/**
 * Employee class that contains name, username, password and email and sets up employee emails and
 * passwords.
 *
 * @author Devin Arena
 * @version 1.0
 * @since 12/2/2020
 */
public class Employee {

  private final StringBuilder name;
  private String username;
  private final String password;
  private String email;

  /**
   * Initializes an employee object with a name and password, username is determined by name, if an
   * invalid name is supplied, username is defaulted to 'default' and email is defaulted to
   * 'user@oracleacademy.Test', if password is invalid it is defaulted to 'pw'.
   *
   * @param name     the name of the employee
   * @param password the employee's password
   */
  public Employee(String name, String password) {
    this.name = new StringBuilder(name);
    if (checkName(name)) {
      setUsername(name);
      setEmail(name);
    } else {
      username = "default";
      email = "user@oracleacademy.Test";
    }
    if (isValidPassword(password)) {
      this.password = password;
    } else {
      this.password = "pw";
    }
  }

  /**
   * Initializes an employee object with all fields, used when loading an employee record from the
   * database.
   *
   * @param name     the name of the employee
   * @param password the employee's password
   */
  public Employee(String name, String username, String email, String password) {
    this.name = new StringBuilder(name);
    this.username = username;
    this.email = email;
    this.password = password;
  }

  /**
   * Sets the username of an employee, username format is first letter of first name followed by
   * last name (all lower case).
   *
   * @param name the name of the employee
   */
  private void setUsername(String name) {
    username = name.toLowerCase().charAt(0) + name.split(" ")[1];
  }

  /**
   * Checks if an employee name is valid (contains a space).
   *
   * @param name the name of the employee
   * @return true if name contains a space, false if it does not
   */
  private boolean checkName(String name) {
    return name.contains(" ");
  }

  /**
   * Sets the email of the employee, in the format first.last@oracleacademy.Test.
   *
   * @param name the name of the employee
   */
  private void setEmail(String name) {
    email = name.replace(' ', '.').toLowerCase() + "@oracleacademy.Test";
  }

  public String getName() {
    return name.toString();
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  /**
   * Checks if the password is valid, containing at least one lowercase and uppercase letter and a
   * special character.
   *
   * @param password the password of the employee to check
   * @return true if the password contains an uppercase and lowercase letter and a special character
   */
  private boolean isValidPassword(String password) {
    return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{3,}$");
  }

  /**
   * Returns employee as a string with name, username, email, and password with a new line between
   * each.
   *
   * @return employee details as a string
   */
  @Override
  public String toString() {
    return "Employee Details" + "\nName: " + name.toString() + "\nUsername: " + username
        + "\nEmail: " + email
        + "\nInitial Password: " + password;
  }
}
