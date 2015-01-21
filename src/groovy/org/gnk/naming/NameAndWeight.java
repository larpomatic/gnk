package org.gnk.naming;

public class NameAndWeight implements Comparable<NameAndWeight>{
	String name;
	Integer weight;
	
	
	public NameAndWeight(String name, Integer weight) {
		this.name = name;
		this.weight = weight;
	}

    @Override
    public int compareTo(NameAndWeight o) {

        /*On défini la règle de comparaison car les primitifs n'ont pas de méthodes compare ou compareTo()

          0 lorsque les deux objets ou primitifs comparés sont identiques
          un entier négatif lorsque le premier élément est à considérer "inférieur" au deuxième élément
          un entier positif lorsque le premier élément est à considérer "supérieur" au deuxième élément
        */
        int i = getWeight() > o.getWeight() ? 1 : getWeight() < o.getWeight() ? -1 : 0;
        return i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
