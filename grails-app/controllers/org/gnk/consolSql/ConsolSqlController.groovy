package org.gnk.consolSql

import groovy.sql.Sql
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.gnk.admin.RequestService
import org.gnk.admin.SqlRequest
import org.gnk.admin.SqlRequestCategory
import org.gnk.user.User
import org.hibernate.SessionFactory

class ConsolSqlController {

    SessionFactory sessionFactory
    RequestService requestService

    def terminal() {
        User user = session.getAttribute("user")

            if (!user) {
                params.setProperty("redirectaction", "terminal")
                params.setProperty("redirectcontroller", "adminUser")
                redirect(controller: "adminUser", action: "checkcookie", params: [actionredirect: "terminal", controllerredirect: "consolSQL"])
                return false
            } else {
            List<SqlRequestCategory> listCategory = SqlRequestCategory.list();
            [listCategory: listCategory]
        }
    }

    def request() {
            String catName;
            String subCatName;
            String requestName;
            String requestSql;
            GrailsParameterMap param = getParams()
            if (param.get("isSaved")){
                catName = param.get("categoryname")
                subCatName = param.get("subcategory")
                requestName = param.get("requestname")
                requestSql = param.get("sqlconsol")
                if (catName && subCatName && requestName){
                    requestService.update(requestName, catName, subCatName, requestSql)
                }
            }
            if (param.get("isDeleted")){
                catName = param.get("categorynameD")
                subCatName = param.get("subcategoryD")
                requestName = param.get("requestnameD")
                if (catName && subCatName && requestName){
                    requestService.delete(catName, subCatName, requestName)
                }
            }
                        //Correction of a bug (bad display in the view when they have,ust one element)
        redirect(action: "resultRequest", params: [sqlconsol : requestSql])
    }
    def resultRequest(String request){
        String result = "";
        request = params.sqlconsol;
        if (request) {
            def sql = new Sql(sessionFactory.currentSession.connection())
            def resultsql = sql.rows(request)
            result+="<table class=\"table\">\n <thead></thead>\n <tbody>"
            for (def it : resultsql) {
                result+=" <tr>\n"
                for (def key : it.keySet()) {
                    result+="  <td>"
                    result+=it.get(key)
                    result+="</td>\n"
                }
                result+="  </tr>\n"
            }
            result+=" </tbody>\n</table>\n"
        }
        params.remove("resultRequest")
        params.setProperty("resultRequest", "")
        [result: result]
    }

}
