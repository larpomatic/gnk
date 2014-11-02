package org.gnk.resplacetime

import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.selectintrigue.Plot

import java.text.SimpleDateFormat

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




    //*************
    // à virer ****
    //*************
    Integer timingRelative
    String unitTimingRelative
    def correctTimingVariables()
    {
        if (dateYear == isAbsoluteYear && unitTimingRelative.toLowerCase().startsWith("y"))
        {
            dateYear = timingRelative
            isAbsoluteYear = false
        }
        if (dateMonth == isAbsoluteMonth && unitTimingRelative.toLowerCase().startsWith("m"))
        {
            dateMonth = timingRelative
            isAbsoluteMonth = false
        }
        if (dateDay == isAbsoluteDay && unitTimingRelative.toLowerCase().startsWith("d"))
        {
            dateDay = timingRelative
            isAbsoluteDay = false
        }
    }
    //*************
    //*************
    //*************






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

    public Date getAbsoluteDate(Date t0Date)
    {
        correctTimingVariables()
        Calendar calendar = new GregorianCalendar()
        calendar.setTime(t0Date)
        if (dateYear != null)
        {
            if(!isAbsoluteYear)
                calendar.add(Calendar.YEAR, -dateYear)
            else
                calendar.set(Calendar.YEAR, dateYear)
        }
        if (dateMonth != null)
        {
            if(!isAbsoluteMonth)
                calendar.add(Calendar.MONTH, -dateMonth)
            else
                calendar.set(Calendar.MONTH, dateMonth-1) // On fait -1 car "Janvier" est le mois numéro "0"; "février" est le "1" etc...
        }
        if (dateDay != null)
        {
            if(!isAbsoluteDay)
                calendar.add(Calendar.DAY_OF_MONTH, -dateDay)
            else
                calendar.set(Calendar.DAY_OF_MONTH, dateDay)
        }
        if (dateHour != null)
        {
            if(!isAbsoluteHour)
                calendar.add(Calendar.HOUR_OF_DAY, -dateHour)
            else
                calendar.set(Calendar.HOUR_OF_DAY, dateHour)
        }
        if (dateMinute != null)
        {
            if(!isAbsoluteMinute)
                calendar.add(Calendar.MINUTE, -dateMinute)
            else
                calendar.set(Calendar.MINUTE, dateMinute)
        }


        Date date = calendar.getTime()
        return date
    }

    public String printDate(Date t0Date)
    {
        Date d = getAbsoluteDate(t0Date);
        SimpleDateFormat formater = null;
        if (dateHour > 0 || dateMinute > 0 || isAbsoluteHour || isAbsoluteMinute)
            formater = new SimpleDateFormat("'Le' dd MMMM yyyy 'à' HH'h'mm");
        else
            formater = new SimpleDateFormat("'Le' dd MMMM yyyy");
        return formater.format(d);
    }
}
