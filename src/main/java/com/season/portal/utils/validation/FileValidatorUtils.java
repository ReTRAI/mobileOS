package com.season.portal.utils.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileValidatorUtils {

    protected long calcMaxMBSize(long maxSizeMB) {
        if(maxSizeMB > 0)
            maxSizeMB *= 1024*1024;
        return maxSizeMB;
    }

    public static String[] groupsToFileTypes(String[] groups, String[] fileTypes) {
        if(groups.length > 0){
            List<String> typeList = new ArrayList<String>(Arrays.asList(fileTypes));
            for (String group : groups){
                switch (group) {
                    case "image":
                        typeList.add("jpg");
                        typeList.add("png");
                        typeList.add("jpeg");
                        break;
                    case "zip":
                        typeList.add("zip");
                        typeList.add("rar");
                        break;
                }
            }
            fileTypes = typeList.toArray(fileTypes);
        }
        return fileTypes;
    }

    protected boolean validateFileSize(MultipartFile f, ConstraintValidatorContext context, long maxSizeMB) {
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

    protected boolean validateFileType(MultipartFile f, ConstraintValidatorContext context, String[] fileTypes) {
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
                    case "zip":
                    case "pdf":
                        if(type.equals("application/"+ft))
                            valid = true;
                        break;
                    case "rar":
                        if(type.equals("application/x-rar-compressed"))
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
