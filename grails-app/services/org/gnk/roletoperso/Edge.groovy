package org.gnk.roletoperso

class Edge{
    String nodeTo
    String nodeFrom
    String lien
    Node n1
    Node n2
    boolean isHidden

    Edge(Node n1, Node n2, String lien) {
        this.nodeTo = n2.id
        this.nodeFrom = n1.id
        this.n1 = n1
        this.n2 = n2
        this.lien = lien
        this.isHidden = false
    }
    void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden
    }
}
