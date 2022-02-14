package com.season.portal.utils.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;

public class FileValidatorUtils {
    private boolean validateFileSize(MultipartFile f, ConstraintValidatorContext context, long maxSizeMB) {
        boolean valid = true;
        long fs = f.getSize();

        if(maxSizeMB > 0){
            if (f.getSize() > maxSizeMB){
                context.buildConstraintViolationWithTemplate(
                        "utils_form_file_sizeExceeded").addConstraintViolation();
                valid = false;
            }
        }


        return valid;
    }

    private boolean validateFileType(MultipartFile f, ConstraintValidatorContext context, String[] fileTypes) {
        boolean valid = false;

        if (fileTypes.length > 0){
            String type = f.getContentType().toLowerCase();

            for (String ft : fileTypes){
                switch(ft){
                    case "jpeg":
                    case "jpg":
                    case "png":
                        if(type.equals("image/"+ft))
                            valid = true;
                        break;
                }

            }
        }
        else
            valid = true;

        if(!valid)
            context.buildConstraintViolationWithTemplate(
                    "utils_form_file_invalidType").addConstraintViolation();

        return valid;
    }
}
