package org.gnk.tag

import org.gnk.resplacetime.PlaceHasTag
import org.gnk.resplacetime.ResourceHasTag
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.PlotHasTag

class Tag {

    Integer id
    Integer version

	Date lastUpdated
	Date dateCreated
	String name

	static belongsTo = [ tagFamily: TagFamily ]
	
	static hasMany = [ extPlaceTags: PlaceHasTag,
                       extResourceTags: ResourceHasTag,
                       extPlotTags: PlotHasTag,
                       extRoleTags: RoleHasTag ]

	static constraints = {
		name maxSize: 45
	}
	
	@Override
	public String toString() {
		return name;
	}

    static mapping = {
        id type: 'integer'
        version type: 'integer'
    }

    String getterName() {
        return name;
    }
}
