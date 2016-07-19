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
    Boolean isYearAbsolute
    Boolean isMonthAbsolute
    Boolean isDayAbsolute
    Boolean isHourAbsolute
    Boolean isMinuteAbsolute

    String plotId
    String plotName

    Pastscene pastscenePredecessor
    GenericPlace genericPlace

    // Id referenced into DTD
    static transients = ["DTDId", "absoluteYear", "absoluteMonth", "absoluteDay", "relativeTime",
                         "relativeTimeUnit", "absoluteHour", "absoluteMinute", "plotId", "plotName"]
    Integer DTDId

    Integer absoluteYear
    Integer absoluteMonth
    Integer absoluteDay
    Integer absoluteHour
    Integer absoluteMinute

    Integer relativeTime
    String relativeTimeUnit

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
        relativeTime (nullable: true)
        relativeTimeUnit (nullable: true)
        genericPlace (nullable: true)
        pastscenePredecessor (nullable: true)

	}

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
        roleHasPastscenes cascade: 'all-delete-orphan'

        isYearAbsolute column: 'is_absolute_year'
        isMonthAbsolute column: 'is_absolute_month'
        isDayAbsolute column: 'is_absolute_day'
        isHourAbsolute column: 'is_absolute_hour'
        isMinuteAbsolute column: 'is_absolute_minute'
        relativeTime column: 'timing_relative'
        relativeTimeUnit column: 'unit_timing_relative'
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
        //correctTimingVariables()
        Calendar calendar = new GregorianCalendar()
        calendar.setTime(t0Date)
        if (dateYear != null)
        {
            if(!isYearAbsolute)
                calendar.add(Calendar.YEAR, -dateYear)
            else
                calendar.set(Calendar.YEAR, dateYear)
        }
        if (dateMonth != null)
        {
            if(!isMonthAbsolute)
                calendar.add(Calendar.MONTH, -dateMonth)
            else
                calendar.set(Calendar.MONTH, dateMonth-1) // On fait -1 car "Janvier" est le mois numéro "0"; "février" est le "1" etc...
        }
        if (dateDay != null)
        {
            if(!isDayAbsolute)
                calendar.add(Calendar.DAY_OF_MONTH, -dateDay)
            else
                calendar.set(Calendar.DAY_OF_MONTH, dateDay)
        }
        if (dateHour != null)
        {
            if(!isHourAbsolute)
                calendar.add(Calendar.HOUR_OF_DAY, -dateHour)
            else
                calendar.set(Calendar.HOUR_OF_DAY, dateHour)
        }
        if (dateMinute != null)
        {
            if(!isMinuteAbsolute)
                calendar.add(Calendar.MINUTE, -dateMinute)
            else
                calendar.set(Calendar.MINUTE, dateMinute)
        }


        Date date = calendar.getTime()
        return date
    }

    public String printDate(Date t0Date)
    {
        return "Le "
        Date d = getAbsoluteDate(t0Date);
        SimpleDateFormat formater = null;
        if (dateHour > 0 || dateMinute > 0 || isHourAbsolute || isMinuteAbsolute)
            formater = new SimpleDateFormat("'Le' dd MMMM yyyy 'à' HH'h'mm");
        else
            formater = new SimpleDateFormat("'Le' dd MMMM yyyy");
        return formater.format(d);
    }
}
