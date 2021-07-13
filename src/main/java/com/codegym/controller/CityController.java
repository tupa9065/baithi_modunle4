package com.codegym.controller;

import com.codegym.model.City;
import com.codegym.model.Country;
import com.codegym.service.city.ICityService;
import com.codegym.service.country.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/cities")
public class CityController {
    @Autowired
    private ICityService cityService;

    @Autowired
    private ICountryService countryService;

    @GetMapping
    public ModelAndView showAll(){
            ModelAndView modelAndView = new ModelAndView("/city/list");
            modelAndView.addObject("cities",cityService.findAll());
            return modelAndView;
    }
    @GetMapping("/add-new")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("/city/add");
        Iterable<Country> countries = countryService.findAll();
        modelAndView.addObject("countries", countries);
        modelAndView.addObject("city", new City());
        return modelAndView;
    }
    @PostMapping("/add-new")
    public ModelAndView addNewCity( @ModelAttribute City city) {
//        city.validate(city, bindingResult);
//        ModelAndView modelAndView;
//        if (bindingResult.hasFieldErrors()) {
//            modelAndView = new ModelAndView("/city/add");
//            Iterable<Country> countries = countryService.findAll();
//            modelAndView.addObject("countries", countries);
//            return modelAndView;
//        }
        cityService.save(city);
        Iterable<Country> countries = countryService.findAll();
        ModelAndView modelAndView = new ModelAndView("/city/add");
        modelAndView.addObject("city", new City());
        modelAndView.addObject("countries", countries);
        return modelAndView;
    }
    @GetMapping("/show-city/{id}")
    public ModelAndView showOneCity(@PathVariable Long id) {
       Optional<City> cityOptional = cityService.findById(id);
       if(!cityOptional.isPresent()){
           return new ModelAndView("/city/404-notfound");
       }
        ModelAndView modelAndView = new ModelAndView("/city/city-detail");
        modelAndView.addObject("city", cityOptional.get());
        return modelAndView;
    }
    @GetMapping("/delete/{cityId}")
    public String deleteCity(@PathVariable("cityId") Long cityId) {
        cityService.deleteById(cityId);
        return "redirect:/cities";
    }
    @GetMapping("/update/{cityId}")
    public ModelAndView showUpdateForm(@PathVariable("cityId") Long cityId) {
        Optional<City> city = cityService.findById(cityId);
        ModelAndView modelAndView = new ModelAndView("/city/update");
        if (city.isPresent()) {
            modelAndView.addObject("city", city.get());
            Iterable<Country> countries = countryService.findAll();
            modelAndView.addObject("countries", countries);
            return modelAndView;
        }
        return null;
    }
    @PostMapping("/update")
    public ModelAndView updateCity (@ModelAttribute("city") City city) {

        cityService.save(city);
        Iterable<Country> countries = countryService.findAll();
        ModelAndView modelAndView = new ModelAndView("/city/update");
        modelAndView.addObject("city", city);
        modelAndView.addObject("countries", countries);
        return modelAndView;
    }
}
