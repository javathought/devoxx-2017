#language: fr
  
  Fonctionnalité: En tant qu'utilisateur, je veux pouvoir consulter mes "todos"
    
    Contexte: les informations d'identification sont correctes
      Etant donné l'url de l'application "https://localhost:9090/"
      Quand je me connecte avec le compte "admin" et le mot de passe "admin"
      Alors la connexion est acceptée
      
    Scénario: visualisation des todos
      Quand je vais à la page "todos"
      