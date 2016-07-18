package gnk

import org.apache.commons.lang3.StringEscapeUtils
import org.gnk.resplacetime.Pastscene

import java.text.DateFormatSymbols

class TimeUnitTagLib {

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    def timeMonth = { attrs, body ->
        def monthNumber = attrs['month'];
        if (monthNumber == null) {
            out << ""
        }
        else {
            def realMonth = getMonth(monthNumber as Integer);
            out << realMonth
        }
    }

    def buildDateList(Pastscene pastscene) {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ArrayList<String> relativeList = new ArrayList<>();
        ArrayList<String> absoluteList = new ArrayList<>();
        if (pastscene.isYearAbsolute && pastscene.dateYear) {
            absoluteList.add("Year");
        }
        else if (pastscene.dateYear) {
            relativeList.add("Year");
        }
        if (pastscene.isDayAbsolute && pastscene.dateDay) {
            absoluteList.add("Day");
        }
        else if (pastscene.dateDay) {
            relativeList.add("Day");
        }
        if (pastscene.isMonthAbsolute && pastscene.dateMonth) {
            absoluteList.add("Month");
        }
        else if (pastscene.dateMonth) {
            relativeList.add("Month");
        }
        if (pastscene.isHourAbsolute && pastscene.dateHour) {
            absoluteList.add("Hour");
        }
        else if (pastscene.dateHour) {
            relativeList.add("Hour");
        }
        if (pastscene.isMinuteAbsolute && pastscene.dateMinute) {
            absoluteList.add("Minute");
        }
        else if (pastscene.dateMinute) {
            relativeList.add("Minute");
        }
        list.add(relativeList);
        list.add(absoluteList);
        return list;
    }

    def buildRelativeString(ArrayList<ArrayList<String>> list, String res, Pastscene pastscene) {
        boolean first = true;
        if (list.first().size() != 0)  {
            res += "Il y a ";
        }
        for (String s : list.first()) {
            if (!first) {
                res += ", "
            }
            first = false;
            if (s == "Year") {
                res += pastscene.dateYear + " ans"
            }
            else if (s == "Day") {
                res += pastscene.dateDay + " jours"
            }
            else if (s == "Month") {
                res += pastscene.dateMonth + " mois"
            }
            else if (s == "Hour") {
                res += pastscene.dateHour + " heures"
            }
            else if (s == "Minute") {
                res += pastscene.dateMinute + " minutes"
            }
        }
        return res;
    }

    def buildAbsoluteString(ArrayList<ArrayList<String>> list, String res, Pastscene pastscene) {
        boolean first = true;
        for (String s : list.last()) {
            if (!first && s != "Minute") {
                res += ", "
            }
            first = false;
            if (s == "Year") {
                res += "en " + pastscene.dateYear;
            }
            else if (s == "Day") {
                res += "le " + pastscene.dateDay;
            }
            else if (s == "Month") {
                res += "en " + getMonth(pastscene.dateMonth);
            }
            else if (s == "Hour") {
                res += "à " + pastscene.dateHour + "h"
            }
            else if (s == "Minute") {
                res += pastscene.dateMinute;
            }
        }
        return res;
    }

    def pastsceneTime = { attrs, body ->
        Integer pastsceneid = attrs['pastsceneId'] as Integer;
        String res = "";
        Pastscene pastscene = Pastscene.findById(pastsceneid);
        ArrayList<ArrayList<String>> list = buildDateList(pastscene);
        res = buildRelativeString(list, res, pastscene);
        if (list.first().size() != 0 && list.last().size() != 0) {
            res += ", ";
        }
        res = buildAbsoluteString(list, res, pastscene);
        res += " - " + StringEscapeUtils.escapeHtml4(pastscene.title);
        out << res;
    }

    def timeUnit = { attrs, body ->
        def unit = attrs['unit'];
        def quantity = attrs['quantity'];
        def realUnit = "";
        if(unit == "Y" || unit == "y") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneYear')
        }
        else if (unit == "M") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneMonth')
        }
        else if (unit == "d" || unit == "D") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneDay')
        }
        else if (unit == "h" || unit == "H") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneHour')
        }
        else if (unit == "m") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneMinute')
        }
        else {
            realUnit = unit
        }
        if (quantity && (quantity > 1) && (unit != "M")) {
            realUnit += "s";
        }
        out << realUnit
    }

    def timeUnitU = { attrs, body ->
        def unit = attrs['unit'];
        def realUnit = "";
        if(unit == "Y" || unit == "y") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneYear')
        }
        else if (unit == "M") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneMonth')
        }
        else if (unit == "d" || unit == "D") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneDay')
        }
        else if (unit == "h" || unit == "H") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneHour')
        }
        else if (unit == "m") {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneMinute')
        }
        else {
            realUnit = unit
        }
        if (unit == null) {
            realUnit = g.message(code: 'redactintrigue.pastScene.pastSceneYear')
        }
        out << realUnit
    }
}
