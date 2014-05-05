import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.gnk.cookie.CookieService
import org.gnk.user.User
import org.springframework.security.core.context.SecurityContextHolder

import javax.servlet.http.Cookie
import javax.servlet.http.HttpSession

class LogoutController {
	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {

		// TODO put any pre-logout code here
        Cookie[] cookies = request.getCookies()
        Cookie cookie = cookies.find { it.name == "prcgn" }

        if(cookie)
        {
            cookie.setMaxAge(0)
            cookie.setPath('/gnk/')
            response.addCookie(cookie)
        }
        redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl
         // '/j_spring_security_logout'
	}
}
