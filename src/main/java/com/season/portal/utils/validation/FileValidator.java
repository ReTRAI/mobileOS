package com.season.portal.utils.validation;

import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.IPasswordValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileValidator implements ConstraintValidator<IFileValidatorConstrain, MultipartFile> {

    private boolean canBeNull;
    private String[] fileTypes;
    private long maxSizeMB;

    @Override
    public void initialize(IFileValidatorConstrain constraintAnnotation) {
        canBeNull = constraintAnnotation.canBeNull();
        fileTypes = constraintAnnotation.fileTypes();
        maxSizeMB = constraintAnnotation.maxSizeMB();
        if(maxSizeMB > 0)
            maxSizeMB *=1024*1024;

        String[] groups = constraintAnnotation.fileTypeGroups();
        if(groups.length > 0) {
            List<String> typeList = new ArrayList<String>(Arrays.asList(fileTypes));
            for (String group : groups){
                switch (group) {
                    case "image":
                        typeList.add("jpg");
                        typeList.add("png");
                        typeList.add("jpeg");
                        break;
                }
            }
            fileTypes = typeList.toArray(fileTypes);
        }
    }

    @Override
    public boolean isValid(MultipartFile f, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!validateEmpty(f, context))
            return false;
        
        if(f.isEmpty())
            return true;
        
        return validateFileSize(f, context) &&
               validateFileType(f, context);
    }

    private boolean validateFileSize(MultipartFile f, ConstraintValidatorContext context) {
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

    private boolean validateFileType(MultipartFile f, ConstraintValidatorContext context) {
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

    public boolean validateEmpty(MultipartFile f, ConstraintValidatorContext context) {
        boolean valid = true;
        
        if (!canBeNull && f.isEmpty()){
            context.buildConstraintViolationWithTemplate(
                    "utils_form_required").addConstraintViolation();
            valid = false;
        }
        return valid;
    }

}
