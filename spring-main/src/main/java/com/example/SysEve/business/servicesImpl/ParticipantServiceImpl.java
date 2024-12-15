package com.example.SysEve.business.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SysEve.business.services.ParticipantService;
import com.example.SysEve.dao.entities.Participant;
import com.example.SysEve.dao.repositories.ParticipantRepository;

@Service
public class ParticipantServiceImpl implements ParticipantService{


    private final ParticipantRepository  participantRepository;
    public ParticipantServiceImpl(ParticipantRepository participantRepository){
        this.participantRepository=participantRepository;
    } 
    @Override
    public List<Participant> getAllParticipants() {
        return    this.participantRepository.findAll();
        
        
    }

    @Override
    public Participant getParticipantById(Long id) {
        if(id==null){
            return null; 
        }
       return this.participantRepository.findById(id).get();
    }

    @Override
    public List<Participant> getParticipantByName(String name) {
        return this.participantRepository.findByFirstName(name);
    }

    @Override
    public Participant addParticipant(Participant event) {
        if(event==null){
            return null;
        }
       return this.participantRepository.save(event);
    }


    @Override
    public void deleteParticipantById(Long id) {
        if(id==null){
            return ;
        }
         this.participantRepository.deleteById(id);
    }
    @Override
    public void updateParticipant(Participant participant) {
        if (participant.getId() == null) {
            throw new IllegalArgumentException("Participant ID must not be null for an update.");
        }

        // Optional: Validate if the participant exists
        Participant existingParticipant = participantRepository.findById(participant.getId())
            .orElseThrow(() -> new RuntimeException("Participant not found"));

        // Update the existing participant
        existingParticipant.setFirstName(participant.getFirstName());
        existingParticipant.setLastName(participant.getLastName());
        existingParticipant.setBirthday(participant.getBirthday());
        existingParticipant.setEmail(participant.getEmail());
        existingParticipant.setNumber(participant.getNumber());
        existingParticipant.setImage(participant.getImage()); // Handles null if no image is uploaded

        // Save the updated participant back to the database
        participantRepository.save(existingParticipant);
    }

    public boolean isParticipantAlreadyRegistered(String email, Long eventId) {
        // Find participant by email
        Participant existingParticipant = participantRepository.findByEmail(email);
        
        if (existingParticipant != null) {
            // Check if the participant is already registered for the event
            return existingParticipant.getEvents().stream()
                .anyMatch(event -> event.getId().equals(eventId));
        }
        return false; // No participant with the same email
    }
    
    
    
}