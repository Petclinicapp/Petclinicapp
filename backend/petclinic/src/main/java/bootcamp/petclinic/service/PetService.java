package bootcamp.petclinic.service;

import bootcamp.petclinic.model.Pet;
import bootcamp.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public void createPet(Pet pet) {
        petRepository.save(pet);
    }

    public void updatePet(String petId, Pet updatedPet) {
        Optional<Pet> existingPet = petRepository.findById(petId);
        if (existingPet.isPresent()) {
            Pet pet = existingPet.get();
            pet.setPetName(updatedPet.getPetName());
            pet.setSpecies(updatedPet.getSpecies());
            pet.setBreed(updatedPet.getBreed());
            pet.setGender(updatedPet.getGender());
            pet.setAge(updatedPet.getAge());
            pet.setWeight(updatedPet.getWeight());
            petRepository.save(pet);
        } else {
            throw new IllegalArgumentException("Pet not found with ID: " + petId);
        }
    }

    public Optional<Pet> getPetById(String petId) {
        return petRepository.findById(petId);
    }

    public void deletePet(String petId) {
        petRepository.deleteById(petId);
    }
}
