GNK
===

Repository du projet GN en Kit (GNK)

How To
------

   <ol>
    <li>clone repository</li>
    <li>import project into your favorite IDE</li>
    <li>upgrade project</li>

    grails upgrade // choose yes (you are sure)
  <li>install MySQL and create database gnkdb and user gnk (without password) [http://dev.mysql.com/downloads/mysql] (http://dev.mysql.com/downloads/mysql)</li>
    
    CREATE DATABASE gnkdb;
    CREATE USER 'gnk'@'localhost';
    GRANT ALL PRIVILEGES ON gnkdb.* TO 'gnk'@'localhost';
    FLUSH PRIVILEGES;
  <li>download schema and database data from [db](db) folder</li>
  <li>import it into your newly created database</li>
  
    mysql -u gnk gnkdb < dump20140122.sql
  <li>Thats'it :) run your project!</li>
  
    grails run-app
  <li>Navigate to [http://localhost:8090/gnk](http://localhost:8090/gnk)</li>
   </lo>
    
