#language: fr
Fonctionnalité: Contrôle d'accès utilisateur

  Contexte: L'utilisateur se connecte avec un compte sans privilège
    Quand l'utilisateur se connecte avec l'identifiant "normal" et le mot de passe "demo"

  @owasp-a7
  Scénario: un utilisateur standard peut accéder à la liste des tâches
    Quand l'utilisateur appelle l'url "todos"
    Alors le code retour est 200

  @owasp-a7
  Scénario: un utilisateur standard ne peut pas accéder à la liste des utilisateurs
    Quand l'utilisateur appelle l'url "users"
    Alors le code retour est 403

