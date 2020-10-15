public class Person {

  //attributes
  private int personID;
  private String name;
  private String personType;
  private String telephoneNumber;
  private String emailAddress;
  private String address;

  //constructor
  public Person(
      int personID,
      String name,
      String personType,
      String telephoneNumber,
      String emailAddress,
      String address
  ) {
    this.personID = personID;
    this.name = name;
    this.personType = personType;
    this.telephoneNumber = telephoneNumber;
    this.emailAddress = emailAddress;
    this.address = address;
  }

  //methods

  /**
   * toString Method
   * <br>
   * Constructs and returns an output string of person details.
   *
   * @return output String of person details
   * @since version 1.0
   */
  public String toString() {

    String output = "Name: " + getName();
    output += "\nPerson Type: " + getPersonType();
    output += "\nTelephone Number: " + getTelephoneNumber();
    output += "\nEmail Address: " + getEmailAddress();
    output += "\nAddress: " + getAddress();
    return output;
  }

  /**
   * Method to request all person details from user
   * <br>
   * A sequence of user inputs to fill data about the person
   *
   * @param type     - person type to create
   * @param usrInput - Scanner for data entry
   * @since version 1.0
   */
  public void getPersonDetails(String type, java.util.Scanner usrInput) {

    //get the person name
    System.out.println("Please enter the " + type + " Name: ");
    this.setName(usrInput.next());
    //set the person type
    this.personType = type;
    //get the phone number
    System.out.println("Please enter the " + type + " Phone Number: ");
    this.setTelephoneNumber(usrInput.next());
    //get the email address
    System.out.println("Please enter the " + type + " Email Address: ");
    this.setEmailAddress(usrInput.next());
    //get the address
    System.out.println("Please enter the " + type + " Address: ");
    this.setAddress(usrInput.next());
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPersonType() {
    return personType;
  }

  public void setPersonType(String personType) {
    this.personType = personType;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getPersonID() {
    return personID;
  }

  public void setPersonID(int personID) {
    this.personID = personID;
  }
}
