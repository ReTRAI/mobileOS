package com.season.portal.utils.validation;

import com.season.portal.utils.validation.constrain.IFileValidatorConstrain;
import com.season.portal.utils.validation.constrain.IFilesValidatorConstrain;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilesValidator extends FileValidatorUtils implements ConstraintValidator<IFilesValidatorConstrain, List<MultipartFile>> {

    private boolean required;
    private String[] fileTypes;
    private long maxSizeMB;
    private int maxFiles;

    @Override
    public void initialize(IFilesValidatorConstrain constraintAnnotation) {
        required = constraintAnnotation.required();
        fileTypes = groupsToFileTypes(constraintAnnotation.fileTypeGroups(), constraintAnnotation.fileTypes());
        maxSizeMB = calcMaxMBSize(constraintAnnotation.maxSizeMB());
        maxFiles = constraintAnnotation.maxFiles();
    }



    @Override
    public boolean isValid(List<MultipartFile> fs, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        if(!validateSize(fs, context))
            return false;

        //If not required no further validation
        if(fs.size() <= 0 || fs.get(0).isEmpty())
            return true;

        for(MultipartFile f:fs){
            if(!( validateFileSize(f, context, maxSizeMB) &&
                    validateFileType(f, context, fileTypes))){
                return false;
            }
        }
        return true;

    }

    public boolean validateSize(List<MultipartFile> fs, ConstraintValidatorContext context) {
        boolean valid = true;

        if(required){
            if (fs.size() <= 0 || fs.get(0).isEmpty()){
                context.buildConstraintViolationWithTemplate(
                        "utils_form_required").addConstraintViolation();
                valid = false;
            }
        }

        if (fs.size() > maxFiles){
            context.buildConstraintViolationWithTemplate(
                    "utils_form_file_maxFiles").addConstraintViolation();
            valid = false;
        }

        return valid;
    }

}
