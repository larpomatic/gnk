package gnk

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
