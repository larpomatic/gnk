import grails.plugins.springsecurity.SpringSecurityService
import org.gnk.tag.TagFamily
import org.gnk.user.SecRole
import org.gnk.user.User
import org.gnk.user.UserSecRole

class BootStrap {
	
	SpringSecurityService springSecurityService

    def init = { servletContext ->

        // TAG FAMILY
        if (!TagFamily.findByValue("Age")) {
            TagFamily tagFamily = new TagFamily();
            tagFamily.value = "Age"
            tagFamily.save(failOnError: true)
        }

        // TAG
//        ResourceTag resourceTag
//        if (!Tag.findByName("Jeune")) {
//            Tag tag = new Tag();
//            tag.name = "Jeune"
//            tag.tagFamily = TagFamily.findByValue("Age")
//            tag.save(failOnError: true)
//            resourceTag = new ResourceTag()
//            resourceTag.tag = tag
//            resourceTag.save()
//        }

        // RESOURCE
//        if (!Resource.findByName("Statuette")) {
//            Resource resource = new Resource();
//            resource.name = "Statuette"
//            resource.description = "Statuette d'un enfant"
//            resource.gender = "F"
//
//            resource.save(failOnError: true)
//        }

        // ROLE
        SecRole adminRole = SecRole.findByAuthority("ROLE_ADMIN") ?: new SecRole(authority: "ROLE_ADMIN").save(failOnError: true)
        SecRole userRole = SecRole.findByAuthority("ROLE_USER") ?: new SecRole(authority: "ROLE_USER").save(failOnError: true)

        // USER
        User admin = User.findByUsername("admin@gnk.com");
        User user  = User.findByUsername("user@gnk.com");
        User user1 = User.findByUsername("nicolas.sotty@gmail.com")
        User user2 = User.findByUsername("said.essaki@gmail.com")
        User user3 = User.findByUsername("v@v.fr")
        User user4 = User.findByUsername("jacnic@epita.fr")
        User user5 = User.findByUsername("pico.2607@gmail.com")
        User user6 = User.findByUsername("alex-belingard@live.fr")
        User user7 = User.findByUsername("mehdibest91@gmail.com")

        if (!user7){
            user7 = new  User()
            user7.username = "mehdibest91@gmail.com"
            user7.firstname = "Mehdi"
            user7.lastname = "Baless"
            user7.password = "mehdi"
            user7.enabled = true
            user7.gright = 2097151
            user7.countConnexion = 0
            user7.lastConnexion = new Date()
            user7.save(failOnError: true)
        }
        if (!user6){
            user6 = new  User()
            user6.username = "alex-belingard@live.fr"
            user6.firstname = "Alexandre"
            user6.lastname = "Belingard"
            user6.password = "test"
            user6.enabled = true
            user6.gright = 2097151
            user6.countConnexion = 0
            user6.lastConnexion = new Date()
            user6.save(failOnError: true)
        }
        if (!user5){
            user5 = new  User()
            user5.username = "pico.2607@gmail.com"
            user5.firstname = "Pierre"
            user5.lastname = "Coppee"
            user5.password = "gnk"
            user5.enabled = true
            user5.gright = 2097151
            user5.countConnexion = 0
            user5.lastConnexion = new Date()
            user5.save(failOnError: true)
        }

        if (!user4){
            user4 = new  User()
            user4.username = "jacnic@epita.fr"
            user4.firstname = "Nicolas"
            user4.lastname = "Jaccoud"
            user4.password = "jacnic"
            user4.enabled = true
            user4.gright = 2097151
            user4.countConnexion = 0
            user4.lastConnexion = new Date()
            user4.save(failOnError: true)
        }
        if (!user3){
            user3 = new  User()
            user3.username = "v@v.fr"
            user3.firstname = "Vincent"
            user3.lastname = "Larroque"
            user3.password = "lol"
            user3.enabled = true
            user3.gright = 2097151
            user3.countConnexion = 0
            user3.lastConnexion = new Date()
            user3.save(failOnError: true)
        }
        if (!user2){
            user2 = new  User()
            user2.username = "said.essaki@gmail.com"
            user2.firstname = "Said"
            user2.lastname = "Essaki"
            user2.password = "root"
            user2.enabled = true
            user2.gright = 2097151
            user2.countConnexion = 0
            user2.lastConnexion = new Date()
            user2.save(failOnError: true)
        }

        if (!user) {
            user = new User()
            user.username = "user@gnk.com"
            user.firstname = "Nico"
            user.lastname = "Sotty"
            user.password = "1"
            user.enabled = true
            user.gright = 1
            user.countConnexion = 0
            user.lastConnexion = new Date()
            user.save(failOnError: true)
        }

        if (!user1) {
            user1 = new User()
            user1.username = "nicolas.sotty@gmail.com"
            user1.firstname = "Nicolas"
            user1.lastname = "Sotty"
            user1.password = "admin"
            user1.enabled = true
            user1.gright = 131072
            user1.lastConnexion = new Date()
            user1.countConnexion = 0
            user1.save(failOnError: true)
        }

        if (!admin) {
            admin = new User();

			admin.username = "admin@gnk.com"
            admin.firstname = "John"
            admin.lastname = "Doe"
            admin.password = "admin"
            admin.enabled = true
            admin.gright = 131072
            admin.countConnexion = 0
            admin.lastConnexion = new Date()
            admin.save(failOnError: true)
		}

        if (!admin.getAuthorities().contains(adminRole)) {
            UserSecRole.create admin, adminRole
        }
        if (!user.getAuthorities().contains(userRole)) {
            UserSecRole.create(user, userRole, true)
        }
        if (!user1.getAuthorities().contains(adminRole)) {
            UserSecRole.create(user1, adminRole, true)
        }
        if (!user2.getAuthorities().contains(userRole)) {
            UserSecRole.create(user2, userRole, true)
        }
        if (!user3.getAuthorities().contains(userRole)) {
            UserSecRole.create(user3, userRole, true)
        }
        if (!user4.getAuthorities().contains(userRole)) {
            UserSecRole.create(user4, userRole, true)
        }
        if (!user5.getAuthorities().contains(userRole)) {
            UserSecRole.create(user5, userRole, true)
        }
        if (!user6.getAuthorities().contains(userRole)) {
            UserSecRole.create(user6, userRole, true)
        }
        if (!user7.getAuthorities().contains(userRole)) {
            UserSecRole.create(user7, userRole, true)
        }
    }

    def destroy = {}

}
