import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;

public class Project {

  //attributes
  private int projectNumber;
  private String projectName;
  private String buildingType;
  private String address;
  private int ERFNumber;
  private double totalFee;
  private double totalToDate;
  private Date deadline;
  private final Person architect;
  private final Person contractor;
  private final Person customer;
  private final Person projectManager;
  private final Person structuralEngineer;
  private boolean complete;
  private java.sql.Date completeDate;

  //constructor
  public Project(int projectNumber,
                 String projectName,
                 String buildingType,
                 String address,
                 int ERFNumber,
                 double totalFee,
                 double totalToDate,
                 Date deadline,
                 Person architect,
                 Person contractor,
                 Person customer,
                 Person projectManager,
                 Person structuralEngineer,
                 boolean complete,
                 java.sql.Date completeDate) {

    this.projectNumber = projectNumber;
    this.projectName = projectName;
    this.buildingType = buildingType;
    this.address = address;
    this.ERFNumber = ERFNumber;
    this.totalFee = totalFee;
    this.totalToDate = totalToDate;
    this.deadline = deadline;
    this.architect = architect;
    this.contractor = contractor;
    this.customer = customer;
    this.projectManager = projectManager;
    this.structuralEngineer = structuralEngineer;
    this.complete = complete;
    this.completeDate = completeDate;
  }

  //methods

  /**
   * toString Method
   * <br>
   * Constructs and returns an output String of project details
   *
   * @return output String of project details
   * @since version 1.0
   */
  public String toString() {

    String output = "Project Number: " + getProjectNumber();
    output += "\n\nProject Name: " + getProjectName();
    output += "\nBuilding Type: " + getBuildingType();
    output += "\nAddress: " + getAddress();
    output += "\nERF Number: " + getERFNumber();
    output += "\nTotal Fee: " + getTotalFee();
    output += "\nTotal Paid to Date: " + getTotalToDate();
    output += "\nDeadline: " + getDeadline();
    output += "\n\nArchitect:\n" + getArchitect();
    output += "\n\nContractor Details:\n" + getContractor();
    output += "\n\nCustomer Details:\n" + getCustomer();
    output += "\n\nProject Manager Details:\n" + getProjectManager();
    output += "\n\nStructural Engineer Details:\n" + getStructuralEngineer();
    output += "\n\nComplete: " + isComplete();
    if (isComplete()) {
      output += "\n\nCompleted Date: " + getCompleteDate();
    }
    return output;
  }

  /**
   * Method to request all project details from user
   * <br>
   * A sequence of user inputs to fill data about the project
   * input is validated.
   *
   * @param usrInput - a java.util.Scanner object
   * @since version 1.0
   */
  public void getProjectDetails(java.util.Scanner usrInput) {

    //get the project name
    System.out.println("Please enter the project name: ");
    this.setProjectName(usrInput.next());

    //get the building type
    System.out.println("Please enter the building type: ");
    this.setBuildingType(usrInput.next());

    //get the address
    System.out.println("Please enter the project address: ");
    this.setAddress(usrInput.next());

    //get the ERF number
    while (true) {
      try {
        System.out.println("Please enter the ERF number: ");
        this.setERFNumber(usrInput.nextInt());
        break;
      } catch (InputMismatchException ex) {
        System.out.println("Please ensure the ERF number is an integer");
        usrInput.next();
      }
    }

    //get the total fee
    while (true) {
      try {
        System.out.println("Please enter the total fee: ");
        this.setTotalFee(usrInput.nextDouble());
        break;
      } catch (InputMismatchException ex) {
        System.out.println("Please ensure the amount entered is a number");
        usrInput.next();
      }
    }

    //get the total to date
    while (true) {
      try {
        System.out.println("Please enter the total paid to date: ");
        this.setTotalToDate(usrInput.nextDouble());
        break;
      } catch (InputMismatchException ex) {
        System.out.println("Please ensure the amount entered is a number");
        usrInput.next();
      }
    }

    //set up date format
    SimpleDateFormat dateFormatInput = new SimpleDateFormat("dd/MM/yyyy");

    //get the date
    while (true) {
      try {
        System.out.println("Please enter the deadline date, as dd/MM/yyyy: ");
        String strDeadline = usrInput.next();
        Date dateDeadline = dateFormatInput.parse(strDeadline);
        this.setDeadline(dateDeadline);
        break;
      } catch (ParseException ex) {
        usrInput.nextLine();
        System.out.println("Please check the date format");
      }
    }
  }

  public int getProjectNumber() {
    return projectNumber;
  }

  public void setProjectNumber(int projectNumber) {
    this.projectNumber = projectNumber;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getBuildingType() {
    return buildingType;
  }

  public void setBuildingType(String buildingType) {
    this.buildingType = buildingType;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getERFNumber() {
    return ERFNumber;
  }

  public void setERFNumber(int ERFNumber) {
    this.ERFNumber = ERFNumber;
  }

  public double getTotalFee() {
    return totalFee;
  }

  public void setTotalFee(double totalFee) {
    this.totalFee = totalFee;
  }

  public double getTotalToDate() {
    return totalToDate;
  }

  public void setTotalToDate(double totalToDate) {
    this.totalToDate = totalToDate;
  }

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public Person getArchitect() {
    return architect;
  }

  public Person getContractor() {
    return contractor;
  }

  public Person getCustomer() {
    return customer;
  }

  public Person getProjectManager() {
    return projectManager;
  }

  public Person getStructuralEngineer() {
    return structuralEngineer;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  public java.sql.Date getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(java.sql.Date completeDate) {
    this.completeDate = completeDate;
  }
}
