package org.gnk.publication

import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.CTBorder
import org.docx4j.wml.ObjectFactory
import org.docx4j.wml.STBorder
import org.docx4j.wml.Tbl
import org.docx4j.wml.TblBorders
import org.docx4j.wml.TblPr
import org.docx4j.wml.Tc
import org.docx4j.wml.Tr

/**
 * Created with IntelliJ IDEA.
 * User: aurel_000
 * Date: 13/10/13
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
class WordReader {
    public WordprocessingMLPackage wordMLPackage
    public ObjectFactory factory

    public WordReader(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) {
        this.factory = factory
        this.wordMLPackage = wordMLPackage
    }

    def void addBorders(Tbl table) {
        table.setTblPr(new TblPr());
        CTBorder border = new CTBorder();
        border.setColor("auto");
        border.setSz(new BigInteger("4"));
        border.setSpace(new BigInteger("0"));
        border.setVal(STBorder.SINGLE);

        TblBorders borders = new TblBorders();
        borders.setBottom(border);
        borders.setLeft(border);
        borders.setRight(border);
        borders.setTop(border);
        borders.setInsideH(border);
        borders.setInsideV(border);
        table.getTblPr().setTblBorders(borders);
    }

    def addTableCell(Tr tableRow, String content) {
        Tc tableCell = factory.createTc();
        tableCell.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(content));
        tableRow.getContent().add(tableCell);
    }
}
