# Software Systems Engineering

This project aims to use the concepts related to software systems design, development, implementation, testing, and maintenance.

### Application Theme:

The application provides each company agent with a terminal through which: 

• The agent views a list of all products the company sells, their prices, and the quantities available in stock. 

• The agent can order a quantity of a product. After any valid order, all agents logged into the application will see the updated list of stocks (triggering an order from an agent may result in an informative message 'insufficient quantity in stock').

### Design Steps:
    
    * Requirement analysis (identifying requirements, functionalities)
    * Analysis
    * System design (diagrams and application development planning)
    * Implementation
    * Testing

### Used Technologies:
 
    * SQLite
    * IntelliJ IDEA Ultimate (Development environment)
    * Java
    * JavaFX (GUI)
    * Hibernate
    • ORM: Object / Relational Mapping (ORM)
    • StarUML (Diagram design) 
    

### Project Structure: 
The project is organized into levels: Model, Persistence, Services, Server, and Client.

-	Model: contains entity declarations for database control.
-	Persistence: uses repositories that allow querying the database using Hibernate's
-	Services: contains all interfaces used in the application and the exception class. 
-	Server: contains all implementations specified so far. 
-	Client: contains all visible interfaces and associated controllers communicating information to the server.
	
(For more details, the documentation is included in the project.)"
