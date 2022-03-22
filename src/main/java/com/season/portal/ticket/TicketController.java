package com.season.portal.ticket;

import com.season.portal.utils.ModelViewBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class TicketController extends ModelViewBaseController {

    @GetMapping("/ticket/new")
    public ModelAndView ticketNew() {

        return ticketNewView(new TicketModel());
    }

    @PostMapping(value={"/ticket/new"})
    public ModelAndView ticketNew(@Valid TicketModel model, BindingResult result, HttpServletRequest request){

        if(!result.hasErrors()){

        }
        return ticketNewView( model);
    }

    public ModelAndView ticketNewView(TicketModel model){
        ModelAndView mv = new ModelAndView("ticket/new");

        mv.addObject("ticketModel", model);

        return dispatchView(mv);
    }

}
