package org.gnk.publication

import org.docx4j.dml.wordprocessingDrawing.Inline
import org.docx4j.jaxb.Context
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage
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
    public def mainPart



    public WordWriter(String templateWordSelected, String publicationFolder) {
        this.factory = Context.getWmlObjectFactory()
        try{
            this.wordMLPackage = WordprocessingMLPackage.load(new File(publicationFolder+templateWordSelected+".docx"))
        } catch (Exception e){
            this.wordMLPackage = WordprocessingMLPackage.load(new File(publicationFolder+"DEFAULT.docx"))
            this.wordMLPackage = WordprocessingMLPackage.createPackage()
            alterStyleSheet()
        }
        this.wordMLPackage = WordprocessingMLPackage.load(new File(publicationFolder+"DEFAULT.docx"))
        mainPart = wordMLPackage.getMainDocumentPart()
    }

    public void addRelationGraph (ArrayList<String> jsoncharlist, String fileName, String targetedGraph){
        int i = 0;
        for (String character : jsoncharlist){
            if (character.equals(targetedGraph)){
                this.addImage(fileName + "-" + i.toString() + ".png")
                return;
            }
            else
                i++
        }
        throw new Exception("WordWritter.addRelationGraph : \"" + targetedGraph.toString() + "\" not found")
    }

    public void addImage(String imgFile) throws Exception {
        File file = new File(imgFile)
        // Our utility method wants that as a byte array
        java.io.InputStream is = new java.io.FileInputStream(file );
        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }
        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "+file.getName());
        }
        is.close();

        String filenameHint = null;
        String altText = null;
        int id1 = 0;
        int id2 = 1;

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(this.wordMLPackage, bytes);

        Inline inline = imagePart.createImageInline( filenameHint, altText,
                id1, id2, false);

        // Now add the inline in w:p/w:r/w:drawing
        org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.P  p = factory.createP();
        org.docx4j.wml.R  run = factory.createR();
        p.getContent().add(run);
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);

        this.wordMLPackage.getMainDocumentPart().addObject(p);
    }


    def void addParagraphOfText(String text)
    {
        if (text != null)
        {
            String[] lines = text.split("\n")
            for (String line : lines)
                this.mainPart.addParagraphOfText(line)
        }
    }

    def void addObject(Object o)
    {
        mainPart.addObject(o)
    }

    def void addStyledParagraphOfText(String style, String text)
    {
        String[] lines = text.split("\n")
        for (String line : lines)
            this.mainPart.addStyledParagraphOfText(style, line)
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

    /**
     * Ajoute une ligne au tableau en style normal ce qui peut être trop gros dans des tableaux avec beaucoup d'informations...
     *
     * @deprecated utiliser addTableStyledCell(String Style, Tr tableRow, String Content) à la place
     */
    @Deprecated
    def addTableCell(Tr tableRow, String content) {
        Tc tableCell = factory.createTc();
        String[] lines = content.split("\n")
        for (String line : lines)
            tableCell.getContent().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(line));
        tableRow.getContent().add(tableCell);
    }

    def addTableStyledCell(String Style, Tr tableRow, String content) {
        Tc tableCell = factory.createTc();
        String[] lines = content.split("\n")
        for (String line : lines)
            tableCell.getContent().add(wordMLPackage.getMainDocumentPart().createStyledParagraphOfText(Style, line));
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
