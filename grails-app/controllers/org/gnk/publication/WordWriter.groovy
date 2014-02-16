package org.gnk.publication

import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart
import org.docx4j.wml.CTBorder
import org.docx4j.wml.HpsMeasure
import org.docx4j.wml.ObjectFactory
import org.docx4j.wml.RPr
import org.docx4j.wml.STBorder
import org.docx4j.wml.Style
import org.docx4j.wml.Styles
import org.docx4j.wml.Tbl
import org.docx4j.wml.TblBorders
import org.docx4j.wml.TblPr
import org.docx4j.wml.Tc
import org.docx4j.wml.Tr
import org.docx4j.wml.U
import org.docx4j.wml.UnderlineEnumeration

/**
 * Created with IntelliJ IDEA.
 * User: aurel_000
 * Date: 13/10/13
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
class WordWriter {
    public WordprocessingMLPackage wordMLPackage
    public ObjectFactory factory

    public WordWriter(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) {
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

    def alterStyleSheet() {
        StyleDefinitionsPart styleDefinitionsPart =
            wordMLPackage.getMainDocumentPart().getStyleDefinitionsPart();
        Styles styles = styleDefinitionsPart.getJaxbElement();

        List<Style>  stylesList = styles.getStyle();
        for (Style style : stylesList) {
            if (style.getStyleId().equals("Normal")) {
                //alter normal style
            }
            if (style.getStyleId().equals("Title"))
                alterTitleStyle(style)
            if (style.getStyleId().equals("Heading2"))
                alterHeading2Style(style)
            if (style.getStyleId().equals("Heading1"))
                alterHeading1Style(style)
        }
    }

    private static void alterHeading2Style(Style style) {
        RPr rpr = style.getRPr()
        addUnderline(rpr);
        changeFontSize(rpr, 20 * 2)
    }

    private static void alterHeading1Style(Style style) {
        RPr rpr = style.getRPr()
        addUnderline(rpr);
        changeFontSize(rpr, 26 * 2)
    }

    private static void alterTitleStyle(Style style) {
        RPr rpr = style.getRPr()
        changeFontSize(rpr, 32 * 2)
    }



    private static void changeFontSize(RPr runProperties, int fontSize) {
        HpsMeasure size = new HpsMeasure();
        size.setVal(BigInteger.valueOf(fontSize));
        runProperties.setSz(size);
    }

    private static void addUnderline(RPr runProperties) {
        U underline = new U();
        underline.setVal(UnderlineEnumeration.SINGLE);
        runProperties.setU(underline );
    }
}
