package org.gnk.parser;

import org.gnk.resplacetime.Event;
import org.gnk.resplacetime.Pastscene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: GN
 * Date: 29/10/13
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class EventXMLNode {
    public Event event = null;
    public List<Integer> predecessorIds = new ArrayList<Integer>();
}
