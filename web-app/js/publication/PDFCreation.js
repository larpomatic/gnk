/**
 * Created by Raphaël on 13/07/2015.
 */

$(function(){

    $('form[id="exportPDFButton"] button[id="PDFButtonPublication"]').click(function(){
        $('form[id="exportPDFButton"] input[id="templatePDFSelect"]').val($('form[id="exportWordButton"] input[id="templateWordSelect"]').val());
        $('form[id="exportPDFButton"] input[id="IncludeInterpretationAdvice_pdf"]').val($('form[id="exportWordButton"] input[id="IncludeInterpretationAdvice"]').val());
        $('form[id="exportPDFButton"] input[id="jsoncharlistpdf"]').val($('form[id="exportWordButton"] input[id="jsoncharlist"]').val());
        $('form[id="exportPDFButton"] input[id="imgsrcpdf"]').val($('form[id="exportWordButton"] input[id="imgsrc"]').val());
    });
});