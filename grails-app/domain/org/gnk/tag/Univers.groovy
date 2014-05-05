package org.gnk.tag

import org.gnk.resplacetime.PlaceHasUnivers
import org.gnk.resplacetime.ResourceHasUnivers
import org.gnk.selectintrigue.PlotHasUnivers

class Univers {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String name

	static hasMany = [ placeHasUniverses: PlaceHasUnivers,
	                   plotHasUniverses: PlotHasUnivers,
	                   resourceHasUniverses: ResourceHasUnivers ]

	static constraints = {
		name maxSize: 45
	}

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
