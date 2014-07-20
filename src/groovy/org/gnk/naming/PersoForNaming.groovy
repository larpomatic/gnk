package org.gnk.naming

import org.gnk.substitution.data.RelationCharacter

import java.util.List;

import com.gnk.substitution.Tag

class PersoForNaming {
	 String code
	 String gender
     String universe
	 List<Tag> tag
	 List<String> bannedNames /* ceux qui n'ont pas �t� s�lectionn�s par l'utilisateur */
	 List<String> selectedNames /* ceux que je renvoie si plein = s�lectionn� */
	 List<String> bannedFirstnames
	 List<String> selectedFirstnames
	 Boolean is_selectedName /* True si je dois le traiter */
	 Boolean is_selectedFirstName
	 List<String> family /* List de String de code */
     List<RelationCharacter> relationList /* Relation de Filiation*/

    public  PersoForNaming()
    {

    }
	 
	public PersoForNaming(String code, String gender, String universe, List<Tag> tag,
			List<String> bannedNames, List<String> selectedNames,
			List<String> bannedFirstnames, List<String> selectedFirstnames,
			Boolean is_selectedName, Boolean is_selectedFirstName,
			List<String> family) {
		super();
		this.code = code;
		this.gender = gender;
        this.universe = universe;
		this.tag = tag;
		this.bannedNames = bannedNames;
		this.selectedNames = selectedNames;
		this.bannedFirstnames = bannedFirstnames;
		this.selectedFirstnames = selectedFirstnames;
		this.is_selectedName = is_selectedName;
		this.is_selectedFirstName = is_selectedFirstName;
		this.family = family;
	}

    @java.lang.Override
    public String getgender() {
        return this.gender.toUpperCase();
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PersoForNaming [code= ");
		builder.append(code);
		builder.append("] , gender= ");
		builder.append(gender);
        builder.append("] , universe= ");
        builder.append(universe);
		builder.append("] , liste de tag=");
		builder.append(tag.toListString());
		builder.append(" , bannedNames=");
		builder.append(bannedNames.toListString());
		builder.append(" , selectedNames=");
		builder.append(selectedNames.toListString());
		builder.append(" , bannedFirstnames=");
		builder.append(bannedFirstnames.toListString());
		builder.append(" , selectedFirstnames=");
		builder.append(selectedFirstnames.toListString());
		builder.append(" , Bool�en pour les noms=");
		builder.append(is_selectedName.toString());
		builder.append("] , Bool�en pour les pr�noms=");
		builder.append(is_selectedFirstName.toString());
		builder.append("] , family=");
		builder.append(family.toListString());
		builder.append("");
		return builder.toString();
	}
}
