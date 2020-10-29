/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
var newSubWindow;
function startChildWindow(reqParm)
{
newSubWindow = window.open("WebFacing?SpecialRequest="+reqParm,"sub","resizable,dependent,menubar,titlbar,scrollbars,toolbar,status,height=400,width=800");
newSubWindow.focus();
newSubWindow.document.execCommand("Refresh");
}
function setFieldValueAndSubmit(id,newValue,thisRef)
{
id2 = eval(id);
id2.value = newValue;
wfInfoDB[id2.id].mdt = true;
validateAndSubmit('ENTER') ;
}
function setOptionAndSubmit(newValue)
{
if( bufferAlreadySentToHost == false && !(javascriptLoaded === "undefined") && javascriptLoaded)
{
var buffer = "<INPUT NAME=MNUDDS_OPTION VALUE='" +newValue+ "'> ";
buildOutputBuffer("ENTER", "AID", buffer);
}
}
function setFocusAndValue(id,newValue)
{
id2 = eval(id);
if (typeof(id2) != "undefined")
{
id2.value = newValue;
setFocusForTagID(id2.id,0);
wfInfoDB[id2.id].mdt = true;
} else {
id = id.replace(/^document.SCREEN./,'');
setFocusForTagID(id,0);
}
}
function setFocusAndSubmitKey(id,newValue,key,thisRef)
{
setFocusAndValue(id,newValue);
validateAndSubmit(key) ;
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function blankOK( e )
{
if( isBlank( e.value ) && hasCheck(e, "AB") )
return true;
else
return false;
}
function checkme( e ) {
if(wfInfoDB[e.id].mdt ==  false)
return 2;
else
return 0;
}
function checkmf( e ) {
if( e.value.length != e.maxLength )
return 3;
else
return 0;
}
function checkvn( e )
{  var wftmp = wfInfoDB[e.id] ;
var val = e.value + '';
val = trimTrailingChars(val,' ');
var re = /^[\$#@a-zA-Z][\$#@a-zA-Z0-9_]*$/ ;
if( re.test(val) )
{  return 0 ;
}
else
{  if( wftmp.chkmsg == "" ) return 6;
else return -1;
}
}
function checkvne(e)
{  var wftmp = wfInfoDB[e.id] ;
var val = e.value + '';
val = trimTrailingChars(val, ' ') ;
var found = false;
if(val != "")
{  var re ;
if(val.charAt(0) == '"')
{  re = /^"[^"'\*\? ]*"$/ ;
}
else
{  re = /^[\$#@A-Za-z][\$#@A-Za-z0-9_\.]*$/ ;
}
found = re.test(val) ;
}
if(found == false)
{  if(wftmp.chkmsg == "") return 7;
else return -1;
}
return 0;
}
function checkrz( e ) {
var v = e.value;
v = trimTrailingSpaces( v );
v = trimLeadingSpaces( v );
var wftmp = wfInfoDB[e.id] ;
if (v.charAt(0) == '-')
{
v = v.substr(1);
v = rightJustify(v, wftmp.datalength, "0");
v = "-" + v;
}
else
{
v = rightJustify(v, wftmp.datalength, "0");
if (e.maxLength > wftmp.datalength)
{
v = " " + v;
}
}
e.value = v;
return 0;
}
function checkrb( e ) {
var v = e.value;
v = trimTrailingSpaces( v );
var wftmp = wfInfoDB[e.id];
if (wftmp.ptype != "FT_NUMERIC")
{
v = rightJustify(v, wftmp.datalength, " ");
}
e.value = v;
return 0;
}
function handleCHECK_ER() {
var e ;
if(arguments.length == 0) { e = window.event.srcElement ; }
else { e = arguments[0] ; }
var pos = CaretPositionFinder.getCaretIndex(e);
if (e.maxLength == pos && e.previousValue != e.value)
{
validateAndSubmit("ENTER");
}
}
function handleCHECK_ERForEDTMSK() {
var e ;
if(arguments.length == 0) { e = window.event.srcElement ; }
else { e = arguments[0] ; }
e.testIfChanged();
handleCHECK_ER(e);
}
/*
*(c) Copyright International Business Machines Corporation 2003. All rights reserved
*/
function bo_ibm(myObj)
{
var rv = false;
try
{
rv = bo_usr(myObj);
}
catch(any_exp){ rv = false;}
if (false == rv) {myObj.className='buttonover';}
}
function bu_ibm(myObj )
{
var rv = false;
try
{
rv =bu_usr(myObj);
}
catch(any_exp){ rv = false;}
if (false == rv) {myObj.className='buttonup';}
}
function bd_ibm(myObj)
{
var rv = false;
try
{
rv = bd_usr(myObj);
}
catch(any_exp){ rv = false;}
if (false == rv) {myObj.className='buttondown';}
}
function initAllCmdKey()
{  try
{  var i, k, t, aCmdObj, val, aCmdObjCnt = 0 ;
for(k in wfCmdKeyDB)
{  aCmdObj = wfCmdKeyDB[k] ;
for(i = 0 ; i < aCmdObj.listOfId.length ; i++)
{  t = document.getElementById(aCmdObj.listOfId[i]);
try {  bu_ibm(t); } catch(any_exp0){}
if(aCmdObj.listOfElem==null){aCmdObj.listOfElem=[t];}
else { aCmdObj.listOfElem[i] = t ; }
}
aCmdObjCnt++ ;
}
if(aCmdObjCnt == 0) { window.status = wfCmdKeyObj.validCmdKey ; }
} catch(any_exp){}
}
function removeHighlightOnCmdKey()
{  try
{  if(isDefined(wfCmdKeyObj.lastActiveKeys))
{  var arr = wfCmdKeyObj.lastActiveKeys ;
for(i = 0 ; i < arr.length ; i++)
{  try { bu_ibm(arr[i]) ; } catch(any_exp0){}
}
}
} catch(any_exp){}
}
function getAIDKey(fnkeyName)
{  try
{        var key = null ;
var arr = wfCmdKeyObj.validCmdKey.split('|');
for(i = 0 ; i < arr.length ; i++)
{  if(calcIndex(arr[i]) == fnkeyName)
{  key = arr[i] ; break ;
}
}
return key ;
} catch(any_exp){}
}
var firstPageUpEnabledRec = "";
var firstPageDnEnabledRec = "";
function handlePageUpDnKeys(fnkeyName){
try{
if(sflPgUpDnObj.run(fnkeyName, focusedRecord) == true)
{
wfCancelEvent();
return;
}
if( trySubmittingPageUPDOWN(fnkeyName, focusedRecord) == true)
{
wfCancelEvent();
return;
}
var firstRollEnabledRec;
if( fnkeyName == "PAGEUP")
firstRollEnabledRec = firstPageUpEnabledRec;
else
firstRollEnabledRec = firstPageDnEnabledRec;
if( firstRollEnabledRec != "")
{
if(sflPgUpDnObj.run(fnkeyName, firstRollEnabledRec ) == true)
{
wfCancelEvent();
return;
}
if( trySubmittingPageUPDOWN(fnkeyName, firstRollEnabledRec ) == true)
{
wfCancelEvent();
return;
}
}
if( getAIDKey(fnkeyName) != null )
{
validateAndBuildBuffer(fnkeyName,"AID");
wfCancelEvent();
}
return;
}catch(exp){}
}
function trySubmittingPageUPDOWN(fnkeyName,record)
{
try{
if( fnkeyName == 'PAGEDOWN' && isDefined(wfCmdKeyObj.recWtPGDN))
{
for(var i=0; i < wfCmdKeyObj.recWtPGDN.length; i++)
{
var rollEnabledRec = wfCmdKeyObj.recWtPGDN[i];
if ( record.indexOf(rollEnabledRec) > 0)
{
validateAndBuildBuffer(fnkeyName,"AID");
wfCancelEvent();
return true;
}
}
}
else if( fnkeyName == 'PAGEUP' && isDefined(wfCmdKeyObj.recWtPGUP))
{
for(var i=0; i < wfCmdKeyObj.recWtPGUP.length; i++)
{
var rollEnabledRec = wfCmdKeyObj.recWtPGUP[i];
if ( record.indexOf(rollEnabledRec) > 0)
{
validateAndBuildBuffer(fnkeyName,"AID");
wfCancelEvent();
return true;
}
}
}
return false;
}catch(ex){return false;}
}
function pushAKey(fnkeyName)
{  try
{
if(fnkeyName == null || isUndefined(fnkeyName) || allBlanks(fnkeyName) ||
fnkeyName == wfCmdKeyObj.UNKNOWN_CMD_KEY)
{
return ;
}
if(fnkeyName == 'F1' && (isUndefined(wfCmdKeyDB['F1']) && getAIDKey('F1') == null))
{ // Alt-F1 is HELP, we also translate F1 to HELP if the key is not active on page.
fnkeyName = 'HELP';
}
var ss = null, uri='', frame='', arr, i ;
if(isDefined(wfCmdKeyDB[fnkeyName]))
{
arr = wfCmdKeyDB[fnkeyName].listOfElem ;
if(isDefined(arr) && arr != null)
{
wfCmdKeyObj.lastActiveKeys = arr ;
ss = wfCmdKeyDB[fnkeyName].keyStr ;
uri = wfCmdKeyDB[fnkeyName].uri;
frame = wfCmdKeyDB[fnkeyName].frame;
for(i = 0 ; i < arr.length ; i++)
{  try { bd_ibm(arr[i]); } catch(any_exp0){}
}
}
}
if(fnkeyName == 'PAGEUP' || fnkeyName == 'PAGEDOWN')
{
handlePageUpDnKeys(fnkeyName);
return;
}
if(ss == null)
{
if(fnkeyName == 'ENTER')
{	ss = 'ENTER' ;
}
else
{  ss = getAIDKey(fnkeyName);
}
}
if(ss != null || (isDefined(uri) && uri != ''))
{
if(isDefined(uri) && uri != '')
{
setOvrInfo(uri,frame);
removeHighlightOnCmdKey();
}
validateAndSubmit(ss, false);
}
else
{
if('|HOME|CLEAR|PAGEUP|PAGEDOWN|PRINT|'.indexOf('|'+fnkeyName+'|') != -1)
{
return ;
}
}
wfCancelEvent();
} catch(any_exp){}
}
function wfCancelEvent()
{  window.event.returnValue = false;
window.event.keyCode = 0;
}
var keydowncode = 0;
document.onkeydown = function key_handler()
{
try
{
var ch = window.event.keyCode ;
keydowncode = ch;
if(ch == 9)
{
wfAppErrorMsgClose();
}
else
if(ch == 45)
{
var selectyp = document.selection.type;
if(selectyp == 'None')
{
var typ = window.event.srcElement.type;
if( isValidRef(typ)){ typ = typ.toLowerCase() ;}
if ( typ == 'text' || typ == 'password' || typ == 'textarea')
WFInsertMode = !WFInsertMode ;
}
}
var k_index = getFnKeyName(ch,window.event.altKey,window.event.shiftKey);
pushAKey(k_index);
}
catch(any_exp){}
}
function getFnKeyName(keyCode,altKey,shiftKey) {
try
{  var key, k;
var k_index = null ;
key = keyCode;
if((key > 111) && (key < 124))
{
k = key - 111;
if((altKey == true) && (k == 1))
{
k_index = "HELP";
}
else if(shiftKey == true)
{
k += 12;
k_index = "F" + k;
}
else
{
k_index = "F" + k;
}
}
else
{  switch (key)
{  case 13:
k_index = "ENTER";
break;
case 19:
if(altKey == false)
{
k_index = "CLEAR";
}
else
{  k_index = "PRINT";
}
break;
case 33:
k_index = "PAGEUP";
break;
case 34:
k_index = "PAGEDOWN";
break;
case 36:
k_index = "HOME";
break;
}
}
return k_index;
} catch(any_exp){}
}
document.onhelp = function doHelp() {
try
{  window.event.returnValue = false;
key_handler();
} catch(any_exp){}
}
function SflPgUpDn() {}
SflPgUpDn.prototype.idPrefix = null ;
SflPgUpDn.prototype.pgUpEn = null ;
SflPgUpDn.prototype.pgDnEn = null ;
SflPgUpDn.prototype.reg = function(idSubStr,ispgUpEn,ispgDnEn) {
try
{
if (isDefined(ispgUpEn))
{
if(this.pgUpEn == null)
{	this.idPrefix = [idSubStr];
this.pgUpEn = [ispgUpEn];
this.pgDnEn = [ispgDnEn];
}
else
{	this.idPrefix[this.idPrefix.length] = idSubStr ;
this.pgUpEn[this.pgUpEn.length] = ispgUpEn ;
this.pgDnEn[this.pgDnEn.length] = ispgDnEn ;
}
}
} catch(any_exp){}
}
SflPgUpDn.prototype.run = function(key,recNamePrefix)
{
try
{
var t = (key == 'PAGEUP')? '$scrollbarTableUpArrow' : '$scrollbarTableDownArrow' ;
t = document.getElementById(recNamePrefix+t);
if (t != null){
t.onclick();
return true;
}
var comboBoxObj = document.getElementById( recNamePrefix + "$$cbField" );
if ( comboBoxObj != null )
{
for(var i = 0 ; i < this.pgUpEn.length ; i++)
{  if(this.idPrefix[i] == recNamePrefix)
{
if (key == 'PAGEUP'){
SingleLineSubfileScrollUp(comboBoxObj, this.pgUpEn[i]);
}
else{
SingleLineSubfileScrollDown(comboBoxObj, this.pgDnEn[i]);
}
return true;
}
}
}
return false;
} catch(any_exp) {return false;}
}
var sflPgUpDnObj = new SflPgUpDn();
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function EDTMSKObject(field)
{
this.field = field;
this.fieldSegments = new Array();
this.fieldSegmentsSize = 0;
disposeList.push(this);
}
EDTMSKObject.prototype.updateHiddenField = EDTMSKObject_updateHiddenField;
EDTMSKObject.prototype.addFieldSegment = EDTMSKObject_addFieldSegment;
EDTMSKObject.prototype.setFocus = EDTMSKObject_setFocus;
EDTMSKObject.prototype.dispose = EDTMSKObject_dispose;
function EDTMSKObject_addFieldSegment(fieldSegment)
{
this.fieldSegments[this.fieldSegmentsSize]=fieldSegment;
this.fieldSegmentsSize++;
}
function EDTMSKObject_updateHiddenField()
{
var newValue = "";
var rawValue = "";
var i = 0;
while (i < this.fieldSegmentsSize)
{
var adjustedValue = rightJustify(this.fieldSegments[i].inputField.value, this.fieldSegments[i].inputField.maxLength, " ");
newValue += adjustedValue;
if (this.fieldSegments[i].inputField.readOnly == false)
{
rawValue += adjustedValue;
}
i++;
}
this.field.value = newValue;
if (rawValue.length > 0)
{
this.field.valueWithoutEDTMSK = rawValue;
}
}
function EDTMSKObject_setFocus()
{
if (this.fieldSegmentsSize > 0)
{
this.fieldSegments[0].inputField.focus();
}
}
function EDTMSKObject_dispose()
{
this.field = null;
}
function FieldSegment(edtmskObj, begIndex, endIndex, inputField)
{
this.edtmskObj = edtmskObj;
this.begIndex = begIndex;
this.endIndex = endIndex;
this.inputField = inputField;
inputField.onkeydown = addStatementToFunction(inputField.onkeydown,
'updateEDTMSKDataBeforeSubmit(this);',true);
this.inputField.testIfChanged = InputField_testIfChanged;
this.inputField.fieldSegmentObj=this;
this.inputField.checkEDTMSKKeyPress = InputField_checkEDTMSKKeyPress;
disposeList.push(this);
}
FieldSegment.prototype.testIfChanged = FieldSegment_testIfChanged;
FieldSegment.prototype.dispose = FieldSegment_dispose;
function FieldSegment_testIfChanged()
{
this.edtmskObj.updateHiddenField();
testIfChanged(this.edtmskObj.field);
}
function FieldSegment_dispose()
{
this.inputField = null;
}
function InputField_checkEDTMSKKeyPress()
{
if (wfInfoDB[this.fieldSegmentObj.edtmskObj.field.id].shift=="Y")
{
validateKeyPressForSBCS(window.event.srcElement,'Y');
onSelectUpdateCursorOffset();
}
}
function updateEDTMSKDataBeforeSubmit(th)
{  var evt = window.event ;
var k_index = getFnKeyName(evt.keyCode,evt.altKey,evt.shiftKey);
if(getAIDKey(k_index) != null)
{  th.testIfChanged();
}
}
function InputField_testIfChanged()
{
this.fieldSegmentObj.testIfChanged();
}
function EDTMSKHiddenField_focus()
{
this.edtmskObj.setFocus();
}
function getEDTMSKSegmentValue(edtmskObj, begIndex, endIndex)
{
if (endIndex+1 < edtmskObj.field.value.length)
{
return edtmskObj.field.value.substring(begIndex, endIndex+1);
}
else
{
return edtmskObj.field.value.substring(begIndex);
}
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
var errorOnForm = false;
var errorElem;
var decsep = ".";
var thousandSep = ",";
var fieldHasChanged = false;
var javascriptLoaded = false;
var hostJobCCSID = 0; //Will be initialized in the setAttr() function
var HELPenabled = false;
var callHelp = false;
var isHTMLHelp = false;
var jobDatFmt;
var jobDatSep;
var jobTimSep;
var WFInsertMode ;
var WFFieldExitKeyCode;
var showErrMsgWindow = true;
var showmsg = true;
var finalValidation = false;
var cursorPosTagId = "";
var cursorPosColOffset = 0;
var focusedRecord = "";
var projectRuntimeEnvironment = "";
var projectJ2EELevel = "";
var bufferAlreadySentToHost = false;
var disposeList = new Array();
var wfInfoDB = new Object() ;
function cf(idstr,r,c)
{  try
{  var i, arr = new Array();
for(i = 3 ; i < arguments.length ; i++){ arr[i-3] = arguments[i]; }
crtfld(idstr,r,c,arr);
}
catch(any_exp){}
}
function scf(idstr)
{  try
{  var i, arr = new Array() ;
for(i = 1 ; i < arguments.length ; i++){ arr[i-1] = arguments[i];}
crtfld(idstr,null,null,arr);
}
catch(any_exp){}
}
function rc(idStr)
{  try
{  var t = new WFOutputField();
wfInfoDB[idStr] = t ;
var tElem = document.getElementById(idStr);
t.dhtml = tElem ;
if(arguments.length == 3)
{  t.rowValue = arguments[1] ;
t.colValue = arguments[2] ;
}
else
{  t.rowValue = tElem.rowValue ;
t.colValue = tElem.colValue ;
}
addWFHandlerOnTop(t.dhtml,'onclick',"changeCursor(this);");
}
catch(any_exp){}
}
function crtfld(idstr,r,c,arrInitObj)
{  try
{  var t = new WFInputField() ;
wfInfoDB[idstr] = t ;
var i, k, initObj ;
for(k = 0 ; k < arrInitObj.length ; k++)
{  initObj = arrInitObj[k] ;
for(i in initObj) { t[i] = initObj[i]; }
}
t.dhtml = document.getElementById(idstr);
processChanges(t);
if(r != null && c != null)
{  t.rowValue = r ;
t.colValue = c ;
}
else
{  t.rowValue = t.dhtml.rowValue ;
t.colValue = t.dhtml.colValue ;
}
}
catch(any_exp){}
}
function processChanges(wfelem)
{  try
{  var elem = wfelem.dhtml ;
var typ = elem.type.toLowerCase() ;
var notHidden = (typ != 'hidden') ;
var notHiddenAndReadOnly = (notHidden && elem.readOnly == false) ;
if(notHidden)
{  if(wfelem.onKeyUp != null) addWFHandlerOnTop(elem,'onkeyup',"handleCHECK_ER(this);");
if(wfelem.onClick != null) addWFHandlerOnTop(elem,'onclick',"handleDSPATR_SP(this);");
addWFHandlerOnTop(elem,'onfocus',"onFocusUpdateCursorOffset(this);");
addWFHandlerOnTop(elem,'onblur',"testIfChanged(this);");
}
if(notHiddenAndReadOnly) /* avoid hidden/read-only fields */
{  if(typ == 'textarea') /* set textarea specific event handlers */
{  addWFHandlerOnTop(elem,'onselect',"setTextSelectedInTextArea(this);");
addWFHandlerOnTop(elem,'onkeypress',"respectMaxLengthOnKeyPress(this);");
addWFHandlerOnTop(elem,'onbeforepaste',"respectMaxLengthBeforePaste(this);");
}
addWFHandlerOnTop(elem,'onpaste',"onPasteUpdateCursorOffset(this);");
addWFHandlerOnTop(elem,'oncontextmenu',"onSelectUpdateCursorOffset(this);");
addWFHandlerOnTop(elem,'onclick',"onSelectUpdateCursorOffset(this);");
if(wfelem.shift == "S" || wfelem.shift == "Y" || wfelem.shift == "D"
|| wfelem.shift == "X" || wfelem.shift == "M" || wfelem.shift == "F" )
{
addWFHandlerOnTop(elem,'onkeypress',"validateKeyPressForSBCS(this)");
}
if(wfelem.values != "" && typ != 'password')
{  addWFHandlerOnTop(elem,'onmouseover',"initRadioOption(this);") ;
addWFHandlerOnTop(elem,'onmouseout',"unmarkRadioOptionSrc();") ;
addWFHandlerOnTop(elem,'onmouseup',"unmarkRadioOptionSrc();") ;
}
}
}
catch(any_exp){}
}
function addWFHandlerOnTop(elem,eventName,codeStr)
{  elem[eventName] = addStatementToFunction(elem[eventName],codeStr,true) ;
}
function WFInputField(){}
WFInputField.prototype.dhtml = null;
WFInputField.prototype.onClick = null;
WFInputField.prototype.onKeyUp = null;
WFInputField.prototype.mdt = false;
WFInputField.prototype.shift = "A";
WFInputField.prototype.ptype = "FT_ALPHA";/*???*/
WFInputField.prototype.datalength = 0;
WFInputField.prototype.decpos = 0;
WFInputField.prototype.check = "";
WFInputField.prototype.range = "";
WFInputField.prototype.values = "";
WFInputField.prototype.valuesLabelArr = null;
WFInputField.prototype.edtcde = "";
WFInputField.prototype.edtwrd = "";
WFInputField.prototype.valnum = "FALSE" ;
WFInputField.prototype.comp = "";
WFInputField.prototype.validate = "TRUE" ;
WFInputField.prototype.chkmsg = "" ;
WFInputField.prototype.timfmt = "ISO";
WFInputField.prototype.timsep = "JOB"; /*???*/
WFInputField.prototype.datfmt = "ISO";
WFInputField.prototype.datsep = "JOB";
WFInputField.prototype.errorid = 0;
WFInputField.prototype.attrs = "";
WFInputField.prototype.current = false ;
WFInputField.prototype.row = 1 ;
WFInputField.prototype.column = 1 ;
WFInputField.prototype.buffer = "" ;
WFInputField.prototype.format = "FMTXXX" ;
WFInputField.prototype.textSelected = false ;
WFInputField.prototype.rowValue = 1; /*???*/
WFInputField.prototype.colValue = 1; /*???*/
WFInputField.prototype.columnOffset = 0;
WFInputField.prototype.maxNumOfCols = 0;
WFInputField.prototype.previousValue = '';
WFInputField.prototype.needsTransform = false;
WFInputField.prototype.toString = function() { return "[WFInputField:" + this.dhtml.id + "]"; } /*For debugging*/
function WFOutputField(){}
WFOutputField.prototype.dhtml = null;
WFOutputField.prototype.rowValue = 1;
WFOutputField.prototype.colValue = 1;
WFOutputField.prototype.columnOffset = 0;
WFOutputField.prototype.maxNumOfCols = 0;
WFOutputField.prototype.toString = function() { return "[WFOutputField]"; }  /*For debugging*/
function wfBodyOnLoad()
{  try
{
document.body.focus() ;
setAttrImmediate() ;
initAllCmdKey();
setTimeout("try{callSetAttr();}catch(ex){}",1);
window.status = "Done";
}
catch(any_exp){}
}
function wfBeforeSetAttr()
{  try
{  if(firstTextFieldProp != null)
{  var t = firstTextFieldProp[0];
t.onfocus = firstTextFieldProp[1];
t.onblur = firstTextFieldProp[2];
}
}
catch(any_exp){}
}
function callSetAttr() {
try
{
wfBeforeSetAttr();
setAttr() ;
} catch(any_exp){}
}
var lastActivekey = 0;
function restrictTabsToDocument()
{  try
{  var fm = document.getElementById("SCREEN").elements;
var i , fmFirst = null, fmLast = null;
var reType = /^(text)|(textarea)|(password)$/i ;
var reId = /\$\$cbField$/;
var fldArr = new Array();
for(i = 0 ; i < fm.length ; i++)
{  if( reType.test(fm[i].type) == true && reId.test(fm[i].id) != true &&
fm[i].tabIndex >= 0 && fm[i].style.visibility != 'hidden' &&
fm[i].style.display != 'none'
)
{  if(fmFirst == null) { fmFirst = fm[i] ; }
fmLast = fm[i] ;
fldArr[fldArr.length] = fm[i] ;
}
}
if(fmLast == null || fmFirst == null) return ;
if(fldArr.length > 1)
{  for(i = 1 ; i < fldArr.length ; i++)
{  new AutoTabbingField(fldArr[i-1],fldArr[i]);
}
new AutoTabbingField(fldArr[fldArr.length-1],fldArr[0]);
}
var codeStr = "loopTabbing('" + fmFirst.id + "',false);" ;
fmLast.onkeydown = addStatementToFunction(fmLast.onkeydown,codeStr,false) ;
codeStr = "loopTabbing('" + fmLast.id + "',true);" ;
fmFirst.onkeydown = addStatementToFunction(fmFirst.onkeydown,codeStr,false) ;
if(WFInsertMode == false)
{  var onf = fmFirst.onfocus ;
var onb = fmFirst.onblur ;
fmFirst.onfocus = new Function("");
fmFirst.onblur = new Function("");
fmFirst.focus();
document.execCommand("OverWrite","false","true");
document.body.focus() ;
firstTextFieldProp = [fmFirst,onf,onb];
}
}
catch(any_exp){}
}
var firstTextFieldProp = null;
function loopTabbing(elemId,isActionShiftTab)
{  try
{  if(window.event.keyCode == 9 && window.event.shiftKey == isActionShiftTab)
{  setFocusForTagID(elemId,0);
document.activeElement.select();
wfAppErrorMsgClose();
wfCancelEvent() ;
}
} catch(any_exp){}
}
function disposeAll()
{  try
{  while (disposeList.length > 0)
{
disposeList.pop().dispose();
}
}catch(any_exp){}
}
document.body.onunload = function(){
disposeAll();
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function invokeHelp(cursor) {
var winLeft = window.screenLeft;
var winLeftStr = "left=" + (winLeft + 100);
var winTop = window.screenTop;
var winTopStr = "top=" + (winTop + 100);
var statusStr;
if (isHTMLHelp)
{
statusStr = "status,toolbar,";
}
else
{
statusStr = "status=no,toolbar=no,";
}
var helpWin = window.open("",
"helpwin",
"resizable,scrollbars," + statusStr + "height=500,width=850,"+
winTopStr + "," + winLeftStr);
helpWin.document.open();
helpWin.document.writeln("<FORM NAME=HELPDATA METHOD=GET ACTION='WFHelp'>");
helpWin.document.writeln("<INPUT TYPE=HIDDEN NAME=CURSOR SIZE=30 VALUE='" + cursor + "'> ");
helpWin.document.writeln("</FORM>");
helpWin.document.close();
helpWin.document.HELPDATA.submit();
helpWin.focus();
}
function getOutsideHelpAreaMsg() {
return msg[70];
}
function getExternalHelpNotFoundMsg() {
return msg[71];
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function submitOnEnter(event)
{
if (event.keyCode == 13)
{
validateAndSubmit('ENTER');
return false;
}
}
function respectMaxLengthOnKeyPress() {
var e = window.event ;
var f ;
if(arguments.length == 0) { f = window.event.srcElement ; }
else { f = arguments[0] ; }
var wftmp = wfInfoDB[f.id] ;
if(WFInsertMode == true)
{  if(f.maxLength && ((f.value.length > f.maxLength-1 && wftmp.textSelected != true) || e.keyCode == 13) )
{
e.returnValue = false;
}
}
else if(f.value.length == f.maxLength)
{  var pos = CaretPositionFinder.getCaretIndex(f);
if(pos >= f.maxLength)
{  e.returnValue = false;
}
}
wftmp.textSelected = false;
}
function setTextSelectedInTextArea()
{  var field ;
if(arguments.length == 0) { field = window.event.srcElement ; }
else { field = arguments[0] ; }
wfInfoDB[field.id].textSelected=true;
}
function CaretPositionFinder(textElem)
{  try
{  this.elem = textElem ;
}
catch(any_exp){}
}
CaretPositionFinder.prototype.elem = null ;
CaretPositionFinder.prototype.getCaretIndex = function() {
try
{  return CaretPositionFinder.getCaretIndex(this.elem);
}
catch(any_exp){ return -1 ; }
}
CaretPositionFinder.getActiveElem = function() {
try
{  var caretPos = document.selection.createRange().duplicate();
return caretPos.parentElement();
}
catch(any_exp){ return null ; }
}
CaretPositionFinder.getCaretIndex = function(t) {
try
{  var caretPos = document.selection.createRange().duplicate();
var tActual = caretPos.parentElement();
if(isValidRef(t)) { if(tActual.id != t.id) return -1 ; }
else { t = tActual; }
if(isInputFieldElement(t)==false) { return -1; }
var slen = t.value.length ;
if(slen == 0) { return 0 ; }
caretPos.collapse();
var beginField = t.createTextRange();
beginField.collapse();
var isNotTA = (t.type != 'textarea');
var pos ;
for(pos = 0 ; pos <= slen ; pos++)
{  if(isRectEqualOrLess(caretPos.getBoundingClientRect(),
beginField.getBoundingClientRect(),isNotTA))
{  break ;
}
caretPos.move("character",-1);
}
return pos ;
}
catch(any_exp){ return -1 ; }
}
function isRectEqualOrLess(bcr1,bcr2,isSingleLine) {
try
{  return (bcr1.left <= bcr2.left) && (isSingleLine || bcr1.top <= bcr2.top);
}
catch(any_exp){ return false ;}
}
function AutoTabbingField(textElem,textElemNext)
{  try
{  this.current = textElem ;
this.next = textElemNext ;
textElem.jsObjAtf = this ;
disposeList.push(this);
}
catch(any_exp){}
}
AutoTabbingField.prototype.current = null ;
AutoTabbingField.prototype.next = null ;
AutoTabbingField.prototype.dispose = AutoTabbingField_dispose;
AutoTabbingField.prototype._tabToNext = function() {
try
{
if (isDefined(wfInfoDB[this.next.id]) && wfInfoDB[this.next.id].shift=="I")
{
AutoTabbingField.tabToNext(this.next);
}
else
{
this.next.focus() ;
}
}
catch(any_exp){}
}
AutoTabbingField.tabToNext = function(elem) {
try
{  var atfRef = elem.jsObjAtf ;
if(isValidRef(atfRef))
{  atfRef._tabToNext() ;
return true ;
}
}
catch(any_exp){}
return false ;
}
function AutoTabbingField_dispose()
{
this.current.jsObjAtf = null;
}
function updateCursorOffset(fld,pos)
{  try
{
if(pos >= 0 && pos <= fld.value.length)
{  if(pos >= fld.maxLength) { pos = fld.maxLength -1 ;}
wfInfoDB[fld.id].columnOffset = pos ;
changeCursor(fld,pos);
}
}
catch(any_exp){}
}
function positionCursorAtOffset(t,pos)
{  try
{  var beginField = t.createTextRange();
beginField.collapse();
if(pos > 0) beginField.moveStart("character",pos);
beginField.select();
updateCursorOffset(t,pos);
}
catch(any_exp){}
}
function onFocusUpdateCursorOffset(fld)
{  try
{
if( !finalValidation )
{
clearMessages();
}
if (!isValidRef(fld.errcode) || fld.errcode == 0)
{
}
else if ( fld.errcode != 0 && showmsg && !finalValidation)
{
clearMessages();
addMessage(fld.errcode,fld);
}else if ( fld.errcode != 0 && showmsg && finalValidation)
{
var msg = getMessage(fld.errcode,fld) ;
var staticMsgln = document.getElementById(WF_MSGLINE_FIELD_ID);
wfAppErrorMsgOpen([staticMsgln,fld,msg]);
}
showmsg = true;
var pos = CaretPositionFinder.getCaretIndex(fld);
updateCursorOffset(fld,pos);
}catch(any_exp){}
}
function onSelectUpdateCursorOffset(fld)
{  try
{  if(isValidRef(fld)==false) { fld = CaretPositionFinder.getActiveElem(); }
if(isInputFieldElement(fld) == false){ return ; }
var pos = CaretPositionFinder.getCaretIndex(fld);
if(pos < 0) {
}
else if( ( pos >= fld.maxLength ||
( isValidRef(fld.maxCharLength) && pos >= fld.maxCharLength ) )
&& WFInsertMode==false  && isDataChangingKey(window.event.keyCode)
&& window.event.type != "click" && window.event.type != "contextmenu" && !hasCheck(fld,"FE") )
{
wfAppErrorMsgClose();
AutoTabbingField.tabToNext(fld);
}
else{
updateCursorOffset(fld,pos);
}
}
catch(any_exp){}
}
function isDataChangingKey(k)
{  return !(k == 8 || k == 9 || (k >= 16 && k <= 18) || k == 20 || k == 27 || (k >= 33 && k <= 40) || k == 45 || k == 46 || (k >= 112 && k <= 123) || k == 144) ;
}
var keyupcode = 0;
document.onkeyup = function() {
try
{
var elem = document.activeElement;
var wfelem = wfInfoDB[elem.id];
var shift = wfelem.shift;
keyupcode = window.event.keyCode;
if (keyupcode == WFFieldExitKeyCode && keyupcode == keydowncode)
{
var pos = CaretPositionFinder.getCaretIndex(elem);
elem.value = elem.value.substring(0,pos);
var atfRef = elem.jsObjAtf;
if(isValidRef(atfRef))
{
keypresscode = -1;
atfRef._tabToNext();
return true;
}
}
if ( (
( (keypresscode != -1 || keyupcode == 13) && (shift == "G" || shift == "J" || shift == "E" || shift == "O") )
|| ( (keypresscode == -1 && keyupcode == 13) )
|| ( (keyupcode == keydowncode) && (keyupcode == 8 || keyupcode == 46) )
)
&& isDefined(wfelem)
&& shift != "I"
)
{
keypresscode = -1;
checkFieldForDBCSValidation(elem);
onSelectUpdateCursorOffset();
}
else if( ( keypresscode != -1)
||
(
(keyupcode == keydowncode) &&
((keyupcode >= 33 && keyupcode <= 40) || keyupcode == 8 || keyupcode == 46)
)
)
{	// Not In IME mode (keyup = keydown) or (keypresscode != -1), and cursor may have moved
keypresscode = -1;
onSelectUpdateCursorOffset();
}
}catch(any_exp){}
}
function onPasteUpdateCursorOffset(fld)
{  try
{  respectMaxLengthOnPaste(fld) ;
onSelectUpdateCursorOffset(fld) ;
}
catch(any_exp){}
}
var keypresscode = -1;
document.onkeypress = function()
{
try
{
keypresscode = event.keyCode;
if ( keydowncode == WFFieldExitKeyCode )
{
wfCancelEvent();
}
}
catch(ex){}
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function mod10( elem ) {
var wftmp = wfInfoDB[elem.id];
var value = wftmp.buffer;
value = trimLeadingZeroes( value );
var i;
var j = value.length - 2;
var total = 0;
var flag = true;
var tmp = 0;
var check;
i=value.length-1;
if( parseFloat( value ) == 0 )
return 0;
if (i >= 0)
check = parseInt(value.substr( i , 1 ));
else
return 4;
j = i-1;
for( i = j; i >= 0; i-- ) {
var c = value.substr( i , 1 );
if(c != "") {
var digit = parseInt( c );
if( flag == true ) {
digit = digit * 2;
if( digit >= 10 ) {
total = total + 1 + (digit % 10);
}
else
total = total + digit;
}
else
total = total + digit;
flag = !flag;
}
}
if (total %10 == 0)
{
total = 0;
}
else
{
tmp = total + 10;
i = tmp - (tmp % 10);
total = i - total;
}
if(check == total)
return 0;
else {
if( wfInfoDB[elem.id].chkmsg == "" )
return 4;
else
return -1;
}
}
function mod11( elem ) {
var wftmp = wfInfoDB[elem.id];
var value = wftmp.buffer;
if( parseFloat( value ) == 0 )
return 0;
value = trimLeadingZeroes( value );
var i;
var j = value.length - 2;
var total = 0;
var tmp = 0;
var w = 2;
var c = "";
var digit;
var check;
i=value.length-1;
if (i >= 0)
check = parseInt(value.substr( i , 1 ));
else
return 4;
j = i-1;
for( i = j; i >= 0; i-- ) {
c = value.substr( i , 1 );
if(c != "") {
digit = parseInt( c );
total = total + ( digit * w );
w = w + 1;
if( w > 7 )
w = 2;
}
}
tmp = total % 11;
if (tmp != 0)
{
tmp = 11 - tmp;
}
if( check == tmp )
return 0;
else {
if( wfInfoDB[elem.id].chkmsg == "" ) {
return 4;
}
else
return -1;
}
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function msglnInit(allmsgs,cbFieldSize,tagIdArr)
{  try
{  var cbField = document.getElementById("MSGLINE$$$cbField") ;
var cbButton = document.getElementById("MSGLINE$$$cbButton") ;
var comboBox = document.getElementById("MSGLINE$$$COMBOBOX") ;
if(cbFieldSize > 0)
{  cbField.size = cbFieldSize;
}
cbField.comboBoxField = new ComboBoxField(cbField,cbButton) ;
var cbList = document.getElementById("MSGLINE$$$cbList") ;
cbButton.comboBoxButton = new ComboBoxButton(cbField) ;
if(allmsgs.length != 0 && tagIdArr != null)
{  cbList.comboBoxList = new ComboBoxList(comboBox,cbField,cbList,[],-1) ;
var i, elemWithMsg = null ;
for(i = 0 ; i < tagIdArr.length ; i++)
{  if(tagIdArr[i] != null)
{  elemWithMsg = document.getElementById(tagIdArr[i]);
cbField.comboBoxField.addItem(allmsgs[i],selectText,elemWithMsg,false);
}
else { cbField.comboBoxField.addItem(allmsgs[i]); }
}
}
else
{  cbList.comboBoxList = new ComboBoxList(comboBox,cbField,cbList,allmsgs,-1) ;
if(allmsgs.length != 0)
{  comboBox.style.visibility="visible";
}
}
}
catch(any_exp){}
}
var msglnArray = new Array() ;
function winOnClick()
{  var i ;
for(i = 0 ; i < msglnArray.length ; i++)
{  var flds = msglnArray[i] ;
var loc = document.all(flds[0]) ;
if(loc.comboBoxField.listShowing)
{  var srcid = window.event.srcElement.id ;
if(srcid != flds[0] && srcid != flds[1] && srcid != flds[2])
{  loc.comboBoxField.listShowing = false;
loc.comboBoxField.list.style.visibility = "hidden";
}
}
}
}
function ComboBoxField(field, button, list)
{
this.field = field;
this.button = button;
this.listShowing = false;
if (isDefined(list))
{
list.parentNode.removeChild(list);
}
var elem = document.createElement("DIV") ;
elem.border = 1;
elem.className = "cbList" ;
elem.id = field.id.replace(/cbField$/, "cbList");
elem.style.visibility = "hidden" ;
elem.style.zIndex = 256 ;
elem.noWrap = true;
document.SCREEN.appendChild(elem);
this.list = elem;
var flds = new Array() ;
flds[0] = this.field.id + "" ;
flds[1] = this.button.id + "" ;
flds[2] = this.list.id + "" ;
msglnArray[msglnArray.length] = flds ;
window.document.onclick = addStatementToFunction(window.document.onclick, "winOnClick();",false) ;
disposeList.push(this);
}
ComboBoxField.prototype.showList = ComboBoxField_showList;
ComboBoxField.prototype.nextItem = ComboBoxField_nextItem;
ComboBoxField.prototype.prevItem = ComboBoxField_prevItem;
ComboBoxField.prototype.lastItem = ComboBoxField_lastItem;
ComboBoxField.prototype.addItem = ComboBoxField_addItem;
ComboBoxField.prototype.removeItem = ComboBoxField_removeItem;
ComboBoxField.prototype.clear = ComboBoxField_clear;
ComboBoxField.prototype.dispose = ComboBoxField_dispose;
function ComboBoxButton(field)
{
this.listField = field;
}
ComboBoxButton.prototype.showList = ComboBoxButton_showList;
function ComboBoxList(comboBox, field, list, allItems, itemIndex)
{
this.comboBox = comboBox;
this.listField = field;
this.list = list;
this.allItems = allItems;
this.itemId = null;
if (itemIndex >= 0)
{
this.listField.value = allItems[itemIndex];
this.comboBox.style.visibility = "visible";
this.itemIndex = itemIndex;
} else
{
this.itemIndex = 0;
}
disposeList.push(this);
}
ComboBoxList.prototype.associatedActions = new Array();
ComboBoxList.prototype.show = ComboBoxList_show;
ComboBoxList.prototype.setItem = ComboBoxList_setItem;
ComboBoxList.prototype.mouseOverItem = ComboBoxList_mouseOverItem;
ComboBoxList.prototype.nextItem = ComboBoxList_nextItem;
ComboBoxList.prototype.prevItem = ComboBoxList_prevItem;
ComboBoxList.prototype.lastItem = ComboBoxList_lastItem;
ComboBoxList.prototype.addItem = ComboBoxList_addItem;
ComboBoxList.prototype.removeItem = ComboBoxList_removeItem;
ComboBoxList.prototype.clear = ComboBoxList_clear;
ComboBoxList.prototype.dispose = ComboBoxList_dispose;
function ComboBoxField_showList()
{
if (this.listShowing)
{
this.listShowing = false;
this.list.style.visibility = "hidden";
} else
{  this.listShowing = true;
var posLeft = this.field.offsetLeft;
var posTop = this.field.offsetTop;
var p = this.field.offsetParent;
while (p != null)
{
posLeft += p.offsetLeft;
posTop += p.offsetTop;
p = p.offsetParent;
}
var posWidth = this.button.offsetParent.offsetLeft - this.field.offsetParent.offsetLeft + this.button.offsetLeft + this.button.offsetWidth;
this.list.comboBoxList.show(posLeft, posTop, posWidth);
}
}
function ComboBoxField_nextItem()
{
return this.list.comboBoxList.nextItem();
}
function ComboBoxField_prevItem()
{
return this.list.comboBoxList.prevItem();
}
function ComboBoxField_lastItem()
{
return this.list.comboBoxList.lastItem();
}
function ComboBoxField_addItem(s, action, data, performAction)
{
this.list.comboBoxList.addItem(s, action, data,performAction);
}
function ComboBoxField_removeItem(msg)
{
this.list.comboBoxList.removeItem(msg);
}
function ComboBoxField_clear()
{
this.list.comboBoxList.clear();
}
function ComboBoxField_dispose()
{
this.field.comboBoxField = null;
}
function ComboBoxButton_showList()
{
this.listField.comboBoxField.showList();
}
function ComboBoxList_show(left, top, width)
{
var len = this.allItems.length;
if (len != 0)
{
var str = "";
var widthofitems = width ;
this.list.style.height = "" ;
for(i = 0; i < len; i++)
{
str +="<input class=\"cbListItem\" id=\""+this.list.id+"$item" + i + "\" style=\"border-width:0; border-style:none; width:" + widthofitems +";\" onClick=\"" + this.list.id + ".comboBoxList.setItem(this, " + i + ");\" onMouseOver = \"" + this.list.id + ".comboBoxList.mouseOverItem(this);\" onMouseOut =\"this.className = 'cbListItem';\" value=\"";
str += transformForQuotedHtml(this.allItems[i]) + "\" size=" + this.listField.size + "\" maxlength=" + this.listField.size + " readonly><br>";
}
this.list.innerHTML = str;
if(top < this.list.offsetHeight)
{  this.list.style.height = top / 2 ;
}
this.list.style.left = left;
this.list.style.top = top - this.list.offsetHeight;
this.list.style.width = width;
this.itemId = document.all(this.list.id +"$item" + this.itemIndex);
if (this.itemId == null)
{
this.itemId = document.all(this.list.id +"$item0");
}
this.itemId.className = "cbListItemOver";
this.list.style.visibility = "visible";
}
}
function ComboBoxList_setItem(item, index)
{
this.listField.value = item.value;
this.itemId = item;
this.itemIndex = index;
this.listField.comboBoxField.listShowing = false;
this.list.style.visibility = "hidden";
if (typeof(this.associatedActions[index]) != "undefined")
{
action = this.associatedActions[index].action;
param = this.associatedActions[index].data;
if (param != null)
{
action(param);
}
else //otherwise pass no params
{
action();
}
}
}
function ComboBoxList_mouseOverItem(item)
{
this.itemId.className = "cbListItem";
item.className = "cbListItemOver";
this.itemId = item;
}
function ComboBoxList_nextItem()
{
if (this.itemIndex < (this.allItems.length - 1))
{
if (this.listField.comboBoxField.listShowing)
{
this.listField.comboBoxField.listShowing = false;
this.list.style.visibility = "hidden";
}
this.itemIndex++;
this.listField.value = this.allItems[this.itemIndex];
this.itemId = document.all(this.list.id +"$item" + this.itemIndex);
return true;
} else
{
return false;
}
}
function ComboBoxList_prevItem()
{
if (this.itemIndex > 0)
{
if (this.listField.comboBoxField.listShowing)
{
this.listField.comboBoxField.listShowing = false;
this.list.style.visibility = "hidden";
}
this.itemIndex--;
this.listField.value = this.allItems[this.itemIndex];
this.itemId = document.all(this.list.id +"$item" + this.itemIndex);
return true;
} else
{
return false;
}
}
function ComboBoxList_lastItem()
{
if (this.allItems.length > 0)
{
if (this.listField.comboBoxField.listShowing)
{
this.listField.comboBoxField.listShowing = false;
this.list.style.visibility = "hidden";
}
this.itemIndex = this.allItems.length - 1;
this.listField.value = this.allItems[this.itemIndex];
this.itemId = document.all(this.list.id +"$item" + this.itemIndex);
return true;
} else
{
return false;
}
}
function ComboBoxList_addItem(s, action, data, performAction)
{
if (this.allItems.length == 0)
{
this.listField.value = s;
this.comboBox.style.visibility = "visible";
try
{  if(performAction)
{  action(data);
}
} catch(any_exp){}
}
var itemsSoFar = this.allItems.length;
this.allItems[itemsSoFar] = s;
var complexAction = new Object();
if (typeof(action) != "undefined")
{
complexAction.action = action;
}
else
{
complexAction.action = new Function();
}
if (typeof(data) != "undefined")
{
complexAction.data = data;
}
else
{
complexAction.data = null;
}
this.associatedActions[itemsSoFar] = complexAction;
}
function ComboBoxList_removeItem(msg)
{
if (this.allItems.length == 0)
return;
var oldMsgList = this.allItems;
var oldActions = this.associatedActions;
var oldMsgListLen = this.allItems.length;
this.clear();
for(i=0; i<oldMsgListLen; i++) {
if (oldMsgList[i] != msg) {
this.addItem(oldMsgList[i], oldActions[i].action, oldActions[i].data);
}
}
}
function ComboBoxList_clear()
{
this.allItems = new Array();
this.associatedActions = new Array();
this.itemId = null;
this.itemIndex = 0;
wfAppErrorMsg.close(this.listField);
this.comboBox.style.visibility = "hidden";
}
function ComboBoxList_dispose()
{
this.list.comboBoxList = null;
}
var WF_MSGLINE_FIELD_PREFIX = "MSGLINE$$$" ;
var WF_MSGLINE_FIELD_ID = "MSGLINE$$$cbField" ;
var wfAppErrorMsg = new PopupErrorMsg("WF_PopupErrorMsg",WF_MSGLINE_FIELD_ID,WF_MSGLINE_FIELD_PREFIX);
function wfAppErrorMsgClose()
{  try
{  if(wfAppErrorMsg.errorElem != null)
{  wfAppErrorMsg.close();
}
}
catch(any_exp){}
}
function wfAppErrorMsgOpen(arr)
{  try
{  var listField = arr[0] ;
var elemWithError = arr[1] ;
var msg = arr[2] ;
selectText(elemWithError);
wfAppErrorMsg.open(listField,elemWithError,msg);
}
catch(any_exp){}
}
function PopupErrorMsg(id,msgLineId,idPrefix) {
this.id = id ;
this.msgLineId = msgLineId ;
this.idPrefix = idPrefix ;
}
PopupErrorMsg.Max_zIndex = 254 ;
PopupErrorMsg.prototype.id = null ;
PopupErrorMsg.prototype.msgLineId = null ;
PopupErrorMsg.prototype.msgElem = null;
PopupErrorMsg.prototype.idPrefix = null;
PopupErrorMsg.prototype.errorElem = null ;
PopupErrorMsg.prototype.color = null;
PopupErrorMsg.prototype.bgColor = null;
PopupErrorMsg.prototype.dispose = PopupErrorMsg_dispose;
PopupErrorMsg.prototype.getMsgElem = function() {
try
{  if(this.msgElem == null)
{  var t = document.createElement("DIV");
t.id = this.id ;
t.jsObjPopupErrorMsg = this ;
t.className = 'popupErrorMsg' ;
t.onclick = wfCancelEventAndBubble;
window.document.onclick = addStatementToFunction(window.document.onclick,
"PopupErrorMsg.CLOSEID('" + this.id + "');",false) ; /* listen for global onclick */
var elemStyle = t.style ;
elemStyle.position = 'absolute';
elemStyle.visibility = 'hidden';
elemStyle.zIndex = PopupErrorMsg.Max_zIndex ;
document.body.appendChild(t) ;
this.msgElem = t ;
disposeList.push(this);
}
return this.msgElem ;
} catch(any_exp){ return null; }
}
PopupErrorMsg.prototype.isValidCaller = function(elem) {
return isValidRef(elem) && elem.id == this.msgLineId ;
}
PopupErrorMsg.prototype.open = function(listField,elemWithError,msg) {
try
{  if(isValidDOMElement(elemWithError) == false || isValidRef(msg) == false || isBlank(msg) == true)
{  return false ;
}
if(elemWithError.type == 'hidden') { return false; }
if(this.isValidCaller(listField) == false) { return false; }
if(popupController != null && popupController.jobPending == true)
{  erasePopupLayerIm() ;
}
this.close();
var elem = this.getMsgElem() ;
var elemWithErrorLoc = getAbsPosition(elemWithError);
elem.style.left = elemWithErrorLoc.left + elemWithError.offsetWidth ;
elem.style.top = elemWithErrorLoc.top ;
elem.innerHTML = '<span class="popupErrorMsgClose" onclick="PopupErrorMsg.CLOSE(this.offsetParent);">&nbsp;X&nbsp;</span>&nbsp;&nbsp;' + msg ;
positionWithIn(elem,elemWithError,document.body);
this.errorElem = elemWithError ;
this.bgColor = elemWithError.currentStyle.backgroundColor;
this.color = elemWithError.currentStyle.color;
elemWithError.style.backgroundColor = elem.currentStyle.backgroundColor;
elemWithError.style.color = elem.currentStyle.color;
if( showErrMsgWindow  == true)
elem.style.visibility = 'visible';
return true ;
} catch(any_exp){ return false; }
}
PopupErrorMsg.prototype.close = function(listField) {
try
{  if(isDefined(listField) && this.isValidCaller(listField) == false)
{  return false ;
}
this.resetStyling();
if(this.msgElem != null)
{
var elemStyle = this.msgElem.style ;
elemStyle.visibility = 'hidden';
elemStyle.innerHtml = '' ;
}
return true ;
} catch(anyExp){ return false; }
}
PopupErrorMsg.prototype.resetStyling = function() {
try
{  var t = this.errorElem ;
if(t != null)
{  t.style.backgroundColor = this.bgColor ;
t.style.color = this.color ;
}
this.errorElem = null ;
this.color = null;
this.bgColor = null;
} catch(anyExp){}
}
PopupErrorMsg.CLOSE = function(th) {
try
{
winOnClick(); // Hides the combo box list
if( finalValidation)
th.jsObjPopupErrorMsg.close();
else
clearMessages();
var fld = document.getElementById(cursorPosTagId);
positionCursorAtOffset(fld,cursorPosColOffset );
showmsg = false;
selectText(fld);
} catch(any_exp){
}
}
PopupErrorMsg.CLOSEID = function(idStr) {
try
{
var elem = document.getElementById(idStr) ;
var th = elem.jsObjPopupErrorMsg ;
if((window.event.srcElement.id).indexOf(th.idPrefix) != 0
&& window.event.srcElement != th.errorElem)
{
if(finalValidation)
th.close();
else
clearMessages();
}
} catch(any_exp){}
}
function PopupErrorMsg_dispose()
{
this.msgElem = null;
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
var el_in_focus;
var i_row;
var i_el_originalBackgroundColor;
var i_el_originalColor;
var i_el_checkAB;
var d_el_top;
var d_el_left;
var d_el_height;
var d_el_width;
function showMenuOptions(i_el, d_el, mode)
{
var p;
d_el.style.whiteSpace="nowrap";
if (mode)
{
i_el_originalBackgroundColor=i_el.currentStyle.backgroundColor;
i_el_originalColor = i_el.currentStyle.color;
p = i_el.offsetParent;
el_in_focus = i_el;
d_el.style.posLeft = i_el.offsetLeft;
d_el.style.posTop = i_el.offsetTop;
while (p != null && p.currentStyle.position != "absolute")
{
d_el.style.posLeft += p.offsetLeft;
d_el.style.posTop += p.offsetTop;
p = p.offsetParent;
}
d_el.style.left = d_el.style.posLeft+i_el.offsetWidth;
d_el.style.visibility = "visible";
d_el_actualHeight = d_el.offsetHeight;
d_el_actualWidth = d_el.offsetWidth;
d_el.style.visibility = "hidden";
d_el.style.width = d_el_actualWidth;
d_el.style.posTop = d_el.style.posTop-d_el_actualHeight+i_el.offsetHeight;
d_el.style.top = d_el.style.posTop;
d_el_height = d_el.style.height;
d_el_width = d_el.style.width;
d_el_top = d_el.style.top;
d_el_left = d_el.style.left;
}
else
{
d_el.style.height = d_el_height;
d_el.style.width = d_el_width;
d_el.style.top = d_el_top;
d_el.style.left = d_el_left;
}
d_el.style.zIndex="1";
d_el.style.visibility = "visible";
i_el.style.backgroundColor=d_el.currentStyle.backgroundColor;
i_el.style.color=d_el.currentStyle.color;
}
function eraseMenuOptions(i_el,d_el)
{
i_el.style.backgroundColor=i_el_originalBackgroundColor;
i_el.style.color=i_el_originalColor;
d_el.style.visibility = "hidden";
}
function showRadioOptions(i_el, d_el)
{
el_in_focus = i_el;
i_el_checkAB = getCheckAB(i_el);
var cleft = i_el.offsetLeft;
var ctop = i_el.offsetTop;
var p = i_el.offsetParent;
while(p != null)
{  cleft += p.offsetLeft + p.clientLeft;
ctop += p.offsetTop + p.clientTop;
p = p.offsetParent;
}
var cleft2 = cleft + i_el.offsetWidth;
d_el.style.whiteSpace="nowrap";
d_el.style.visibility = "hidden";
d_el.style.position="absolute";
d_el.style.left = cleft2 ;
d_el.style.top = ctop;
d_el.style.fontWeight="bolder";
d_el.style.overflowX = "visible";
d_el.style.overflowY = "visible";
var ht = d_el.offsetHeight;
var wt = d_el.offsetWidth;
d_el.style.fontWeight="normal";
d_el.style.overflowX = "hidden";
d_el.style.overflowY = "auto";
d_el.style.width = wt ;
var ay1 = document.body.clientHeight ;
if(ht > ay1) { d_el.style.height = ay1 ; }
else { d_el.style.height = ht ; }
positionWithIn(d_el,i_el,document.body);
var r_el = d_el.getElementsByTagName("INPUT");
var i_el_value = trim(i_el.value);
var r_el_trimed ;
for(i = 0; i < r_el.length; i++)
{  if(r_el[i].type != "radio") { continue ; }
r_el_trimed = trim(r_el[i].value) ;
if( i_el_value != "" && (r_el_trimed == i_el_value ||
(!hasCheck(i_el,"LC") && r_el_trimed == i_el_value.toUpperCase())
)
)
{  r_el[i].checked=true;
break;
}
else if(r_el_trimed == "" && i_el_value == "")
{  r_el[i].checked=true;
break;
}
else { r_el[i].checked = false; }
}
d_el.style.visibility = "visible";
i_el_originalBackgroundColor=i_el.currentStyle.backgroundColor;
i_el_originalColor = i_el.currentStyle.color;
i_el.style.backgroundColor=d_el.currentStyle.backgroundColor;
i_el.style.color=d_el.currentStyle.color;
}
function trim(s)
{  return trimTrailingChars(s, ' ') ;
}
function getCheckAB(i_el)
{  var s = wfInfoDB[i_el.id].check ;
if(typeof(s) == "undefined") { return false ; }
var re = /^(.*;)?AB(;.*)?$/ ;
return re.test(s) ;
}
function eraseRadioOptions(i_el,d_el)
{
if ((i_el.type != "password") && (i_el.readOnly != true))
{
i_el.style.backgroundColor=i_el_originalBackgroundColor;
i_el.style.color=i_el_originalColor;
d_el.style.visibility = "hidden";
}
}
function setRadioOption(i_el,r_el,index)
{
i_el.errcode = 0;
if (i_el_checkAB && r_el.value == i_el.value) //deselect the option
{
i_el.value = " ";
i_el.option = -1;
}
else
{
i_el.value = r_el.value;
i_el.option = index;
}
i_el.focus();
if (wfInfoDB[i_el.id].onKeyUp != null)
{
validateAndSubmit("ENTER");
}
}
var popupController = null ;
var popupLayerFadingDelay = 3000 ; /* in milliseconds */
function erasePopupLayerIm()
{  if(popupController == null || popupController.jobPending == false) return ;
popupController.timerDisable = true ;
window.clearInterval(popupController.timer) ;
eraseRadioOptions(popupController.i_el, popupController.d_el) ;
popupController.jobPending = false ;
popupController.timerDisable = false ;
}
function erasePopupLayerOnTimer()
{  if(popupController == null || popupController.timerDisable || popupController.on_i || popupController.on_d)
{  return ; /* return after 'popupLayerFadingDelay' time */
}
erasePopupLayerIm() ;
}
function initRadioOption(i_el)
{  var d_el = getRadioOptionLayer(i_el) ;
if(popupController == null)
{  popupController = new Object() ;
popupController.timerDisable = false ;
window.document.onclick = addStatementToFunction(window.document.onclick, "erasePopupLayerOnClick();", false) ;
}
else if(popupController.jobPending)
{  erasePopupLayerIm() ;
}
wfAppErrorMsgClose();
popupController.i_el = i_el ;
popupController.d_el = d_el ;
popupController.on_i = true ;
popupController.on_d = false ;
popupController.jobPending = true ;
popupController.timer = window.setInterval("erasePopupLayerOnTimer()",popupLayerFadingDelay) ;
showRadioOptions(i_el, d_el) ;
}
var radioOptionsLayer = null ;
var radioOptionsCache = null ;
function getRadioOptionCache(s)
{  try
{  if(radioOptionsCache == null)
{  radioOptionsCache = new Object() ;
}
else if(typeof(radioOptionsCache[s]) != 'undefined')
{  return radioOptionsCache[s] ;
}
}
catch(any_exp){}
return null ;
}
function setRadioOptionCache(s,v)
{  try
{  if(radioOptionsCache == null)
{  radioOptionsCache = new Object() ;
}
radioOptionsCache[s] = v ;
}
catch(any_exp){}
}
function getRadioOptionLayer(i_el)
{  try
{  if(radioOptionsLayer == null)
{  radioOptionsLayer = document.createElement("DIV") ;
radioOptionsLayer.className = 'radioSel' ;
radioOptionsLayer.style.position = 'absolute' ;
radioOptionsLayer.style.visibility = 'hidden' ;
radioOptionsLayer.style.zIndex ="255" ;
radioOptionsLayer.onmouseover=markRadioOption;
radioOptionsLayer.onmouseout=unmarkRadioOption;
document.body.appendChild(radioOptionsLayer);
}
radioOptionsLayer.innerHTML = '' ;
radioOptionsLayer.style.height = '' ;
radioOptionsLayer.style.width = '' ;
var fldObj = wfInfoDB[i_el.id] ;
var frmcacheDB = getRadioOptionCache(fldObj.values) ;
if(frmcacheDB == null || fldObj.valuesLabelArr != null)
{  var s = fldObj.values ;
s = s.replace(/[ ]*;[ ]*$/,'') ;
var arr = s.split(';') ;
var i, str = '' ;
for(i = 0 ; i < arr.length ; i++)
{  var valLbl, val = trim(arr[i]) ;
if(fldObj.valuesLabelArr == null || isUndefined(fldObj.valuesLabelArr[i]))
{  valLbl = val ;
}
else
{  valLbl = trim(fldObj.valuesLabelArr[i]) ;
}
str += '<input type="radio" name="radioDivButton" onClick="setRadioOptionValue(el_in_focus, this,0);"' +
' value="' + val + '"' +
' onMouseOver="radioOptionsLabelSpanObj' + i + '.className=\'radioOver\';"' +
' onMouseOut="radioOptionsLabelSpanObj' + i + '.className=\'radioOut\';">' +
'<SPAN id="radioOptionsLabelSpanObj' + i + '">'+ valLbl +'</SPAN><br>' ;
}
if(fldObj.valuesLabelArr == null)
{  setRadioOptionCache(fldObj.values,str) ;
}
radioOptionsLayer.innerHTML = str ;
}
else { radioOptionsLayer.innerHTML = frmcacheDB ; }
}
catch(any_exp){}
return radioOptionsLayer ;
}
function unmarkRadioOptionSrc()
{  if(popupController != null) popupController.on_i = false ;
}
function markRadioOption()
{  if(popupController != null) popupController.on_d = true ;
}
function unmarkRadioOption()
{  if(popupController != null) popupController.on_d = false ;
}
function setRadioOptionValue(i_el,r_el,index)
{  setRadioOption(i_el,r_el,index) ;
erasePopupLayerIm() ;
}
function erasePopupLayerOnClick()
{  if(popupController != null && !popupController.on_i && !popupController.on_d)
{  erasePopupLayerIm() ;
}
}
function positionWithIn(elemAbsPos,elemBase,elemContainer)
{  try
{  var t = elemContainer.scrollLeft + elemContainer.clientWidth;
if(elemAbsPos.offsetLeft + elemAbsPos.offsetWidth > t)
{  t = elemAbsPos.offsetLeft - elemBase.offsetWidth - elemAbsPos.offsetWidth ;
if(t > elemContainer.scrollLeft)
{  elemAbsPos.style.left = t ;
}
}
t = elemContainer.scrollTop + elemContainer.clientHeight ;
if(elemAbsPos.offsetTop + elemAbsPos.offsetHeight > t)
{  t = t - elemAbsPos.offsetHeight ;
if(t > elemContainer.scrollTop)
{  elemAbsPos.style.top = t ;
}
}
} catch(any_exp){}
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function checkShift( e ) {
var wftmp = wfInfoDB[e.id] ;
var rc = 0;
rc = validateDBCS(e);
if (rc == 0)
{
if( wftmp.shift == " " )
rc = shiftA( e );
if( wftmp.shift == "A" )
rc = shiftA( e );
else if( wftmp.shift == "D" )
rc = shiftD( e );
else if( wftmp.shift == "F" )
rc = shiftF( e );
else if( wftmp.shift == "L" )
rc = shiftL( e );
else if( wftmp.shift == "M" )
rc = shiftM( e );
else if( wftmp.shift == "N" || wftmp.shift == "I")
rc = shiftN( e );
else if( wftmp.shift == "S" )
rc = shiftS( e );
else if( wftmp.shift == "T" )
rc = shiftT( e );
else if( wftmp.shift == "W" )
rc = shiftA( e );
else if( wftmp.shift == "X" )
rc = shiftX( e );
else if( wftmp.shift == "Y" )
rc = shiftY( e );
else if( wftmp.shift == "Z" )
rc = shiftZ( e );
else
rc = 0;
}
return rc;
}
var lowerIndex = new Array(5);
var highIndex = new Array(5);
lowerIndex[0] = new Array(0x0080,0x00A4,0x00A6,0x00AD,0x1100,0x1E00,0x203F,0x20AD,0xAC00,0xF900,0xFE20,0xFF00,0xFFE0,0xFFF0);
highIndex[0]  = new Array(0x00A1,0x00A4,0x00AB,0x04FF,0x11FF,0x203D,0x20AB,0x9FA5,0xF86F,0xFB4F,0xFE6F,0xFF5F,0xFFE6,0xFFFF);
lowerIndex[1] = new Array(0x0080,0x00A4,0x00A7,0x00AD,0x00B0,0x0600,0x0F00,0x1100,0x203F,0x20AA,0xF900,0xFE20,0xFF00,0xFFE0,0xFFF0);
highIndex[1] =  new Array(0x00A1,0x00A4,0x00AB,0x00AE,0x04FF,0x06FF,0x0FBF,0x203D,0x20A8,0xF86F,0xFB4F,0xFE6F,0xFF5F,0xFFE6,0xFFFF);
lowerIndex[2] = new Array(0x0080,0x00A4,0x00A7,0x00AD,0x1100,0x1E00,0x20AA,0x20AD,0xAC00,0xF900,0xFE20,0xFF00,0xFFE0,0xFFF0);
highIndex[2] =  new Array(0x00A1,0x00A4,0x00AB,0x04FF,0x11FF,0x20A8,0x20AB,0x9FA5,0xF86F,0xFB4F,0xFE6F,0xFF5F,0xFFE6,0xFFFF);
lowerIndex[3] = new Array(0x0080,0x00A4,0x00A7,0x00AD,0x00B0,0x1100,0x1E00,0x203F,0x20AA,0xAC00,0xF900,0xFE20,0xFF00,0xFFE0,0xFFF0);
highIndex[3]  = new Array(0x00A1,0x00A4,0x00AB,0x00AE,0x04FF,0x11FF,0x203D,0x20A8,0x9FA5,0xF86F,0xFB4F,0xFE6F,0xFF5F,0xFFE6,0xFFFF);
lowerIndex[4] = new Array(0x1100,0x3000,0x3130,0xAC00,0xF900,0xFE30,0xFF00,0xFFE0);
highIndex[4] =  new Array(0x11FF,0x30FF,0x9FA5,0xF86F,0xFAFF,0xFE6F,0xFF5F,0xFFE6);
function isDBCS(e, ccsid)
{
var index;
if (ccsid == 930 || ccsid == 939 || ccsid == 1399 || ccsid == 5035 || ccsid == 5026)
{
index = 0;
} else if ( ccsid == 933 || ccsid == 1364)
{
index = 3;
} else if ( ccsid == 935 || ccsid == 1388)
{
index = 1;
} else if ( ccsid == 937 )
{
index = 2;
} else
{
index = 4;
}
var arrayLen = lowerIndex[index].length;
var i;
for (i=0;i<arrayLen;i++)
{
if (e >= lowerIndex[index][i] && e <= highIndex[index][i])
{
return true;
}
}
return false;
}
function checkFieldForDBCSValidation(e)
{
try{
var rc = validateDBCS(e);
if ( !( keyupcode == keydowncode && keyupcode == 13) )
clearMessages();
if(rc == 0)
{
return true;
}
else
{
addMessage( rc, e);
window.event.returnValue = false;
}
}catch(exp){}
}
function validateDBCS(e)
{
var str = e.value;
var shift = wfInfoDB[e.id].shift;
var currIndex = 0;
var isFirstCharDBCS;
var currCharDBCS;
var inDBCSBracket = false;
var rc = 0;
var charCode;
var dataLength = 0;
wfInfoDB[e.id].needsTransform = false; //unless we find a quote or ampersand in the field we won't require a transform
while (currIndex < str.length)
{
charCode = str.charCodeAt(currIndex);
currCharDBCS = isDBCS(charCode,hostJobCCSID);
if (charCode == 34 || charCode == 38)
{
wfInfoDB[e.id].needsTransform = true;
}
if (shift == 'J' || shift == 'G')
{
if (!currCharDBCS)
{
rc = 66;
}
}
else if (shift == 'E')
{
if (currIndex == 0)
{
isFirstCharDBCS = currCharDBCS;
}
else
{
if (isFirstCharDBCS != currCharDBCS)
{
if (isFirstCharDBCS)
{
rc = 68;
}
else
{
rc = 67;
}
}
}
}
else if (shift != 'O') //alphanumeric shifts
{
if (currCharDBCS)
{
rc = 65;
}
}
if (rc != 0)
{
e.sub1 = "'" + str.charAt(currIndex) + "'";
e.sub2 = currIndex + 1;
return rc;
}
if (shift == 'O' || shift == 'E')
{
if (currCharDBCS)
{
dataLength += 2;
}
else
{
dataLength++;
}
if (!inDBCSBracket && currCharDBCS)
{
dataLength += 2;
}
inDBCSBracket = currCharDBCS;
if (dataLength == e.maxLength)
{
e.maxCharLength = currIndex + 1;
}
else
{
e.maxCharLength = 'undefined';
}
if (dataLength > e.maxLength)
{
rc = 69;
e.sub1 = "'" + str.substring(currIndex,str.length) + "'";
return rc;
}
}
currIndex++;
}
if (str.length > e.maxLength)
{
rc = 69;
e.sub1 = "'" + str.substring(e.maxLength,str.length) + "'";
return rc;
}
return rc;
}
function shiftF( e ) {
var p = 0;
var m = 0;
var exp = "";
var sig = "";
var allow = "0123456789E+-.";
var digits = "0123456789";
var val = e.value;
var rc = 0;
val = val.toUpperCase();
for( var j = 0; j < val.length; j++ ) {
var c = val.charAt(j);
p = allow.indexOf( c );
if( p == -1 ) {
return 62;
}
}
p = val.indexOf("E");
if( p != -1 ) {
sig = val.substring( 0, p );
rc = validDecimal( sig, wfInfoDB[e.id].decpos);
if( rc > 0 )
return 62;
exp = val.substring( p + 1, val.length );
if( exp.length > 5 )
return 62;
for( var j = 0; j < exp.length; j++ ) {
var c = exp.charAt( j );
if( ( c == "-" && j == 0 ) || ( c == "+" && j == 0 ) ){
val = val.substr( 1 );
val = rightJustify(val, e.maxLength, "0");
val = c + val;
}
else {
val = rightJustify( val, e.maxLength, "0");
}
var i = digits.indexOf( c );
if( i == -1 ) {
return 62;
}
}
}
else {
rc = validDecimal( val, wfInfoDB[e.id].decpos);
var c = val.charAt(0);
if (( c == "-" ) || ( c == "+" )) {
val = val.substr( 1 );
val = rightJustify(val, e.maxLength, "0");
val = c + val;
}
else {
val = rightJustify( val, e.maxLength, "0");
}
}
if( rc == 0 )
wfInfoDB[e.id].buffer = val;
return rc;
}
function shiftZ( e ) {
var val = e.value;
val = trimLeadingSpaces( val );
val = trimTrailingSpaces( val );
var rc = 0;
if (val.length == 0 )
return 0;
if ( allBlanks( val ) )
val = "0001-01-01-01.01.01.000000";
if( val.length < 20 )
return 34;
if( allNumbers( val ) ) {
if( val.length != 20 )
return 34;
else {
val = val.substr( 0, 4) + '-' + val.substr(4, 2) + '-' + val.substr(6, 2) + '-' +
val.substr(8, 2) + '.'+ val.substr(10, 2) + '.' + val.substr(12,2) + '.' +
val.substr(14, 6);
}
}
wfInfoDB[e.id].buffer = val;
if( val.length != 26 || charsOK( val, "nnnn-nn-nn-nn.nn.nn.nnnnnn") !=0 )
return 34;
else {
var ymd = val.substr(0, 4) + val.substr(5, 2) + val.substr(8, 2)
if( validDate( ymd ) != 0 )
return 34;
var hours   = val.substr( 11, 2 );
var minutes = val.substr( 14, 2 );
var seconds = val.substr( 17, 2 );
if(hours > 24 ||
minutes > 59 ||
seconds > 59)
return 34;
}
return 0;
}
function shiftA( e ) {
var rc = 0;
var val = e.value;
var l = e.value.length-1;
if( !hasCheck( e, "LC" ) ) {
var f = val.charAt(0);
var l = val.charAt(l);
if(f == '"' && l == '"' && hasCheck( e, "VE"))
val = val.substring(1, val.length-1);
else
val = val.toUpperCase();
}
val = leftJustify( val, wfInfoDB[e.id].datalength, " ");
wfInfoDB[e.id].buffer = val;
return rc;
}
function shiftN( e ) {
var rc = 0;
var wftmp = wfInfoDB[e.id];
if (wftmp.ptype == "FT_NUMERIC")
rc = shiftY( e );
else if (wftmp.ptype == "FT_ALPHA") {
rc = shiftA( e );
}
return rc;
}
function validateKeyPressForSBCS()
{
try{
var keyCode = window.event.keyCode ;
if ( keyCode == 27) {
return;
}
clearMessages(); // Clear old error messages
var field;
var shift;
if(arguments.length == 0){
field = window.event.srcElement ;
shift = wfInfoDB[field.id].shift;
}
else if (arguments.length == 1){
field = arguments[0];
shift = wfInfoDB[field.id].shift;
}
else{ // This is only for EDTMSK fields
field = arguments[0];
shift = arguments[1];
}
var errcode = 0;
switch ( shift)
{
case "S":
if(field.readOnly == false &&
!((keyCode >= 48 && keyCode <= 57) || //'0' to '9'
(keyCode == 43 || keyCode == 45) || //'+','-'
keyCode == 32) && //space
keyCode != 13 ) //enter
{
errcode = 5;
}
break;
case "X":
if(field.readOnly == false &&
!((keyCode >= 65 && keyCode <= 90) || //'A' to 'Z'
(keyCode >= 97 && keyCode <= 122) || //'a' to 'z'
(keyCode >= 44 && keyCode <= 46) || // ',', '-', and '.'
keyCode == 32) && //space
keyCode != 13 ) //enter
{
errcode = 32;
}
break;
case "M":
case "Y":
if(field.readOnly == false &&
!((keyCode >= 48 && keyCode <= 57) || //'0' to '9'
(keyCode >= 43 && keyCode <= 46) || //'+', ',', '-', and '.'
keyCode == 32) && //space
keyCode != 13 ) //enter
{
errcode = 31;
}
break;
case "D":
if( (field.readOnly == false)
&& !( ( keyCode >= 48 && keyCode <= 57 ) //'0' to '9'
|| keyCode == 32 ) //space
&& keyCode != 13 )  //!enter
{
errcode = 30;
}
break;
case "F":
if(field.readOnly == false &&
!((keyCode >= 48 && keyCode <= 57) || //'0' to '9'
(keyCode >= 43 && keyCode <= 46) || //'+', ',', '-', and '.'
(keyCode == 69 || keyCode == 101 || keyCode == 32 ) ) && //'E' and 'e' and space
keyCode != 13 ) //enter
{
errcode = 62;
}
break;
default:
break;
}
if (errcode != 0)
{
addMessage( errcode,field);
window.event.returnValue = false;
}
}catch(anyexp){}
}
function getAllowedCharsForEdtcde(e, allow)
{
var edtcde = wfInfoDB[e.id].edtcde;
var edtcdeParm1, edtcdeParm2;
if (isDefined(edtcde) && edtcde.length > 0)
{
edtcdeParm1 = edtcde.charAt(0);
if (edtcdeParm1 >= "A" && edtcdeParm1 <= "D")
{
allow += "CR";
}
if (edtcdeParm1 == "W" || edtcdeParm1 == "Y")
{
allow += "/";
}
if (edtcde.length > 1)
{
edtcdeParm2 = edtcde.charAt(1);
allow += edtcdeParm2;
}
}
return allow;
}
function getAllowedCharsForEdtwrd(e, allow)
{
var edtwrd = wfInfoDB[e.id].edtwrd;
var c;
if (isDefined(edtwrd))
{
for (var i = 0; i < edtwrd.length; i++)
{
c = edtwrd.charAt(i);
if (c != "&" && allow.indexOf(c) < 0)
{
allow += c;
}
}
}
return allow;
}
function getAllowedCharsForShiftY(e)
{
var allow = "01234567989+-,. ";
allow = getAllowedCharsForEdtcde(e, allow);
allow = getAllowedCharsForEdtwrd(e, allow);
return allow;
}
function shiftY( e )
{
var val = e.value;
var pos = wfInfoDB[e.id].decpos;
var rc = 0;
var minusfound = false;
var dec = 0;
var bval = "";
var predec = "";
var postdec = "";
if (isDefined(e.valueWithoutEDTMSK))
{
val = e.valueWithoutEDTMSK;
}
if (wfInfoDB[e.id].shift == 'Y')
{
var allow = getAllowedCharsForShiftY(e);
for(var j = 0; j < val.length; j++)
{
c = val.charAt(j);
if( allow.indexOf( c ) < 0)
{
return 31;
}
}
}
try
{
bval = normalizeNumber(val);
if (bval.charAt(0) == "-")
{
bval = bval.substr(1);
minusfound = true;
}
}
catch (e)
{
rc = e;
}
if (rc == 0)
{
dec = bval.indexOf(decsep);
if( dec != -1 )
{
var dec_pos = bval.length - dec - 1;
if( dec_pos > pos )
{
rc = 33;
}
else
{
postdec = bval.substring( dec + 1, bval.length );
predec = bval.substring( 0, dec );
}
}
else // no decimals
{
predec = bval;
}
if ( predec.length > (wfInfoDB[e.id].datalength - pos) )
{
rc = 33;
}
postdec = leftJustify( postdec, pos, "0" );
predec = rightJustify( predec, wfInfoDB[e.id].datalength - pos, "0" );
}
if( rc == 0 )
{
var bval = predec + postdec;
if ( minusfound )
{
var bval = "-" + bval;
}
wfInfoDB[e.id].buffer = bval;
}
return rc;
}
function shiftX( e ) {
var allow = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ,.- ";
var c = "";
var rc = 0;
val = e.value;
for(var j = 0; j <= val.length; j++) {
c = val.substr(j, 1);
if( allow.indexOf( c ) == -1)
rc = 32;
}
if( rc == 0 ) {
if( !hasCheck( e, "LC" ) )
val = val.toUpperCase();
val = leftJustify( val, e.maxLength, " ");
wfInfoDB[e.id].buffer = val;
}
return rc;
}
function shiftS( e ) {
var allow = "0123456789 ";
var c = "";
var rc = 0;
var minusfound = false;
val = e.value;
val = trimLeadingSpaces( val );
val = trimTrailingSpaces( val );
if ( (val.charAt( 0 ) == "-" || val.charAt( 0 ) == "+" ) && ( val.charAt( val.length-1 ) == "-"
|| val.charAt( val.length-1 ) == "+" ))
return 64;
if ( val.charAt( 0 ) == "+" )
val = trimLeadingSpaces( val.substr( 1 ));
if ( val.charAt( val.length - 1 ) == "+" )
val = trimTrailingSpaces( val.substring( 0, val.length - 1 ));
for(var j = 0; j <= val.length; j++) {
c = val.substr(j, 1);
if ( c == "-" && ( j == 0 || j == val.length - 1)) {
minusfound = true;
continue;
}
if( allow.indexOf( c ) == -1)
rc = 5;
}
if( val.charAt( 0 ) == "-" )
val = val.substr( 1 );
if ( val.charAt( val.length - 1 ) == "-" )
val = trimTrailingSpaces( val.substring( 0, val.length - 1 ));
if ( val.length > wfInfoDB[e.id].datalength )
rc = 33;
else {
val = replaceSpaceWithZero( val );
val = rightJustify( val, wfInfoDB[e.id].datalength, "0");
if( minusfound )
val = "-" + val;
wfInfoDB[e.id].buffer = val;
}
return rc;
}
function shiftD( e ) {
var allow = '0123456789 ';
var c = "";
var rc = 0;
val = e.value;
for(var j = 0; j <= val.length; j++) {
c = val.substr(j, 1);
if( allow.indexOf( c ) == -1)
rc = 30;
}
if( rc == 0 ) {
if (wfInfoDB[e.id].ptype == "FT_ALPHA")
{
val = leftJustify(val, e.maxLength, " ");
}
else if (wfInfoDB[e.id].ptype == "FT_NUMERIC") {
val = trimTrailingSpaces( val );
val = replaceSpaceWithZero( val );
val = rightJustify(val, e.maxLength, "0");
}
wfInfoDB[e.id].buffer = val;
}
return rc;
}
function shiftM( e )
{  var val = e.value + '';
var re ;
if( (decsep == '.' || decsep == ',') && (thousandSep == '.' || thousandSep == ',') )
{  re = /^[-+ 0-9\.,]*$/ ;
}
else
{  re = new RegExp('^[-+ 0-9' + fixCharForRegex(decsep) + fixCharForRegex(thousandSep) + ']*$') ;
}
if(re.test(val))
{  val = leftJustify( val, e.maxLength, " ");
wfInfoDB[e.id].buffer = val;
return 0 ;
}
return 31;
}
function shiftT( e ) {
var val = e.value;
var rc = 0;
var sepIndex = -1;
var temp;
var wftmp = wfInfoDB[e.id];
var sep = wftmp.timsep;
var f = wftmp.timfmt;
if ( val.length == 0 )
return 0;
if (isVersionOlderThanSP1(e))
{
if ( f == "ISO" || f == "EUR" )
sep = ".";
else if ( f == "USA" || f == "JIS" )
sep = ":";
else if ( f == "HMS" && sep == null)
sep = ":";
} else {
if (sep == "JOB")
sep = jobTimSep;
}
if (f != "USA" ) {
if ( allBlanks( val ))
val = "00" + sep + "00" + sep + "00";
val = trimLeadingSpaces( val );
val = trimTrailingSpaces( val );
if ( allNumbers( val )) {
if ( isEven( val.length )){
if ( val.length == 2 )
val = val + sep + "00" + sep + "00";
else if ( val.length == 4 )
val = val.substr( 0, 2 ) + sep + val.substr( 2, 2 ) + sep + "00";
else if ( val.length == 6 )
val = val.substr( 0, 2 ) + sep + val.substr( 2, 2 ) + sep + val.substr( 4, 2);
else
return 48;
}
else
return 48;
}
else {
var allow = "0123456789" + sep;
for(var j = 0; j <= val.length; j++) {
c = val.substr(j, 1);
if( allow.indexOf( c ) == -1)
return 48;
}
temp = val;
sepIndex = temp.indexOf( sep );
if ( sepIndex < 3 && sepIndex > 0 ) {  // check datalen of hours
val = rightJustify( temp.substr( 0, sepIndex ), 2, "0" ) + sep;
temp = temp.substr( sepIndex + 1 ); // get the remainder of the time string
sepIndex = temp.indexOf( sep );
if ( sepIndex != -1 ) {
var tmp1 = temp.substr( 0, sepIndex );
if ( tmp1.length < 3 && tmp1.length > 0 ){
val += rightJustify( tmp1, 2, "0" ) + sep;
temp = temp.substr( sepIndex + 1 );
if ( temp.length < 3 && temp.length > 0)
val += rightJustify( temp, 2, "0" );
else
return 48;
}
else
return 48;
}
else {
if ( temp.length < 3 && temp.length > 0 ) {
val += rightJustify( temp, 2, "0") + sep + "00";
}
else
return 48;
}
}
else
return 48;
}
var hour = val.substr( 0, 2 );
var minute = val.substr( 3, 2 );
var second = val.substr( 6, 2 );
if ( isNaN( second ) || isNaN( minute ) || isNaN( hour ) )
rc = 48;
else {
if ( hour > 24 || hour < 0 || minute > 59 || second > 59 )
return 48;
else {
wfInfoDB[e.id].buffer = val;
}
}
}
else {
if ( allBlanks( val ))
val = "00" + sep + "00 AM";
if ( val.length < 6 )
return 48;
else {
sepIndex = val.indexOf( sep );
if ( sepIndex > 0 && sepIndex < 3 ) {
temp = val;
val = rightJustify( temp.substr( 0, sepIndex ), 2, "0" ) + sep;
temp = temp.substr( sepIndex + 1 );
var tmp1 = temp.substr( 0, 2 );
temp = temp.substr( 2 );
temp = temp.toUpperCase();
temp = trimLeadingSpaces( temp );
temp = trimTrailingSpaces( temp );
if ( temp == "AM" || temp == "PM" ) {
val += tmp1 + rightJustify( temp, 3, " " );
var hour = val.substr( 0, 2 );
var minute = val.substr( 3, 2 );
if ( isNaN( hour ) || isNaN( minute ))
return 48;
else {
if ( hour < 0 || hour > 12 || minute > 59 )
return 48;
else
wfInfoDB[e.id].buffer = val;
}
}
else
return 48;
}
else
return 48;
}
}
return rc;
}
function shiftL( e ) {
var val = e.value;
var rc = 0;
var wftmp = wfInfoDB[e.id];
var f = wftmp.datfmt;
var sep = wftmp.datsep;
var y = "";
var m = "";
var d = "";
var date = "";
var sepIndex = -1;
var isOlderVersion = isVersionOlderThanSP1(e);
if (sep == "JOB")
sep = jobDatSep;
if (f =="JOB")
f = jobDatFmt;
if ( val.length == 0 )
return 0;
if ( ! allBlanks( val )) {
val = trimLeadingSpaces( val );
val = trimTrailingSpaces( val );
}
switch( f ) {
case "MDY" :
case "DMY" :
case "YMD" :
if ( allBlanks( val ))
val = "000000";
if( val.length < 5 )
return 34;
if ( allNumbers( val )) {
val = rightJustify( val, 6, "0" );
val = val.substr( 0, 2 ) + sep + val.substr( 2, 2 ) + sep + val.substr( 4, 2 );
}
else {
if ( allowDateTime( val, sep )) {
sepIndex = val.indexOf( sep );
if ( sepIndex < 3 && sepIndex > 0 ) {
var temp = val.substr( sepIndex + 1 );
val = rightJustify( val.substr( 0, sepIndex ), 2 , "0" ) + sep;
sepIndex = temp.indexOf( sep );
if ( sepIndex > 0 && sepIndex < 3 ) {
var tmp1 = rightJustify( temp.substr( 0, sepIndex ), 2, "0" );
temp = rightJustify( temp.substr( sepIndex + 1), 2, "0" );
val += tmp1 + sep + temp;
}
else
return 34;  // wrong second separator
}
else
return 34;  // 1st sepIndex invalid
}
else
return 34;  // includes other chars not allowed
}
wfInfoDB[e.id].buffer = val;
mask = "nn" + sep + "nn" + sep + "nn";
rc = charsOK( val, mask );
if ( rc == 0 ) {
if ( f == "MDY" ){
m = val.substr( 0, 2 );
d = val.substr( 3, 2 );
y = val.substr( 6, 2 );
}
if ( f== "DMY" ) {
d = val.substr( 0, 2 );
m = val.substr( 3, 2 );
y = val.substr( 6, 2 );
}
if ( f == "YMD" ) {
y = val.substr( 0, 2 );
m = val.substr( 3, 2 );
d = val.substr( 6, 2 );
}
if( y < 40 )
y = "20" + y;
else
y = "19" + y;
date = y + m + d;
rc = validDate( date );
}
break;
case "JUL" :
if( val.length < 1 )
return 38;
if ( allBlanks( val ))
val = "01001";
if ( allNumbers( val )) {
if ( val.length > 5 )
return 38;
else {
val = rightJustify( val, 5, "0" );
y = val.substr( 0, 2 );
d = val.substr( 2 );
}
}
else {
if ( allowDateTime( val, sep )) {
sepIndex = val.indexOf( sep );
if ( sepIndex < 3 && sepIndex > 0 ) {
y = rightJustify( val.substr( 0, sepIndex ), 2 , "0" );
d = rightJustify( val.substr( sepIndex + 1 ), 3, "0" );
}
else
return 34;  // incorrect format
}
else
return 34;
}
val = y + sep + d;
mask = "nn" + sep + "nnn";
wfInfoDB[e.id].buffer = val;
rc = charsOK( val, mask);
var maxdays = 365;
if( rc == 0 ) {
if( y < 40 )
y = "20" + y;
else
y = "19" + y;
if( y % 400 == 0 )
maxdays = 366;
if( y % 100 != 0 && y % 4 == 0 )
maxdays = 366;
if( d < 1 || d > maxdays )
rc = 34;
}
break;
case "ISO" :
case "JIS" :
if (isVersionOlderThanSP1)
sep = "-";
if ( allBlanks( val ))
val = "0001" + sep + "01" + sep + "01";
if( val.length < 8)
return 38;
if ( allNumbers( val )) {
if ( val.length != 8 )
return 38;
else {
y = val.substr( 0, 4 );
m = val.substr( 4, 2 );
d = val.substr( 6, 2 );
}
}
else {
if ( allowDateTime( val, sep )) {
sepIndex = val.indexOf( sep );
if ( sepIndex == 4 ) {
y = val.substr( 0, 4 );
temp = val.substr( 5 );
sepIndex = temp.indexOf( sep );
if ( sepIndex > 0 && sepIndex < 3 ) {
m = rightJustify( temp.substr( 0, sepIndex ), 2, "0" );
d = rightJustify( temp.substr( sepIndex + 1 ), 2, "0" );
}
else
return 48;
}
else
return 38;  // the whole 4 digits for year must be given
}
else
return 48;  // if foreign chars in date
}
val = y + sep + m + sep + d;
mask = "nnnn" + sep + "nn" + sep + "nn";
wfInfoDB[e.id].buffer = val;
rc = charsOK( val, mask);
if( rc == 0 ) {
date= y + m + d;
rc = validDate( date );
}
break;
case "USA" :
case "EUR" :
if (isVersionOlderThanSP1)
{
if ( f == "USA" )
sep = "/";
else
sep = ".";
}
if ( allBlanks( val ))
val = "01" + sep + "01" + sep + "0001";
if ( allNumbers( val )) {
if ( val.length == 7 || val.length == 8 ) {
val = rightJustify( val, 8, "0" );
val = val.substr( 0, 2 ) + sep + val.substr( 2, 2 ) + sep + val.substr( 4 );
}
else
return 38;
}
else {
if ( val.length < 8 )
return 48;
else {
sepIndex = val.indexOf( sep );
if ( sepIndex > 0 && sepIndex < 3 ) {
temp = val;
val = rightJustify( val.substr( 0, sepIndex ), 2, "0" ) + sep;
temp = temp.substr( sepIndex + 1 );
sepIndex = temp.indexOf( sep );
if ( sepIndex > 0 && sepIndex < 3 ) {
val += rightJustify( temp.substr( 0, sepIndex ), 2, "0" ) + sep;
temp = temp.substr( sepIndex + 1 );
if ( temp.length != 4 )
return 38;
else
val += temp;
}
else
return 48;  // incorrect 2nd sep position
}
else
return 34;     // 1st separator in incorrect position
}
}
mask = "nn" + sep + "nn" + sep + "nnnn";
wfInfoDB[e.id].buffer = val;
rc = charsOK( val, mask);
if( rc == 0 ) {
if ( f == "USA" ) {
m = val.substr( 0, 2 );
d = val.substr( 3, 2 );
y = val.substr( 6 );
}
if ( f == "EUR" ) {
d = val.substr( 0, 2 );
m = val.substr( 3, 2 );
y = val.substr( 6 );
}
date = y + m + d;
rc = validDate( date );
}
break;
}
return rc;
}
function charsOK( val, mask ) {
var allow = "0123456789";
var rc = 0;
if( !isNaN( val ) )
return 0;
for(var j = 0; j < mask.length; j++) {
m = mask.substr( j, 1 );
c = val.substr( j, 1 );
if( ( m == 'n' ) && ( allow.indexOf( c ) == -1 ) )
rc = 34;
if( ( m != 'n' ) && ( c != m ) )
rc = 34;
}
return rc;
}
function validDate( ymd ) {
var days = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
var year = parseInt(trimLeadingZeroes(ymd.substr(0, 4)));
var month = parseInt(trimLeadingZeroes(ymd.substr(4, 2)));
var day = parseInt(trimLeadingZeroes(ymd.substr(6, 2)));
if( isNaN( year ) ) {
year = 0;
return 34;
}
if( isNaN( month ) )
month = 0;
if( isNaN( day ) )
day = 0;
if( ( month < 1 ) || ( month > 12 ) )
return 34;
if( year % 400 == 0 )
days[1] = 29;
if( ( year % 4 == 0 ) && ( year % 100 !=0 ) )
days[1] = 29;
var d = days[month -1]
if( ( day < 1 ) || ( day > d ) )
return 34;
return 0;
}
function allNumbers( val )
{  var re = /^[0-9]*$/ ; // string contains only char 0 to 9.
return re.test(val+'') ;
}
function allBlanks( val )
{  var re = /^[ ]*$/ ; // string contains only spaces
return re.test(val+'') ;
}
function validDecimal( num, dec ) {
var allow = "0123456789";
var decfound = false;
var rc = 0;
var d = 0;
for( var j = 0; j < num.length; j++ ) {
var c = num.charAt( j );
if( ( c == "-" && j == 0 ) || ( c == "+" && j == 0 ) )
continue;
if( c == decsep ) {
if( decfound ) {
rc = 33;
break;
}
decfound = true;
d = j;
continue;
}
var p = allow.indexOf( c );
if( p == - 1 ) {
rc = 33;
break;
}
}
if( rc == 0 ) {
if( decfound ) {
var pos = num.length - d -1;
if( pos > dec ) {
rc = 33;
}
}
}
return rc;
}
function isEven( val ) {
if (( val % 2 ) == 0 )
return true;
return false;
}
function allowDateTime( val, sep )
{  var re ;
if(sep == '/')
{  re = /^[\/0-9]*$/ ;
}
else if(sep == '-')
{  re = /^[-0-9]*$/ ; // '-' must be the first char in the set
}
else if(sep == '.')
{  re = /^[\.0-9]*$/ ;
}
else
{  re = new RegExp('^[' + fixCharForRegex(sep)+ '0-9]*$') ;
}
return re.test(val) ;
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function normalizeNumber(val)
{
var normNumber = ""; //The normalized version of the number.
var c;
var decfound = false;
var minusfound = false;
val = trimLeadingSpaces( val );
val = trimTrailingSpaces( val );
if ((val.charAt( 0 ) == "-" || val.charAt( 0 ) == "+" ) &&
( val.charAt( val.length-1 ) == "-" || val.charAt( val.length-1 ) == "+" ))
{
throw 64;
}
for( var j = 0; j < val.length; j++)
{
c = val.charAt(j);
var number = c >= "0" && c <= "9";
if( c >= '0' && c <= "9" || c == decsep || c == "-" || c == " ")
{
if (c == decsep && decfound == true)
{
throw 33;
}
if (( c == "-" && j != 0) && (c == "-" && j != val.length -1))
{
continue;
}
if (c == "-" && ( j == 0 || j == val.length - 1))
{
minusfound = true;
continue;
}
if( c == decsep )
{
decfound = true;
}
normNumber += c;
}
}
normNumber = trimTrailingSpaces( normNumber );
normNumber = replaceSpaceWithZero( normNumber );
normNumber = trimLeadingZeroes( normNumber );
if ( minusfound )
{
var normNumber = "-" + normNumber;
}
return normNumber;
}
function rightJustify( val, len, c ) {
if( val.length >= len ) {
return val;
}
var n = len - val.length;
for( var j = 0; j < n; j ++ ) {
val = c + val;
}
return val;
}
function leftJustify( val, len, c) {
if( val.length >= len ) {
return val;
}
var n = len - val.length;
for( var j = 0; j < n; j ++ ) {
val = val + c;
}
return val;
}
function shiftLeft( val, decplaces) {
var dec = val.indexOf(decsep);
if (dec == -1)
{
while (decplaces > 0)
{
val += 0;
decplaces--;
}
} else
{
postdec = val.substring( dec + 1, val.length );
val = val.substring( 0, dec );
var i = 0;
while (decplaces > 0)
{
if (i < postdec.length)
{
val += postdec.substr(i,1);
i++;
} else
{
val += 0;
}
decplaces--;
}
}
return val;
}
function fixCharForRegex( c )
{  var r = c ;
if(typeof(c).toLowerCase() != 'string' || c.length == 0)
{  r = ' ' ;
}
else if('.[]{}()+*?$^\\|'.indexOf(c.charAt(0)) != -1)
{  r = '\\' + c.charAt(0) ;
}
return r ;
}
function trimLeadingChars( val , c )
{  var re ;
if(c == ' ') { re = /^[ ]+/ ; }
else if(c == '0') { re = /^[0]+/ ; }
else { re = new RegExp('^[' + fixCharForRegex(c+'') + ']+') ; }
return (val+'').replace(re,'') ;
}
function trimTrailingChars( val, c )
{  var re ;
if(c == ' ') { re = /[ ]+$/ ; }
else if(c == '0') { re = /[0]+$/ ; }
else { re = new RegExp('[' + fixCharForRegex(c+'') + ']+$') ; }
return (val+'').replace(re,'') ;
}
function trimLeadingSpaces( val ){
return trimLeadingChars( val, " " );
}
function trimTrailingSpaces( val ){
return trimTrailingChars( val, " " );
}
function trimTrailingDBCSSpaces( val ){
return trimTrailingChars( val, "\u3000" );
}
function replaceSpaceWithZero( val )
{  var re = /[ ]/g ;
return (val+'').replace(re,'0');
}
function trimLeadingZeroes( val ){
return trimLeadingChars( val, "0" );
}
function trimTrailingZeroes( val ){
return trimTrailingChars( val, "0" );
}
function numField( e ) {
var wftmp = wfInfoDB[e.id];
if (wftmp.shift == 'S' || wftmp.shift == 'Y' || wftmp.shift == 'F'
|| ((wftmp.shift == 'N' || wftmp.shift == 'D') && wftmp.ptype == "FT_NUMERIC"))
{
return true;
} else
{
return false;
}
}
function hasCheck( e, a ) {
if (isUndefined(wfInfoDB[e.id]) || isUndefined(wfInfoDB[e.id].check))
{
return false;
}
var check = wfInfoDB[e.id].check;
var x = 0;
x = check.indexOf( a, 0 )
if( x == -1 )
return false;
else
return true;
}
function isBlank( s )
{  var re = /^[ \n\t]*$/ ;
return re.test(s+'') ;
}
function sizeOf( e )
{  var re = /[ \n\t]/g ;
return ((e.value + '').replace(re,'')).length ;
}
function respectMaxLengthBeforePaste() {
var f ;
if(arguments.length == 0) { f = window.event.srcElement ; }
else { f = arguments[0] ; }
if(f.maxLength)
{
window.event.returnValue = false;
}
}
function removeControlChars(s)
{  var re = /[\f\n\r\t]/g ;
return (s+'').replace(re,'') ;
}
function respectMaxLengthOnPaste(){
var e = window.event ;
var f ;
if(arguments.length == 0) { f = window.event.srcElement ; }
else { f = arguments[0] ; }
if(f.maxLength){
e.returnValue = false;
var maxLength = parseInt(f.maxLength);
var oTR = document.selection.createRange();
var iInsertLength = maxLength - f.value.length + oTR.text.length;
var sData = window.clipboardData.getData("Text");
sData = removeControlChars(sData);
sData = sData.substr(0,iInsertLength);
oTR.text = sData;
}
wfInfoDB[f.id].textSelected = false;
}
function toUpperCaseIgnoreDBCS (str)
{
var i;
var newStr = "";
var ch, charCode;
for (i = 0; i < str.length; i++)
{
ch = str.charAt(i);
charCode = str.charCodeAt(i);
if (!isDBCS(charCode,hostJobCCSID))
{
ch = ch.toUpperCase();
}
newStr = newStr + ch;
}
return newStr;
}
function focusAtIndex(field, index)
{
selectText(field, index, 0);
}
function selectText(field)
{
try
{
var index = field.selectIndex;
var width = field.selectWidth;
var textRange = field.createTextRange();
textRange.collapse();
if (index > field.value.length)
{
textRange.move("character", field.value.length);
}
else if (index > 0)
{
textRange.move("character", index);
}
if (index + width > field.value.length)
{
textRange.moveEnd("character", field.value.length - index);
}
else if (width > 0)
{
textRange.moveEnd("character", width);
}
textRange.select();
}
catch(any_exp) {} //in case the field parameter cannot be selected
}
function transformForQuotedHtml(s)
{
s = replaceSubstring(s, "&", "&amp;");
s = replaceSubstring(s, "\"", "&quot;");
return s;
}
function replaceSubstring(s, oldSubstring, newSubstring)
{
var len = oldSubstring.length;
var newstr = "";
var lastIndex = 0;
var thisIndex = s.indexOf(oldSubstring);
while (thisIndex >= 0)
{
newstr += s.substring(lastIndex, thisIndex);
newstr += newSubstring;
lastIndex = thisIndex + len;
thisIndex = s.indexOf(oldSubstring, lastIndex);
}
newstr += s.substring(lastIndex);
return newstr;
}
function addStatementToFunction(functionObj,jsStatement,beginOrEnd)
{  try
{  if(functionObj == null)
{  functionObj = new Function(jsStatement) ;
}
else
{  var s = functionObj.toString() ;  /* s is of the format 'function functionName() { existingJsStatements ; }' */
s = s.substring(s.indexOf('{')+1,s.lastIndexOf('}')) ; /* extract all the text inside the first '{' and last '}' */
if(beginOrEnd == true) { s = jsStatement + ' ' + s ; }
else  { s = s + ' ' + jsStatement  ; }
functionObj = new Function(s) ;
}
} catch(any_exptn) {}
return functionObj ;
}
function isValidDOMElement(obj)
{  try
{  return isValidRef(obj) && isValidRef(obj.id) &&
isValidRef(document.getElementById(obj.id));
} catch(any_exptn) { return false ;}
}
function isValidRef(obj)
{  return   obj != null && isDefined(obj);
}
function wfCancelEventAndBubble()
{  try
{  wfCancelEvent();
window.event.cancelBubble=true;
} catch(any_exp){}
}
function getAbsPosition(elem)
{  try
{  var left = elem.offsetLeft ;
var top = elem.offsetTop ;
var parentObj = elem.offsetParent ;
while(parentObj != null)
{  left += parentObj.offsetLeft + parentObj.clientLeft;
top += parentObj.offsetTop + parentObj.clientTop;
parentObj = parentObj.offsetParent;
}
return new PositionOnScreen(left,top);
}
catch(any_exp){}
return new PositionOnScreen() ;
}
function PositionOnScreen(left,top)
{  this.left = (isUndefined(left))? -1 : left;
this.top = (isUndefined(top))? -1 : top;
}
PositionOnScreen.prototype.left = -1;
PositionOnScreen.prototype.top = -1;
function wfEmptyFunction(){}
function gotoURL(thisRef,sURL)
{  try
{  sURL = trimTrailingSpaces(trimLeadingSpaces(sURL));
if(sURL.indexOf('javascript:') == 0)
{
sURL = sURL.replace(/^javascript:/,'');
window.execScript(sURL);
wfCancelEvent();
}
else
{
thisRef.onclick = wfEmptyFunction;
thisRef.href = sURL ;
thisRef.click();
}
}
catch(any_exp){}
}
function simulateClickOnParentElements(currentElem)
{  try
{  var parentObj = currentElem.parentElement ;
while(parentObj != null)
{  if(parentObj.onclick) { parentObj.click(); }
parentObj = parentObj.parentElement ;
}
return true ;
}
catch(any_exp){}
return false ;
}
function showWaitSign(elem)
{  try
{  elem.style.cursor = 'wait';
}
catch(any_exp){}
}
function isInputFieldElement(elem)
{  try
{  var re = /^input|textarea$/i ;
return re.test(elem.tagName+'') ;
}
catch(any_exp){ return false ;}
}
function isUndefined(elem)
{  try
{  return (typeof(elem) == 'undefined') ;
}
catch(any_exp){ return false ;}
}
function isDefined(elem)
{  try
{  return (typeof(elem) != 'undefined');
}
catch(any_exp){ return true ;}
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
function addMessage( id, e ) {
calculateErrorSelection(id, e);
var msg = getMessage(id,e) ;
var staticMsgln = document.getElementById(WF_MSGLINE_FIELD_ID);
staticMsgln.comboBoxField.addItem(msg,wfAppErrorMsgOpen,[staticMsgln,e,msg],true);
}
function getMessage(id, e) {
if( id >= 0 ) {
var m = msg[ id ];
var s = sub( m , e );
return s;
}
else {
return wfInfoDB[e.id].chkmsg;
}
}
function clearMessages() {
document.SCREEN.MSGLINE$$$cbField.comboBoxField.clear();
finalValidation = false;
}
function sub( m, e ) {
var r = "";
var p=0;
p = m.indexOf("&1");
if( p != -1) {
r=m.substr(0, p-1);
r +=  " " + e.sub1;
r += m.substr( p+2,  m.length);
m = r;
}
var s = "";
p = m.indexOf("&2");
if( p != -1) {
s=m.substr(0, p-1);
s += " " + e.sub2;
s += r.substr( p+2,  r.length);
m = s;
}
return m;
}
function setMDT( e ) {
var rc = 0;
wfInfoDB[e.id].mdt = true;
fieldHasChanged = true;
rc = validateField( e );
if( rc != 0 ) {
addMessage( rc, e );
selectText(e);
showmsg = false;
}
finalValidation = false;
}
function testIfChanged() {
var e ;
if(arguments.length == 0) { e = window.event.srcElement ; }
else { e = arguments[0] ; }
if (e.readOnly == false && !(javascriptLoaded === "undefined") && javascriptLoaded)
{
if (e.previousValue != e.value)
{
setMDT(e);
e.previousValue = e.value;
}
}
}
function submitForImmediateWrite()
{
parent.sender.document.open();
parent.sender.document.writeln("<FORM NAME=APPDATA TARGET='app' METHOD=POST ACTION='" + contextPath + servletPath + "'>");
parent.sender.document.writeln("</FORM>");
parent.sender.document.close();
parent.sender.document.APPDATA.submit();
bufferAlreadySentToHost = true;
}
function setUpMT()
{
try{
parent.sender.document.writeln("<html>");
parent.sender.document.writeln("<head>");
parent.sender.document.writeln("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
parent.sender.document.writeln("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/apparea/apparea.css\">");
parent.sender.document.writeln("</head>");
parent.sender.document.writeln("<BODY>");
parent.sender.document.writeln("<SPAN style=\"width: auto\" ID=\"oSpan1\" class=\"wf_default wf_field\"></SPAN><SPAN style=\"width: auto\" ID=\"oSpan\" class=\"wf_default wf_field\">&nbsp;</SPAN>");
parent.sender.document.writeln("<TABLE class=\"wf_font\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><TR><TD ID=\"topLine\"><INPUT class=\"wf_default wf_field\" SIZE=1></TD></TR><TR><TD ID='bottomLine'></TD></TR></TABLE>");
parent.sender.document.writeln("</BODY>");
parent.sender.document.writeln("</html>");
} catch(exp){document.location.reload();}
}
function findWWidth()
{
try{
var x = parent.sender.oSpan1.offsetLeft;
parent.sender.oSpan1.innerHTML="W";
var w_width = parent.sender.oSpan.offsetLeft - x;
if (isNaN(w_width))
{
w_width = -1;
}
return w_width;
}catch(ex){return -1;}
}
function findInputFieldHeight()
{
try
{
var height = parent.sender.bottomLine.offsetTop - parent.sender.topLine.offsetTop;
if (isNaN(height))
{
height = -1;
}
return height;
}catch(ex){return -1;}
}
function buildOutputBuffer( command, key, buffer )
{
var w_width = findWWidth();
var i_height = findInputFieldHeight();
parent.sender.document.open();
parent.sender.document.writeln("<html>");
parent.sender.document.writeln("<head>");
parent.sender.document.writeln("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
parent.sender.document.writeln("</head>");
parent.sender.document.writeln("<BODY>");
parent.sender.document.writeln("<FORM NAME=APPDATA TARGET='app' METHOD=POST ACTION='" + contextPath + servletPath + "'>");
parent.sender.document.writeln(buffer);
parent.sender.document.writeln("<INPUT NAME=" + key + "  SIZE=30 VALUE=" + command + "> ");
parent.sender.document.writeln("<INPUT NAME=CURSOR   SIZE=30 VALUE='" + document.SCREEN.CURSOR.value + "'> ");
parent.sender.document.writeln("<INPUT NAME=PAGEID   SIZE=30 VALUE='" + document.SCREEN.PAGEID.value + "'> ");
parent.sender.document.writeln("<INPUT NAME=INSERTMODE value='" + WFInsertMode + "'> ");
parent.sender.document.writeln("<INPUT NAME=ENCODING_TEST value='" + "\u3310" + "'> ");
if (w_width != -1)
{
parent.sender.document.writeln("<INPUT NAME=W_WIDTH  VALUE='" + w_width + "'>");
}
if (i_height != -1)
{
parent.sender.document.writeln("<INPUT NAME=INPUTFIELD_HEIGHT  VALUE='" + i_height + "'>");
}
parent.sender.document.writeln("</FORM>");
parent.sender.document.writeln("</BODY>");
parent.sender.document.writeln("</html>");
parent.sender.document.close();
parent.sender.document.APPDATA.submit();
bufferAlreadySentToHost = true;
showWaitSign(document.body);
}
function validateAndSubmit( aid , dontPg)
{
if( bufferAlreadySentToHost == true && window.event == null && document.activeElement != null)
{
var activeelem =  ((document.activeElement)+ " ");
if( activeelem.indexOf("javascript:") > -1)
{
if( projectJ2EELevel == "J2EE12" && projectRuntimeEnvironment == "struts")
{
if( bufferAlreadySentToHost == true)
return '<HTML><body onload="history.back();"> </body></HTML>';
}
else{
bufferAlreadySentToHost = false;
}
}
}
if (isDefined(isOverriden) && isOverriden == "true")
{
if (isDefined(uriOvr) && isDefined(targetFrameOvr))
{
if (targetFrameOvr == "*NEW")
{
window.open(uriOvr);
}
else if (targetFrameOvr == "*SAME" || targetFrameOvr == "")
{
window.location = uriOvr;
}
else
{
window.open(uriOvr,targetFrameOvr);
}
}
isOverriden = "false";
return false;
}
if (aid == "HELP")
{
if (HELPenabled)
{
if (callHelp)
{
invokeHelp(document.SCREEN.CURSOR.value);
}
else
{
validateAndBuildBuffer(aid, "AID");
}
return false;
}
}
else
{
if ( (!isValidRef(dontPg) || dontPg==true) && (aid == "PAGEUP" || aid == "PAGEDOWN"))
{
handlePageUpDnKeys(aid);
return false;
}
validateAndBuildBuffer(aid,"AID");
return false;
}
}
function validateAndBuildBuffer(cmd, key)
{
if (bufferAlreadySentToHost == false && !(javascriptLoaded === "undefined") && javascriptLoaded)
{
if (key == "AID")
{
var ca = "CA";
var clear = "CLEAR";
var help = "HELP";
var home = "HOME";
var print = "PRINT";
var isCAKey = true;
cmd = cmd.toUpperCase( );
if (cmd.indexOf(ca) == 0 || cmd == clear || cmd == help || cmd == home || cmd == print)
{
isCAKey = true;
} else
{
isCAKey = false;
}
} else {
isCAKey = false;
}
errorOnForm = false;
clearMessages();
validateForms(isCAKey);
if ( !errorOnForm )
{
var buffer = buildBufferForInputFields();
buildOutputBuffer( cmd, key, buffer);
} else { removeHighlightOnCmdKey() ; }
}
}
function buildBufferForInputFields()
{
var e = 0;
var f = document.forms.length;
var buffer = "";
for(var i = 0; i < f; i++)
{
e = document.forms[i].length;
for(var j = 0; j < e; j++)
{
var elem = document.forms[i].elements[j];
if(typeof(wfInfoDB[elem.id]) == 'undefined') { continue ; }
var elemFormat = wfInfoDB[elem.id].format;
if( wfInfoDB[elem.id].mdt == true)
{
var val = elem.value;
if (needToPad(elem))
val = wfInfoDB[elem.id].buffer;
if (wfInfoDB[elem.id].needsTransform == true)
{
val = transformForQuotedHtml(val);
}
var tag = "<INPUT NAME=" + elem.id + " VALUE=\"" + val + "\"> ";
buffer += tag;
}
}
}
return buffer;
}
function changePage( recordName, relRecordNumber )
{
var command = recordName + ":" + relRecordNumber;
validateAndBuildBuffer(command, "SFLRRN");
}
function extractLayerNumberFromElement(elem)
{  try
{  var re = /^l([0-9]+)_.*$/ ;
var arr = elem.id.match(re) ;
if(arr != null)
{  return arr[1] ;
}
}
catch(any_exp){}
return "";
}
function changeCursor(field,colOffset)
{
if (isUndefined(field))
{
field = window.event.srcElement;
}
try
{  var wffield = wfInfoDB[field.id] ;
var pos = 0;
if(isInputFieldElement(field) == true)
{   pos = parseInt(wffield.columnOffset + '');
pos = convertToDbcsOffset(pos,wffield.shift,field.value,field);
}
setCursorForNamedField(field.id,pos);
}
catch(any_exp){}
}
function convertToDbcsOffset(pos,shft,str,field)
{  try
{  if(shft != 'J' && shft != 'G' && shft != 'E' && shft != 'O')
{  return pos ;
}
if(pos == 0 || str.length == 0)
{  return 0 ;
}
if(shft == 'J')
{ return 1+ 2*(pos) + 2*Math.floor( (2*pos + 1)/(field.cols - 2) ) ;
}
if(shft == 'G')
{  return 2*pos;
}
if(shft == 'E')
{  return (isDBCS(str.charCodeAt(str.length-1),hostJobCCSID))? (2*pos+1 + 2*Math.floor((2*pos + 1)/(field.cols - 2)) ): pos ;
}
if(shft == 'O')
{  var patternStr = '',i  ;
for(i = 0; i < str.length ; i++)
{  if(i == pos) { patternStr += 'c'; }
patternStr += (isDBCS(str.charCodeAt(i),hostJobCCSID))? 'd' : 'a';
}
patternStr = patternStr.replace(/([d]+)/g,'s$1$1s').replace(/scs/g,'c');
pos = patternStr.indexOf('c');
pos = pos+ 2*Math.floor( (pos)/(field.cols - 2) )
return (pos != -1)? pos : patternStr.length-1 ;
}
}
catch(anyExp){}
return pos;
}
function setCursorForEDTMSK_DBCS(tagId,colOffset)
{
try
{
setFocusedRecord(tagId);
var field = document.SCREEN[tagId];
if(isValidRef(field.edtmskObj))
{  field.edtmskObj.setFocus();
}
if ( colOffset > 0)
{
var wffield = wfInfoDB[field.id];
var shft = wffield.shift;
if(shft == 'J' || shft == 'G' || shft == 'E' || shft == 'O')
{
positionCursorAtOffset(field,colOffset);
}
}
} catch(anyExp) { }
}
function setCursor(row, column, scopeQualifier, recordTagId)
{
if (isUndefined(scopeQualifier))
{
scopeQualifier = extractLayerNumberFromElement(window.event.srcElement);
}
if (isDefined(recordTagId))
{
setFocusedRecord(recordTagId);
}
else
{
if( window.event != null && isValidRef(window.event.srcElement))
{
setFocusedRecord(window.event.srcElement.id);
}
}
document.SCREEN.CURSOR.value = "num:" + row + ":" + column + ":" + scopeQualifier;
}
function setFocusedRecord(fld_id)
{
try
{
var recNameEndIndex = fld_id.indexOf('$');
if(recNameEndIndex == -1)
return false;
focusedRecord = (fld_id).substring(0,recNameEndIndex) ;
}
catch(exp){}
}
function setCursorForNamedField(tagId,columnOffset)
{
cursorPosTagId = tagId;
cursorPosColOffset = columnOffset;
document.SCREEN.CURSOR.value = "tag:" + tagId + ":" + columnOffset;
setFocusedRecord(tagId);
}
function validateForms(isCAKey)
{
errorElem= null;
var f = document.forms.length;
var rc = true;
finalValidation = true;
for(var i = 0; i < f; i++)
{
rc = validateForm(document.forms[i],isCAKey);
}
if( errorElem != null && errorElem.type != "hidden" )
{ //Select problem text on first error field.
selectText(errorElem);
}
return rc;
}
function validateForm( f,isCAKey ) {
var i = f.length;
var elems = f.elements;
var i, type;
var elem = "";
var rc=0;
var formok = true;
var j = elems.length;
for( i = 0; i < j; i++ ) {
elem = elems[ i ];
type = elem.type;
if(typeof(wfInfoDB[elem.id]) != "undefined" && typeof(wfInfoDB[elem.id].validate) != "undefined" && wfInfoDB[elem.id].validate == "TRUE")
{
if (elem.value != elem.previousValue)
{
elem.previousValue = elem.value;
fieldHasChanged = true;
wfInfoDB[elem.id].mdt = true;
}
else if (wfInfoDB[elem.id].mdt == true)
{
fieldHasChanged = true;
if(isCAKey)
{
wfInfoDB[elem.id].buffer = elem.value;
}
}
}
}
if(isCAKey != true)
{
for( i = 0; i < j; i++ ) {
elem = elems[ i ];
type = elem.type;
if(typeof(wfInfoDB[elem.id]) != "undefined" && typeof(wfInfoDB[elem.id].validate) != "undefined" && wfInfoDB[elem.id].validate == "TRUE") {
if( wfInfoDB[elem.id].mdt == true  || (fieldHasChanged && hasCheck(elem, "ME") && elem.readOnly == false))
{
rc = validateField( elem );
if( rc != 0 )
{
addMessage( rc, elem );
formok = false;
}
}
}
}
}
return formok;
}
function validateField( e ) {
var rc = 0;
var wftmp = wfInfoDB[e.id];
wftmp.errorid=0;
wftmp.buffer = e.value;
rc = checkShift( e );
if( ( rc == 0 ) && hasCheck( e, "RZ" ) ) {
rc = checkrz( e );
}
if( ( rc == 0 ) && hasCheck( e, "RB" ) ) {
rc = checkrb( e );
}
if( ( rc == 0 ) && hasCheck( e, "MF" ) ) {
rc = checkmf( e );
}
if( ( rc == 0 ) && hasCheck( e, "ME" ) ) {
rc = checkme( e );
}
if( !( ( rc == 0 ) && blankOK( e ) ) ) {
if( ( rc == 0 ) && hasCheck( e, "VN" ) ) {
rc = checkvn( e );
}
if( ( rc == 0 ) && hasCheck( e, "VE" ) ) {
rc = checkvne( e );
}
if( ( rc == 0 ) && hasCheck( e, "M10" ) ) {
rc = mod10( e );
}
if( ( rc == 0 ) && hasCheck( e, "M11" ) ) {
rc = mod11( e );
}
if( ( rc == 0 ) && ( wftmp.comp != null ) && ( wftmp.comp != "" ) ) {
rc = checkComp( e );
}
if( ( rc == 0 ) && ( wftmp.range != null ) && ( wftmp.range != "" ) ) {
rc = checkRange( e );
}
if( ( rc == 0 ) && ( wftmp.values != null ) && ( wftmp.values != "" ) ) {
rc = checkValues( e );
}
}
if( ( rc == 0 ) && ( wftmp.valnum == "TRUE" ) ) {
rc=checkValNum( e );
}
wftmp.errorid = rc;
if( rc != 0 ) {
errorOnForm = true;
if( errorElem == null ) {
errorElem = e;
}
}
e.errcode = rc;
return rc;
}
function calculateErrorSelection(errorCode, field)
{
try{
if (errorCode == 65 || errorCode == 66 || errorCode == 67 || errorCode == 68)
{
field.selectIndex = field.sub2 - 1;
field.selectWidth = 1;
}
else if (errorCode == 69)
{
field.selectIndex = field.value.length - (field.sub1.length - 2);
field.selectWidth = field.sub1.length;
}
else
{
var pos = CaretPositionFinder.getCaretIndex(field);
field.selectIndex = pos;
field.selectWidth = 0; // 1 to select data
}
}catch(anyexp){}
}
function setFocusForTagID(tagId,colOffset)
{
try
{
setCursorForNamedField(tagId,colOffset);
var field = document.SCREEN[tagId];
if (typeof(field) != "undefined")
{
if ( field.type != "hidden" ){
field.focus();
}
else{
findFirstInputFieldAndFocus();
}
if (colOffset > 0 && !isValidRef(field.edtmskObj) )
positionCursorAtOffset(field,colOffset);
}
else
{
field = window[tagId];
if (typeof(field) != "undefined")  //If the field is an output-only field
{
field.click(); // This line is only effective for text constants with DSPATR(PC). For normal output fields this does not do anything because click is not associated with anything yet.
}
}
setFocusedRecord(tagId);
}
catch(any_exp) {}
}
function findFirstInputFieldAndFocus(){
var elem;
var reType = /^(text)|(textarea)|(password)$/i ;
var reId = /\$\$cbField$/;
var f = document.forms.length;
for(var j = 0; j < f; j++)
{
var e = document.forms[j].elements.length;
for(var i = 0; i < e; i++)
{
elem = document.forms[j].elements[i];
if( reType.test(elem.type) == true && reId.test(elem.id) != true &&
elem.tabIndex >= 0 && elem.style.visibility != 'hidden' &&
elem.style.display != 'none' )
{
elem.focus();
return true;
}
}
}
return false;
}
function SingleLineSubfileScrollDown(cbField, canPageDown)
{
if (!cbField.comboBoxField.nextItem() && canPageDown)
{
validateAndSubmit( 'PAGEDOWN');
}
}
function SingleLineSubfileScrollUp(cbField, canPageUp)
{
if (!cbField.comboBoxField.prevItem() && canPageUp)
{
validateAndSubmit('PAGEUP');
}
}
function handleDSPATR_SP()
{
var field ;
if(arguments.length == 0) { field = window.event.srcElement ; }
else { field = arguments[0] ; }
var isCHECKER = (wfInfoDB[field.id].onKeyUp != null);
if (wfInfoDB[field.id].mdt == false)
{
field.value = '>' + field.value.substr(1);
field.previousValue = field.value;
wfInfoDB[field.id].mdt = true;
if (isCHECKER)
{
validateAndSubmit("SLPAUTOENTER");
}
} else
{
field.value = '?' + field.value.substr(1);
field.previousValue = field.value;
wfInfoDB[field.id].mdt = false;
}
}
function getVersionElem(fieldName)
{  try
{  var arr = fieldName.match(/^([^\$]+)\$/) ;
if(arr != null)
{  return document.getElementById(arr[1] + "_version");
}
}
catch(any_exp){}
return null ;
}
function needToPad(e)
{
var shift = wfInfoDB[e.id].shift;
if (shift == 'E' || shift == 'J' || shift == 'G' || shift == 'O')
return false;
return isVersionOlderThanSP1(e);
}
function isVersionOlderThanSP1(e)
{
var id = e.id;
var version = getVersionElem(id) ;
if ( version == null || typeof(version) == "undefined" || version.value < 501010100)
return true;
return false;
}
/*
*(c) Copyright International Business Machines Corporation 1999-2002. All rights reserved
*/
function checkValues( elem ) {
var value = elem.value;
if( !hasCheck( elem, "LC" ) )
{
value = value.toUpperCase();
}
if( numField( elem ) )
{
value = normalizeNumber(value);
}
value = trimTrailingSpaces(value);
value = trimTrailingDBCSSpaces(value);
var values = wfInfoDB[elem.id].values;
var y = 0;
var temp;
var found = false;
var x = values.indexOf( ';' );
while( x != -1 ) {
temp = values.substring(y, x);
temp = trimTrailingSpaces (temp);
temp = trimTrailingDBCSSpaces (temp);
if( !hasCheck( elem, "LC" ) )
temp = temp.toUpperCase();
if( numField( elem ) ) {
temp = normalizeNumber(temp);
}
y = x + 1;
if( value == temp ) {
found = true;
break;
}
x = values.indexOf( ';', y );
}
if( !found ) {
if( wfInfoDB[elem.id].chkmsg != "" )
return -1
else
return 9;
}
else
return 0;
}
function checkRange( elem ) {
if( !numField( elem ) )
{
return 0;
} else
{
var range = wfInfoDB[elem.id].range;
var x = range.indexOf( ';' );
var value = wfInfoDB[elem.id].buffer;
var lo = range.substring( 0, x );
var hi = range.substring( x + 1, range.length );
elem.sub1 = lo;
elem.sub2 = hi;
if( !hasCheck( elem, "LC" ) )
value = value.toUpperCase();
var wftmp = wfInfoDB[elem.id];
if (wftmp.shift == 'Y' || wftmp.shift == 'S'
|| (wftmp.shift == 'N' && wftmp.ptype == "FT_NUMERIC"))
{
value = replaceSpaceWithZero(value);
}
if( numField( elem ) ) {
if (wfInfoDB[elem.id].shift != 'D')
{
value = replaceSpaceWithZero(value);
}
value = parseFloat(value);
lo = parseFloat(shiftLeft(lo, wfInfoDB[elem.id].decpos));
hi = parseFloat(shiftLeft(hi, wfInfoDB[elem.id].decpos))
}
if( value < lo || value > hi )
if( wfInfoDB[elem.id].chkmsg != "" )
return -1;
else
return 1;
else
return 0;
}
}
function fixStringForComp(s)
{  var str = s + '' ;
if(str == "")
{  str = '" "' ;
}
else if(str.search(/[^0-9]/g) != -1) /* if string is non-numeric then */
{  str = str.replace(/["]/g,"\\\"") ;
str = str.replace(/[']/g,"\\\'") ;
str = '"' + str + '"' ;
}
return str ;
}
function checkComp( elem ) {
var values = wfInfoDB[elem.id].values;
var COMP = "EQ NE LT NL GT NG LE GE";
var CMP  = "== != <  !< >  !> <= >=";
var tmpCmp;
var expr;
var compareValue = "";
var comparison = wfInfoDB[elem.id].comp;
var elemValue = wfInfoDB[elem.id].buffer;
var tmpComp = comparison.substr( 0, 2 );
var y = COMP.indexOf( tmpComp );
var m = y / 3 + 10;
compareValue = comparison.substr(3, comparison.length - 2 );    // get the comparison value
elem.sub1 = compareValue;
var wftmp = wfInfoDB[elem.id] ;
if (wftmp.shift == 'Y' || wftmp.shift == 'S'
|| (wftmp.shift == 'N' && wftmp.ptype == "FT_NUMERIC"))
{
elemValue = replaceSpaceWithZero(elemValue);
} else
{
elemValue = trimTrailingSpaces(elemValue);
elemValue = trimTrailingDBCSSpaces(elemValue);
}
compareValue = trimTrailingSpaces(compareValue);
compareValue = trimTrailingDBCSSpaces(compareValue);
if( !numField( elem ) && !hasCheck( elem, "LC" )) {
elemValue = '"' + elemValue.toUpperCase() + '"';
compareValue = '"' + compareValue.toUpperCase() + '"';
}
if (numField(elem))
{
elemValue = parseFloat(elemValue);
compareValue = parseFloat(shiftLeft(compareValue, wfInfoDB[elem.id].decpos));
}
if( tmpComp != "NL" && tmpComp != "NG" ) {
tmpCmp = CMP.substr( y, 2 );                                // get JavaScript comparison operator
var b ;
try
{  if((elemValue+'').indexOf('//') != -1 || (compareValue+'').indexOf('//') != -1)
{  expr = '"' + elemValue + '" ' + tmpCmp + ' "' + compareValue + '"' ;
}
else { expr = elemValue + ' ' + tmpCmp + ' ' + compareValue ; }
b = eval( expr ) ;
if(b == null) throw "incorrect expr" ;
}
catch(any_exception) /* input data may be all blanks or may have symbols (like {+,",'} etc) */
{  var elemValuetmp = fixStringForComp(elemValue) ;
var compareValuetmp = fixStringForComp(compareValue) ;
var exprtmp = elemValuetmp + ' ' + tmpCmp + ' ' + compareValuetmp ;
b = eval( exprtmp ) ;
}
if( b != true ) {
if( wfInfoDB[elem.id].chkmsg != "" )
return -1;
else
return m;
}
else
return 0;
}
else {
if( tmpComp == "NL" ) {
if( elemValue < compareValue ) {
if( wfInfoDB[elem.id].chkmsg != "" )
return -1;
else
return 13;
}
else
return 0;
}
else {
if( elemValue > compareValue ) {
if( wfInfoDB[elem.id].chkmsg != "" )
return -1;
else
return 15;
}
else
return 0;
}
}
}
function checkValNum (elem) {
var length = elem.value.length;
var value = elem.value;
var firstDigitFound = false;
var lastDigitFound = false;
var allowDigit = "0123456789.";
var allow = "+-";
var i = 0;
while (i < length)
{
if (lastDigitFound && value.charAt(i) != ' ')
{
return 63;
}
else
{
if (firstDigitFound && allow.indexOf(value.charAt(i)) >= 0 && i < (length - 1) && value.charAt(i+1) != ' ')
{
return 63;
}
if (!lastDigitFound && firstDigitFound && value.charAt(i) == ' ')
{
lastDigitFound=true;
}
if (!firstDigitFound && (allowDigit.indexOf(value.charAt(i)) >= 0 || allow.indexOf(value.charAt(i)) >= 0))
{
firstDigitFound=true;
}
}
i++;
}
return 0;
}
/*
*(c) Copyright International Business Machines Corporation 1999-2003. All rights reserved
*/
var clickleft, clicktop;
var drag=false;
var dragObj=null;
var dragIndex;
var move=false;
var zIndex=255;
function positionLayer(table, layer, row, col, maxRow, maxCol)
{
try
{	var offsetLeftToBrowser = SCREEN.offsetLeft;
var offsetTopToBrowser = SCREEN.offsetTop;
var offsetObj = SCREEN.offsetParent;
while (offsetObj != null)
{
offsetLeftToBrowser += offsetObj.offsetLeft;
offsetTopToBrowser += offsetObj.offsetTop;
offsetObj = offsetObj.offsetParent;
}
var rowTag = document.getElementById("l" + layer + "r" + row);
var colTag = document.getElementById("l" + layer + "c" + col);
if (isValidRef(rowTag))
{
table.style.top = rowTag.offsetTop + offsetTopToBrowser;
}
else
{
table.style.top = offsetTopToBrowser;
}
if (isValidRef(colTag))
{
table.style.left = colTag.offsetLeft + offsetLeftToBrowser;
}
else
{
table.style.left = offsetLeftToBrowser;
}
table.style.visibility = "visible";
}
catch(any_exp){}
}
function winInit()
{
window.document.onmousemove = mouseMove
window.document.onmousedown = mouseDown
window.document.onmouseup = mouseUp
window.document.ondragstart = mouseStop
}
function mouseDown()
{
if (drag)
{
clickleft = window.event.x - parseInt(dragObj.style.left)
clicktop = window.event.y - parseInt(dragObj.style.top)
dragIndex = dragObj.style.zIndex;
dragObj.style.zIndex = 255;
dragObj.className = "wdwShadowDef";
move = true
}
}
function mouseUp()
{
if (move)
{
dragObj.style.zIndex = dragIndex;
dragObj.className = "wdwDef";
move = false
}
}
function mouseStop()
{
window.event.returnValue = false
}
function mouseMove()
{
if (move)
{
dragObj.style.left = window.event.x - clickleft
dragObj.style.top = window.event.y - clicktop
}
}
function mouseOver(obj)
{
if (!drag)
{
dragObj = obj;
drag = true;
}
}
function mouseOut()
{
if (drag && !move)
{
drag = false;
}
}
function posScb(idSubStr)
{  try
{
var scrollbarTableElem = document.getElementById(idSubStr + "$scrollbarTable");
var cellBelowScrollbarElem = document.getElementById(idSubStr + "$cellBelowScrollbar");
var scrollbarCellElem = document.getElementById(idSubStr + "$scrollbarCell");
if ( isValidRef(scrollbarTableElem) ){
scrollbarTableElem.style.height = cellBelowScrollbarElem.offsetTop - scrollbarCellElem.offsetTop;
scrollbarTableElem.style.visibility = "visible";
sflPgUpDnObj.reg(idSubStr);
}else{
sflPgUpDnObj.reg(idSubStr,pgUpEnabled,pgDnEnabled);
}
}
catch(any_expn){}
}
