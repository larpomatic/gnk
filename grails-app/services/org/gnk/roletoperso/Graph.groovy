package org.gnk.roletoperso

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.gn.Gn

class Graph {
    private ArrayList<String> edgeList = new ArrayList<Edge>()
    private Gn gn

    public ArrayList<Node> nodeList = new ArrayList<Node>()

    Graph(Gn gn, boolean getHiddenRelation = true) {
        this.edgeList = new ArrayList<Edge>()
        this.nodeList = new ArrayList<Node>()
        this.gn = gn
        Set<Character> characters = gn.characterSet.clone();
        characters.addAll(gn.nonPlayerCharSet);
        for (Character c : characters)
            this.addNode(c)
        for (Node n1 : this.nodeList)
            this.generateEdge(n1, getHiddenRelation)
    }

    private void addNode(Character c) {
        this.nodeList.add(new Node(c))
    }

    private void addEdge(Node n1, Node n2, String lien) {
        Edge e = new Edge(n1, n2, lien)
        this.edgeList.add(e)
        n1.edges.add(e)
        Edge e2 = new Edge(n2, n1, lien)
        n2.edges.add(e2)
    }

    private void generateEdge(Node n1, boolean getHiddenRelation = true) {
        for (Node n2 : this.nodeList) {
            if (!n1.id.equals(n2.id)) {
                String lien1
                if (getHiddenRelation)
                    lien1 = n1.c.getRelatedCharactersExceptBijectivesLabel(gn).get(n2.c)
                else
                    lien1 = n1.c.getRelatedCharactersExceptBijectivesLabelAndHiddenRelation(gn).get(n2.c)

                if ((lien1 != null) && (lien1.isEmpty() == false)) {
                    lien1 = lien1.replaceAll("<", "");
                    lien1 = lien1.replaceAll("i:", "");
                    lien1 = lien1.replaceAll(">", "");
                    this.addEdge(n1, n2, lien1)
                }
            }
        }
    }

    public String buildGlobalGraphJSON() {
        JSONArray json_array = new JSONArray();
        for (Node n1 : this.nodeList) {
            if (n1.edges.isEmpty())
                continue
            JSONObject json_object = new JSONObject();

            // ajout des edges de n1
            JSONArray json_adjacencies = new JSONArray();
            for (Edge e : n1.edges) {
                if (e.isHidden)
                    continue
                JSONObject json_adjacencies_obj = new JSONObject();
                json_adjacencies_obj.put("nodeFrom", e.nodeFrom)
                json_adjacencies_obj.put("nodeTo", e.nodeTo);
                JSONObject json_data = new JSONObject();
                json_data.put("lien", e.lien);
                json_adjacencies_obj.put("data", json_data);
                json_adjacencies.add(json_adjacencies_obj);
            }
            json_object.put("adjacencies", json_adjacencies);

            // ajout du node n1
            JSONObject json_colortype = new JSONObject();
            json_colortype.put("\$color", n1.color);
            json_colortype.put("\$type", n1.type);
            json_object.put("data", json_colortype);

            json_object.put("id", n1.id);
            json_object.put("name", n1.name);
            json_array.add(json_object);
        }
        return json_array.toString()
    }

    public String buildCharGraphJSON(Node n1) {
        JSONArray json_array = new JSONArray();
        JSONObject json_object = new JSONObject();
        if (!n1.edges.isEmpty()) {
            // ajout des edges de n1
            JSONArray json_adjacencies = new JSONArray();

            for (Edge e : n1.edges) {
                if (e.isHidden)
                    continue
                JSONObject json_adjacencies_obj = new JSONObject();
                json_adjacencies_obj.put("nodeFrom", e.nodeFrom)

                json_adjacencies_obj.put("nodeTo", e.nodeTo);
                JSONObject json_data = new JSONObject();
                json_data.put("lien", e.lien);
                json_adjacencies_obj.put("data", json_data);
                json_adjacencies.add(json_adjacencies_obj);
            }
            json_object.put("adjacencies", json_adjacencies);
        }

        // ajout du node n1
        JSONObject json_colortype = new JSONObject();
        json_colortype.put("\$color", n1.color);
        json_colortype.put("\$type", n1.type);
        json_object.put("data", json_colortype);

        json_object.put("id", n1.id);
        json_object.put("name", n1.name);
        json_array.add(json_object);

        for (Edge e : n1.edges) {
            if (e.isHidden)
                continue
            JSONObject json_object2 = new JSONObject();
            JSONObject json_colortype2 = new JSONObject();
            json_colortype2.put("\$color", e.n2.color);
//            json_colortype2.put("\$type", e.n2.type); -> Les personnage ne peuvent pas savoir de quels type sont les autres joueurs
            json_colortype2.put("\$type", "circle");
            json_object2.put("data", json_colortype2);

            json_object2.put("id", e.n2.id);
            json_object2.put("name", e.n2.name);
            json_array.add(json_object2);
        }


        return json_array.toString()
    }

    public String getRelation(String name){
        Node n1 = null;
        for (Node n : this.nodeList){
            if(n.name.equals(name)){
                n1 = n
                break
            }
        }
        if (n1==null){
            return "Aucunes relations"
        }
        String res = ""
        if (!n1.edges.isEmpty()){
            for (Edge e: n1.edges){
                if (e.isHidden)
                    continue
                res += e.lien + " -> " + (e.n1.name.equals(n1.name)?e.n2.name:e.n1.name) + "\n"
            }
        }
        return (res.isEmpty()?"Aucunes relations":res)
    }

    @Override
    public String toString() {
        return this.buildGlobalGraphJSON()
    }
}