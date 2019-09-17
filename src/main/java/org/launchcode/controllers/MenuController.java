package org.launchcode.controllers;


import com.sun.org.apache.bcel.internal.generic.ARETURN;
import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping(value="menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;
    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model)
    {
        model.addAttribute("title",  "All Menus");
        model.addAttribute("menus",menuDao.findAll());
        return "menu/index";
    }

    @RequestMapping(value="add",method=RequestMethod.GET)
    public String add(Model model)
    {
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");
        return "menu/add";
    }

    @RequestMapping(value="add",method=RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid Menu menu, Errors errors)
    {
        if (errors.hasErrors())
        {
            return "menu/add";
        }
        menuDao.save(menu);
        return "redirect:/menu/view/" +menu.getId();
    }
    @RequestMapping(value="view/{menuId}",method=RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int menuId )
    {
        Menu menu =menuDao.findOne(menuId);

        model.addAttribute("menu",menu);
        model.addAttribute("title",menu.getName());
        return "menu/view";
    }

    @RequestMapping(value="add-item/{menuId}",method = RequestMethod.GET)
    public String addItem(Model model,@PathVariable int menuId )
    {
        Menu menu =menuDao.findOne(menuId);
        //AddMenuItemForm addMenuItemForm= new AddMenuItemForm(cheeseDao.findAll(),menu);
        AddMenuItemForm addMenuItemForm= new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("title","Add Item to menu" +menu.getName());
        model.addAttribute("form",addMenuItemForm);
        return "menu/add-item";
    }
    @RequestMapping(value="add-item",method = RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm addMenuItemForm, Errors errors)
    {
        if (errors.hasErrors())
        {
            return "menu/add-item";
        }
        Menu menu =menuDao.findOne(addMenuItemForm.getMenuId());
        menu.addItem(cheeseDao.findOne(addMenuItemForm.getCheeseId()));
        menuDao.save(menu);

        return "redirect:/menu/view/"+ menu.getId();
    }

}
