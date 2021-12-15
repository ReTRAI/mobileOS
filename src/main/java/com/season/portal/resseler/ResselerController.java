package com.season.portal.resseler;

import com.season.portal.PortalApplication;
import com.season.portal.utils.model.StringPageModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ResselerController {

    @GetMapping("/resselers")
    public ModelAndView resselersFilter(StringPageModel model) {
        return resselersView(model);
    }

    @RequestMapping(value={"/resselers"})
    public ModelAndView resselers(){
        return resselersView(new StringPageModel(10));
    }

    public ModelAndView resselersView(StringPageModel model){
        ModelAndView mv = new ModelAndView("resseler/resselers");

        ArrayList<Resseler> resselers = new ArrayList<Resseler>();
        int total = 47;
        int max = model.getOffset()+model.getNumPerPage();
        max = (max > total)?total:max;

        if(model.getValue() == "Joana"){
            resselers.add(new Resseler((long) 0, "Joana", 500.50f, 200, 180, 19, 19, 1));
        }
        else if(model.getValue() == null || model.getValue() == ""){
            if(model.getOffset() == 0){
                resselers.add(new Resseler((long) 0, "Joana", 500.50f, 200, 180, 19, 19, 1));

                for(int i = model.getOffset()+1; i < max; i++) {
                    resselers.add(new Resseler((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
                }
            }
            else{
                for(int i = model.getOffset()+1; i < max; i++) {
                    resselers.add(new Resseler((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
                }
            }
        }

        mv.addObject("resselers", resselers);
        mv.addObject("totalResselers", total);

        return PortalApplication.addStatus(mv);
    }

}
