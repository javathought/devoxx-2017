#language: fr
Fonctionnalité: Contrôle d'accès administrateur

  Contexte: : L'utilisateur se connecte avec un compte avec privilège administrateur
    Quand l'utilisateur se connecte avec l'identifiant "admin" et le mot de passe "root"

  @owasp-a7
  Scénario: un utilisateur avec le droit administrateur peut accéder à la liste des tâches
    Quand l'utilisateur appelle l'url "todos"
    Alors le code retour est 200

  @owasp-a7
  Scénario: un utilisateur avec le droit administrateur peut accéder à la liste des utilisateurs
    Quand l'utilisateur appelle l'url "users"
    Alors le code retour est 200
