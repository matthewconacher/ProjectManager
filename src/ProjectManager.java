import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Poised - Project Management System
 * <p>
 * A Simple Project Management System
 * to keep track of projects and
 * produce administrative output.
 *
 * @author Matthew Conacher
 * @version 1.0, 23 Jun 2020
 */
public class ProjectManager {

  public static void main(String[] args) throws SQLException {

    //keep a count of projects
    int countProjects = 0;

    //ArrayList to hold projects read from database
    ArrayList<Project> projectsArray = new ArrayList<>();

    Connection connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
        "otheruser",
        "swordfish"
    );

    Statement statement = connection.createStatement();

    try {
      //read all projects from file
      countProjects = readProjects(statement, projectsArray);

    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    //set up scanner for input
    Scanner usrInput = new Scanner(System.in);
    //use delimiter of line return for scanner
    usrInput.useDelimiter(System.lineSeparator());

    //use a variable to store menu choices made
    String usrChoice = "";

    //main menu loop
    while (!usrChoice.equals("e")) {

      printMainMenu();
      usrChoice = usrInput.next();

      //check what the user enters
      switch (usrChoice) {
        case "n": //new project
          Project newProject = getProject(usrInput, countProjects, projectsArray, statement);
          printOut(newProject);
          break;
        case "p": //pick a project
          System.out.println("Please enter a project number or name: ");
          //allow either project number or project name search
          String projectSearch = usrInput.next();
          int projectNum = 0;
          try {
            //numeric project number
            projectNum = Integer.parseInt(projectSearch) - 1;
            //print out the project
            printOut(projectsArray.get(projectNum));
          } catch (NumberFormatException ex) {
            //not numeric, so search, use user input as search term
            for (int projectIndex = 0; projectIndex < projectsArray.size(); projectIndex++) {
              if (projectsArray.get(projectIndex).getProjectName().equals(projectSearch)) {
                projectNum = projectIndex;
                //print out the project
                printOut(projectsArray.get(projectNum));
                break;
              } else if (projectIndex == projectsArray.size() - 1) {
                //not found
                System.out.println("Project not found");
                break;
              }
            }
          } catch (IndexOutOfBoundsException ex) {
            System.out.println("Project does not exist");
            break;
          }

          try {
            changeProject(usrInput, projectsArray, usrChoice, projectNum, statement);
          } catch (ParseException ex) {
            System.out.println("There is an error on the project date format, please check");
          }
          break;
        case "i": //display incomplete projects
          System.out.println("Incomplete Projects");
          for (Project value : projectsArray) {
            if (!value.isComplete()) {
              System.out.println("Project Number: " + value.getProjectNumber());
              System.out.println("Project Name: " + value.getProjectName() + "\n");
            }
          }
          break;
        case "l": //display late projects
          Date today = new Date();
          System.out.println("Late Projects");
          for (Project project : projectsArray) {
            if (today.after(project.getDeadline())) {
              System.out.println("Project Number: " + project.getProjectNumber());
              System.out.println("Project Name: " + project.getProjectName() + "\n");
            }
          }
          break;
      }
    }
    usrInput.close();
  }

  /**
   * Method to change project details - menu driven
   *
   * @param usrInput      - a Scanner for user input
   * @param projectsArray - an ArrayList to hold the projects
   * @param usrChoice     - to hold the users menu selection
   * @param projectNum    - the project number variable
   * @param statement     - the SQL statement
   * @since version 1.0
   */
  private static void changeProject(Scanner usrInput,
                                    ArrayList<Project> projectsArray,
                                    String usrChoice,
                                    int projectNum,
                                    Statement statement)
      throws ParseException {

    //set up date formats
    SimpleDateFormat dateFormatInput = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateFormatSQL = new SimpleDateFormat("yyyy-MM-dd");

    printProjectMenu();

    while (!usrChoice.equals("e")) {
      usrChoice = usrInput.next();
      //check what the user enters
      switch (usrChoice) {
        case "d": //change due date
          System.out.println("Current Due date is: " +
              dateFormatInput.format(projectsArray.get(projectNum).getDeadline()));
          System.out.println("Please enter a new date (dd/MM/yyyy): ");
          String strDeadline = usrInput.next();
          Date dateDeadline = dateFormatInput.parse(strDeadline);
          projectsArray.get(projectNum).setDeadline(dateDeadline);
          //use SQL to write to database
          try {
            statement.executeUpdate("UPDATE Project SET Deadline ='" +
                dateFormatSQL.format(dateDeadline) + "'" +
                " WHERE ProjectID=" + projectsArray.get(projectNum).getProjectNumber() + ";");
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          printOut(projectsArray.get(projectNum));
          printProjectMenu();
          break;
        case "t": //change total paid
          System.out.println("Total paid to date: " + projectsArray.get(projectNum).getTotalToDate());
          System.out.println("Please enter a new amount: ");
          projectsArray.get(projectNum).setTotalToDate(usrInput.nextInt());
          //use SQL to write to database
          try {
            statement.executeUpdate("UPDATE Project SET TotalPaid=" +
                projectsArray.get(projectNum).getTotalToDate() +
                " WHERE ProjectID=" + projectsArray.get(projectNum).getProjectNumber() + ";");
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          printOut(projectsArray.get(projectNum));
          printProjectMenu();
          break;
        case "c": //change contractor details
          //get the contractor name
          System.out.println("Please enter the Contractor Name: ");
          projectsArray.get(projectNum).getContractor().setName(usrInput.next());
          //get the contractor phone number
          System.out.println("Please enter the Contractor Phone Number: ");
          projectsArray.get(projectNum).getContractor().setTelephoneNumber(usrInput.next());
          //get the contractor email address
          System.out.println("Please enter the Contractor Email Address: ");
          projectsArray.get(projectNum).getContractor().setEmailAddress(usrInput.next());
          //get the contractor phone number
          System.out.println("Please enter the Contractor Address: ");
          projectsArray.get(projectNum).getContractor().setAddress(usrInput.next());
          try {
            statement.executeUpdate("UPDATE Contractor SET " +
                "ContractorName='" + projectsArray.get(projectNum).getContractor().getName() + "', " +
                "ContractorTel='" + projectsArray.get(projectNum).getContractor().getTelephoneNumber() + "', " +
                "ContractorEmail='" + projectsArray.get(projectNum).getContractor().getEmailAddress() + "', " +
                "ContractorAddress='" + projectsArray.get(projectNum).getContractor().getAddress() + "' " +
                "WHERE ContractorID=" + projectsArray.get(projectNum).getContractor().getPersonID() + ";");
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          //Update the projectsArray
          try {
            //read all projects from file
            readProjects(statement, projectsArray);

          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          printOut(projectsArray.get(projectNum));
          printProjectMenu();
          break;
        case "a": //change architect details
          //get the Architect name
          System.out.println("Please enter the Architect Name: ");
          projectsArray.get(projectNum).getArchitect().setName(usrInput.next());
          //get the Architect phone number
          System.out.println("Please enter the Architect Phone Number: ");
          projectsArray.get(projectNum).getArchitect().setTelephoneNumber(usrInput.next());
          //get the Architect email address
          System.out.println("Please enter the Architect Email Address: ");
          projectsArray.get(projectNum).getArchitect().setEmailAddress(usrInput.next());
          //get the Architect phone number
          System.out.println("Please enter the Architect Address: ");
          projectsArray.get(projectNum).getArchitect().setAddress(usrInput.next());
          try {
            statement.executeUpdate("UPDATE Architect SET " +
                "ArchitectName='" + projectsArray.get(projectNum).getArchitect().getName() + "', " +
                "ArchitectTel='" + projectsArray.get(projectNum).getArchitect().getTelephoneNumber() + "', " +
                "ArchitectEmail='" + projectsArray.get(projectNum).getArchitect().getEmailAddress() + "', " +
                "ArchitectAddress='" + projectsArray.get(projectNum).getArchitect().getAddress() + "' " +
                "WHERE ArchitectID=" + projectsArray.get(projectNum).getArchitect().getPersonID() + ";");
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          //Update the projectsArray
          try {
            //read all projects from file
            readProjects(statement, projectsArray);

          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          printOut(projectsArray.get(projectNum));
          printProjectMenu();
          break;
        case "s": //change Customer details
          //get the Customer name
          System.out.println("Please enter the Customer Name: ");
          projectsArray.get(projectNum).getCustomer().setName(usrInput.next());
          //get the Customer phone number
          System.out.println("Please enter the Customer Phone Number: ");
          projectsArray.get(projectNum).getCustomer().setTelephoneNumber(usrInput.next());
          //get the Customer email address
          System.out.println("Please enter the Customer Email Address: ");
          projectsArray.get(projectNum).getCustomer().setEmailAddress(usrInput.next());
          //get the Customer phone number
          System.out.println("Please enter the Customer Address: ");
          projectsArray.get(projectNum).getCustomer().setAddress(usrInput.next());
          try {
            statement.executeUpdate("UPDATE Customer SET " +
                "CustomerName='" + projectsArray.get(projectNum).getCustomer().getName() + "', " +
                "CustomerTel='" + projectsArray.get(projectNum).getCustomer().getTelephoneNumber() + "', " +
                "CustomerEmail='" + projectsArray.get(projectNum).getCustomer().getEmailAddress() + "', " +
                "CustomerAddress='" + projectsArray.get(projectNum).getCustomer().getAddress() + "' " +
                "WHERE CustomerID=" + projectsArray.get(projectNum).getCustomer().getPersonID() + ";");
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          //Update the projectsArray
          try {
            //read all projects from file
            readProjects(statement, projectsArray);

          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          printOut(projectsArray.get(projectNum));
          printProjectMenu();
          break;
        case "f": //finalise project
          //finalise project
          projectsArray.get(projectNum).setComplete(true);
          java.sql.Date completeDate = new java.sql.Date(System.currentTimeMillis());
          projectsArray.get(projectNum).setCompleteDate(completeDate);

          //Update the database
          try {
            statement.executeUpdate("UPDATE Project SET " +
                "Completed='1', " +
                "CompletedDate='" + dateFormatSQL.format(completeDate) + "' " +
                "WHERE ProjectID=" + projectsArray.get(projectNum).getProjectNumber() + ";");
          } catch (SQLException ex) {
            ex.printStackTrace();
          }

          try {
            //calculate the amount left to pay
            double amountToPay = projectsArray.get(projectNum).getTotalFee()
                - projectsArray.get(projectNum).getTotalToDate();
            //only create invoice if there is something left to pay
            if (amountToPay > 0) {
              createInvoice(projectsArray, projectNum, amountToPay);
            }
          } catch (IOException ex) {
            System.out.println("There was an error creating the invoice file");
          }
          printOut(projectsArray.get(projectNum));
          try {
            //create the completed project file
            createCompletedProjectFile(projectsArray, projectNum);
          } catch (IOException ex) {
            System.out.println("There was an error creating the completed project file");
          }
          printProjectMenu();
          break;
        case "e": //return to main menu
          return;
      }
    }
  }

  /**
   * This method generates the completed projects file (.txt).
   *
   * @param projectsArray - an ArrayList of projects
   * @param projectNum    - the project number
   * @throws IOException - there could be an IOException on creating the file.
   */
  private static void createCompletedProjectFile(ArrayList<Project> projectsArray, int projectNum)
      throws IOException {
    Project project = projectsArray.get(projectNum);
    File completedProjectFile = new File("Completed Project - " + project.getProjectName() + ".txt");

    //write details of project to file
    FileWriter completedProjectWriter = new FileWriter(completedProjectFile);
    completedProjectWriter.write("Completed Project Details\n\n");
    completedProjectWriter.write(project.toString());
    completedProjectWriter.close();

  }

  /**
   * This method calls all the user input methods to fill
   * data into a project object from the database
   * and then add this to the projectsArray.
   *
   * @param usrInput      - to accept input from the user
   * @param countProjects - a count of the projects read from db
   * @param projectsArray - an array to hold the projects read from db
   * @return - return
   */
  private static Project getProject(Scanner usrInput,
                                    int countProjects,
                                    ArrayList<Project> projectsArray,
                                    Statement statement) throws SQLException {
    //new architect object
    Person architect = new Person(0, "", "", "", "", "");
    //get the architect details
    architect.getPersonDetails("Architect", usrInput);
    //update database if required with new Architect
    ResultSet result = statement.executeQuery("SELECT * FROM Architect " +
        "WHERE ArchitectName LIKE '%" + architect.getName() + "%' " +
        "AND ArchitectAddress LIKE '%" + architect.getAddress() + "%';");
    if (result.next()) {
      architect.setPersonID(result.getInt(1));
    } else { //only add to the database if not found
      statement.executeUpdate("INSERT INTO Architect (ArchitectName, ArchitectTel, " +
          "ArchitectEmail, ArchitectAddress) \n" +
          "VALUES ('" +
          architect.getName() + "', '" +
          architect.getTelephoneNumber() + "', '" +
          architect.getEmailAddress() + "', '" +
          architect.getAddress() + "');");
      //get the last generated ID from the table
      result = statement.executeQuery("SELECT LAST_INSERT_ID();");
      //set the architect object ID to the added ID value
      architect.setPersonID(result.getInt(1));
    }

    //new contractor object
    Person contractor = new Person(0, "", "", "", "", "");
    //get the contractor details
    contractor.getPersonDetails("Contractor", usrInput);
    //update the database if required with new Contractor
    result = statement.executeQuery("SELECT * FROM Contractor " +
        "WHERE ContractorName LIKE '%" + contractor.getName() + "%' " +
        "AND ContractorAddress LIKE '%" + contractor.getAddress() + "%';");
    if (result.next()) {
      contractor.setPersonID(result.getInt(1));
    } else { //only add to the database if not found
      statement.executeUpdate("INSERT INTO Contractor (ContractorName, ContractorTel, " +
          "ContractorEmail, ContractorAddress) \n" +
          "VALUES ('" +
          contractor.getName() + "', '" +
          contractor.getTelephoneNumber() + "', '" +
          contractor.getEmailAddress() + "', '" +
          contractor.getAddress() + "');");
      //get the last generated ID from the table
      result = statement.executeQuery("SELECT LAST_INSERT_ID();");
      //set the contractor object ID to the added ID value
      contractor.setPersonID(result.getInt(1));
    }

    //new customer object
    Person customer = new Person(0, "", "", "", "", "");
    //get the customer details
    customer.getPersonDetails("Customer", usrInput);
    //update the database if required with new Contractor
    result = statement.executeQuery("SELECT * FROM Customer " +
        "WHERE CustomerName LIKE '%" + customer.getName() + "%' " +
        "AND CustomerAddress LIKE '%" + customer.getAddress() + "%';");
    if (result.next()) {
      customer.setPersonID(result.getInt(1));
    } else { //only add to the database if not found
      statement.executeUpdate("INSERT INTO Customer (CustomerName, CustomerTel, " +
          "CustomerEmail, CustomerAddress) \n" +
          "VALUES ('" +
          customer.getName() + "', '" +
          customer.getTelephoneNumber() + "', '" +
          customer.getEmailAddress() + "', '" +
          customer.getAddress() + "');");
      //get the last generated ID from the table
      result = statement.executeQuery("SELECT LAST_INSERT_ID();");
      //set the customer object ID to the added ID value
      customer.setPersonID(result.getInt(1));

    }

    //new projectManager object
    Person projectManager = new Person(0, "", "", "", "", "");
    //get the project manager details
    projectManager.getPersonDetails("Project Manager", usrInput);
    //update the database if required with new Project Manager
    result = statement.executeQuery("SELECT * FROM ProjectManager " +
        "WHERE ProjectManagerName LIKE '%" + projectManager.getName() + "%' " +
        "AND ProjectManagerAddress LIKE '%" + projectManager.getAddress() + "%';");
    if (result.next()) {
      projectManager.setPersonID(result.getInt(1));
    } else { //only add to the database if not found
      statement.executeUpdate("INSERT INTO ProjectManager (ProjectManagerName, ProjectManagerTel, " +
          "ProjectManagerEmail, ProjectManagerAddress) \n" +
          "VALUES ('" +
          projectManager.getName() + "', '" +
          projectManager.getTelephoneNumber() + "', '" +
          projectManager.getEmailAddress() + "', '" +
          projectManager.getAddress() + "');");
      //get the last generated ID from the table
      result = statement.executeQuery("SELECT LAST_INSERT_ID();");
      //set the project manager object ID to the added ID value
      projectManager.setPersonID(result.getInt(1));
    }

    //new structuralEngineer object
    Person structuralEngineer = new Person(0, "", "", "", "", "");
//get the structural engineer details
    structuralEngineer.getPersonDetails("Structural Engineer", usrInput);
    //update the database if required with new Contractor
    result = statement.executeQuery("SELECT * FROM StructuralEng " +
        "WHERE StructuralEngName LIKE '%" + contractor.getName() + "%' " +
        "AND StructuralEngAddress LIKE '%" + contractor.getAddress() + "%';");
    if (result.next()) {
      structuralEngineer.setPersonID(result.getInt(1));
    } else { //only add to the database if not found
      statement.executeUpdate("INSERT INTO StructuralEngineer (StructuralEngName, StructuralEngTel, " +
          "StructuralEngEmail, StructuralEngAddress) \n" +
          "VALUES ('" +
          structuralEngineer.getName() + "', '" +
          structuralEngineer.getTelephoneNumber() + "', '" +
          structuralEngineer.getEmailAddress() + "', '" +
          structuralEngineer.getAddress() + "');");
      //get the last generated ID from the table
      result = statement.executeQuery("SELECT LAST_INSERT_ID();");
      //set the structural engineer object ID to the added ID value
      structuralEngineer.setPersonID(result.getInt(1));
    }

    //new project object
    Project newProject = new Project(countProjects + 1,
        "",
        "",
        "",
        0,
        0,
        0,
        null,
        architect,
        contractor,
        customer,
        projectManager,
        structuralEngineer,
        false,
        null);

    //get new project details
    newProject.getProjectDetails(usrInput);

    //if the project name is left blank create the project name
    if (newProject.getProjectName().equals("")) {
      String fullName = newProject.getCustomer().getName();
      String lastName = fullName.substring(fullName.indexOf(" "));
      //set project name to building type + customer last name if no name given
      newProject.setProjectName(newProject.getBuildingType() + lastName);
    }

    //add project to Array
    projectsArray.add(countProjects, newProject);

    //Update the master project table
    statement.executeUpdate("INSERT INTO Project (" +
        "ProjectName, " +
        "BuildingType, " +
        "Address, " +
        "ERFNumber, " +
        "TotalFee, " +
        "TotalPaid, " +
        "Deadline, " +
        "ArchitectID, " +
        "ContractorID, " +
        "CustomerID, " +
        "ProjectManagerID, " +
        "StructuralEngID, " +
        "Completed, " +
        "CompletedDate) " +
        "VALUES ('" +
        newProject.getProjectName() + "', '" +
        newProject.getBuildingType() + "', '" +
        newProject.getAddress() + "', " +
        newProject.getERFNumber() + ", " +
        newProject.getTotalFee() + ", " +
        newProject.getTotalToDate() + ", '" +
        newProject.getDeadline() + "', " +
        architect.getPersonID() + ", " +
        contractor.getPersonID() + ", " +
        customer.getPersonID() + ", " +
        projectManager.getPersonID() + ", " +
        structuralEngineer.getPersonID() + ", " +
        "0, " +
        "null)");

    return newProject;
  }

  /**
   * Prints out the project to the standard output
   *
   * @param project - the project object to print out to screen
   */
  public static void printOut(Project project) {
    //print the details of the project
    System.out.print(project);
  }

  /**
   * Prints the main menu - with top level options
   */
  private static void printMainMenu() {
    //print the main menu
    System.out.println("\nPoised - Project Management Software\n");

    System.out.println("Enter 'n' to add a new project");
    System.out.println("Enter 'p' pick a project to display");
    System.out.println("Enter 'i' to display incomplete projects");
    System.out.println("Enter 'l' to display late projects");
    System.out.println("Enter 'e' to exit");
  }

  /**
   * Prints the project menu - with project editing and finalisation options
   */
  private static void printProjectMenu() {
    System.out.println("\n\nEnter 'd' to change due date");
    System.out.println("Enter 't' to change total amount of the fee paid to date");
    System.out.println("Enter 'a' to change update the Architect details");
    System.out.println("Enter 'c' to change update the Contractor details");
    System.out.println("Enter 's' to change update the Customer details");
    System.out.println("Enter 'f' to finalise the project and produce invoice");
    System.out.println("Enter 'e' to return to main menu");
  }

  /**
   * Reads all projects from the database using SQL
   *
   * @param projectsArray - an array of project objects
   * @return - return
   */
  private static int readProjects(Statement statement,
                                  ArrayList<Project> projectsArray) throws SQLException {
    int countProjects = 0;
    //declare results set
    ResultSet results;
    //run query to return rows from projects table
    results = statement.executeQuery("SELECT * FROM Project " +
        "LEFT JOIN Architect ON Project.ArchitectID = Architect.ArchitectID " +
        "LEFT JOIN Contractor ON Project.ContractorID = Contractor.ContractorID " +
        "LEFT JOIN Customer ON Project.CustomerID = Customer.CustomerID " +
        "LEFT JOIN ProjectManager ON Project.ProjectManagerID = ProjectManager.ProjectManagerID " +
        "LEFT JOIN StructuralEngineer ON Project.StructuralEngID = StructuralEngineer.StructuralEngID;");

    while (results.next()) {
      //new architect object
      Person architect = new Person(0, "", "", "", "", "");
      //add values from query
      architect.setPersonID(results.getInt("Architect.ArchitectID"));
      architect.setName(results.getString("ArchitectName"));
      architect.setPersonType("Architect");
      architect.setTelephoneNumber(results.getString("ArchitectTel"));
      architect.setEmailAddress(results.getString("ArchitectEmail"));
      architect.setAddress(results.getString("ArchitectAddress"));

      //new contractor object
      Person contractor = new Person(0, "", "", "", "", "");
      //add values from query
      contractor.setPersonID(results.getInt("Contractor.ContractorID"));
      contractor.setName(results.getString("ContractorName"));
      contractor.setPersonType("Contractor");
      contractor.setTelephoneNumber(results.getString("ContractorTel"));
      contractor.setEmailAddress(results.getString("ContractorEmail"));
      contractor.setAddress(results.getString("ContractorAddress"));

      //new customer object
      Person customer = new Person(0, "", "", "", "", "");
      //add values from query
      customer.setPersonID(results.getInt("Customer.CustomerID"));
      customer.setName(results.getString("CustomerName"));
      customer.setPersonType("Customer");
      customer.setTelephoneNumber(results.getString("CustomerTel"));
      customer.setEmailAddress(results.getString("CustomerEmail"));
      customer.setAddress(results.getString("CustomerAddress"));

      //new project manager object
      Person projectManager = new Person(0, "", "", "", "", "");
      //add values from query
      projectManager.setPersonID(results.getInt("ProjectManager.ProjectManagerID"));
      projectManager.setName(results.getString("ProjectManagerName"));
      projectManager.setPersonType("Project Manager");
      projectManager.setTelephoneNumber(results.getString("ProjectManagerTel"));
      projectManager.setEmailAddress(results.getString("ProjectManagerEmail"));
      projectManager.setAddress(results.getString("ProjectManagerAddress"));

      //new structural engineer object
      Person structuralEngineer = new Person(0, "", "", "", "", "");
      //add values from query
      structuralEngineer.setPersonID(results.getInt("StructuralEngineer.StructuralEngID"));
      structuralEngineer.setName(results.getString("StructuralEngName"));
      structuralEngineer.setPersonType("Structural Engineer");
      structuralEngineer.setTelephoneNumber(results.getString("StructuralEngTel"));
      structuralEngineer.setEmailAddress(results.getString("StructuralEngEmail"));
      structuralEngineer.setAddress(results.getString("StructuralEngAddress"));

      //new project object
      Project newProject = new Project(0, "", "", "", 0, 0, 0, null, architect, contractor, customer,
          projectManager, structuralEngineer, false, null);

      //set up new project object from database results
      newProject.setProjectNumber(results.getInt("ProjectID"));
      newProject.setProjectName(results.getString("ProjectName"));
      newProject.setBuildingType(results.getString("BuildingType"));
      newProject.setAddress(results.getString("Address"));
      newProject.setERFNumber(results.getInt("ERFNumber"));
      newProject.setTotalFee(results.getDouble("TotalFee"));
      newProject.setTotalToDate(results.getDouble("TotalPaid"));
      newProject.setDeadline(results.getDate("Deadline"));
      newProject.setComplete(results.getBoolean("Completed"));
      newProject.setCompleteDate(results.getDate("CompletedDate"));

      //add project to Array
      projectsArray.add(countProjects, newProject);
      //increment count of projects
      countProjects += 1;
    }
    return countProjects;
  }

  /**
   * Creates a customer invoice
   *
   * @param projectsArray - an array of project objects
   * @param projectNum    - the project number to be used to create the invoice
   * @param amountToPay   - the amount left to pay
   * @throws IOException - An exception is thrown if there is an error creating the invoice
   */
  private static void createInvoice(ArrayList<Project> projectsArray,
                                    int projectNum,
                                    double amountToPay) throws IOException {
    Project project = projectsArray.get(projectNum);
    File invoiceFile = new File(project.getProjectName() + " - Invoice.txt");

    //write details of project to file
    FileWriter invoiceWriter = new FileWriter(invoiceFile);
    invoiceWriter.write("Invoice for " + project.getProjectName() + "\n\n");
    invoiceWriter.write("Date Completed: " + project.getCompleteDate() + "\n");
    invoiceWriter.write("Customer Name: " + project.getCustomer().getName() + "\n");
    invoiceWriter.write("Customer Address: " + project.getCustomer().getAddress() + "\n");
    invoiceWriter.write("Customer Telephone: " + project.getCustomer().getTelephoneNumber() + "\n");
    invoiceWriter.write("Customer Email: " + project.getCustomer().getEmailAddress() + "\n\n");
    invoiceWriter.write("Total to Pay: £" + amountToPay + "\n");
    invoiceWriter.close();

  }

}
