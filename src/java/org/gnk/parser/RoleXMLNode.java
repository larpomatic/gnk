package org.gnk.parser;

import org.gnk.roletoperso.Role;
import org.gnk.roletoperso.RoleHasRelationWithRole;
import org.gnk.roletoperso.RoleRelationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: GN
 * Date: 29/10/13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class RoleXMLNode {
    public Role role;
    public List<RoleHasRelationWithRoleXMLNode> XMLrelations = new ArrayList<RoleHasRelationWithRoleXMLNode>();

    public void addRelation(Integer roleId, RoleRelationType type, boolean isBijective, boolean isExclusive, Integer weight, String description) {
        XMLrelations.add(new RoleHasRelationWithRoleXMLNode(roleId, type, isBijective, isExclusive, weight, description));
    }

    public List<RoleHasRelationWithRoleXMLNode> getXMLrelations(){
        return XMLrelations;
    }

    private class RoleHasRelationWithRoleXMLNode {
        Integer receiverId;
        RoleRelationType type;
        boolean isBijective;
        boolean isExclusive;
        Integer weight;
        String description;

        RoleHasRelationWithRoleXMLNode (Integer roleId, RoleRelationType type, boolean isBijective, boolean isExclusive, Integer weight, String description) {
            this.receiverId = roleId;
            this.type = type;
            this.isBijective = isBijective;
            this.isExclusive = isExclusive;
            this.weight = weight;
            this.description = description;
        }
    }
}
