package gnk

import grails.plugins.springsecurity.SpringSecurityService
import org.gnk.admin.right
import org.gnk.cookie.CookieService
import org.gnk.rights.RightsService
import org.gnk.user.User

import javax.servlet.http.Cookie

class SecurityFilters {

    RightsService rightsService;
    CookieService cookieService;
    SpringSecurityService springSecurityService

    def filters = {
        consolSQLfilter(controller: 'consolSql', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right1 = rightsService.hasRight(currentuser.gright, right.RIGHTSHOW.value())
                def right2 = rightsService.hasRight(currentuser.gright, right.REFOPEN.value())
                if (!currentuser || !right1 || !right2) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        tagfilter(controller: 'tag', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.REFOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
        tagRelationfilter(controller: 'tagRelation', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.REFOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        userfilter(controller: 'user', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.PROFILOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        adminUserfilter(controller: 'adminUser', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.USEROPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        roletopersofilter(controller: 'roleToPerso', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        selectIntriguefilter(controller: 'selectIntrigue', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        redactIntriguefilter(controller: 'redactIntrigue', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MINTRIGUEOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        rolefilter(controller: 'role', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        dbCoherencefilter(controller: 'dbCoherence', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        dtdImportfilter(controller: 'dtdImport', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        genericPlacefilter(controller: 'genericPlace', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.REFOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        genericResourcefilter(controller: 'genericResource', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        pastScenefilter(controller: 'pastScene', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        plotfilter(controller: 'plot', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        publicationfilter(controller: 'publication', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        placefilter(controller: 'place', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        relationfilter(controller: 'relation', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        substitutionfilter(controller: 'substitution', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        resourcefilter(controller: 'resource', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        placefilter(controller: 'place', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        xmlDtdfilter(controller: 'xmlDtd', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        consolfilter(controller: 'console', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.REFOPEN.value())
                def right1 = rightsService.hasRight(currentuser.gright, right.RIGHTMODIF.value())
                if (!currentuser || !right || !right1) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
        buildInfofilter(controller: 'buildInfo', action: '*') {
            before = {
                User user = (User) session.getAttribute("user")
                Cookie cookie
                if (user == null) {
                    Cookie[] cookies = request.getCookies()
                    if (cookies) {
                        cookie = cookies.find { it.name == "gnk_cookie" }
                        if (cookie) {
                            Cookie cookie1 = cookies.find { it.name == "prcgn" }
                            String password = cookieService.cookiepassword(cookie1.getValue())
                            String login = cookieService.cookieusern(cookie1.getValue())
                            User testuser = User.findByUsername(login);
                            if (testuser && cookieService.isAuth(password, testuser.password)) {
                                session.setAttribute("user", testuser)
                            } else {
                                redirect(controller: "logout", action: "index")
                                return false
                            }
                        }
                    }
                }
                user = (User) session.getAttribute("user")
                if (!user) {
                    user = (User) springSecurityService.getCurrentUser()
                    session.setAttribute("user", user)
                }
                User currentuser = User.findById(user.id)
                def right = rightsService.hasRight(currentuser.gright, right.MGNOPEN.value())
                if (!currentuser || !right) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
            }
        }
    }
}
