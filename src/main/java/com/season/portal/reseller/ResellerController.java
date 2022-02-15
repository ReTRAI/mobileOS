package com.season.portal.reseller;

import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.model.StringPageModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class ResellerController extends ModelViewBaseController {

    @GetMapping("/resellers")
    public ModelAndView resellers(StringPageModel model) {
        model.setNumPerPage(10);
        return resellersView(model);
    }

    private ModelAndView resellersView(StringPageModel model){
        ModelAndView mv = new ModelAndView("reseller/resellers");

        ArrayList<Reseller> resellers = new ArrayList<Reseller>();
        int totalElements = 47;

        Pagination resellerPag = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 2);

        int max = resellerPag.getOffset()+model.getNumPerPage();
        max = (max > totalElements)?totalElements:max;

        if(model.getValue().equals("Joana")){
            resellers.add(new Reseller((long) 0, "Joana", 500.50f, 200, 180, 19, 19, 1));
        }
        else if(model.getValue().equals("Pedro")){
            for(int i = resellerPag.getOffset()+1; i <= max; i++) {
                resellers.add(new Reseller((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
            }
        }
        else if(model.getValue() == null || model.getValue().equals("")){
            if(resellerPag.getActualPage() == 1){
                resellers.add(new Reseller((long) 0, "Joana", 500.50f, 200, 180, 19, 19, 1));

                for(int i = resellerPag.getOffset()+1; i <= max; i++) {
                    resellers.add(new Reseller((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
                }
            }
            else{
                for(int i = resellerPag.getOffset()+1; i <= max; i++) {
                    resellers.add(new Reseller((long) i, "Pedro", 500.50f, 200, 180, 19, 19, 1));
                }
            }
        }

        mv.addObject("resellers", resellers);
        mv.addObject("resellerPag", resellerPag);
        mv.addObject("stringPageModel", model);

        return dispatchView(mv);
    }

}
