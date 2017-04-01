#language: fr

Fonctionnalité: Résistance aux risques Owasp

  @owasp-a1
  Scénario: Résiste aux injections SQL (A1)
    Quand l'utilisateur se connecte avec l'identifiant "normal" et le mot de passe "e' or '1' = '1"
    Alors le code retour est 401

  @owasp-a1
  Scénario: Se comporte normalement en l'absence d'injection SQL (A1)
    Quand l'utilisateur se connecte avec l'identifiant "normal" et le mot de passe "demo"
    Alors le code retour est 200

  @owasp-a2
  Scénario: La session est stockée dans un cookie securisé (A2)
    Quand l'utilisateur se connecte avec l'identifiant "normal" et le mot de passe "demo"
    Alors le code retour est 200
    Et la session est présente dans le cookie "Authorization"
    Et le cookie de session a l'attribut htpOnly
    Et le cookie de session a l'attribut secure

  @owasp-a4
  Scénario: La session est stockée dans un cookie securisé (A4)
    Quand l'utilisateur se connecte avec l'identifiant "normal" et le mot de passe "demo"
    Alors le code retour est 200
    Et la session est présente dans le cookie "Authorization"
    Et le cookie de session a l'attribut htpOnly
    Et le cookie de session a l'attribut secure
