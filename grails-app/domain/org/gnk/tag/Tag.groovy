package org.gnk.tag

import org.gnk.resplacetime.PlaceHasTag
import org.gnk.resplacetime.ResourceHasTag
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.PlotHasTag

class Tag{

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    String name
    Tag parent

    static transients = ["value_substitution", "type_substitution", "weight_substitution", "status_substitution"]
    String value_substitution
    String type_substitution
    Integer weight_substitution
    String status_substitution

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
        parent cascade: 'all'
    }


    String getterName() {
        return name;
    }

    TagRelevant getTagRelevant(){
         return TagRelevant.findByTag(this)
    }

}
