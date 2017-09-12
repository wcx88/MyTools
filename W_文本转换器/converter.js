var CODER = {
     //ASCII -> Unicode转换
     AsciiToUnicode : function (source) {
         if (source == '') {
             return '';
         }
         var result = '';
         for (var i = 0; i < source.length; i++) {
             result += '&#' + source.charCodeAt(i) + ';';
         }
         return result;
     },
     //Unicode -> ASCII转换
     UnicodeToAscii : function (source) {
         var code = source.match(/&#(\d+);/g);
         if (code == null) {
             return '';
         }
         var result = '';
         for (var i = 0; i < code.length; i++) {
             result += String.fromCharCode(code[i].replace(/[&#;]/g, ''));
         }
         return result;
     },
    //  Unicode编码
    strToUxxxx: function(str) {
        var a = [];
        var i = 0;
        for (; i < str.length;) {
            a[i] = ("00" + str.charCodeAt(i++).toString(16)).slice(- 4);
        }
        return ("\\u" + a.join("\\u"));
    },
    // Unicode解码
    UxxxxToStr: function(str) {
        return unescape(str.replace(/\\/g, "%")).replace(/%u00d/ig, "\r").replace(/%u00a/ig, "\n");
    }
};
String.prototype.trim = function(){
    // 用正则表达式将前后空格
    // 用空字符串替代。
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
String.prototype.matchCount = function(rgExp){
    var arr = this.match(rgExp);
    return (null == arr) ? 0 : arr.length;
};

var operate = function(objId, action) {
    var obj = document.getElementById(objId);
    switch(action) {
        case "clear":
            obj.value = "";
            break;
        case "copy":
            clipboardData.setData("text", obj.value);
            break;
        case "cut":
            clipboardData.setData("text", obj.value);
            obj.value = "";
            break;
        case "paste":
            obj.value = clipboardData.getData("text");
            break;
        case "quick":
            obj.value = clipboardData.getData("text");

            if ("id_textarea_left" == objId) {
                document.getElementById("btnToRight").click();
                clipboardData.setData("text", document.getElementById("id_textarea_right").value);
            } else {
                document.getElementById("btnToLeft").click();
                clipboardData.setData("text", document.getElementById("id_textarea_left").value);
            }

            break;
        default:
            break;
    }
};
var swapText = function() {
    var objTextAreaLeft = document.getElementById("id_textarea_left");
    var objTextAreaRight = document.getElementById("id_textarea_right");
    var sTemp = objTextAreaLeft.value;
    objTextAreaLeft.value = objTextAreaRight.value;
    objTextAreaRight.value = sTemp;
    
};
var setScrol = function (objId, scrollTop, scrollLeft) {
    var obj = document.getElementById(objId);
    obj.scrollTop = scrollTop;
    obj.scrollLeft = scrollLeft;
};
var synchronizeScroll = function(srcObj) {
    var objId = "id_textarea_right";
    if (objId == srcObj.id) {
        objId = "id_textarea_left";
    }
    setScrol(objId, srcObj.scrollTop, srcObj.scrollLeft);
};
var showCtrl = function(obj) {
    var hideName = "right";
    var showName = "left";
    if ("id_btn_left" == obj.id) {
        hideName = "left";
        showName = "right";
    }

    if("<" == obj.value) {
        obj.value = ">";
    } else {
        obj.value = "<";
    }
    if (obj.title.indexOf("隐藏") >= 0) {
        obj.title = "显示" + obj.showTitle;
    } else {
        obj.title = "隐藏" + obj.showTitle;
    }
    obj.blur();

    if ("none" != document.getElementById("id_th_" + hideName).style.display) {
        document.getElementById("id_th_" + hideName).style.display = "none";
        document.getElementById("id_td_" + hideName).style.display = "none";

        document.getElementById("id_th_" + showName).style.width = "100%";
        document.getElementById("id_td_" + showName).style.width = "100%";

        document.getElementById("id_btn_" + showName).style.display = "none";
    } else {
        document.getElementById("id_th_" + hideName).style.display = "";
        document.getElementById("id_td_" + hideName).style.display = "";

        document.getElementById("id_th_" + showName).style.width = "50%";
        document.getElementById("id_td_" + showName).style.width = "50%";

        document.getElementById("id_btn_" + showName).style.display = "";
    }
};

var WshShell;
var sendKey = function(keys, delay) {
    if (!WshShell) WshShell = new ActiveXObject("WScript.Shell");
    for(var n in keys) {
        if (/([\D])|(^[ ]*$)|(^[0]+$)/.test(delay[n])) {
            WshShell.SendKeys(keys[n]);
        } else {
            setTimeout(function(){WshShell.SendKeys(keys[n]);}, delay[n]);
        }
    }
};
var doQuery = function (str) {
    if ("" == str.trim()) {
        return;
    }
    clipboardData.setData("text", str);
    sendKey(["^f", "^v"], [0, 1]);

    // ENTER {ENTER} 或 ~
    // 键 特殊字符
    // SHIFT +
    // CTRL ^
    // ALT %
};
var getSelectionText = function () {
    if (window.getSelection) {
        return window.getSelection().toString();
    } else if (document.selection && document.selection.createRange) {
        return document.selection.createRange().text;
    }
    return "";
};
var getRadioBoxValue = function (radioName) {
    var obj = document.getElementsByName(radioName);             //这个是以标签的name来取控件
         for(var i = 0; i < obj.length; i++) {
          if(obj[i].checked) {
              return obj[i].value;
          }
      }
     return "undefined";
};

var unWrapJS_Line = function(txtLine, varName) {
    if (!txtLine || !varName) {
        return "";
    }
     if (0 == txtLine.trim().indexOf("//")) {
        return txtLine;
    }

    if (txtLine.indexOf(varName) < 0 ) {
        return "";
    }
    var arrTemp, strTemp;
    arrTemp = txtLine.split(varName);
    if (!arrTemp[1]) {
        return "";
    }
    strTemp = arrTemp[1].trim();

    if (!(/(^=[ ]*")|(^=[ ]*')|(^\+[ ]*=[ ]*')|(^\+[ ]*=[ ]*")/.test(strTemp))) {
        return "";
    }

    var point1_1=-1, point1_2=-1;
    point1_1 = txtLine.indexOf('"');
    point1_2 = txtLine.indexOf("'");

    var C_LONG_MAX = 2147483647;
    if (point1_1 <= 0) point1_1 = C_LONG_MAX;
    if (point1_2 <= 0) point1_2 = C_LONG_MAX;

    if (C_LONG_MAX == point1_1 && C_LONG_MAX == point1_2) {
        return txtLine;
    }

    var cutPoint1 = 0, cutPoint2 = 0;
    var intQuot = 0;               // 引号类型：1 -单引号，2 -双引号

    if (point1_1 < point1_2) {
        cutPoint1 = point1_1;
        cutPoint2 = txtLine.lastIndexOf('"');
        intQuot = 2;
    } else {
        cutPoint1 = point1_2;
        cutPoint2 =  txtLine.lastIndexOf("'");
        intQuot = 1;
    }

    if (cutPoint1 <= txtLine.length && cutPoint2 <= txtLine.length && cutPoint2 - cutPoint1 > 0) {
        txtLine = txtLine.substr(cutPoint1 + 1, cutPoint2 - cutPoint1 - 1)
    }

    if (1 == intQuot) {
        txtLine = txtLine.replace(/\\'/g, "'")
    }
    else if (2 == intQuot) {
        txtLine = txtLine.replace(/\\"/g, '"')
    }
    var len = txtLine.length - 2;
    if(txtLine.indexOf("\\n") == len) {
        txtLine = txtLine.substr(0, len);
    }
    return txtLine;
};
var unWrapSqlLine = function(wrapedSql) {

    var cutPoint1 = 0, cutPoint2 = 0;
    cutPoint1 = wrapedSql.indexOf('"');
    cutPoint2 = wrapedSql.lastIndexOf('"');
    if (cutPoint1 <= wrapedSql.length && cutPoint2 <= wrapedSql.length && cutPoint2 - cutPoint1 > 0) {
        wrapedSql = wrapedSql.substr(cutPoint1 + 1, cutPoint2 - cutPoint1 - 1)
    }
    return wrapedSql;
};
var wrapSql = function(txt) {
    if (!txt.trim()) {
        return "";
    }

    var txtLines;
    if (txt.indexOf("\r\n") >= 0) {
        txtLines = txt.split("\r\n");
    } else {
        txtLines = txt.split("\n");
    }

    // 缩进
    var indentation = "";
    for (var i = 0; i < 12; i++) {
        indentation += " ";
    }
    var sql = "StringBuilder sbSQL = new StringBuilder();\n";
    sql += "    sbSQL";

    for (var i in txtLines) {
        if (i > 0) {
            sql += indentation;
        }
         sql +=  '.append("' + txtLines[i] + '")\n';
    }
    return sql.replace(/[\n]*$/, "") + ";";
};
var unWrapSql = function(txt) {
    if (!txt.trim()) {
        return "";
    }

    var txtLines;
    if (txt.indexOf("\r\n") >= 0) {
        txtLines = txt.split("\r\n");
    } else {
        txtLines = txt.split("\n");
    }

    var sql = "";

    for (var i in txtLines) {
        sql = sql + unWrapSqlLine(txtLines[i]) + "\n";
    }
    return sql.replace(/[\n]*$/, "");
} ;
var unWrapJS = function(txt) {
    if (!txt.trim()) {
        return "";
    }

    if(txt.indexOf("&lt;script") >= 0 || txt.indexOf("script&gt;") >= 0) {
        //return convHtmlJS(txt);
    }

    var txtLines;
    if (txt.indexOf("\r\n") >= 0) {
        txtLines = txt.split("\r\n");
    } else {
        txtLines = txt.split("\n");
    }

    var varName = "str";
    var lastLine = txtLines[txtLines.length - 1];
    varName = lastLine.trim().replace(/;/g, "");
    // 不是一个合法变量
    if (!/^[a-z|A-Z|_][a-z|A-Z|0-9|_]*$/.test(varName)){
        for(var i = txtLines.length - 1; i >= 0; i-- ) {
            lastLine = txtLines[i].trim();
            if(lastLine.indexOf("=") < 0) {
                continue;
            }
            varName = lastLine.split("=")[0].trim();
            if(!varName) {
                continue;
            }
            varName = varName.replace("+", " ").trim();
            if(!/^[a-z|A-Z|_][a-z|A-Z|0-9|_]*$/.test(varName)) {
                continue;
            }
            break;
        }
    }   
    var txtTemp = "";
    for (var i in txtLines) {
        txtTemp = txtTemp + unWrapJS_Line(txtLines[i], varName) + "\n";
    }
    return txtTemp.replace(/[\n]*$/, "");
};
var wrapJS_Line = function(txtLine, varName) {
    if (!txtLine || !varName) {
        return "";
    }

    if (0 == txtLine.trim().indexOf("//")) {
        return txtLine;
    }
    var point1_1=-1, point1_2=-1;
    point1_1 = txtLine.indexOf('"');
    point1_2 = txtLine.indexOf("'");

    // 引号类型：1 -单引号，2 -双引号
    if (point1_1 < point1_2) {
        txtLine = varName + "+='" + txtLine + "\\n';";
    } else {
        txtLine = varName +"+='" + txtLine + "\\n';";
    }

    return txtLine;
};

var wrapJS = function(txt) {
    if (!txt.trim()) {
        return "";
    }
    var txtLines;
    if (txt.indexOf("\r\n") >= 0) {
        txtLines = txt.split("\r\n");
    } else {
        txtLines = txt.split("\n");
    }
    var varName = "str";
    var txtTemp = "";
    for (var i in txtLines) {
        txtTemp = txtTemp + wrapJS_Line(txtLines[i], varName) + "\n";
    }
    return txtTemp + varName + ";";
};

var autoSelectType = function(txt) {
    var txtType = getRadioBoxValue("txtType");
    if ("auto" == txtType) {
        if(/<%@ livebos /i.test(txt) ||
                /type="text\/javascript"/i.test(txt) ||
                /<\/script>/i.test(txt)
                ) {

            return "js";
        }

        if (/select[\w\W]*from/i.test(txt) ||
                /update[\w\W]*set/i.test(txt) ||
                /insert[ ]*into/i.test(txt) ||
                /delete[\w\W]*where/i.test(txt) ||
                /mergin[\w\W]*into/i.test(txt)
                ) {
            return "sql";
        }

        if (/(\\u[0-9a-fA-F]{4}){5,}/i.test(txt)) {
            return "unicode";
        }
    }
    return txtType;
};

var getRateByRow = function(srcStr, rgExp) {
    return srcStr.matchCount(rgExp) / srcStr.matchCount(/\n/ig);
};
var getRate = function(srcStr, rgExp1, rgExp2) {
    return srcStr.matchCount(rgExp1) / srcStr.matchCount(rgExp2);
};

var bindEvent = function(obj, eventType, func) {
    if(window.attachEvent) {
        obj.attachEvent("on" + eventType, func);
    } else {
        obj.addEventListener(eventType, func);
    }
};

var getAction = function(txt, e) {
    var txtType = autoSelectType(txt);

    if (document.getElementById("id_chk_enforce").checked) {
        switch(txtType) {
            case "sql":
                return ("btnToRight" == e.id) ? unWrapSql : wrapSql;
                break;
            case "unicode":
                return ("btnToRight" == e.id) ? CODER.UxxxxToStr : CODER.strToUxxxx;
                break;
            case "url":
                return ("btnToRight" == e.id) ?  encodeURIComponent: decodeURIComponent;
                break;
            case "html":
                return ("btnToRight" == e.id) ?  encodeHTML: decodeHTML;
                break;
            case "base64":
                return ("btnToRight" == e.id) ?  base64.decode: base64.encode;
                break;
            case "md5":
                return  hex_md5;
                break;
            default:
                return ("btnToRight" == e.id)  ? unWrapJS : wrapJS;
                break;
        }
    } else {     
        switch(txtType) {
            case "sql":
                return (getRateByRow(txt, /.append\(/ig) > 0.6) ? unWrapSql : wrapSql;
                break;
            case "unicode":
                return (getRate(txt, /\\u[0-9a-fA-F]{4}/ig, /./ig)) * 6 > 0.4 ? CODER.UxxxxToStr : CODER.strToUxxxx;
                break;
            case "url":
                return (getRate(txt, /\\%[0-9a-fA-F]{2}/ig, /./ig)) * 6 > 0.4 ?  encodeURIComponent: decodeURIComponent;
                break;
            case "md5":
                return hex_md5;
            case "base64":
                return ("btnToRight" == e.id) ?  base64.decode: base64.encode;
            default:
                return (/^[\s]*<%@ livebos /i.test(txt) || getRateByRow(txt, /(str[ ]*\+=)/ig) > 10.6)  ? unWrapJS : wrapJS;
                break;
        }
    }
};
bindEvent(window, "load", function() {
    document.getElementById("id_textarea_left").focus();
    var obj_textarea_right = document.getElementById("id_textarea_right");
    var obj_textarea_left = document.getElementById("id_textarea_left");
    var objs = document.getElementsByTagName("input");

    var btnToRight = document.getElementById("btnToRight");
    var btnToLeft = document.getElementById("btnToLeft");
    bindEvent(btnToRight, "click", function () {
        var action = getAction(obj_textarea_left.value, btnToRight);
        obj_textarea_right.value = action(obj_textarea_left.value);
    });
    bindEvent(btnToLeft, "click", function () {
        var action = getAction(obj_textarea_right.value, btnToLeft);
        obj_textarea_left.value = action(obj_textarea_right.value);
    });
    bindEvent(window.document, "dblclick", function () {
        doQuery(getSelectionText());
    });
});


// *********************************************************************************************************************
//     Javascript 代码整型器     start
// *********************************************************************************************************************
function add_onload_function(fn) {
    var oe = window.onload;
    window.onload = function() {
        if (oe) oe();
        fn();
    }
}

add_onload_function(function() {
    var tabsize = get_var('tabsize');
    if (tabsize) {
        document.getElementById('tabsize').value = tabsize;
    }


    var c = document.forms[0]["id_textarea_right"];
    c && c.setSelectionRange && c.setSelectionRange(0, 0);
    c && c.focus && c.focus();
});

function starts_with(str, what) {
    return str.substr(0, what.length) === what;
}

function trim_leading_comments(str) {
    str = str.replace(/^(\s*\/\/[^\n]*\n)+/, '');
    str = str.replace(/^\s+/, '');
    return str;
}

function unpacker_filter(source) {
    if (document.getElementById('detect-packers').checked) {

        var stripped_source = trim_leading_comments(source);

        if (starts_with(stripped_source.toLowerCase().replace(/ +/g, ''), 'eval(function(p,a,c,k')) {
            eval('var unpacked_source = ' + stripped_source.substring(4) + ';')
            return unpacker_filter(unpacked_source);
        } else if (JavascriptObfuscator.detect(stripped_source)) {
            var unpacked = JavascriptObfuscator.unpack(stripped_source);
            if (unpacked !== stripped_source) { // avoid infinite loop if nothing done
                return unpacker_filter(unpacked);
            }
        }
    }
    return source;
}

function do_js_beautify() {
    document.getElementById('beautify').disabled = true;
    var js_source = document.getElementById('id_textarea_right').value.replace(/^\s+/, '');
    var indent_size = document.getElementById('tabsize').value;
    var indent_char = ' ';
    var preserve_newlines = document.getElementById('preserve-newlines').checked;
    var keep_array_indentation = document.getElementById('keep-array-indentation').checked;

    if (indent_size == 1) {
        indent_char = '\t';
    }

    if (js_source && js_source[0] === '<' && js_source.substring(0, 4) !== '<!--') {
        document.getElementById('id_textarea_right').value = style_html(js_source, indent_size, indent_char, 80);
    } else {
        document.getElementById('id_textarea_right').value = js_beautify(unpacker_filter(js_source), {
            indent_size: indent_size,
            indent_char: indent_char,
            preserve_newlines:preserve_newlines,
            keep_array_indentation:keep_array_indentation,
            space_after_anon_function:true});
    }

    document.getElementById('beautify').disabled = false;
    return false;
}

function get_var(name) {
    var res = new RegExp("[\\?&]" + name + "=([^&#]*)").exec(window.location.href);
    return res ? res[1] : "";
}

// *********************************************************************************************************************
//     Javascript 代码整型器     end
// *********************************************************************************************************************