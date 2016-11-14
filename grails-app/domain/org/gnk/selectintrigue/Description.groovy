package org.gnk.selectintrigue

class Description {

    Integer id
    Integer version
    Integer idDescription

    Boolean isPnj
    Boolean isPj
    Boolean isOrga

    String type
    String pitch

    Integer plotId

    public Description(Integer id, Integer desc_idDescription, String desc_Type, String desc_Pitch, String desc_isPnj, String desc_isPj, String desc_isOrga)
    {
        plotId = id
        type = desc_Type
        pitch = desc_Pitch
        System.out.println("desc_IsPnj value : " + desc_isPnj)
        isPnj = desc_isPnj == "on" ? true : false
        isPj = desc_isPj == "on" ? true : false
        isOrga = desc_isOrga == "on" ? true : false
        idDescription = desc_idDescription
    }

    static mapping = {
        pitch type:'text'
        plotId type: 'integer'
    }

    static constraints = {
    }
}
