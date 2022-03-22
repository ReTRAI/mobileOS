function getStatus_numberMinMax(min, max){
    var status = "";
    status += '<span class="toTranslate" data-langKey="utils_form_minmaxChar0"></span>';
    status += '<span>'+min+'</span>';
    status += '<span class="toTranslate" data-langKey="utils_form_minmaxChar1"></span>';
    status += '<span>'+max+'</span>';
    status += '<span class="toTranslate" data-langKey="utils_form_minmaxChar2"></span>';
    return status;
}

function getStatus_numberHouses(whole, decimal){
    var status = "";
    status += '<span class="toTranslate" data-langKey="utils_form_number_houses0"></span>';
    status += '<span>'+whole+'</span>';
    status += '<span class="toTranslate" data-langKey="utils_form_number_houses1"></span>';
    status += '<span>'+decimal+'</span>';
    status += '<span class="toTranslate" data-langKey="utils_form_number_houses2"></span>';
    return status;
}

function getStatus_numberMax(max){
    var status = "";
    status += '<span class="toTranslate" data-langKey="utils_form_number_maxNum0"></span>';
    status += '<span>'+max+'</span>';
    status += '<span class="toTranslate" data-langKey="utils_form_number_maxNum1"></span>';
    return status;
}