package org.gnk.consolSql

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException
import gnk.RightsUserTagLib
import groovy.sql.Sql
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.gnk.admin.RequestService
import org.gnk.admin.SqlRequest
import org.gnk.admin.SqlRequestCategory
import org.gnk.admin.right
import org.gnk.rights.RightsService
import org.gnk.user.User
import org.hibernate.SessionFactory

class ConsolSqlController {

    SessionFactory sessionFactory
    RequestService requestService
    RightsService rightsService

      def terminal() {
        User user = session.getAttribute("user")
            if (!user) {
                params.setProperty("redirectaction", "terminal")
                params.setProperty("redirectcontroller", "adminUser")
                redirect(controller: "adminUser", action: "checkcookie", params: [actionredirect: "terminal", controllerredirect: "consolSQL"])
                return false
            }
            List<SqlRequestCategory> listCategory = SqlRequestCategory.list();
            [listCategory: listCategory]
    }

    def deleteRequest (){
        GrailsParameterMap param = getParams()
        String catName = param.get("categoryname");
        String subCatName = param.get("subcategory");
        String requestName = param.get("requestname");
            if (catName && subCatName && requestName){
                requestService.delete(catName, subCatName, requestName)
            }
        redirect( action:"terminal")
    }

    def request() {
            String catName;
            String subCatName;
            String requestName;
            GrailsParameterMap param = getParams()
            String requestSql = param.get("sqlconsol")
            if (param.get("isSaved")){
                catName = param.get("categoryname")
                subCatName = param.get("subcategory")
                requestName = param.get("requestname")
                if (catName && subCatName && requestName){
                    requestService.update(requestName, catName, subCatName, requestSql)
                }
            }

        redirect(action: "resultRequest", params: [sqlconsol : requestSql])
    }
    def resultRequest(String request){
        String result = "";
        def resultsql;
        request = params.sqlconsol;
        if (request) {

            def sql = new Sql(sessionFactory.currentSession.connection())
            try{
                resultsql = sql.rows(request)
            }
            catch (Exception e){
                redirect(action: "terminal")
                return
            }
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
        params.remove("resultRequest")
        params.setProperty("resultRequest", "")
        }
        [result: result]
    }
    }
