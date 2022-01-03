package com.season.portal.resseler;

import com.season.portal.PortalApplication;
import com.season.portal.utils.model.StringPageModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ResselerController {

    @GetMapping("/resselers")
    public ModelAndView resselers(StringPageModel model) {
        model.setNumPerPage(10);
        return resselersView(model);
    }

    public ModelAndView resselersView(StringPageModel model){
        ModelAndView mv = new ModelAndView("resseler/resselers");

        ArrayList<Resseler> resselers = new ArrayList<Resseler>();
        int totalElements = 47;

        Pagination resselerPag = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 2);

        int max = resselerPag.getOffset()+model.getNumPerPage();
        max = (max > totalElements)?totalElements:max;

        if(model.getValue().equals("Joana")){
            resselers.add(new Resseler((long) 0, "Joana", 500.50f, 200, 180, 19, 19, 1));
        }
        else if(model.getValue().equals("Pedro")){
            for(int i = resselerPag.getOffset()+1; i <= max; i++) {
                resselers.add(new Resseler((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
            }
        }
        else if(model.getValue() == null || model.getValue().equals("")){
            if(resselerPag.getActualPage() == 1){
                resselers.add(new Resseler((long) 0, "Joana", 500.50f, 200, 180, 19, 19, 1));

                for(int i = resselerPag.getOffset()+1; i <= max; i++) {
                    resselers.add(new Resseler((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
                }
            }
            else{
                for(int i = resselerPag.getOffset()+1; i <= max; i++) {
                    resselers.add(new Resseler((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
                }
            }
        }

        mv.addObject("resselers", resselers);
        mv.addObject("resselerPag", resselerPag);
        mv.addObject("stringPageModel", model);

        return PortalApplication.addStatus(mv);
    }

}
