package org.gnk.roletoperso

class Node{
    String id
    String name
    String color
    String type
    Character c;
    ArrayList<Edge> edges;

    Node(Character c) {
        if (c.firstname != null || c.lastname != null) {
            this.id = c.firstname + " " + c.lastname.toUpperCase()
            this.name = c.firstname + " " + c.lastname.toUpperCase()
        } else {
            this.id = "CHAR-" + c.getDTDId();
            this.name = "CHAR-" + c.getDTDId();
        }
        if (c.isMen())
            this.color = "#0040FF"
        else if (c.isWomen())
            this.color = "#FE2EC8"
        else
            this.color = "#848484"
        if (c.isPJ())
            this.type = "circle"
        else if (c.isPHJ())
            this.type = "triangle"
        else
            this.type = "square"
        this.c = c
        this.edges = new ArrayList<Edge>()
    }

    Node(Character c, boolean a) {
        if (a == false) {
            if (c.firstname != null || c.lastname != null) {
                this.id = c.firstname + " " + c.lastname.toUpperCase()
                this.name = c.firstname + " " + c.lastname.toUpperCase()
            } else {
                this.id = "CHAR-" + c.getDTDId();
                this.name = "CHAR-" + c.getDTDId();
            }
            if (c.isMen())
                this.color = "#0040FF"
            else if (c.isWomen())
                this.color = "#FE2EC8"
            else
                this.color = "#848484"
            if (c.isPJ())
                this.type = "circle"
            else if (c.isPHJ())
                this.type = "circle"
            else
                this.type = "circle"
            this.c = c
            this.edges = new ArrayList<Edge>()
        }
    }


    @Override
    public boolean equals(Object v) {
        if ((v instanceof Node) &&  (v.id == this.id)){
            if (this.color.equals("#848484"))
                this.color = v.color
            return true
        }
        else
            return false
    }
}
