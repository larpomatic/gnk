package org.gnk.administration

import javax.swing.JFrame
import javax.swing.JOptionPane
import java.awt.Graphics

class ErrorHandlerController extends JFrame{

    def ParseErrorHandler()
    {
        String str;

        str += "There is a problem on your data format, please check the validity!";
        JOptionPane.showMessageDialog(this,"Voici l'erreur : "+str,"Titre : exception",JOptionPane.ERROR_MESSAGE);


    }
    def IntrigueCaracteristics()
    {
        String str;
          str += "Il se peut que le nombre de personnages soit inférieur au nombre de joueurs présents !";
        JOptionPane.showMessageDialog(this,"Voici l'erreur : "+str,"Titre : exception",JOptionPane.ERROR_MESSAGE);
    }
    def RuleraiseError()
    {
        String str;
        str += "Vous n'avez pas selectionné la bonne description !";
        JOptionPane.showMessageDialog(this,"Voici l'erreur : "+str,"Titre : exception",JOptionPane.ERROR_MESSAGE);

    }
    def filesendingerror()
    {
        String str;
        str += "Une erreure s'est produite lors de l'envoi du fichier !"
        JOptionPane.showMessageDialog(this,"Voici l'erreur : "+str,"Titre : exception",JOptionPane.ERROR_MESSAGE);

    }

}
