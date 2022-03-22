const NUMBER_DECIMAL_CHAR = ",";

function inputToNumStr(str) {
    str = "" + str;
    return str.replace(NUMBER_DECIMAL_CHAR, ".");
}

function valToNumById(id) {
    return valToNum($("#" + id).val());
}

function valToNum(val) {
    var str = inputToNumStr(val);
    return numStrToNum(str);
}

function numStrToNum(str) {
    var val = 0;
    if (str !== null && str !== undefined) {
        var aux = parseFloat(str);
        if (!isNaN(aux)) {
            val = aux;
        }
    }
    return val;
}

function validateHouses(value, _intHouses, _decimalHouses){
    var re;
    var valid = true;
    if(_intHouses == 0){
        re = new RegExp("^[+-]?[0]?([,.]\\d{0," + _decimalHouses + "})$");
    }
    else if(_decimalHouses == 0){
        re = new RegExp("^[-+]?(\\d{1," + _intHouses + "})$");
    }
    else{
        re = new RegExp("^[+-]?((\\d{1," + _intHouses + "}([,.]\\d{0," + _decimalHouses + "})?)|([,.]\\d{1," + _decimalHouses + "}))$");
    }
    return re.test(value);
}