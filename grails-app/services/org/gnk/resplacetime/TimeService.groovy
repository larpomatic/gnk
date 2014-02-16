package org.gnk.resplacetime

import org.gnk.ressplacetime.EventTime
import org.gnk.ressplacetime.PastsceneTime
import java.text.SimpleDateFormat

class TimeService {

    /* Exposed Methods */
	def PastsceneTime pastsceneRealDate (PastsceneTime pastscene, Date gnBeginDate) {
        // If there is a relative date
        if (pastscene.relativeDateValue && pastscene.relativeDateUnit) {
            // Instantiates the calendar.
            Calendar calendar = Calendar.getInstance()
            Integer deltaTime = - pastscene.relativeDateValue
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm")

            // T0 = gnBeginDate
            calendar.setTime(gnBeginDate)

            // If the relative date is expressed in minutes.
            if (pastscene.relativeDateUnit.equals("m"))
                calendar.add(Calendar.MINUTE, deltaTime)

            // If the relative date is expressed in hours.
            if (pastscene.relativeDateUnit.equals("h"))
                calendar.add(Calendar.HOUR, deltaTime)

            // If the relative date is expressed in days.
            if (pastscene.relativeDateUnit.equals("D"))
                calendar.add(Calendar.DATE, deltaTime)

            // If the relative date is expressed in weeks.
            if (pastscene.relativeDateUnit.equals("W"))
                calendar.add(Calendar.WEEK_OF_MONTH, deltaTime)

            // If the relative date is expressed in months.
            if (pastscene.relativeDateUnit.equals("M"))
                calendar.add(Calendar.MONTH, deltaTime)

            // If the relative date is expressed in years.
            if (pastscene.relativeDateUnit.equals("Y"))
                calendar.add(Calendar.YEAR, deltaTime)

            // If the relative date is expressed in centuries.
            if (pastscene.relativeDateUnit.equals("C"))
                calendar.add(Calendar.YEAR, deltaTime * 100)

            // The computed date is transformed in string format
            Date relativeDate = calendar.getTime()
            String relativeDateString = format.format(relativeDate)

            // Sets Pastscene absolute date in Pastscene object
            if (!pastscene.absoluteYear)
                pastscene.absoluteYear = relativeDateString.substring(0, 4).toInteger()
            if (!pastscene.absoluteMonth)
                pastscene.absoluteMonth = relativeDateString.substring(5, 7).toInteger()
            if (!pastscene.absoluteDay)
                pastscene.absoluteDay = relativeDateString.substring(8, 10).toInteger()
            if (!pastscene.absoluteHour)
                pastscene.absoluteHour = relativeDateString.substring(11, 13).toInteger()
            if (!pastscene.absoluteMinute)
                pastscene.absoluteMinute = relativeDateString.substring(14, 16).toInteger()
        }

		pastscene
	}

    def	EventTime eventRealDate (EventTime event, Date gnBeginDate, Integer gnDuration) {

        // Instantiates the calendar.
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(gnBeginDate)

        // Computes the date of the event beginning
        Float deltaTime = (gnDuration * event.timing / 100) * 60
		calendar.add(Calendar.MINUTE, Math.round(deltaTime))
		Date beginDate = calendar.getTime()

        // The computed date is transformed in string format
        Date absoluteDate = calendar.getTime()
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm")
        String absoluteDateString = format.format(absoluteDate)

        // Sets Event absolute date in Event object
        if (!event.absoluteYear)
            event.absoluteYear = absoluteDateString.substring(0, 4).toInteger()
        if (!event.absoluteMonth)
            event.absoluteMonth = absoluteDateString.substring(5, 7).toInteger()
        if (!event.absoluteDay)
            event.absoluteDay = absoluteDateString.substring(8, 10).toInteger()
        if (!event.absoluteHour)
            event.absoluteHour = absoluteDateString.substring(11, 13).toInteger()
        if (!event.absoluteMinute)
            event.absoluteMinute = absoluteDateString.substring(14, 16).toInteger()

		event
	}
    /* !Exposed Methods */
}
