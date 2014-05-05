package org.gnk.adminUser

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.gnk.selectintrigue.Plot
import org.gnk.user.User
import org.gnk.user.UserService

class AdminUserController {
    UserService userService
    def list(){
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

    def edit (long id){
        User user
        if (id != null && id != 0){
            user = User.findById(id)
            session.setAttribute("id_user_check", id)
        } else {
            int iduser = session.getAttribute("id_user_check")
            user = User.findById(iduser)
        }
        int rightuser = user.gright
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
        List<Plot> plots = Plot.list()
        int countPublicPlot = 0;
        int countPrivatePlot = 0;
        for (int i = 0; i < plots.size(); i++){
            if (plots[i].user.id == user.id){
                if (plots[i].isPublic){
                    countPublicPlot++
                } else {
                    countPrivatePlot++
                }
            }
        }
        [user:user, countPublicPlot:countPublicPlot, countPrivatePlot:countPrivatePlot]
    }
}
