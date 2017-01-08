package org.gnk

public class ErrorHandlerService {

    ErrorHandlerService() {
    }

    def alert = com.eviware.soapui.support.UISupport;

    def ParseErrorHandler(String str1, String str2)
    {
     String s;

        s += "There is a problem on your data format, please check the validity!";
        alert.showInfoMessage(this,"Voici l'erreur : " + s, + str1, "ou", str2 ,"Titre : exception");


    }
    def IntrigueCaracteristics()
    {
        String str;
        str += "Il se peut que le nombre de personnages soit inférieur au nombre de joueurs présents !";
        alert.showInfoMessage(this,"Voici l'erreur : "+str + "Titre : exception");
    }
    def RuleraiseError()
    {
        String s2;
        s2 += "Vous n'avez pas selectionné la bonne description !";
        alert.showInfoMessage(this,"Voici l'erreur : "+s2,"Titre : exception");

    }
    def filesendingerror()
    {
        String str;
        str += "Une erreure s'est produite lors de l'envoi du fichier !"
        alert.showErrorMessage(this,"Voici l'erreur : "+str,"Titre : exception");

    }

    def serviceMethod() {

    }
}
