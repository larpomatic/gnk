package org.gnk.selectintrigue

import org.gnk.tag.Univers

class PlotHasUnivers {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	Integer weight
	Plot plot
	Univers univers

	static belongsTo = [ Plot, Univers ]

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }

}
