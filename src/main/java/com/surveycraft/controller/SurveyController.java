package com.surveycraft.controller;

import com.surveycraft.dto.survey.CreateSurveyRequest;
import com.surveycraft.dto.survey.SubmitResponseRequest;
import com.surveycraft.dto.survey.SurveyResultsDTO;
import com.surveycraft.service.SurveyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * 1) Crear encuesta (Crafter)
     * POST /api/surveys
     */
    @PostMapping
    public ResponseEntity<SurveyResultsDTO> createSurvey(
            @RequestBody CreateSurveyRequest request,
            Authentication authentication) {
        Long crafterId = extractUserId(authentication);
        SurveyResultsDTO created = surveyService.createSurvey(request, crafterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 2) Obtener encuesta
     * GET /api/surveys/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResultsDTO> getSurveyById(@PathVariable Long id) {
        SurveyResultsDTO survey = surveyService.getSurveyById(id);
        return ResponseEntity.ok(survey);
    }

    /**
     * 3) Responder encuesta (Player)
     * POST /api/surveys/{id}/respond
     */
    @PostMapping("/{id}/respond")
    public ResponseEntity<Void> respondSurvey(
            @PathVariable Long id,
            @RequestBody SubmitResponseRequest request,
            Authentication authentication) {
        Long playerId = extractUserId(authentication);
        surveyService.submitResponses(id, playerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 4) Ver resultados de una encuesta
     * GET /api/surveys/{id}/results
     */
    @GetMapping("/{id}/results")
    public ResponseEntity<SurveyResultsDTO> getSurveyResults(@PathVariable Long id) {
        SurveyResultsDTO results = surveyService.getSurveyResults(id);
        return ResponseEntity.ok(results);
    }

    /**
     * 5) Buscar encuestas por tipo
     * GET /api/surveys/tipo/{tipoEncuesta}
     */
    @GetMapping("/tipo/{tipoEncuesta}")
    public ResponseEntity<List<SurveyResultsDTO>> buscarPorTipo(@PathVariable String tipoEncuesta) {
        List<SurveyResultsDTO> encuestas = surveyService.buscarEncuestasPorTipo(tipoEncuesta);
        return ResponseEntity.ok(encuestas);
    }

    /**
     * 6) Obtener todas las encuestas
     * GET /api/surveys
     */
    @GetMapping
    public ResponseEntity<List<SurveyResultsDTO>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    /**
     * 7) Update survey (CRAFTER only)
     * PUT /api/surveys/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<SurveyResultsDTO> updateSurvey(
            @PathVariable Long id,
            @RequestBody CreateSurveyRequest request,
            Authentication authentication) {
        Long crafterId = extractUserId(authentication);
        SurveyResultsDTO updated = surveyService.updateSurvey(id, request, crafterId);
        return ResponseEntity.ok(updated);
    }

    /**
     * 8) Delete survey
     * DELETE /api/surveys/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id, Authentication authentication) {
        Long userId = extractUserId(authentication);
        surveyService.deleteSurvey(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 9) Get my surveys
     * GET /api/surveys/my-surveys
     */
    @GetMapping("/my-surveys")
    public ResponseEntity<List<SurveyResultsDTO>> getMySurveys(Authentication authentication) {
        Long userId = extractUserId(authentication);
        return ResponseEntity.ok(surveyService.getSurveysByCreator(userId));
    }

    /**
     * 10) Change survey status
     * PATCH /api/surveys/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<SurveyResultsDTO> changeSurveyStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        SurveyResultsDTO updated = surveyService.changeSurveyStatus(id, status, userId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Helper para extraer el ID del usuario autenticado.
     */
    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return (Long) authentication.getPrincipal();
    }
}