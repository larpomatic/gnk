package org.gnk.tag

class TagFamily {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String value
	boolean relevantPlot
	boolean relevantRole
    boolean relevantPlace
    boolean relevantResource

    static hasMany = [ tags: Tag ]



	static constraints = {
		value maxSize: 45
	}

    static mapping = {
        id type: 'integer'
        version type: 'integer'
    }
}
