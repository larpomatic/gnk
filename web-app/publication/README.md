Pour faire votre template word de façon efficace :
    Copier/Coller le fichier "DEFAULT.docx"
    Puis modifier cette copie en modifiant les styles déjà présents ("T", "ST", "T1" etc... cf plus bas pour explications), en y ajoutant des images, des footers & headers, des pages de gardes etc...

Voici les règles à suivre pour que le GNK puisse lire vos template :
- Ne pas utiliser les headers templatés de microsoft word. En effet la lib docx4j, qui s'occupe de l'ouverture du fichier word, n'arrive pas à lire la structure du fichier word pour ces headers => Solution : Faire son header soit même via un tableau à plusieurs colonne
- Afin de palier au problème de Dox4J sur son défault d'écraser nos styles par défault ("Title", "Heading1" etc...). Veuillez créer vos templates via ces styles personnalisé avec ces noms :
    "T" pour un grand Titre
    "ST" pour un Sous-Titre
    "T1" pour un Titre 1
    "T2" pour un Titre 2
    "T3" pour un Titre 3
    "T4" pour un Titre 4
    "T5" pour un Titre 5