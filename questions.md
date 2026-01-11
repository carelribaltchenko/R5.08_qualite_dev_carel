# Rapport de TP : Qualité de Développement - Projet Order Flow

## Tâche 1 : Ségrégation des responsabilités

### 1.1 Principaux domaines métiers
D'après la cartographie du contexte, l'application **Order Flow** s'articule autour de trois domaines clés identifiés pour la suite du développement :
* **Product Registry** : Gestion centralisée du catalogue et cycle de vie des produits.
* **Store** : Gestion de la boutique et de l'exposition des offres clients.
* **Order Management** : Gestion du flux de commandes et de leur état.

### 1.2 Conception des services
Les services sont structurés selon une approche microservices pour isoler les responsabilités. L'implémentation repose sur le pattern **CQRS** (Command Query Responsibility Segregation) afin de séparer les flux :
* Les **"Domain-Services"** se concentrent sur la validation métier et le traitement des commandes (écriture).
* Les **"Read-Services"** sont projetés pour optimiser la consultation des données (lecture).

### 1.3 Responsabilités des modules
| Module | Responsabilité identifiée |
| :--- | :--- |
| **`apps/store-back`** | Backend-for-Frontend (BFF). Sert de passerelle entre le front et les services internes. |
| **`apps/store-front`** | Interface utilisateur (en cours d'intégration). |
| **`libs/kernel`** | Bibliothèque transverse contenant les types de base et la logique commune immuable. |
| **`apps/product-registry-domain-service`** | Service gérant les commandes de modification du catalogue (Côté Écriture). |
| **`apps/product-registry-read-service`** | Service gérant la projection des données pour l'affichage (Côté Lecture). |
| **`libs/bom-platform`** | Gestion centralisée des versions des dépendances pour assurer la cohérence globale. |
| **`libs/cqrs-support`** | Fournit l'infrastructure technique (interfaces, bus) pour le pattern CQRS. |
| **`libs/sql`** | Couche d'abstraction pour les interactions avec les bases de données SQL. |

---

## Tâche 2 : Identifier les concepts principaux

### 2.1 Concepts fondamentaux (DDD & EDA)
* **Persistance et Cohérence** : Le projet utilise JPA/Hibernate. L'intégrité des données est gérée au sein de chaque service, avec une stratégie de consistance éventuelle entre les modules de lecture et d'écriture.
* **Architecture pilotée par les événements (EDA)** : La communication entre services est prévue pour transiter par des événements métiers, permettant de notifier le système de tout changement d'état.
* **Gestion des erreurs** : Le Kernel permet de distinguer les erreurs techniques (infra) des violations de règles métier via des exceptions typées.

### 2.2 Implémentation et Bibliothèques
* **libs/cqrs-support** : Ce module assure le découpage entre l'intention (Command) et l'exécution (Handler), facilitant la testabilité sans dépendre du transport (REST/Message).
* **libs/bom-platform** : Elle garantit la stabilité du projet en imposant un alignement strict des versions de frameworks (Quarkus, Hibernate) sur l'ensemble des modules.

---

## Tâche 3 : Identifier les problèmes de qualité

L'analyse effectuée avec **MegaLinter** a mis en évidence plusieurs points critiques à traiter pour la suite du projet :
1.  **Dette technique initiale** : Présence de fichiers de tests mal placés (dans `src/main` au lieu de `src/test`), bloquant la séparation entre code de production et code de test.
2.  **Couplage infrastructurel** : Des dépendances directes à des hôtes externes (PostgreSQL) dans les configurations par défaut, rendant l'application difficile à démarrer en isolation.
3.  **Standardisation Java** : Nécessité d'harmoniser les imports et de nettoyer les dépendances inutilisées dans les fichiers de configuration Gradle.

---

## Tâche 4 & 5 : Mise en œuvre des tests et Analyse

### 4.1 Stratégie de test amorcée
Le socle de test a été configuré pour valider les composants critiques :
* **Tests Unitaires** : Mise en place de tests sur les entités (JPA) pour valider la structure des données métier.
* **Tests d'Intégration** : Utilisation de `@QuarkusTest` et **RestAssured** pour vérifier la disponibilité et la conformité des endpoints REST de commande.




### 4.2 Analyse de la fiabilité
La fiabilité du système repose sur deux piliers :
1.  **Le Kernel** : En centralisant les Value Objects, il empêche l'injection de données invalides dans le domaine métier.
2.  **Le CQRS** : En isolant les modifications d'état, il permet de protéger les données critiques contre les accès concurrents non maîtrisés.

### 4.3 Conclusion sur la couverture de code
L'objectif n'est pas d'atteindre 100% de couverture de manière arbitraire, mais de se concentrer sur les zones à forte valeur ajoutée : le **Cœur Métier** et les **Contrats d'API**. Tester les éléments d'infrastructure génériques est jugé moins prioritaire que la validation des règles de gestion du catalogue.