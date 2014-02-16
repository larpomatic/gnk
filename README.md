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
  <li>install MySQL and create database gnkdb and user gnk (without password)</li>
    
    CREATE DATABASE gnkdb;
    CREATE USER 'gnk'@'localhost';
    GRANT ALL PRIVILEGES ON gnkdb.* TO 'gnk'@'localhost';
    FLUSH PRIVILEGES;
  <li>download schema and database data from [db](db) folder</li>
   </lo>
    
