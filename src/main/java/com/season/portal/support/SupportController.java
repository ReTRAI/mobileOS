package com.season.portal.support;

import com.season.portal.PortalApplication;
import com.season.portal.auth.LoginModel;
import com.season.portal.resseler.Resseler;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.model.StringPageModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class SupportController extends ModelViewBaseController {

    @GetMapping("/support/new")
    public ModelAndView supportNew() {

        return supportNewView(new SupportModel());
    }

    @PostMapping(value={"/support/new"})
    public ModelAndView supportNew(@Valid SupportModel model, BindingResult result, HttpServletRequest request){

        if(!result.hasErrors()){

        }

        return supportNewView( model);
    }

    public ModelAndView supportNewView(SupportModel model){
        ModelAndView mv = new ModelAndView("support/new");

        mv.addObject("supportModel", model);

        return dispatchView(mv);
    }

}
