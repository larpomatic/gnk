package org.gnk.gn

import org.gnk.naming.Convention

class GnHasConvention {

    Integer id
    Integer version

    static belongsTo = [ gn: Gn, convention: Convention ]

    static mapping = {
        id type:'integer'
        version type: 'integer'
    }
}
