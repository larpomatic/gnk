package org.gnk.roletoperso

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn

/**
 * Created by nicolas on 06/10/2014.
 */
class RelationshipGraphService {


    String create_graph (Gn gn)
    {
        String json_relation = "";

        JSONArray json_array = new JSONArray();
        Set<Character> characters = gn.characterSet.clone();
        characters.addAll(gn.nonPlayerCharSet);

        for (Character c : characters) {
            JSONObject json_object = new JSONObject();

            JSONArray json_adjacencies = new JSONArray();

            for (Character c2 : characters) {
                if (c != c2)
                {
                    String lien1 = c.getRelatedCharactersExceptBijectivesLabel(gn).get(c2);
                    //String lien2 = c2.getRelatedCharactersExceptBijectivesLabel(gn).get(c);
                    if ((lien1 != null) && (lien1.isEmpty() == false))
                    {
                        JSONObject json_adjacencies_obj = new JSONObject();
                        if ((c.firstname != null && c.firstname != "") || (c.lastname != null && c.lastname != ""))
                            json_adjacencies_obj.put("nodeFrom", c.firstname + " " + c.lastname.toUpperCase())
                        else
                            json_adjacencies_obj.put("nodeFrom", "CHAR" + c.getDTDId());
                        if ((c2.firstname != null && c2.firstname != "") || (c2.lastname != null && c2.lastname != ""))
                            json_adjacencies_obj.put("nodeTo", c2.firstname + " " + c2.lastname.toUpperCase());
                        else
                            json_adjacencies_obj.put("nodeTo", "CHAR" + c2.getDTDId());
                        JSONObject json_data = new JSONObject();
                        json_data.put("lien", lien1);
                        //if ((lien2 != null) && (lien2.isEmpty() == false))
                        //    json_data.put("lien2", lien2);
                        json_adjacencies_obj.put("data", json_data);
                        json_adjacencies.add(json_adjacencies_obj);
                    }
                }
            }

            if (json_adjacencies.size() > 0)
            {
                json_object.put("adjacencies", json_adjacencies);

                JSONObject json_colortype = new JSONObject();
                if (c.isMen())
                    json_colortype.put("\$color", "#0040FF");
                else
                {
                    if (c.isWomen())
                        json_colortype.put("\$color", "#FE2EC8");
                    else
                        json_colortype.put("\$color", "#848484");
                }
                if (c.isPJ())
                    json_colortype.put("\$type", "circle");
                else
                {
                    if (c.isPHJ())
                        json_colortype.put("\$type", "triangle");
                    else
                        json_colortype.put("\$type", "square");
                }

                json_object.put("data", json_colortype);

                json_object.put("id", "CHAR" + c.getDTDId());
                json_object.put("name", "CHAR" + c.getDTDId());
                json_array.add(json_object);
            }
        }

        json_relation = json_array.toString();
        return json_relation;
    }
}
