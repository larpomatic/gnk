package org.gnk.resplacetime

import org.gnk.gn.Gn
import org.gnk.ressplacetime.EventTime
import org.gnk.ressplacetime.PastsceneTime

import java.security.InvalidParameterException
import java.text.SimpleDateFormat

class TimeService {

    /* Exposed Methods */
    // Si withCheckAbs est FALSE, alors on mettra toujours à jour les valeurs absolue de date
	def PastsceneTime pastsceneRealDate (PastsceneTime pastscene, Date gnBeginDate, Boolean withCheckAbs) {
        // If there is a relative date
        if (pastscene.relativeDateValue && pastscene.relativeDateUnit) {
            // Instantiates the calendar.
            Calendar calendar = Calendar.getInstance()

            // Computes the date of the event beginning

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
            calendar.add(Calendar.MINUTE, 5 - calendar.get(Calendar.MINUTE) % 5)
            Date relativeDate = calendar.getTime()

            String relativeDateString = format.format(relativeDate)
            // Sets Pastscene absolute date in Pastscene object
            if (!withCheckAbs) {
                pastscene.absoluteYear = calendar.getAt(Calendar.YEAR)//relativeDateString.substring(0, 4).toInteger()
                pastscene.absoluteMonth = relativeDateString.substring(5, 7).toInteger()
                pastscene.absoluteDay = relativeDateString.substring(8, 10).toInteger()
                pastscene.absoluteHour = relativeDateString.substring(11, 13).toInteger()
                pastscene.absoluteMinute = relativeDateString.substring(14, 16).toInteger()
            } else {
                if (!pastscene.absoluteYear)
                    pastscene.absoluteYear = calendar.getAt(Calendar.YEAR)//relativeDateString.substring(0, 4).toInteger()
                if (!pastscene.absoluteMonth)
                    pastscene.absoluteMonth = relativeDateString.substring(5, 7).toInteger()
                if (!pastscene.absoluteDay)
                    pastscene.absoluteDay = relativeDateString.substring(8, 10).toInteger()
                if (!pastscene.absoluteHour)
                    pastscene.absoluteHour = relativeDateString.substring(11, 13).toInteger()
                if (!pastscene.absoluteMinute)
                    pastscene.absoluteMinute = relativeDateString.substring(14, 16).toInteger()
            }
        }

		pastscene
	}

    // Va mettre à jour "relative time" de fonction à ce que le l'unit soit la plus pertinente par rapport à la date du GN
    def void updateRelativeTimeFromAbsolute(Pastscene ps, Date beginGnRealTime) {
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(beginGnRealTime);
        System.out.println("yearGN : " + beginGnRealTime);
        System.out.println("abs year : " + ps.absoluteYear);
    }

    def	EventTime eventRealDate (EventTime event, Date gnBeginDate, Integer gnDuration, Integer gnId) {
        if(event == null) {
            throw new InvalidParameterException("Parameter Event must not be null")
        }

        // Instantiates the calendar.
        Calendar cal = Calendar.getInstance()
        cal.setTime(gnBeginDate)

        //gnDuration is in hours and event.timing is the % of its apparition in the GN
        float minutesBeforeEvent = ((float)gnDuration * 60) * ((float)event.timing / 100)
        //TODO : A round would be better than truncate here
        //we truncate the minutes for 5 by 5 increment (we suppose that the gnBeginDate.getMinutes() == 0)
        minutesBeforeEvent -= (minutesBeforeEvent % 5)
        //We add the event position in the gn duration to the timestamp of the beginning of the Gn
        cal.add(Calendar.MINUTE, (int)minutesBeforeEvent)

        //It is abnormal to need to use the Event Class when the function takes an EventTime as a parameter..
        //But it is currently easier to get the attribute duration from the database than the Json..
        if (event.getDuration() == null) {
            //Get the event if it is persisted
            Event e = Event.findById(event.getId())
            event.setDuration(e.getDuration())
        }
        Date eventBeginning = cal.getTime()
        cal.add(Calendar.MINUTE, event.getDuration())
        Date eventEnd = cal.getTime()

        Gn currentGn = Gn.findById(gnId)

        //logic about blocking period impacts
        def periods = Period.findAll(sort:"beginning", order:"asc") { gn == currentGn }
        periods.each { Period period ->
            if(period.getIsBlocking() && period.isDuring(eventBeginning, eventEnd)) {
                cal.setTime(period.getEnd())
                eventBeginning = cal.getTime()
                cal.add(Calendar.MINUTE, event.getDuration())
                eventEnd = cal.getTime()
            }
            //Cas d'arrêt : début de la période après la fin de l'évènement
            period.getBeginning().before(eventEnd)
        }

        // Sets Event absolute date in Event object

        cal.setTime(eventBeginning)
        event.absoluteYear = cal.get(Calendar.YEAR)
        event.absoluteMonth = cal.get(Calendar.MONTH)
        event.absoluteDay = cal.get(Calendar.DAY_OF_MONTH)
        event.absoluteHour = cal.get(Calendar.HOUR_OF_DAY)
        event.absoluteMinute = cal.get(Calendar.MINUTE)

		return event
	}
    /* !Exposed Methods */
}
