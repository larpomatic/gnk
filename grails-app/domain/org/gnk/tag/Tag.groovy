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
    Tag parent

    static hasMany = [
    extPlaceTags: PlaceHasTag,
    extResourceTags: ResourceHasTag,
    extPlotTags: PlotHasTag,
    extRoleTags: RoleHasTag,
    children: Tag]

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

    TagRelevant getTagRelevant(){
         return TagRelevant.findByTag(this)
    }
}
