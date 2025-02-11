package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.visit.VisitRequestDTO;
import bootcamp.petclinic.dto.visit.VisitResponseDTO;
import bootcamp.petclinic.exceptions.VisitNotFoundException;
import bootcamp.petclinic.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/visits")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/add")
    public ResponseEntity<VisitResponseDTO> createVisit(@RequestBody VisitRequestDTO visitRequestDTO) {
        VisitResponseDTO visit = visitService.createVisit(visitRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(visit);
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<VisitResponseDTO> getVisit(@PathVariable String visitId) {
        return visitService.getVisitById(visitId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<VisitResponseDTO>> getVisitsByUserId(@PathVariable String userId) {
        List<VisitResponseDTO> visits = visitService.getVisitsByUserId(userId);
        return visits.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(visits);
    }

    @PatchMapping("/{visitId}")
    public ResponseEntity<VisitResponseDTO> updateVisit(@PathVariable String visitId, @Valid @RequestBody VisitRequestDTO visitRequestDTO) {
        try {
            VisitResponseDTO response = visitService.updateVisit(visitId, visitRequestDTO);
            return ResponseEntity.ok(response);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new VisitResponseDTO(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VisitResponseDTO(e.getMessage()));
        }
    }

    @DeleteMapping("/{visitId}")
    public ResponseEntity<String> deleteVisit(@PathVariable String visitId) {
        visitService.deleteVisit(visitId);
        return ResponseEntity.ok("Visit deleted successfully!");
    }
}
