package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.pet.PetRequestDTO;
import bootcamp.petclinic.dto.pet.PetResponseDTO;
import bootcamp.petclinic.model.Pet;
import bootcamp.petclinic.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/add")
    public ResponseEntity<PetResponseDTO> createPet(@Valid @RequestBody PetRequestDTO petRequestDTO) {
        PetResponseDTO response = petService.createPet(petRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDTO> getPet(@PathVariable String petId) {
        Optional<PetResponseDTO> pet = petService.getPetById(petId);
        return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{petId}")
    public ResponseEntity<String> updatePet(@PathVariable String petId, @Valid @RequestBody PetRequestDTO updatedPet) {
        petService.updatePet(petId, updatedPet);
        return ResponseEntity.ok("Pet updated successfully!");
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<String> deletePet(@PathVariable String petId) {
        petService.deletePet(petId);
        return ResponseEntity.ok("Pet deleted successfully!");
    }
}
