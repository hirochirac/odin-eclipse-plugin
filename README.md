# Plugin Eclipse pour Odin

Plugin Eclipse avec support du langage Odin incluant OLS (Odin Language Server) et coloration syntaxique.

## Fonctionnalités

- Coloration syntaxique pour les fichiers .odin
- Support du serveur de langage OLS (autocomplétion, diagnostics, navigation)
- Reconnaissance automatique des fichiers .odin

## Prérequis

- Eclipse IDE 2021-03 ou supérieur
- Java 11 ou supérieur
- OLS (Odin Language Server) installé et accessible dans le PATH ou via la variable d'environnement OLS_PATH

## Build

### Avec Maven (recommandé)
```bash
build-updatesite.bat
```
Ou manuellement:
```bash
mvn clean package
```

Le site de mise à jour sera dans `site/target/repository`

## Installation

### Option 1: Depuis le site de mise à jour local
1. Dans Eclipse: Help → Install New Software → Add → Local
2. Sélectionner: `site/target/repository`
3. Cocher "Odin Language Support"
4. Suivre l'assistant d'installation

### Option 2: Distribuer le site de mise à jour
- Compresser `site/target/repository` en ZIP
- Héberger sur un serveur web ou partager le ZIP
- Les utilisateurs peuvent installer via Help → Install New Software → Add → Archive (ZIP) ou URL

## Configuration OLS

Le plugin cherche OLS dans cet ordre:
1. Variable d'environnement `OLS_PATH`
2. Commande `ols` dans le PATH

Définir OLS_PATH dans Eclipse:
- Window → Preferences → Run/Debug → String Substitution
- Ajouter: OLS_PATH = /chemin/vers/ols

## Utilisation

Ouvrir un fichier .odin dans Eclipse. L'éditeur activera automatiquement:
- La coloration syntaxique
- Le serveur de langage OLS (si disponible)

## Structure

- `plugin/` - Code source du plugin
  - `src/com/odin/` - Code Java
  - `plugin.xml` - Configuration des extensions
  - `META-INF/MANIFEST.MF` - Métadonnées OSGi
- `feature/` - Définition de la feature Eclipse
- `site/` - Site de mise à jour p2 (redistribuable)
