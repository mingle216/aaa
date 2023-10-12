String.prototype.getBytesLength = function() {   
    var totalLength = 0;     
    var charCode;  
    for (var i = 0; i < this.length; i++) {  
        charCode = this.charCodeAt(i);  
        if (charCode < 0x007f)  {     
            totalLength++;     
        } else if ((0x0080 <= charCode) && (charCode <= 0x07ff))  {     
            totalLength += 2;     
        } else if ((0x0800 <= charCode) && (charCode <= 0xffff))  {     
            totalLength += 3;   
        } else{  
            totalLength += 4;   
        }          
    }  
    return totalLength;   
} 
export function cutStr(str,len,symbol='...') {
    /*<summary>获得字符串实际长度，中文2，英文1</summary>
    <param name="str">要获得长度的字符串</param>*/
    if (str && typeof str === 'string') {
        let realLength = 0,
            charCode = -1;
        let strArr=toArray(str);
        let newStr = ''
        for (var i = 0; i < strArr.length; i++) {
            charCode = strArr[i];
            newStr += charCode
            if(charCode.getBytesLength()==1){
                realLength += 1
            }
            if(charCode.getBytesLength()==3){
                realLength += 2
            }
            if(charCode.getBytesLength()==6){
                realLength += 3
            }   
            if ( realLength-1 > len ) {
                return {newStr: newStr + symbol,isSymbol:true}
            }
        }
        
        return {newStr:str,isSymbol:false} ;
    }
}
export function getStrLen(str) {
    /*<summary>获得字符串实际长度，中文2，英文1</summary>
    <param name="str">要获得长度的字符串</param>*/
    if (str && typeof str == 'string') {
        var realLength = 0,
            charCode = -1;
        let strArr=toArray(str);
        for (var i = 0; i < strArr.length; i++) {
            charCode = strArr[i];
            if(charCode.getBytesLength()==1){
                realLength += 1
            }
            if(charCode.getBytesLength()==3){
                realLength += 2
            }
            if(charCode.getBytesLength()==6){
                realLength += 3
            }   
        }
        return realLength;
    }
}
let rsAstralRange = '\\ud800-\\udfff',
    rsZWJ = '\\u200d',
    rsVarRange = '\\ufe0e\\ufe0f',
    rsComboMarksRange = '\\u0300-\\u036f',
    reComboHalfMarksRange = '\\ufe20-\\ufe2f',
    rsComboSymbolsRange = '\\u20d0-\\u20ff',
    rsComboRange = rsComboMarksRange + reComboHalfMarksRange + rsComboSymbolsRange;
let reHasUnicode = RegExp('[' + rsZWJ + rsAstralRange + rsComboRange + rsVarRange + ']');

let rsFitz = '\\ud83c[\\udffb-\\udfff]',
    rsOptVar = '[' + rsVarRange + ']?',
    rsCombo = '[' + rsComboRange + ']',
    rsModifier = '(?:' + rsCombo + '|' + rsFitz + ')',
    reOptMod = rsModifier + '?',
    rsAstral = '[' + rsAstralRange + ']',
    rsNonAstral = '[^' + rsAstralRange + ']',
    rsRegional = '(?:\\ud83c[\\udde6-\\uddff]){2}',
    rsSurrPair = '[\\ud800-\\udbff][\\udc00-\\udfff]',
    rsOptJoin = '(?:' + rsZWJ + '(?:' + [rsNonAstral, rsRegional, rsSurrPair].join('|') + ')' + rsOptVar + reOptMod + ')*',
    rsSeq = rsOptVar + reOptMod + rsOptJoin,
    rsSymbol = '(?:' + [rsNonAstral + rsCombo + '?', rsCombo, rsRegional, rsSurrPair, rsAstral].join('|') + ')';
let reUnicode = RegExp(rsFitz + '(?=' + rsFitz + ')|' + rsSymbol + rsSeq, 'g');

export function toArray (val) { // 字符串转成数组
    return hasUnicode(val)
        ? unicodeToArray(val)
        : asciiToArray(val);
}

export function hasUnicode (val) {//是否包含Unicode
    return reHasUnicode.test(val);
}

export function unicodeToArray (val) {//Unicode转数组
    return val.match(reUnicode) || [];
}

export function asciiToArray (val) {//ascii转数组
    return val.split('');
}
export function charType (char) {//字符类型判断
    var pattern = new RegExp("[\u4E00-\u9FA5]+");//中文判断
    var pattern2 = new RegExp("[A-Za-z]+");//英文判断
    var pattern3 = new RegExp("[0-9]+");//数字判断
    if(pattern.test(char)){
        return "zh";
    }
    if(pattern2.test(char)){
        return "en";
    }
    if(pattern3.test(char)){
        return "num";
    }
    return "symbol"
}