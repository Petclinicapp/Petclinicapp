package bootcamp.petclinic.controller;

import bootcamp.petclinic.dto.visit.*;
import bootcamp.petclinic.exceptions.UnauthorizedAccessException;
import bootcamp.petclinic.exceptions.VisitNotFoundException;
import bootcamp.petclinic.service.VisitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public ResponseEntity<List<VisitResponseDTO>> getAllVisits() {
        try {
            List<VisitResponseDTO> response = visitService.getAllUserVisits();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
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

    @PostMapping("/{visitId}/details")
    public ResponseEntity<String> createVisitDetails(@PathVariable String visitId) {
        try {
            visitService.createVisitDetails(visitId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Visit details created successfully!");
        } catch (VisitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (
                RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{visitId}/updateDetails")
    public ResponseEntity<VisitResponseDTO> updateVisitDetails(
            @PathVariable String visitId,
            @Valid @RequestBody VisitDetailsUpdateDTO visitDetailsUpdateDTO) {
        try {
            VisitResponseDTO response = visitService.updateVisitDetails(visitId, visitDetailsUpdateDTO)
                    .orElseThrow(() -> new VisitNotFoundException("Visit details not found"));
            return ResponseEntity.ok(response);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new VisitResponseDTO(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new VisitResponseDTO(e.getMessage()));
        }
    }

    @PatchMapping("/{visitId}/status")
    public ResponseEntity<VisitUpdateResponseDTO> updateVisitStatus(
            @PathVariable String visitId,
            @RequestBody VisitUpdateRequestDTO status
    ) {
        try {
            VisitUpdateResponseDTO response = visitService.updateVisitStatus(visitId, status);
            return ResponseEntity.ok(response);

        } catch (UnauthorizedAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new VisitUpdateResponseDTO(e.getMessage()));

        } catch (VisitNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new VisitUpdateResponseDTO(e.getMessage()));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new VisitUpdateResponseDTO(e.getMessage()));
        }
    }

}
