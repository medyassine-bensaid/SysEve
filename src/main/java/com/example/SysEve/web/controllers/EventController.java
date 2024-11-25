package com.example.SysEve.web.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import com.example.SysEve.business.services.EventService;
import com.example.SysEve.dao.entities.Event;
import com.example.SysEve.web.models.requests.EventForm;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
public class EventController {

    private static List<Event> events = new ArrayList<Event>();
    private static Long idCount = 0L;
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";
    static {
    events.add(new Event(++idCount, "event1", "event 11", "01/11/2024", "tunis", 5, 50D , "samsung-s9.png"));
    events.add(new Event(++idCount, "event2", "event 22", "01/11/2024", "tunis", 5, 50D ,"samsung-s9.png"));
    events.add(new Event(++idCount, "event3", "event 33", "01/11/2024", "tunis", 5, 50D , "samsung-s9.png"));
    }

    private final EventService eventService;

    @RequestMapping({"/", "/events"})
     public String getAllEvents(Model model) {
        model.addAttribute("events", this.eventService.getAllEvent());
         return  "list";
     }

     @RequestMapping("/events/create")
     public String showAddEventForm(Model model) {
         model.addAttribute("eventForm", new EventForm());
         return "add-event";
     }


    //   @RequestMapping(path = "/create", method = RequestMethod.POST)
    // public String addEvent(@Valid @ModelAttribute EventForm eventForm,
    //         BindingResult bindingResult,
    //         Model model,
    //         @RequestParam MultipartFile file) {
    //    /*  if (bindingResult.hasErrors()) {
    //         model.addAttribute("error", "Invalid input");
    //         return "add-event";
    //     }
    //     if (!file.isEmpty()) {
    //         StringBuilder fileName = new StringBuilder();
    //         fileName.append(file.getOriginalFilename());
    //         Path newFilePath = Paths.get(uploadDirectory, fileName.toString());

    //         try {
    //             Files.write(newFilePath, file.getBytes());
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //         this.eventService.addEvent(new Event(null, eventForm.getName(), eventForm.getDescription(),eventForm.getDate(), eventForm.getLocation(),eventForm.getAvailableSeats(), eventForm.getPrice(), fileName.toString()));
    //     } else {
    //       this.eventService.addEvent(new Event(null, eventForm.getName(), eventForm.getDescription(),eventForm.getDate(), eventForm.getLocation(),eventForm.getAvailableSeats(), eventForm.getPrice(), null));
    //     }
    //     /*this.eventService.addEvent(new Event(null, eventForm.getName(), eventForm.getDescription(),eventForm.getDate(), eventForm.getLocation(),eventForm.getAvailableSeats(), eventForm.getPrice(), null));*/
    //     return "redirect:/events";
    // }


 @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String addEvent(
        @Valid @ModelAttribute EventForm eventForm,
        BindingResult bindingResult,
        Model model,
        @RequestParam MultipartFile file) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Données invalides");
            return "add-event";
        }

        if (!file.isEmpty()) {
            StringBuilder fileName = new StringBuilder();
            fileName.append(file.getOriginalFilename());
            Path newFilePath = Paths.get(uploadDirectory, fileName.toString());

            try {
                Files.write(newFilePath, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.eventService.addEvent(new Event(
                null,
                eventForm.getName(),
                eventForm.getDescription(),
                eventForm.getDate(),
                eventForm.getLocation(),
                eventForm.getAvailableSeats(),
                eventForm.getPrice(),
                fileName.toString()
            ));
        } else {
            this.eventService.addEvent(new Event(
                null,
                eventForm.getName(),
                eventForm.getDescription(),
                eventForm.getDate(),
                eventForm.getLocation(),
                eventForm.getAvailableSeats(),
                eventForm.getPrice(),
                null
            ));
        }

        return "redirect:/events";
    }



//     @RequestMapping("{id}/edit")
//     public String showUpdateEventForm(@PathVariable Long id, Model model) {
       
        
//         Event event=this.eventService.getEventById(id);
//         model.addAttribute("eventForm", new EventForm(event.getName(), event.getDescription(), event.getDate(), event.getLocation(), event.getAvailableSeats(), event.getPrice(), event.getImage()));
//         model.addAttribute("id", id);
//         return "update-event";
//     }

//     @RequestMapping(path = "{id}/edit", method = RequestMethod.POST)
//     public String updateEvent(@Valid @ModelAttribute EventForm EventForm,
//             BindingResult bindingResult,
//             @PathVariable Long id,
//             Model model,
//             @RequestParam MultipartFile file
//             ) {
//         /*if (bindingResult.hasErrors()) {
//             model.addAttribute("error", "Invalid input");
//             return "update-Event";
//         }

//     Event event =this.eventService.getEventById(id);
//     event.setName(eventForm.getName());
//     event.setDescription(eventForm.getDescription());
//     event.setDate(eventForm.getDate());
//     event.setLocation(eventForm.getLocation());
//     event.setAvailableSeats(eventForm.getAvailableSeats());
//     event.setPrice(eventForm.getPrice());
//     if (!file.isEmpty()) {
//         StringBuilder fileName = new StringBuilder();
//         Path newFilePath = Paths.get(uploadDirectory, file.getOriginalFilename());
//         fileName.append(file.getOriginalFilename());
//         try {
//             Files.write(newFilePath, file.getBytes());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//          // Supprimer le fichier de photo si existe
//      if (event.getImage() != null) {
//         Path filePath = Paths.get(uploadDirectory, event.getImage());
//         try {
//             Files.deleteIfExists(filePath);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
//         event.setImage(fileName.toString());
//     }
//     this.eventService.updateEvent(event);*/
//     return "redirect:/events";
// }


@RequestMapping(path = "/events/{id}/edit")
public String showUpdateEventForm(@PathVariable Long id, Model model) {
    Event event = this.eventService.getEventById(id);
    if (event == null) {
        model.addAttribute("error", "L'événement n'existe pas.");
        return "redirect:/events";
    }
    model.addAttribute("eventForm", new EventForm(event.getName(), event.getDescription(), event.getDate(), event.getLocation(), event.getAvailableSeats(), event.getPrice()));
    model.addAttribute("existingImage", event.getImage());
    return "update-event";
}

@RequestMapping(path = "/events/{id}/edit", method = RequestMethod.POST)
public String updateEvent(
        @Valid @ModelAttribute EventForm eventForm,
        BindingResult bindingResult,
        @PathVariable Long id,
        Model model,
        @RequestParam MultipartFile file
) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("error", "Invalid input");
        return "update-event";
    }

    Event event = this.eventService.getEventById(id);
    event.setName(eventForm.getName());
    event.setDescription(eventForm.getDescription());
    event.setDate(eventForm.getDate());
    event.setLocation(eventForm.getLocation());
    event.setAvailableSeats(eventForm.getAvailableSeats());
    event.setPrice(eventForm.getPrice());

    if (!file.isEmpty()) {
        StringBuilder fileName = new StringBuilder();
        Path newFilePath = Paths.get(uploadDirectory, file.getOriginalFilename());
        fileName.append(file.getOriginalFilename());
        try {
            Files.write(newFilePath, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (event.getImage() != null) {
            Path filePath = Paths.get(uploadDirectory, event.getImage());
            try {
                Files.deleteIfExists(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        event.setImage(fileName.toString());
    }

    this.eventService.updateEvent(event);
    return "redirect:/events";
}






    // @RequestMapping(path = "/{id}/delete", method = RequestMethod.POST)
    // public String deleteEventById(@PathVariable Long id) {
        
     
    //   /* Event event=this.eventService.getEventById(id);
    //      // Supprimer le fichier de photo si existe
    //      if (event.getImage() != null) {
    //         Path filePath = Paths.get(uploadDirectory, event.getImage());
    //         try {
    //             Files.deleteIfExists(filePath);
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }*/ 
    //    this.eventService.deleteEventById(id);
    //     return "redirect:/events";
    // }

    @RequestMapping(path = "/events/{id}", method = RequestMethod.POST)
public String deleteEventById(@PathVariable Long id) {
    Event event = this.eventService.getEventById(id);

    
    if (event != null && event.getImage() != null) {
        Path filePath = Paths.get(uploadDirectory, event.getImage());
        try {
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    this.eventService.deleteEventById(id);
    return "redirect:/events";
}

}
