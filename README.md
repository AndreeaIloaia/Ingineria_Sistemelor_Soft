# Ingineria_Sistemelor_Soft

Un proiect destinat intelegerii conceptelor care se ocupă de proiectarea, dezvoltarea, implementarea, testarea, și întreținerea sistemelor software.

Tema aplicatiei: 
Aplicatia pune la dispozitia fiecarui agent al firmei un terminal prin care: 
• agentul vizualizeaza lista tuturor produselor vândute de firma, împreuna cu preturile aferente și cantitatile existente pe stoc. 
• agentul poate comanda o cantitate dintr-un produs. După orice comanda valida, toti agentii logati în aplicație vor vedea lista actualizata a stocurilor (este posibil ca declansarea înregistrarii unei comenzi din partea unui agent sa conduca la un mesaj informativ "cantitate insuficienta în stoc"). 

Etapele proiectarii:
o Analizare cerintelor (identificare cerinte, functionalitati
o	Analiza
o	Proiectarea sistemului (diagrame si planificarea dezvoltarii aplicatiei)
o	Implementarea
o	Testare

Detalii:
•	Tehnologii folosite:
    * SQLite
    * IntelliJ IDEA Ultimate (Mediul de dezvoltare)
    * Java
    * JavaFX (GUI)
    * Hibernate
• ORM: Object / Relational Mapping (ORM)
• StarUML (Proiectarea diagramelor) 

Structura proiectului: Proiectul este organizat pe niveluri: Model, Persistence, Services, Server, Client.
-	Model: contine declaratii de entitati pentru controlul bazei de date. 
-	Persistence: folosim repository care permit interogarea bazei de date folosind Session Factory de la Hibernate. 
-	Services: contine toate interfetele folosite in applicate si clasa de exceptii. 
-	Server: contine toate implementarile specificate pana acum. 
-	Client: contine toate interfetele vizibile si controller-urile asociate, care comunica informatii la server. 

(pentru mai multe detalii, documentatia este inclusa in proiect)
