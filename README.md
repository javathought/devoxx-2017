# Devoxx Fr 2017
TIA @ devoxx FR 2017 : Protéger son application web des risques de sécurité les plus critiques 

L'application est volontairement codée avec des failles afin de la corriger en passant en revue les TOP 10 OWASP 
des risques de sécurité les plus critiques : les remédiations ne portent que sur le TOP 10 et ne couvrent pas 
l'ensemble des mesures à mettre en oeuvre pour sécuriser son application.

## Initialisation de la base MySQL

````
  create database devoxx_tia;
  create user 'devoxx'@'localhost' IDENTIFIED BY 'owasp-2017;';
  GRANT ALL PRIVILEGES ON test.* TO 'devoxx'@'localhost';
  create user 'devoxx'@'172.17.0.1' IDENTIFIED BY 'owasp-2017;';
  GRANT ALL PRIVILEGES ON test.* TO 'devoxx'@'172.17.0.1';
````
 
## English 

Voluntarily vulnerable application as support for TIA (Tools In Action) @ DevoxxFr 2017.

This code is so absolutely NOT READY for PRODUCTION.