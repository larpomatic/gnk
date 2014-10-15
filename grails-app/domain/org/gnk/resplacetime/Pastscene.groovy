package org.gnk.resplacetime

import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.selectintrigue.Plot

class Pastscene {

    Integer id
    Integer version

    Date lastUpdated
	Date dateCreated
	String title
	Boolean isPublic
	String description

    Integer dateYear
    Integer dateMonth
    Integer dateDay
    Integer dateHour
    Integer dateMinute
    Boolean isAbsoluteYear
    Boolean isAbsoluteMonth
    Boolean isAbsoluteDay
    Boolean isAbsoluteHour
    Boolean isAbsoluteMinute

    // à virer ****
    Integer timingRelative
    String unitTimingRelative
    // *******

    Pastscene pastscenePredecessor
    GenericPlace genericPlace

    // Id referenced into DTD
    static transients = ["DTDId", "absoluteYear", "absoluteMonth", "absoluteDay", "absoluteHour", "absoluteMinute"]
    Integer DTDId

    Integer absoluteYear
    Integer absoluteMonth
    Integer absoluteDay
    Integer absoluteHour
    Integer absoluteMinute


    static hasMany = [ roleHasPastscenes: RoleHasPastscene ]

	static belongsTo = [ plot: Plot ]

	static constraints = {
		title (maxSize: 256)
        isPublic ()
        description (nullable: true)
        dateYear (nullable: true)
        dateMonth (min: 0, max: 12, nullable: true)
        dateDay (min: 0, max: 31, nullable: true)
        dateHour (min: 0, max: 24, nullable: true)
        dateMinute (min: 0, max: 60, nullable: true)
        timingRelative (nullable: true)
        unitTimingRelative (nullable: true)
        genericPlace (nullable: true)
        pastscenePredecessor (nullable: true)

	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
        roleHasPastscenes cascade: 'all-delete-orphan'
    }

    public RoleHasPastscene getRoleHasPastscene(Role role) {
        for (RoleHasPastscene roleHasPastscene in roleHasPastscenes) {
            if (roleHasPastscene.role == role) {
                return roleHasPastscene;
            }
        }
        return null;
    }
}
