package org.gnk.adminUser

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.gnk.cookie.CookieService
import org.gnk.selectintrigue.Plot
import org.gnk.user.User
import org.gnk.user.UserService
import groovy.sql.Sql
import sun.security.jca.GetInstance

import javax.servlet.http.Cookie

class AdminUserController {
    UserService userService
    CookieService cookieService

    def checkcookie(){

        Cookie[] cookies = request.getCookies()
        if (cookies){
            Cookie cookie =  cookies.find {it.name == "prcgn"}
            if (cookie){
                String username = cookieService.cookieusern(cookie.value)
                User currentuser = User.findByUsername(username)
                if (cookieService.isAuth(currentuser.password, cookieService.cookiepassword(cookie.value)))
                    session.setAttribute("user", currentuser)
                else
                    redirect(controller: "login", action: "denied")
            } else {
                redirect(controller: "login", action: "denied")
            }
        } else {
            redirect(controller: "login", action: "denied")
        }
        String actionredir = params.get("actionredirect")
        String controllerredir = params.get("controllerredirect")
        redirect(controller: controllerredir, action: actionredir)
    }
    def list(){
        User user = session.getAttribute("user")

        if (!user){
            params.setProperty("redirectaction", "profil")
            params.setProperty("redirectcontroller", "user")
            redirect(controller: "adminUser", action: "checkcookie", params: [actionredirect : "list", controllerredirect : "adminUser"])
            return false
        } else {
        GrailsParameterMap param = params
        def name = param.usersearch
        if ("".equals(name) || null == name ){
            List<User> users = User.list()
            [users : users]
        }
        else {
            def c = User.createCriteria()
            List<User> users = c.list { ilike("username", "%"+name+"%")}
            [users : users]
        }
        }
    }

    def edit (long id){
        User user
        if (id != null && id != 0){
            user = User.findById(id)
            session.setAttribute("id_user_check", id)
        } else {
                if (!session.getAttribute("id_user_check")){
                    redirect(action: "list")
                    return false
                }else{
                    int iduser = session.getAttribute("id_user_check")
                    user = User.findById(iduser)
            }
        }
        int rightuser = user.gright
        String newpassword = params.passwordChanged
        if (newpassword && newpassword.size() > 5)
            user.password = newpassword
        List<Boolean> lb = userService.instperm(rightuser)
        [user:user, lb : lb]
    }

    def changeperm(long id){

        List<Boolean> lb = []
        User user = User.findById(id)

        for (int i = 0; i < 24; i++)
        {
            StringBuilder s = new StringBuilder()
            s.append("checkbox")
            s.append(i)
            String checkb = s.toString()
            GrailsParameterMap param = getParams()
            if (param.get(checkb) != null)
            {
                lb.add(true)
            } else {
                lb.add(false)
            }
        }
        user.gright = userService.changeright(lb)
        user.save(failOnError: true)
        redirect(controller: "adminUser", action: "edit")
    }
    def lock(long id){
        User user = User.findById(id)
        if (user.accountLocked == true){
            user.accountLocked = false
        } else {
            user.accountLocked = true
        }

        redirect controller: "adminUser", action: "list"
    }
    def statistic(long id){
        User user = User.findById(id)
        if (!id){
            redirect (action: "list")
            return false
        }
        List<Plot> plots = Plot.list()
        int countPublicPlot = 0;
        int countPrivatePlot = 0;
        int countDraftPlot = 0;
        for (int i = 0; i < plots.size(); i++){
            if (plots[i].user.id == user.id){
                if (plots[i].isPublic){
                    countPublicPlot++
                } else {
                    if (plots[i].isDraft){
                        countDraftPlot++
                    } else {
                    countPrivatePlot++
                    }
                }
            }
        }
        [user:user, countPublicPlot:countPublicPlot, countPrivatePlot:countPrivatePlot, countDraftPlot:countDraftPlot]
    }
    def modifyUser(long id){
        User user = User.findById(id)
        String newpassword = params.passwordChanged
        String confirmpassword = params.passwordChangedConfirm
        print params
        if (newpassword && newpassword.size() > 3 &&  confirmpassword && confirmpassword.equals(newpassword))
            user.password = newpassword
        if (params.firstnamemodif && params.firstnamemodif != user.firstname){
            user.firstname = params.firstnamemodif
        }
        if (params.lastnamemodif && params.lastnamemodif != user.lastname){
            user.lastname = params.lastnamemodif

        }
        if (params.usernamemodif && params.usernamemodif != user.username){
            if (User.findByUsername((String)params.usernamemodif) == null){
                user.username = params.usernamemodif;
            }
        }
        user.save()
        redirect( action: "edit", params: "user : ${user.id}")
    }
}
