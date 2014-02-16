package org.gnk.administration

import groovy.sql.Sql

import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path

// Cette classe a pour but de vérifier la cohérence de la base, c'est à dire voir les doublons, les liens cassés, etc.
class DbCoherenceController {

    List<Query> queryList

    def index() {
        redirect(action: "list")
    }
    def dataSource

    def init()
    {
        queryList = new ArrayList<Query>()
        Path path = FileSystems.getDefault().getPath("${request.getSession().getServletContext().getRealPath("/")}", "CoherenceQueries.csv")

        try {
            BufferedReader reader = Files.newBufferedReader(path,StandardCharsets.UTF_8);

            String header = reader.readLine();
            if (header == null)
                System.out.println("Erreur : Fichier vide");

            Boolean firstligne = true;
            String line = null;
            while ( (line = reader.readLine()) != null )
            {
                String[] fields = line.split(";");
                if (fields.length != 3)
                {
                    System.out.println("Erreur : Nombre de colonnes incorrect (3 attendues)");
                    continue;
                }

                String name = fields[0]
                String description = fields[1]
                String query = fields[2] + ";"

                Query q = new Query(name, description, query)

                queryList.add(q)
            }

        } catch (NoSuchFileException e) {
            System.out.println("Fichier non trouvé : chargement de la liste par défaut des requêtes de cohérence de la base");
            defaultQueryList(queryList)
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode qui remplit la liste de requêtes de base pour vérifier la cohérence de la base (utilisez plutôt le fichier CoherenceQueries.csv dans web-app)
    // Cette méthode est appelée quand le fichier n'est pas trouvé
    private defaultQueryList(ArrayList<Query> queryList) {
        queryList.add(new Query("Tables 'Tag' et 'TAG Family'", "Dans la table 'Tag', les Tag_family.ID n’existant pas dans la table 'Tag_family'", "SELECT tag.id, tag.last_updated, tag.date_created, tag.version, tag.name, tag.tag_family_id " +
                "FROM tag LEFT JOIN tag_family ON tag.tag_family_id = tag_family.id WHERE (((tag_family.id) Is Null));"))
        queryList.add(new Query("Tables 'Tag' et 'Plot_has_Tag'", "Dans la table 'Plot_has_Tag', les Tag.ID n’existant pas dans la table 'Tag'", "SELECT plot_has_tag.id, plot_has_tag.last_updated, plot_has_tag.date_created, plot_has_tag.version, plot_has_tag.plot_id, plot_has_tag.tag_id, plot_has_tag.weight " +
                "FROM plot_has_tag LEFT JOIN tag ON plot_has_tag.tag_id = tag.id WHERE (((tag.id) Is Null));"))
        queryList.add(new Query("Tables 'Tag' et 'Role_has_Tag'", "Dans la table 'Tag', dans la table 'Role_has_Tag', les Tag.ID n’existant pas  dans la table 'Tag''", "SELECT role_has_tag.id, role_has_tag.last_updated, role_has_tag.date_created, role_has_tag.version, role_has_tag.role_id, role_has_tag.weight, role_has_tag.tag_id " +
                "FROM role_has_tag LEFT JOIN tag ON role_has_tag.tag_id = tag.id WHERE (((tag.id) Is Null));"))
        queryList.add(new Query("Tables 'Tag' et 'FirstName_has_Tag'", "", "SELECT firstname_has_tag.id, firstname_has_tag.last_updated, firstname_has_tag.date_created, firstname_has_tag.version, firstname_has_tag.firstname_id, firstname_has_tag.weight, firstname_has_tag.tag_id " +
                "FROM firstname_has_tag LEFT JOIN tag ON firstname_has_tag.tag_id = tag.id WHERE (((tag.id) Is Null));"))
        queryList.add(new Query("Tables 'Tag' et 'Name_has_Tag'", "Dans la table 'Name_has_Tag', les Tag.ID n'existant pas  dans la table ‘Tag'", "SELECT name_has_tag.id, name_has_tag.last_updated, name_has_tag.date_created, name_has_tag.version, name_has_tag.name_id, name_has_tag.weight, name_has_tag.tag_id " +
                "FROM name_has_tag LEFT JOIN tag ON name_has_tag.tag_id = tag.id WHERE (((tag.id) Is Null));"))
        queryList.add(new Query("Tables 'Tag' et 'Generic_Place_has_Tag'", "Dans la table 'Generic_Place _has_Tag', les Tag.ID n'existant pas  dans la table 'Tag'", "SELECT generic_place_has_tag.id, generic_place_has_tag.last_updated, generic_place_has_tag.date_created, generic_place_has_tag.version, generic_place_has_tag.generic_place_id, generic_place_has_tag.weight, generic_place_has_tag.tag_id " +
                "FROM generic_place_has_tag LEFT JOIN tag ON generic_place_has_tag.tag_id = tag.id WHERE (((tag.id) Is Null));"))
        queryList.add(new Query("Tables 'Tag' et 'Generic_Resource_has_Tag'", "Dans la table 'Generic_Resource_has_Tag', les Tag.ID n'existant pas  dans la table 'Tag'", "SELECT generic_resource_has_tag.id, generic_resource_has_tag.last_updated, generic_resource_has_tag.date_created, generic_resource_has_tag.version, generic_resource_has_tag.generic_resource_id, generic_resource_has_tag.weight, generic_resource_has_tag.tag_id " +
                "FROM generic_resource_has_tag LEFT JOIN tag ON generic_resource_has_tag.tag_id = tag.id WHERE (((tag.id) Is Null));"))
        queryList.add(new Query("Tables 'Name' et 'Name_has_Tag'", "'Name_has_Tag' dont le Name.id  est absent de table 'Name'", "SELECT name_has_tag.id, name_has_tag.last_updated, name_has_tag.date_created, name_has_tag.version, name_has_tag.name_id, name_has_tag.weight, name_has_tag.tag_id " +
                "FROM name_has_tag LEFT JOIN name ON name_has_tag.name_id = name.id WHERE (((name.id) Is Null));"))
        queryList.add(new Query("Tables 'FirstName” et 'FirstName_has_Tag'", "'FirstName_has_Tag' dont le FirstName.id  est absent de table ‘FirstName'", "SELECT firstname_has_tag.id, firstname_has_tag.last_updated, firstname_has_tag.date_created, firstname_has_tag.version, firstname_has_tag.firstname_id, firstname_has_tag.weight, firstname_has_tag.tag_id " +
                "FROM firstname_has_tag LEFT JOIN firstname ON firstname_has_tag.firstname_id = firstname.id WHERE (((firstname.id) Is Null));"))
        queryList.add(new Query("Test select", "Test pour vérifier les plots", "SELECT name from plot;"))
    }

    // Exécute toutes les requêtes sql de la liste et affiche le résultat
    def list()
    {
        init()
        groovy.sql.Sql sql = new groovy.sql.Sql(dataSource: dataSource)

        for (Query q : queryList)
        {
            def rows = sql.rows(q.query)
            q.result = rows.join('\n')
            if (!q.result)
                q.result = "Pas de résultat"
        }

        sql.close()
    }
}
