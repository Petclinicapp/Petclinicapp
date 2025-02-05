package bootcamp.petclinic.controller;

import bootcamp.petclinic.model.Pet;
import bootcamp.petclinic.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<String> createPet(@RequestBody Pet pet) {
        petService.createPet(pet);
        return ResponseEntity.ok("Pet created successfully!");
    }

    @PutMapping("/{petId}")
    public ResponseEntity<String> updatePet(@PathVariable String petId, @RequestBody Pet updatedPet) {
        petService.updatePet(petId, updatedPet);
        return ResponseEntity.ok("Pet updated successfully!");
    }

    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPet(@PathVariable String petId) {
        Optional<Pet> pet = petService.getPetById(petId);
        return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<String> deletePet(@PathVariable String petId) {
        petService.deletePet(petId);
        return ResponseEntity.ok("Pet deleted successfully!");
    }
}
