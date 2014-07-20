package gnk

class TimeUnitTagLib {
    def timeUnit = { attrs, body ->
        def unit = attrs['unit']
        if(unit == "Y" || unit == "y") {
            out << g.message(code: 'redactintrigue.pastScene.pastSceneYear')
        }
        else if (unit == "M") {
            out << g.message(code: 'redactintrigue.pastScene.pastSceneMonth')
        }
        else if (unit == "d" || unit == "D") {
            out << g.message(code: 'redactintrigue.pastScene.pastSceneDay')
        }
        else if (unit == "h" || unit == "H") {
            out << g.message(code: 'redactintrigue.pastScene.pastSceneHour')
        }
        else if (unit == "m") {
            out << g.message(code: 'redactintrigue.pastScene.pastSceneMinute')
        }
        else {
            out << unit
        }
    }
}
